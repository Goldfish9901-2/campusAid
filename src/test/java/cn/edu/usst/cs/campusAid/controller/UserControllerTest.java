package cn.edu.usst.cs.campusAid.controller;

import cn.edu.usst.cs.campusAid.model.User;
import cn.edu.usst.cs.campusAid.service.CampusAidException;
import cn.edu.usst.cs.campusAid.service.UploadFileSystemService;
import cn.edu.usst.cs.campusAid.service.auth.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UploadFileSystemService uploadFileSystemService;

    @InjectMocks
    private UserController userController;

    private static final Long TEST_USER_ID = 2235062129L;
    private static final String TEST_USER_NAME = "Test User";
    private static final Long TEST_PHONE = 13800138000L;
    private static final double TEST_BALANCE = 100.0;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(TEST_USER_ID)
                .name(TEST_USER_NAME)
                .phoneNumber(TEST_PHONE)
                .balance(TEST_BALANCE)
                .build();
    }

    @Test
    void testGetUserInfo_WhenViewingOwnInfo() {
        // 准备测试数据
        when(userService.getTargetUserId(TEST_USER_ID, TEST_USER_ID)).thenReturn(TEST_USER_ID);
        when(userService.getUserById(TEST_USER_ID)).thenReturn(testUser);
        when(userService.getBalance(TEST_USER_ID)).thenReturn(TEST_BALANCE);

        // 执行测试
        User response = userController.getUserInfo(TEST_USER_ID, TEST_USER_ID);

        // 验证结果
        assertNotNull(response);
        assertEquals(TEST_USER_ID, response.getId());
        assertEquals(TEST_USER_NAME, response.getName());
        assertEquals(TEST_PHONE, response.getPhoneNumber());
        assertEquals(TEST_BALANCE, response.getBalance());

        // 验证服务调用
        verify(userService).getTargetUserId(TEST_USER_ID, TEST_USER_ID);
        verify(userService).getUserById(TEST_USER_ID);
        verify(userService).getBalance(TEST_USER_ID);
    }

    @Test
    void testGetUserInfo_WhenViewingOtherUserInfo() {
        // 准备测试数据
        Long otherUserId = 2235062130L;
        when(userService.getTargetUserId(TEST_USER_ID, otherUserId)).thenReturn(TEST_USER_ID);
        when(userService.getUserById(TEST_USER_ID)).thenReturn(testUser);

        // 执行测试
        User response = userController.getUserInfo(TEST_USER_ID, otherUserId);

        // 验证结果
        assertNotNull(response);
        assertEquals(TEST_USER_ID, response.getId());
        assertEquals(TEST_USER_NAME, response.getName());
        assertEquals(TEST_PHONE, response.getPhoneNumber());
        assertEquals(-1, response.getBalance()); // 查看其他用户信息时，余额为-1

        // 验证服务调用
        verify(userService).getTargetUserId(TEST_USER_ID, otherUserId);
        verify(userService).getUserById(TEST_USER_ID);
        verify(userService, never()).getBalance(anyLong());
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
        File userDir = new File("test_dir");
        when(uploadFileSystemService.getUserDir(TEST_USER_ID)).thenReturn(userDir);
        when(uploadFileSystemService.uploadFile(any(File.class), any(MultipartFile.class)))
                .thenReturn("/test/test.jpg");

        // 执行测试
        ResponseEntity<String> response = userController.uploadPost(file, TEST_USER_ID);

        // 验证结果
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().contains("上传成功"));

        // 验证服务调用
        verify(uploadFileSystemService).getUserDir(TEST_USER_ID);
        verify(uploadFileSystemService).uploadFile(userDir, file);
    }

    @Test
    void testGetUploadedPosts() {
        // 准备测试数据
        File userDir = mock(File.class);
        File file1 = mock(File.class);
        File file2 = mock(File.class);
        when(file1.getName()).thenReturn("file1.jpg");
        when(file2.getName()).thenReturn("file2.jpg");
        File[] files = new File[]{file1, file2};
        when(uploadFileSystemService.getUserDir(TEST_USER_ID)).thenReturn(userDir);
        when(userDir.listFiles()).thenReturn(files);

        // 执行测试
        ResponseEntity<List<String>> response = userController.getUploadedPosts(TEST_USER_ID);

        // 验证结果
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        assertTrue(response.getBody().contains("file1.jpg"));
        assertTrue(response.getBody().contains("file2.jpg"));

        // 验证服务调用
        verify(uploadFileSystemService).getUserDir(TEST_USER_ID);
    }

    @Test
    void testGetUploadedPosts_WhenNoFiles() {
        // 准备测试数据
        File userDir = mock(File.class);
        when(uploadFileSystemService.getUserDir(TEST_USER_ID)).thenReturn(userDir);
        when(userDir.listFiles()).thenReturn(null);

        // 执行测试
        ResponseEntity<List<String>> response = userController.getUploadedPosts(TEST_USER_ID);

        // 验证结果
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isEmpty());

        // 验证服务调用
        verify(uploadFileSystemService).getUserDir(TEST_USER_ID);
    }
} 