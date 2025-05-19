package cn.edu.usst.cs.campusAid.service.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LocalUploadFileSystemServiceTest {

    @InjectMocks
    private LocalUploadFileSystemService uploadService;

    private static final String TEST_UPLOAD_DIR = "test-uploads";
    private File testUploadDir;

    @BeforeEach
    void setUp() {
        // 设置测试上传目录
        ReflectionTestUtils.setField(uploadService, "uploadDir", TEST_UPLOAD_DIR);
        testUploadDir = new File(System.getProperty("user.dir"), TEST_UPLOAD_DIR);
    }

    @AfterEach
    void tearDown() throws IOException {
        // 清理测试目录
        if (testUploadDir.exists()) {
            Files.walk(testUploadDir.toPath())
                    .map(Path::toFile)
                    .forEach(File::delete);
            testUploadDir.delete();
        }
    }

    @Test
    void testGetUploadRootDir_WithRelativePath() {
        // 执行测试
        File result = uploadService.getUploadRootDir();

        // 验证结果
        assertNotNull(result);
        assertTrue(result.exists());
        assertTrue(result.isDirectory());
        assertEquals(testUploadDir.getAbsolutePath(), result.getAbsolutePath());
    }

    @Test
    void testGetUploadRootDir_WithAbsolutePath() {
        // 准备测试数据
        String absolutePath = testUploadDir.getAbsolutePath();
        ReflectionTestUtils.setField(uploadService, "uploadDir", absolutePath);

        // 执行测试
        File result = uploadService.getUploadRootDir();

        // 验证结果
        assertNotNull(result);
        assertTrue(result.exists());
        assertTrue(result.isDirectory());
        assertEquals(absolutePath, result.getAbsolutePath());
    }

    @Test
    void testEnsureSubDir() {
        // 准备测试数据
        File subDir = new File(testUploadDir, "subdir");

        // 执行测试
        LocalUploadFileSystemService.ensureSubDir(subDir);

        // 验证结果
        assertTrue(subDir.exists());
        assertTrue(subDir.isDirectory());
    }

    @Test
    void testEnsureSubDir_WithNestedPath() {
        // 准备测试数据
        File nestedDir = new File(testUploadDir, "nested/sub/dir");

        // 执行测试
        LocalUploadFileSystemService.ensureSubDir(nestedDir);

        // 验证结果
        assertTrue(nestedDir.exists());
        assertTrue(nestedDir.isDirectory());
    }

    @Test
    void testInit() {
        // 执行测试
        uploadService.init();

        // 验证结果
        assertTrue(testUploadDir.exists());
        assertTrue(testUploadDir.isDirectory());
    }
} 