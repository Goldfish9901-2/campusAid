package cn.edu.usst.cs.campusAid.mapper.db.forum;

import cn.edu.usst.cs.campusAid.dto.forum.Visibility;
import cn.edu.usst.cs.campusAid.model.forum.Blog;
import org.apache.ibatis.session.RowBounds;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class BlogMapperTest {
    private static final Logger logger = LoggerFactory.getLogger(BlogMapperTest.class);

    @Autowired
    private BlogMapper blogMapper;

    private static final Random random = new Random();
    private static final String TEST_TITLE = "测试博客标题";
    private static final String TEST_CONTENT = "测试博客内容 #测试标签 ";
    private static final Long TEST_CREATOR = 2235062128L;

    private Blog createTestBlog() {
        Blog blog = new Blog();
        blog.setTitle(TEST_TITLE);
        blog.setContent(TEST_CONTENT);
        blog.setCreator(TEST_CREATOR);
        blog.setVisibility(Visibility.VISIBLE.getValue());
        blog.setSendTime(LocalDateTime.now());
        return blog;
    }

    @Test
    @DisplayName("测试博客的创建和查询")
    void testCreateAndQuery() {
        // 1. 创建测试博客
        Blog testBlog = createTestBlog();
        blogMapper.insertBlog(testBlog);
        logger.info("插入测试博客成功");

        // 2. 通过标题查询
        List<Blog> foundByTitle = blogMapper.selectByTitle(TEST_TITLE, new RowBounds(0, 10));
        assertFalse(foundByTitle.isEmpty(), "通过标题查询不应返回空列表");
        assertEquals(TEST_TITLE, foundByTitle.get(0).getTitle(), "标题不匹配");
        assertEquals(TEST_CONTENT, foundByTitle.get(0).getContent(), "内容不匹配");

        // 3. 通过创建者查询
        List<Blog> foundByCreator = blogMapper.selectByCreator(TEST_CREATOR.toString(), new RowBounds(0, 10));
        assertFalse(foundByCreator.isEmpty(), "通过创建者查询不应返回空列表");
        assertEquals(TEST_CREATOR, foundByCreator.get(0).getCreator(), "创建者不匹配");

        // 4. 通过标签查询
        List<Blog> foundByTag = blogMapper.selectByContentTag("测试标签", new RowBounds(0, 10));
        assertFalse(foundByTag.isEmpty(), "通过标签查询不应返回空列表");
        assertTrue(foundByTag.get(0).getContent().contains("#测试标签"), "内容应包含测试标签");
    }

    @Test
    @DisplayName("测试博客的删除")
    void testDelete() {
        // 1. 创建测试博客
        Blog testBlog = createTestBlog();
        blogMapper.insertBlog(testBlog);
        logger.info("插入测试博客成功");

        // 2. 查询确认存在
        List<Blog> foundByTitle = blogMapper.selectByTitle(TEST_TITLE, new RowBounds(0, 10));
        assertFalse(foundByTitle.isEmpty(), "删除前博客应存在");

        // 3. 删除博客
        blogMapper.deleteById(foundByTitle.get(0).getId());

        // 4. 再次查询确认已删除
        List<Blog> foundAfterDelete = blogMapper.selectByTitle(TEST_TITLE, new RowBounds(0, 10));
        assertTrue(foundAfterDelete.isEmpty(), "删除后博客不应存在");
    }

    @Test
    @DisplayName("测试博客的可见性修改")
    void testUpdateVisibility() {
        // 1. 创建测试博客
        Blog testBlog = createTestBlog();
        blogMapper.insertBlog(testBlog);
        logger.info("插入测试博客成功");

        // 2. 查询确认存在
        List<Blog> foundByTitle = blogMapper.selectByTitle(TEST_TITLE, new RowBounds(0, 10));
        assertFalse(foundByTitle.isEmpty(), "修改前博客应存在");
        Long blogId = foundByTitle.get(0).getId();

        // 3. 修改可见性
        blogMapper.updateVisibility(blogId, Visibility.ADMIN);

        // 4. 查询确认修改成功
        Blog updatedBlog = blogMapper.selectById(blogId);
        assertEquals(Visibility.ADMIN.getValue(), updatedBlog.getVisibility(), "可见性修改失败");
    }

    @Test
    @DisplayName("测试博客的排序功能")
    void testSorting() {
        // 1. 创建多个测试博客
        for (int i = 0; i < 3; i++) {
            Blog blog = createTestBlog();
            blog.setTitle(TEST_TITLE + i);
            blogMapper.insertBlog(blog);
        }
        logger.info("插入多个测试博客成功");

        // 2. 测试按时间排序
        List<Blog> sortedByTime = blogMapper.selectSortedByTime(new RowBounds(0, 10));
        assertFalse(sortedByTime.isEmpty(), "按时间排序不应返回空列表");
        assertTrue(isSortedByTime(sortedByTime), "博客应按时间降序排列");

        // 3. 测试按点赞数排序
        List<Blog> sortedByLikeCount = blogMapper.selectSortedByLikeCount(new RowBounds(0, 10));
        assertFalse(sortedByLikeCount.isEmpty(), "按点赞数排序不应返回空列表");

        // 4. 测试按回复数排序
        List<Blog> sortedByReplyCount = blogMapper.selectSortedByReplyCount(new RowBounds(0, 10));
        assertFalse(sortedByReplyCount.isEmpty(), "按回复数排序不应返回空列表");
    }

    private boolean isSortedByTime(List<Blog> blogs) {
        for (int i = 0; i < blogs.size() - 1; i++) {
            if (blogs.get(i).getSendTime().isBefore(blogs.get(i + 1).getSendTime())) {
                return false;
            }
        }
        return true;
    }
} 