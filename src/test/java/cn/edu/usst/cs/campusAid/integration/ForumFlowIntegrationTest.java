package cn.edu.usst.cs.campusAid.integration;

import cn.edu.usst.cs.campusAid.dto.forum.ForumPostPreview;
import cn.edu.usst.cs.campusAid.dto.forum.KeywordType;
import cn.edu.usst.cs.campusAid.dto.forum.PostSortOrder;
import cn.edu.usst.cs.campusAid.model.User;
import cn.edu.usst.cs.campusAid.model.forum.Blog;
import cn.edu.usst.cs.campusAid.mapper.db.UserMapper;
import cn.edu.usst.cs.campusAid.mapper.db.forum.BlogMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource(properties = {
    "jdk.instrument.traceUsage=false",
    "jdk.instrument.ignoreAgent=true"
})
public class ForumFlowIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BlogMapper blogMapper;

    private User testUser;
    private Blog testPost;
    private String userSessionCookie;

    private HttpHeaders createAuthHeaders(Long userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-User-Id", userId.toString());
        return headers;
    }

    @BeforeEach
    void setUp() {
        // 创建测试用户
        testUser = createTestUser("测试用户", 13800138000L);

        // 创建测试帖子
        testPost = new Blog();
        testPost.setId(generateUniqueId());
        testPost.setCreator(testUser.getId());
        testPost.setTitle("测试帖子");
        testPost.setContent("这是一个测试帖子的内容");
        testPost.setSendTime(LocalDateTime.now());
        testPost.setVisibility("VISIBLE");
        blogMapper.insertBlog(testPost);

        // 登录获取Session
        userSessionCookie = loginAndGetSessionCookie(String.valueOf(testUser.getPhoneNumber()), "123456");
    }

    @Test
    void testListPosts() {
        // 1. 获取帖子列表
        HttpEntity<?> listEntity = new HttpEntity<>(createSessionHeaders(userSessionCookie));
        ResponseEntity<List<ForumPostPreview>> listResponse = restTemplate.exchange(
            getBaseUrl() + "/api/forum/posts?type=TITLE&keyword=测试&sortBy=TIME&page=0",
            HttpMethod.GET,
            listEntity,
            new ParameterizedTypeReference<List<ForumPostPreview>>() {}
        );
        assertEquals(HttpStatus.OK, listResponse.getStatusCode());
        assertNotNull(listResponse.getBody());
        assertFalse(listResponse.getBody().isEmpty());

        // 2. 验证帖子内容
        List<ForumPostPreview> posts = listResponse.getBody();
        ForumPostPreview post = posts.get(0);
        assertEquals(testPost.getTitle(), post.getTitle());
        assertEquals(testUser.getName(), post.getAuthorName());
    }

    @Test
    void testGetPostDetail() {
        // 1. 获取帖子详情
        HttpEntity<?> detailEntity = new HttpEntity<>(createSessionHeaders(userSessionCookie));
        ResponseEntity<ForumPostPreview> detailResponse = restTemplate.exchange(
            getBaseUrl() + "/api/forum/post/" + testPost.getId(),
            HttpMethod.GET,
            detailEntity,
            ForumPostPreview.class
        );
        assertEquals(HttpStatus.OK, detailResponse.getStatusCode());
        assertNotNull(detailResponse.getBody());

        // 2. 验证帖子详情
        ForumPostPreview post = detailResponse.getBody();
        assertEquals(testPost.getId(), post.getPostId());
        assertEquals(testPost.getTitle(), post.getTitle());
        assertEquals(testPost.getContent(), post.getContent());
        assertEquals(testUser.getId(), post.getAuthorId());
    }
} 