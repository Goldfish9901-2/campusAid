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
        File rootDir = new File(uploadDir);
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
        if (!dir.exists() && !dir.mkdirs()) {
            throw new RuntimeException("无法创建上传目录: " + dir.getAbsolutePath());
        }
    }
}
