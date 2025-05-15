package cn.edu.usst.cs.campusAid;

import cn.edu.usst.cs.campusAid.service.CampusAidException;
import cn.edu.usst.cs.campusAid.dto.forum.*;
import cn.edu.usst.cs.campusAid.mapper.db.BlogMapper;
import cn.edu.usst.cs.campusAid.mapper.db.LikeBlogMapper;
import cn.edu.usst.cs.campusAid.mapper.db.ReplyMapper;
import cn.edu.usst.cs.campusAid.model.forum.Blog;
import cn.edu.usst.cs.campusAid.model.forum.Reply;
import cn.edu.usst.cs.campusAid.service.ForumPostService;
import cn.edu.usst.cs.campusAid.service.UserService;
import cn.edu.usst.cs.campusAid.util.ReplyTreeConverter;
import org.apache.ibatis.session.RowBounds;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Comparator;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 论坛服务层测试类，覆盖发帖、搜索、点赞、回复、删帖等核心功能。
 * {@link Order}: 1x:发帖 2x:搜索 3x:点赞 4x:回复
 */

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@Execution(ExecutionMode.SAME_THREAD)
public class ForumPostServiceImplTest {

    private static final Logger logger = LoggerFactory.getLogger(ForumPostServiceImplTest.class);

    @Autowired
    private ForumPostService forumPostService;

    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private LikeBlogMapper likeBlogMapper;

    @Autowired
    private ReplyMapper replyMapper;

    @Autowired
    private UserService userService;

    // 测试用户ID
    private static final Long USER_ID_1 = 2235062121L; // misaka
    private static final Long USER_ID_2 = 2235062316L; // goldfish

    /**
     * <strong>m</strong>
     */
    private static final String POST_TITLE_1 = "我的第一次发帖";
    /**
     * <strong>m</strong>
     */
    private static final String POST_CONTENT_1 = "这是misaka的内容 #testtag ";
    /**
     * <strong>gf</strong>
     */
    private static final String POST_TITLE_2 = "关于校园生活的思考";
    /**
     * <strong>gf</strong>
     */
    private static final String POST_CONTENT_2 = "goldfish分享 #life ";

    // 回复内容
    private static final String REPLY_CONTENT_1 = "这个帖子不错！";
    private static final String REPLY_CONTENT_2 = "我也来说两句～";

    private static Long postId1,
            postId2,
            postToDelete1,
            postToDelete2;
    private Lock lock = new ReentrantLock();

    /**
     * 测试：用户1发帖
     */
    @Test
    @Order(1)
    @DisplayName("0. 用户1发帖")
    void testUser1CreatePost() {
        lock.lock();
//        try
        synchronized (this) {
            logger.info("🧪 正在执行测试: testUser1CreatePost");

            ForumPostPreview post = new ForumPostPreview();
            post.setTitle(POST_TITLE_1);
            post.setContent(POST_CONTENT_1);
            forumPostService.createPost(USER_ID_1, post);
            postId1 = getLatestPostId();
            assertNotNull(postId1, "❌ 用户1发帖失败");
            logger.info("✅ 用户 {} 成功发布帖子 ID={}", USER_ID_1, postId1);

            List<ForumPostPreview> postsSorted = forumPostService.getPostsSorted(
                    USER_ID_1,
                    KeywordType.TITLE,
                    "",
                    PostSortOrder.TIME,
                    new RowBounds(0, 10)
            );
            for (ForumPostPreview postTemp : postsSorted) {
                if (postTemp.getPostId().equals(postId1)) {
                    assertEquals(POST_TITLE_1, postTemp.getTitle(), "❌ 用户2发帖失败");
                }
            }
        }
//        finally {
//            System.err.println("unlock");
//            lock.unlock();
//        }
    }

    /**
     * 测试：用户2发帖
     */
    @Test
    @Order(2)
    @DisplayName("0. 用户2发帖")
    void testUser2CreatePost() {
        lock.lock();
        synchronized (this) {
            logger.info("🧪 正在执行测试: testUser2CreatePost");

            ForumPostPreview post = new ForumPostPreview();
            // 用唯一标题，防止查找混淆
            String realTitle = POST_TITLE_2 + "-" + System.currentTimeMillis();
            post.setTitle(realTitle);
            post.setContent(POST_CONTENT_2);
            postId2 = forumPostService.submitPost(USER_ID_2, post);
            logger.info("postId2 = {}", postId2);

            assertNotNull(postId2, "❌ 用户2发帖失败");
            logger.info("✅ 用户 {} 成功发布帖子 ID={}", USER_ID_2, postId2);

            List<ForumPostPreview> postsSorted = forumPostService.getPostsSorted(
                    USER_ID_2,
                    KeywordType.TITLE,
                    "",
                    PostSortOrder.TIME,
                    new RowBounds(0, 10)
            );
            boolean found = false;
            for (ForumPostPreview postTemp : postsSorted) {
                System.out.println(postTemp);
                // 只用postId2判断即可
                if (postTemp.getPostId().equals(postId2)) {
                    found = true;
                    break;
                }
            }
            assertTrue(found, "未找到");
        }
    }

    /**
     * 测试：发帖是否成功（依赖上面两个测试）
     */
    @Test
    @DisplayName("1. 用户可以正常发帖")
    @Order(16)
    void testCreatePost() {
        logger.info("🧪 正在执行测试: testCreatePost");
        assertNotNull(postId1, "❌ 帖子1发布失败");
        assertNotNull(postId2, "❌ 帖子2发布失败");
        logger.info("postId1 = {}, postId2 = {}", postId1, postId2);
        logger.info("✅ 发帖测试通过，两个帖子均成功创建");
    }

    /**
     * 获取最新帖子ID
     *
     * @return
     */
    private Long getLatestPostId() {
        List<Blog> blogs = blogMapper.selectSortedByTime(new RowBounds(0, 1));
        return blogs.stream()
                .max(Comparator.comparing(Blog::getId))
                .map(Blog::getId)
                .orElseThrow(() -> new RuntimeException("未找到最新帖子"));
    }

    /**
     * 获取最新回复id
     *
     * @param postId
     * @return
     */
    private Long getLatestReplyId(Long userId,Long postId) {
        List<Reply> replies = replyMapper.selectByBlogId(userId,postId);
        return replies.stream()
                .max(Comparator.comparing(Reply::getId))
                .map(Reply::getId)
                .orElseThrow(() -> new RuntimeException("未找到最新回复"));
    }

    @Test
    @DisplayName("2. 支持按标题搜索")
    @Order(21)
    void testSearchByTitle() {
        logger.info("🧪 正在执行测试: testSearchByTitle");
        List<ForumPostPreview> result = forumPostService.getPostsSorted(
                USER_ID_1,
                KeywordType.TITLE,
                "第一次",
                PostSortOrder.TIME,
                new RowBounds(0, 10)
        );
        logger.info("{}", result);

        boolean found = result.stream().anyMatch(p -> p.getPostId().equals(postId1));
        assertTrue(found, "❌ 未能按标题搜索到帖子");
        //日志显示搜到的所有帖子id和标题
        result.forEach(p -> logger.info("🔍 帖子ID={}, 标题={}", p.getPostId(), p.getTitle()));

        String keyword = "第一次";
logger.info("✅ 成功搜索到标题包含\"" + keyword + "\"的帖子");

    }

    @Test
    @DisplayName("3. 支持按发帖人搜索")
    @Order(22)
    void testSearchByCreator() {
        //日志输出搜索到的帖子的点赞数和回复树
        logger.info("🧪 正在执行测试: testSearchByCreator");
        List<ForumPostPreview> result = forumPostService.getPostsSorted(
                USER_ID_1,
                KeywordType.CREATOR,
                USER_ID_1.toString(),
                PostSortOrder.TIME,
                new RowBounds(0, 10)
        );

        boolean allCreatedByUser1 = result.stream()
                .allMatch(p -> p.getAuthorId().equals(USER_ID_1));

        assertTrue(allCreatedByUser1, "❌ 返回的帖子不全是用户1发布的");
        // 🔁 修改后的日志输出，包含 replyCount 和 likeCount
        result.forEach(p -> logger.info(
                "🔍 发帖人ID={}, 帖子ID={}, 帖子标题={}, 点赞数={}, 回复数={}",
                p.getAuthorId(), p.getPostId(), p.getTitle(), p.getLikeCount(), p.getReplyCount()
        ));
        logger.info("✅ 成功搜索到所有由用户 {} 创建的帖子", USER_ID_1);
    }

    final static String TAG_SEARCH_KEYWORD = "testtag";

    @Test
    @DisplayName("4. 支持按标签搜索（#" + TAG_SEARCH_KEYWORD + "）")
    @Order(23)
    void testSearchByTag() {
        logger.info("🧪 正在执行测试: testSearchByTag");
        List<ForumPostPreview> result = forumPostService.getPostsSorted(
                USER_ID_1,
                KeywordType.TAG,
                TAG_SEARCH_KEYWORD,
                PostSortOrder.TIME,
                new RowBounds(0, 10)
        );
        result.forEach(p -> logger.info("🔍 发帖人ID={}, 帖子ID={}, 帖子标题={}", p.getAuthorId(), p.getPostId(), p.getTitle()));
        boolean found = result.stream().anyMatch(p -> p.getPostId().equals(postId1));
//        found = !result.isEmpty();
        assertTrue(found, "❌ 未能按标签 #" + TAG_SEARCH_KEYWORD + " 搜索到帖子");
        logger.info("✅ 成功按标签搜索到帖子 ID={}", postId1);
    }

    @Test
    @DisplayName("5. 可以点赞和取消点赞")
    @Order(31)
    void testLikeAndUnlike() {
        logger.info("🧪 正在执行测试: testLikeAndUnlike");

        logger.info("👍 用户 {} 正在点赞帖子 {}", USER_ID_2, postId1);
        forumPostService.likePost(postId1, USER_ID_2);
        assertTrue(forumPostService.isLikedByUser(postId1, USER_ID_2));
        assertEquals(1, forumPostService.getPostLikeCount(postId1));
        logger.info("✅ 用户 {} 成功点赞帖子 {}", USER_ID_2, postId1);

//        logger.info("👎 用户 {} 正在取消点赞帖子 {}", USER_ID_2, postId1);
//        forumPostService.unlikePost(postId1, USER_ID_2);
//        assertFalse(forumPostService.isLikedByUser(postId1, USER_ID_2));
//        assertEquals(0, forumPostService.getPostLikeCount(postId1));
//        logger.info("✅ 用户 {} 成功取消点赞帖子 {}", USER_ID_2, postId1);
    }

    @Test
    @DisplayName("6. 可以回复帖子")
    @Order(41)
    void testReplyPost() {
        logger.info("🧪 正在执行测试: testReplyPost");
        logger.info("🗨️ 用户 {} 正在回复帖子 {}", USER_ID_2, postId1);

        ReplyView reply = new ReplyView();
        reply.setContent(REPLY_CONTENT_1);

        forumPostService.replyPost(USER_ID_2, postId1, reply);

        List<ReplyView> replies = forumPostService.getRepliesByPostId(USER_ID_2,postId1);
        boolean found = replies.stream().anyMatch(r -> r.getContent().equals(REPLY_CONTENT_1));
        assertTrue(found, "❌ 未找到指定回复内容");
        logger.info("✅ 成功回复帖子，内容为: {}", REPLY_CONTENT_1);
    }

    @Test
    @DisplayName("7. 支持分页查询")
    @Order(27)
    void testPagination() {
        logger.info("🧪 正在执行测试: testPagination");
        logger.info("🔄 准备批量发帖用于分页测试");

        for (int i = 0; i < 5; i++) {
            ForumPostPreview post = new ForumPostPreview();
            post.setTitle("测试帖子" + i);
            post.setContent("测试内容");
            forumPostService.createPost(USER_ID_1, post);
        }

        List<ForumPostPreview> page1 = forumPostService.getPostsSorted(
                USER_ID_1, KeywordType.TITLE, "", PostSortOrder.TIME, new RowBounds(0, 2));
        List<ForumPostPreview> page2 = forumPostService.getPostsSorted(
                USER_ID_1, KeywordType.TITLE, "", PostSortOrder.TIME, new RowBounds(2, 2));

        logger.info("📄 第一页获取到 {} 条帖子", page1.size());
        logger.info("📄 第二页获取到 {} 条帖子", page2.size());

        assertTrue(page1.size() > 0, "❌ 第一页无数据");
        assertNotEquals(page1, page2, "❌ 分页结果相同，可能未正确分页");
        logger.info("✅ 分页功能测试通过");

        // ====== 新增：断言时间降序 ======
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        List<ForumPostPreview> all = forumPostService.getPostsSorted(
                USER_ID_1, KeywordType.TITLE, "", PostSortOrder.TIME, new RowBounds(0, 10));
        for (int i = 1; i < all.size(); i++) {
            LocalDateTime prev = LocalDateTime.parse(all.get(i-1).getPublishTime(), formatter);
            LocalDateTime curr = LocalDateTime.parse(all.get(i).getPublishTime(), formatter);
            assertTrue(
                prev.isAfter(curr) || prev.isEqual(curr),
                "帖子未按时间降序排列"
            );
        }
        logger.info("✅ 帖子时间排序断言通过");
    }

    @Test
    @DisplayName("8. 支持删除自己的帖子")
    @Order(42)
    void testDeletePost() {
        // 先发一条帖子
        ForumPostPreview post = new ForumPostPreview();
        post.setTitle("待删除帖子");
        post.setContent("内容");
        forumPostService.createPost(USER_ID_1, post);
        Long idToDelete = getLatestPostId(); // 用你已有的工具方法获取最新帖子ID

        logger.info("🧪 正在执行测试: testDeletePost");
        logger.info("🗑️ 用户 {} 正在删除帖子 {}", USER_ID_1, idToDelete);

        forumPostService.deletePost(idToDelete, USER_ID_1);

        // 验证帖子确实被删除
        Blog deletedBlog = blogMapper.selectById(idToDelete);
        assertNull(deletedBlog, "❌ 帖子未被正确删除");

        logger.info("✅ 删除帖子测试通过");
    }


    @Test
    @DisplayName("9. 不能删除别人的帖子")
    @Order(41)
    void testCannotDeleteOthersPost() {
        // 先用用户1发一条帖子
        ForumPostPreview post = new ForumPostPreview();
        post.setTitle("别人发的帖子");
        post.setContent("内容");
        forumPostService.createPost(USER_ID_1, post);
        Long idToDelete = getLatestPostId();

        logger.info("🧪 正在执行测试: testCannotDeleteOthersPost");
        logger.info("🚫 用户 {} 尝试删除不属于自己的帖子 {}", USER_ID_2, idToDelete);

        Exception exception = assertThrows(CampusAidException.class, () ->
                forumPostService.deletePost(idToDelete, USER_ID_2)
        );

        assertTrue(exception.getMessage().contains("无权删除"), "❌ 抛出的异常不是权限错误");
        logger.error("❌ 预期异常: {}", exception.getMessage());
        logger.info("✅ 权限校验生效，无法删除他人帖子");
    }

    /**
     * 测试：使用 ReplyTreeConverter 构建嵌套回复树结构
     */
    @Test
    @DisplayName("10. 使用工具类构建嵌套回复结构")
    @Order(54)
    void testNestedRepliesWithBuildTree() {
        logger.info("🧪 正在执行测试: testNestedRepliesWithBuildTree");
//        postId1 = 161L;
        // Step 1: 用户2对帖子1进行一级回复
        ReplyView firstLevelReply = new ReplyView();
        firstLevelReply.setContent("这个帖子不错！");
        forumPostService.replyPost(USER_ID_2, postId1, firstLevelReply);
        Long firstLevelId = getLatestReplyId(USER_ID_2, postId1);
        assertNotNull(firstLevelId, "❌ 一级回复未成功插入");
        logger.info("✅ 成功创建一级回复 ID={}", firstLevelId);

        // Step 2: 用户1对一级回复进行二级回复
        ReplyView secondLevelReply = new ReplyView();
        secondLevelReply.setContent("谢谢夸奖！");
        secondLevelReply.setParentId(firstLevelId); // 设置父级ID
        forumPostService.replyPost(USER_ID_1, postId1, secondLevelReply);
        Long secondLevelId = getLatestReplyId(USER_ID_1, postId1);
        assertNotNull(secondLevelId, "❌ 二级回复未成功插入");
        logger.info("✅ 成功创建二级回复 ID={}", secondLevelId);

        // Step 3: 获取完整回复并用工具类构建树状结构
        List<Reply> allReplies = replyMapper.selectByBlogId(USER_ID_1, postId1);
        ReplyTreeConverter.Tree replyTreeView = ReplyTreeConverter.buildTree(allReplies);

        assertNotNull(replyTreeView, "❌ 回复树为 null");
        assertFalse(replyTreeView.getChildren().isEmpty(), "❌ 回复树为空");

        // Step 4: 打印树结构（调试用）
//        logger.info("🔍 当前回复树结构如下：");
//        for (ReplyView parent : replyTreeView.getChildren()) {
//            logger.info("🗨️ 一级回复: ID={}, 内容={}, 发帖人={}",
//                    parent.getId(), parent.getContent(), parent.getSenderId());
//
//            if (parent.getReplies() != null && !parent.getReplies().isEmpty()) {
//                for (ReplyView child : parent.getReplies()) {
//                    logger.info("⤷⤷ 二级回复: ID={}, 内容={}, 发帖人={}, 父ID={}",
//                            child.getId(), child.getContent(), child.getSenderId(), child.getParentId());
//                }
//            }
//        }
//
//        // Step 5: 验证结构是否正确
//        assertEquals(1, replyTreeView.size(), "❌ 一级回复数量不为1");
//
//        ReplyView topLevel = replyTreeView.get(0);
//        assertEquals(USER_ID_2, topLevel.getSenderId(), "❌ 一级回复用户不匹配");
//        assertEquals("这个帖子不错！", topLevel.getContent(), "❌ 一级回复内容不一致");
//        assertNull(topLevel.getParentId(), "❌ 一级回复父ID应为 null");
//
//        assertNotNull(topLevel.getReplies(), "❌ 子回复列表为 null");
//        assertEquals(1, topLevel.getReplies().size(), "❌ 二级回复数量不为1");
//
//        ReplyView nested = topLevel.getReplies().get(0);
//        assertEquals(USER_ID_1, nested.getSenderId(), "❌ 二级回复用户不匹配");
//        assertEquals("谢谢夸奖！", nested.getContent(), "❌ 二级回复内容不一致");
//        assertEquals(firstLevelId, nested.getParentId(), "❌ 二级回复父ID不一致");
//
//        logger.info("✅ 已通过 ReplyTreeConverter 构建出正确的嵌套回复结构");
    }

    @Test
    @DisplayName("11. 查询帖子的所有回复（扁平列表）")
    @Order(53)
    void testGetFlatReplies() {
        logger.info("🧪 正在执行测试: testGetFlatReplies");
        // Step 0: 新建一个干净的帖子
        ForumPostPreview post = new ForumPostPreview();
        post.setTitle("扁平回复测试帖");
        post.setContent("内容");
        forumPostService.createPost(USER_ID_1, post);
        Long testPostId = getLatestPostId();

        // Step 1: 用户2对帖子进行一级回复
        ReplyView firstLevelReply = new ReplyView();
        firstLevelReply.setContent("这个帖子不错！");
        forumPostService.replyPost(USER_ID_2, testPostId, firstLevelReply);
        Long firstLevelId = getLatestReplyId(USER_ID_2, testPostId);

        assertNotNull(firstLevelId, "❌ 一级回复未成功插入");
        logger.info("✅ 成功创建一级回复 ID={}", firstLevelId);

        // Step 2: 用户1对帖子再发一条一级回复
        ReplyView secondLevelReply = new ReplyView();
        secondLevelReply.setContent("我也想说点什么～");
        forumPostService.replyPost(USER_ID_1, testPostId, secondLevelReply);
        Long secondLevelId = getLatestReplyId(USER_ID_1, testPostId);

        assertNotNull(secondLevelId, "❌ 第二条回复未成功插入");
        logger.info("✅ 成功创建第二条回复 ID={}", secondLevelId);

        // Step 3: 获取扁平回复列表
        List<ReplyView> flatReplies = forumPostService.getRepliesByPostId(USER_ID_1, testPostId);

        assertNotNull(flatReplies, "❌ 回复列表为 null");
        assertEquals(2, flatReplies.size(), "❌ 回复数量不为2");

        logger.info("🔍 扁平回复列表如下:");
        for (ReplyView reply : flatReplies) {
            logger.info("🗨️ 回复ID={}, 内容='{}', 发帖人={}, 父回复ID={}",
                    reply.getId(), reply.getContent(), reply.getSenderId(), reply.getParentId());
        }

        // Step 4: 验证每条回复字段是否正确
        boolean foundFirst = false;
        boolean foundSecond = false;

        for (ReplyView reply : flatReplies) {
            if (reply.getId().equals(firstLevelId)) {
                assertEquals(USER_ID_2, reply.getSenderId());
                assertEquals("这个帖子不错！", reply.getContent());
                assertNull(reply.getParentId());
                foundFirst = true;
            } else if (reply.getId().equals(secondLevelId)) {
                assertEquals(USER_ID_1, reply.getSenderId());
                assertEquals("我也想说点什么～", reply.getContent());
                assertNull(reply.getParentId()); // 这是直接评论帖子，不是嵌套
                foundSecond = true;
            }
        }

        assertTrue(foundFirst, "❌ 未找到第一条回复");
        assertTrue(foundSecond, "❌ 未找到第二条回复");

        logger.info("✅ 成功通过 testGetFlatReplies，回复字段和数量均匹配");
    }

    @Test
    @DisplayName("删除不存在的帖子应抛出异常")
    void testDeleteNonExistentPost() {
        Exception exception = assertThrows(CampusAidException.class, () -> {
            forumPostService.deletePost(999999L, USER_ID_1); // 用一个一定不存在的ID
        });
        assertTrue(exception.getMessage().contains("帖子不存在"));
    }

}