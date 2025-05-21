package cn.edu.usst.cs.campusAid.mapper.db.forum;

import cn.edu.usst.cs.campusAid.dto.forum.Visibility;
import cn.edu.usst.cs.campusAid.model.forum.Blog;
import cn.edu.usst.cs.campusAid.model.forum.LikeBlog;
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
public class LikeBlogMapperTest {
    private static final Logger logger = LoggerFactory.getLogger(LikeBlogMapperTest.class);

    @Autowired
    private LikeBlogMapper likeBlogMapper;

    @Autowired
    private BlogMapper blogMapper;

    private static final Random random = new Random();
    private static final Long TEST_USER_ID = 2235062128L;
    private static final String TEST_TITLE = "测试博客标题";
    private static final String TEST_CONTENT = "测试博客内容";

    private Blog createTestBlog() {
        Blog blog = new Blog();
        blog.setTitle(TEST_TITLE);
        blog.setContent(TEST_CONTENT);
        blog.setCreator(TEST_USER_ID);
        blog.setVisibility(Visibility.VISIBLE.getValue());
        blog.setSendTime(LocalDateTime.now());
        return blog;
    }

    private LikeBlog createTestLike(Long blogId) {
        LikeBlog likeBlog = new LikeBlog();
        likeBlog.setBlogId(blogId);
        likeBlog.setLiker(TEST_USER_ID);
        return likeBlog;
    }

    @Test
    @DisplayName("测试点赞的创建和查询")
    void testCreateAndQuery() {
        // 1. 创建测试博客
        Blog testBlog = createTestBlog();
        blogMapper.insertBlog(testBlog);
        logger.info("插入测试博客成功，ID: {}", testBlog.getId());

        // 2. 创建测试点赞
        LikeBlog testLike = createTestLike(testBlog.getId());
        likeBlogMapper.insertLike(testLike);
        logger.info("插入测试点赞成功");

        // 3. 检查用户是否已点赞
        int likeCount = likeBlogMapper.countLikesByUserAndPost(testBlog.getId(), TEST_USER_ID);
        assertEquals(1, likeCount, "用户应该已点赞该博客");

        // 4. 检查博客的点赞总数
        int totalLikes = likeBlogMapper.countLikes(testBlog.getId());
        assertEquals(1, totalLikes, "博客应该有1个点赞");
    }

    @Test
    @DisplayName("测试取消点赞")
    void testDeleteLike() {
        // 1. 创建测试博客
        Blog testBlog = createTestBlog();
        blogMapper.insertBlog(testBlog);
        logger.info("插入测试博客成功，ID: {}", testBlog.getId());

        // 2. 创建测试点赞
        LikeBlog testLike = createTestLike(testBlog.getId());
        likeBlogMapper.insertLike(testLike);
        logger.info("插入测试点赞成功");

        // 3. 检查点赞是否存在
        int beforeDelete = likeBlogMapper.countLikesByUserAndPost(testBlog.getId(), TEST_USER_ID);
        assertEquals(1, beforeDelete, "删除前应该存在点赞");

        // 4. 删除点赞
        likeBlogMapper.deleteLike(testBlog.getId(), TEST_USER_ID);

        // 5. 检查点赞是否已删除
        int afterDelete = likeBlogMapper.countLikesByUserAndPost(testBlog.getId(), TEST_USER_ID);
        assertEquals(0, afterDelete, "删除后不应该存在点赞");
    }

    @Test
    @DisplayName("测试批量查询点赞数")
    void testBatchQueryLikes() {
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

        // 2. 为每个博客创建点赞
        for (Long blogId : blogIds) {
            LikeBlog likeBlog = createTestLike(blogId);
            likeBlogMapper.insertLike(likeBlog);
        }
        logger.info("插入多个测试点赞成功");

        // 3. 批量查询点赞数
        List<Map<String, Object>> likeCounts = likeBlogMapper.countLikesByBlogIds(blogIds);
        assertFalse(likeCounts.isEmpty(), "批量查询不应返回空列表");
        assertEquals(blogIds.size(), likeCounts.size(), "返回的点赞数应与博客数相同");

        // 4. 验证每个博客的点赞数
        for (Map<String, Object> likeCount : likeCounts) {
            Long blogId = (Long) likeCount.get("blogId");
            Long count = (Long) likeCount.get("likeCount");
            assertTrue(blogIds.contains(blogId), "返回的博客ID应在查询列表中");
            assertEquals(1L, count, "每个博客应该有1个点赞");
        }
    }

    @Test
    @DisplayName("测试删除博客时级联删除点赞")
    void testDeleteByBlogId() {
        // 1. 创建测试博客
        Blog testBlog = createTestBlog();
        blogMapper.insertBlog(testBlog);
        logger.info("插入测试博客成功，ID: {}", testBlog.getId());

        // 2. 创建测试点赞
        LikeBlog testLike = createTestLike(testBlog.getId());
        likeBlogMapper.insertLike(testLike);
        logger.info("插入测试点赞成功");

        // 3. 检查点赞是否存在
        int beforeDelete = likeBlogMapper.countLikesByUserAndPost(testBlog.getId(), TEST_USER_ID);
        assertEquals(1, beforeDelete, "删除前应该存在点赞");

        // 4. 删除博客的所有点赞
        likeBlogMapper.deleteByBlogId(testBlog.getId());

        // 5. 检查点赞是否已删除
        int afterDelete = likeBlogMapper.countLikesByUserAndPost(testBlog.getId(), TEST_USER_ID);
        assertEquals(0, afterDelete, "删除后不应该存在点赞");
    }
} 