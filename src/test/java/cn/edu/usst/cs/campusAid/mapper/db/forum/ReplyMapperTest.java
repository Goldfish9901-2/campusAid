package cn.edu.usst.cs.campusAid.mapper.db.forum;

import cn.edu.usst.cs.campusAid.dto.forum.Visibility;
import cn.edu.usst.cs.campusAid.model.forum.Blog;
import cn.edu.usst.cs.campusAid.model.forum.Reply;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ReplyMapperTest {
    private static final Logger logger = LoggerFactory.getLogger(ReplyMapperTest.class);

    @Autowired
    private ReplyMapper replyMapper;

    @Autowired
    private BlogMapper blogMapper;

    private static final Random random = new Random();
    private static final Long TEST_USER_ID = 2235062128L;
    private static final String TEST_TITLE = "测试博客标题";
    private static final String TEST_CONTENT = "测试博客内容";
    private static final String TEST_REPLY_CONTENT = "测试回复内容";

    private Blog createTestBlog() {
        Blog blog = new Blog();
        blog.setTitle(TEST_TITLE);
        blog.setContent(TEST_CONTENT);
        blog.setCreator(TEST_USER_ID);
        blog.setVisibility(Visibility.VISIBLE.getValue());
        blog.setSendTime(LocalDateTime.now());
        return blog;
    }

    private Reply createTestReply(Long blogId, Long parentId) {
        Reply reply = new Reply();
        reply.setBlogId(blogId);
        reply.setSender(TEST_USER_ID);
        reply.setContent(TEST_REPLY_CONTENT);
        reply.setSendTime(LocalDateTime.now());
        reply.setParentId(parentId);
        return reply;
    }

    @Test
    @DisplayName("测试回复的创建和查询")
    void testCreateAndQuery() {
        // 1. 创建测试博客
        Blog testBlog = createTestBlog();
        blogMapper.insertBlog(testBlog);
        logger.info("插入测试博客成功，ID: {}", testBlog.getId());

        // 2. 创建测试回复
        Reply testReply = createTestReply(testBlog.getId(), null);
        replyMapper.insertReply(testReply);
        logger.info("插入测试回复成功");

        // 3. 查询博客的回复
        List<Reply> replies = replyMapper.selectByBlogId(TEST_USER_ID, testBlog.getId());
        assertFalse(replies.isEmpty(), "查询不应返回空列表");
        assertEquals(TEST_REPLY_CONTENT, replies.get(0).getContent(), "回复内容不匹配");
        assertEquals(TEST_USER_ID, replies.get(0).getSender(), "回复者不匹配");

        // 4. 检查回复数量
        int replyCount = replyMapper.countRepliesByBlogId(testBlog.getId());
        assertEquals(1, replyCount, "博客应该有1个回复");
    }

    @Test
    @DisplayName("测试回复的删除")
    void testDelete() {
        // 1. 创建测试博客
        Blog testBlog = createTestBlog();
        blogMapper.insertBlog(testBlog);
        logger.info("插入测试博客成功，ID: {}", testBlog.getId());

        // 2. 创建测试回复
        Reply testReply = createTestReply(testBlog.getId(), null);
        replyMapper.insertReply(testReply);
        logger.info("插入测试回复成功");

        // 3. 查询确认存在
        List<Reply> replies = replyMapper.selectByBlogId(TEST_USER_ID, testBlog.getId());
        assertFalse(replies.isEmpty(), "删除前回复应存在");
        Long replyId = replies.get(0).getId();

        // 4. 删除回复
        replyMapper.deleteById(replyId);

        // 5. 再次查询确认已删除
        List<Reply> repliesAfterDelete = replyMapper.selectByBlogId(TEST_USER_ID, testBlog.getId());
        assertTrue(repliesAfterDelete.isEmpty(), "删除后回复不应存在");
    }

    @Test
    @DisplayName("测试删除博客时级联删除回复")
    void testDeleteByBlogId() {
        // 1. 创建测试博客
        Blog testBlog = createTestBlog();
        blogMapper.insertBlog(testBlog);
        logger.info("插入测试博客成功，ID: {}", testBlog.getId());

        // 2. 创建测试回复
        Reply testReply = createTestReply(testBlog.getId(), null);
        replyMapper.insertReply(testReply);
        logger.info("插入测试回复成功");

        // 3. 检查回复是否存在
        List<Reply> replies = replyMapper.selectByBlogId(TEST_USER_ID, testBlog.getId());
        assertFalse(replies.isEmpty(), "删除前回复应存在");

        // 4. 删除博客的所有回复
        replyMapper.deleteByBlogId(testBlog.getId());

        // 5. 检查回复是否已删除
        List<Reply> repliesAfterDelete = replyMapper.selectByBlogId(TEST_USER_ID, testBlog.getId());
        assertTrue(repliesAfterDelete.isEmpty(), "删除后回复不应存在");
    }

    @Test
    @DisplayName("测试批量查询回复数")
    void testBatchQueryReplies() {
        // 1. 创建多个测试博客
        List<Blog> testBlogs = List.of(
            createTestBlog(),
            createTestBlog(),
            createTestBlog()
        );
        
        // 插入博客并记录ID
        List<Long> blogIds = testBlogs.stream()
            .map(blog -> {
                blogMapper.insertBlog(blog);
                return blog.getId();
            })
            .toList();
        logger.info("创建多个测试博客成功，IDs: {}", blogIds);

        // 2. 为每个博客创建回复
        for (Long blogId : blogIds) {
            Reply reply = createTestReply(blogId, null);
            replyMapper.insertReply(reply);
        }
        logger.info("插入多个测试回复成功");

        // 3. 批量查询回复数
        List<Map<String, Object>> replyCounts = replyMapper.countRepliesByBlogIds(blogIds);
        assertFalse(replyCounts.isEmpty(), "批量查询不应返回空列表");
        assertEquals(blogIds.size(), replyCounts.size(), "返回的回复数应与博客数相同");

        // 4. 验证每个博客的回复数
        for (Map<String, Object> replyCount : replyCounts) {
            Long blogId = (Long) replyCount.get("blogId");
            Long count = (Long) replyCount.get("replyCount");
            assertTrue(blogIds.contains(blogId), "返回的博客ID应在查询列表中");
            assertEquals(1L, count, "每个博客应该有1个回复");
        }
    }

    @Test
    @DisplayName("测试回复的层级关系")
    void testReplyHierarchy() {
        // 1. 创建测试博客
        Blog testBlog = createTestBlog();
        blogMapper.insertBlog(testBlog);
        logger.info("插入测试博客成功，ID: {}", testBlog.getId());

        // 2. 创建父回复
        Reply parentReply = createTestReply(testBlog.getId(), null);
        replyMapper.insertReply(parentReply);
        logger.info("插入父回复成功，ID: {}", parentReply.getId());

        // 3. 创建子回复
        Reply childReply = createTestReply(testBlog.getId(), parentReply.getId());
        replyMapper.insertReply(childReply);
        logger.info("插入子回复成功，ID: {}", childReply.getId());

        // 4. 查询所有回复
        List<Reply> replies = replyMapper.selectByBlogId(TEST_USER_ID, testBlog.getId());
        assertEquals(2, replies.size(), "应该有2个回复");

        // 5. 验证子回复的父ID
        Reply foundChildReply = replies.stream()
            .filter(reply -> reply.getParentId() != null)
            .findFirst()
            .orElse(null);
        assertNotNull(foundChildReply, "应该找到子回复");
        assertEquals(parentReply.getId(), foundChildReply.getParentId(), "子回复的父ID不匹配");
    }
} 