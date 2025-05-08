package cn.edu.usst.cs.campusAid.service;

import java.io.File;

public interface UploadFileSystemService {
    File getUploadRootDir();

    default File getBlogsUploadDir() {
        File rootDir = getUploadRootDir();
        File blogsDir = new File(rootDir, "blogs");
        if (!blogsDir.exists()) {
            if (!blogsDir.mkdir())
                throw new CampusAidException("创建 " + blogsDir + " 失败");
        }
        return blogsDir.getAbsoluteFile();
    }
    default File getBlogsUploadDir(Long userId) {
        File blogsDir = new File(getBlogsUploadDir(),String.valueOf(userId));
        if (!blogsDir.exists()) {
            if (!blogsDir.mkdir())
                throw new CampusAidException("创建 " + blogsDir + " 失败");
        }
        return blogsDir.getAbsoluteFile();
    }
}
