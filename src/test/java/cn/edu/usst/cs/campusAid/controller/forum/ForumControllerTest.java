package cn.edu.usst.cs.campusAid.controller.forum;

import cn.edu.usst.cs.campusAid.dto.forum.*;
import cn.edu.usst.cs.campusAid.dto.complaint.ComplaintBlock;
import cn.edu.usst.cs.campusAid.service.CampusAidException;
import cn.edu.usst.cs.campusAid.service.UploadFileSystemService;
import cn.edu.usst.cs.campusAid.service.forum.ForumPostService;
import org.apache.ibatis.session.RowBounds;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ForumControllerTest {

    @Mock
    private ForumPostService forumPostService;

    @Mock
    private UploadFileSystemService uploadFileSystemService;

    @InjectMocks
    private ForumController forumController;

    private static final Long TEST_USER_ID = 2235062128L;
    private static final Long TEST_POST_ID = 1L;
    private static final String TEST_TITLE = "Test Post";
    private static final String TEST_CONTENT = "Test Content";

    private ForumPostPreview testPost;
    private List<ForumPostPreview> testPosts;
    private ReplyView testReply;
    private ReportRequest testReport;

    @BeforeEach
    void setUp() {
        // 初始化测试数据
        testPost = new ForumPostPreview();
        testPost.setPostId(TEST_POST_ID);
        testPost.setTitle(TEST_TITLE);
        testPost.setContent(TEST_CONTENT);
        testPost.setAuthorId(TEST_USER_ID);
        testPost.setAuthorName("Test User");
        testPost.setPublishTime("2024-03-20 10:00:00");
        testPost.setLikeCount(0);
        testPost.setReplyCount(0);
        testPost.setLiked(false);
        testPost.setTags(Arrays.asList("test", "forum"));

        testPosts = new ArrayList<>();
        testPosts.add(testPost);

        testReply = new ReplyView();
        testReply.setContent("Test Reply");
        testReply.setSenderId(TEST_USER_ID);
        testReply.setSendTime("2024-03-20 10:00:00");

        testReport = new ReportRequest();
        testReport.setPostId(TEST_POST_ID);
        testReport.setReasonText("Test Report");
        testReport.setImageUrls(new ArrayList<>());
    }

    @Test
    void testListPosts() {
        // 准备测试数据
        when(forumPostService.getPostsSorted(
                any(Long.class),
                any(KeywordType.class),
                any(String.class),
                any(PostSortOrder.class),
                any(RowBounds.class)
        )).thenReturn(testPosts);

        // 执行测试
        ResponseEntity<List<ForumPostPreview>> response = forumController.listPosts(
                TEST_USER_ID,
                KeywordType.TITLE,
                "test",
                PostSortOrder.TIME,
                0
        );

        // 验证结果
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(testPosts, response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(TEST_POST_ID, response.getBody().get(0).getPostId());

        // 验证服务调用
        verify(forumPostService).getPostsSorted(
                eq(TEST_USER_ID),
                eq(KeywordType.TITLE),
                eq("test"),
                eq(PostSortOrder.TIME),
                any(RowBounds.class)
        );
    }

    @Test
    void testGetPost() {
        // 准备测试数据
        when(forumPostService.getPostById(TEST_POST_ID)).thenReturn(testPost);

        // 执行测试
        ResponseEntity<ForumPostPreview> response = forumController.getPost(TEST_POST_ID, TEST_USER_ID);

        // 验证结果
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(testPost, response.getBody());
        assertEquals(TEST_POST_ID, response.getBody().getPostId());

        // 验证服务调用
        verify(forumPostService).getPostById(TEST_POST_ID);
    }

    @Test
    void testCreatePost() {
        // 准备测试数据
        doNothing().when(forumPostService).createPost(eq(TEST_USER_ID), any(ForumPostPreview.class));

        // 执行测试
        ResponseEntity<String> response = forumController.createPost(testPost, TEST_USER_ID);

        // 验证结果
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("发帖成功", response.getBody());

        // 验证服务调用
        verify(forumPostService).createPost(TEST_USER_ID, testPost);
    }

    @Test
    void testUploadPost() {
        // 准备测试数据
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.jpg",
                "image/jpeg",
                "test image content".getBytes()
        );
        String uploadedUri = "uploads/forum/" + TEST_POST_ID + "/test.jpg";
        when(forumPostService.uploadImage(eq(TEST_USER_ID), eq(TEST_POST_ID), any())).thenReturn(uploadedUri);

        // 执行测试
        ResponseEntity<String> response = forumController.uploadPost(TEST_POST_ID, file, TEST_USER_ID);

        // 验证结果
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().contains("上传成功"));
        assertTrue(response.getBody().contains(uploadedUri));

        // 验证服务调用
        verify(forumPostService).uploadImage(TEST_USER_ID, TEST_POST_ID, file);
    }

    @Test
    void testDeletePost() {
        // 准备测试数据
        doNothing().when(forumPostService).deletePost(eq(TEST_POST_ID), eq(TEST_USER_ID));

        // 执行测试
        ResponseEntity<String> response = forumController.deletePost(TEST_POST_ID, TEST_USER_ID);

        // 验证结果
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("删除成功", response.getBody());

        // 验证服务调用
        verify(forumPostService).deletePost(TEST_POST_ID, TEST_USER_ID);
    }

    @Test
    void testLikePost() {
        // 准备测试数据
        doNothing().when(forumPostService).likePost(eq(TEST_POST_ID), eq(TEST_USER_ID));

        // 执行测试
        ResponseEntity<String> response = forumController.likePost(TEST_POST_ID, TEST_USER_ID);

        // 验证结果
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("点赞成功", response.getBody());

        // 验证服务调用
        verify(forumPostService).likePost(TEST_POST_ID, TEST_USER_ID);
    }

    @Test
    void testUnlikePost() {
        // 准备测试数据
        doNothing().when(forumPostService).unlikePost(eq(TEST_POST_ID), eq(TEST_USER_ID));

        // 执行测试
        ResponseEntity<String> response = forumController.unlikePost(TEST_POST_ID, TEST_USER_ID);

        // 验证结果
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("取消点赞", response.getBody());

        // 验证服务调用
        verify(forumPostService).unlikePost(TEST_POST_ID, TEST_USER_ID);
    }

    @Test
    void testReplyPost() {
        // 准备测试数据
        doNothing().when(forumPostService).replyPost(eq(TEST_USER_ID), eq(TEST_POST_ID), any(ReplyView.class));

        // 执行测试
        ResponseEntity<String> response = forumController.replyPost(TEST_POST_ID, testReply, TEST_USER_ID);

        // 验证结果
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("回复成功", response.getBody());

        // 验证服务调用
        verify(forumPostService).replyPost(TEST_USER_ID, TEST_POST_ID, testReply);
    }

    @Test
    void testReportPost() {
        // 准备测试数据
        doNothing().when(forumPostService).reportPost(eq(TEST_USER_ID), any(ReportRequest.class));

        // 执行测试
        ResponseEntity<String> response = forumController.reportPost(TEST_USER_ID, testReport);

        // 验证结果
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("举报成功", response.getBody());

        // 验证服务调用
        verify(forumPostService).reportPost(TEST_USER_ID, testReport);
    }

    @Test
    void testUpdatePostVisibilityByAdmin() {
        // 准备测试数据
        doNothing().when(forumPostService).updatePostVisibility(eq(TEST_USER_ID), eq(TEST_POST_ID), eq(Visibility.ADMIN));

        // 执行测试
        ResponseEntity<String> response = forumController.updatePostVisibilityByAdmin(
                TEST_POST_ID,
                Visibility.ADMIN,
                TEST_USER_ID
        );

        // 验证结果
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("管理员可见性修改成功", response.getBody());

        // 验证服务调用
        verify(forumPostService).updatePostVisibility(TEST_USER_ID, TEST_POST_ID, Visibility.ADMIN);
    }

    @Test
    void testUpdatePostVisibilityBySender() {
        // 准备测试数据
        doNothing().when(forumPostService).updatePostVisibility(eq(TEST_USER_ID), eq(TEST_POST_ID), eq(Visibility.SENDER));

        // 执行测试
        ResponseEntity<String> response = forumController.updatePostVisibilityBySender(
                TEST_POST_ID,
                Visibility.SENDER,
                TEST_USER_ID
        );

        // 验证结果
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("发帖人可见性修改成功", response.getBody());

        // 验证服务调用
        verify(forumPostService).updatePostVisibility(TEST_USER_ID, TEST_POST_ID, Visibility.SENDER);
    }

    @Test
    void testGetPostContents() {
        // 准备测试数据
        File contentDir = new File("test/forum/" + TEST_POST_ID);
        String[] files = new String[]{"test1.jpg", "test2.jpg"};
        when(forumPostService.getAuthorID(TEST_POST_ID)).thenReturn(TEST_USER_ID);
        when(uploadFileSystemService.getBlogsUploadDir(TEST_POST_ID)).thenReturn(contentDir);
        when(uploadFileSystemService.listFiles(contentDir)).thenReturn(files);

        // 执行测试
        ResponseEntity<List<String>> response = forumController.getPostContents(TEST_USER_ID, TEST_POST_ID);

        // 验证结果
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(Arrays.asList(files), response.getBody());

        // 验证服务调用
        verify(forumPostService).getAuthorID(TEST_POST_ID);
        verify(uploadFileSystemService).getBlogsUploadDir(TEST_POST_ID);
        verify(uploadFileSystemService).listFiles(contentDir);
    }

    @Test
    void testGetPostContents_Unauthorized() {
        // 准备测试数据
        when(forumPostService.getAuthorID(TEST_POST_ID)).thenReturn(TEST_USER_ID + 1);

        // 执行测试并验证异常
        assertThrows(CampusAidException.class, () -> {
            forumController.getPostContents(TEST_USER_ID, TEST_POST_ID);
        });

        // 验证服务调用
        verify(forumPostService).getAuthorID(TEST_POST_ID);
        verify(uploadFileSystemService, never()).getBlogsUploadDir(any());
        verify(uploadFileSystemService, never()).listFiles(any());
    }
} 