package cn.edu.usst.cs.campusAid.service.impl;

import cn.edu.usst.cs.campusAid.service.UploadFileSystemService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@Slf4j
public class LocalUploadFileSystemService implements UploadFileSystemService {

    @Value("${app.upload-dir:uploads}")
    private String uploadDir;

    @Override
    public File getUploadRootDir() {
        log.info("Upload dir: {}", uploadDir);
        File rootDir;
        if (uploadDir.startsWith(".")) {
            // 如果是相对路径，则与项目工作目录拼接
            rootDir = new File(System.getProperty("user.dir"), uploadDir);
        } else {
            rootDir = new File(uploadDir);
        }
        ensureSubDir(rootDir);
        return rootDir;
    }
    @PostConstruct
    public void init() {
        ensureSubDir(getUploadRootDir());
        log.info("Upload root dir: {}", getUploadRootDir().getAbsolutePath());
    }

    // 复用接口中的静态方法
    static void ensureSubDir(File dir) {
        if (!dir.exists()) {
            // 创建目录前先检查父目录是否存在，逐级创建
            File parentDir = dir.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            if (!dir.mkdirs()) {
                throw new RuntimeException("无法创建上传目录: " + dir.getAbsolutePath());
            }
        }
    }
}
