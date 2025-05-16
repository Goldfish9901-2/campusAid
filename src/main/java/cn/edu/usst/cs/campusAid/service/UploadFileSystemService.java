package cn.edu.usst.cs.campusAid.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Arrays;

public interface UploadFileSystemService {
    static void ensureSubDir(File productDir) {
        if (!productDir.exists()) {
            if (!productDir.mkdir())
                throw new CampusAidException("创建 " + productDir + " 失败");
        }
    }

    File getUploadRootDir();

    default File getBlogsUploadDir() {
        File rootDir = getUploadRootDir();
        File blogsDir = new File(rootDir, "blogs");
        ensureSubDir(blogsDir);
        return blogsDir;
    }

    default File getBlogsUploadDir(Long blogId) {
        File blogsDir = new File(getBlogsUploadDir(), String.valueOf(blogId));
        ensureSubDir(blogsDir);
        return blogsDir;
    }

    /**
     * 根据商户名给出其上传商品图片的文件夹
     *
     * @param shopName 商户名
     * @return 文件夹
     * @throws CampusAidException 如果创建文件夹失败
     */
    default File getShopDir(String shopName) {
        File shopDir = new File(getUploadRootDir(), shopName);
        ensureSubDir(shopDir);
        return shopDir;
    }

    /**
     * @param shopName    商户名
     * @param productName 商品名（同一商户不能够拥有多个同名商品）
     * @return 文件夹
     */
    default File getProductDir(String shopName, String productName) {
        File productDir = new File(getShopDir(shopName), productName);
        ensureSubDir(productDir);
        return productDir;
    }

    default File getErrandsUploadDir() {
        File rootDir = getUploadRootDir();
        File errandsDir = new File(rootDir, "errands");
        ensureSubDir(errandsDir);
        return errandsDir;
    }
    default File getErrandDir(Long errandId) {
        File errandDir = new File(getErrandsUploadDir(), String.valueOf(errandId));
        ensureSubDir(errandDir);
        return errandDir;
    }

    default File getUserDir(Long userId) {
        File userDir = new File(getUploadRootDir(), String.valueOf(userId));
        ensureSubDir(userDir);
        return userDir;
    }

    // 下面是各板块通用的方法

    default String getFileExtension(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null && originalFilename.contains(".")) {
            return originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        // 回退：尝试从 content-type 获取
        String contentType = file.getContentType();
        try {
            if (contentType != null) {
                return switch (contentType) {
                    case "image/jpeg" -> ".jpg";
                    case "image/png" -> ".png";
                    case "image/gif" -> ".gif";
                    case "application/pdf" -> ".pdf";
                    default -> contentType.split("/")[1];
                };
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
        return ".unknown";
    }

    default String uploadFile(File dir, MultipartFile file) {
        var contentType = file.getContentType();
        String fileName = file.getOriginalFilename();
        if (contentType == null || !contentType.startsWith("image")) {
            throw new CampusAidException("请上传图片");
        }         String defaultSuffix = getFileExtension(file);
//            fileName = fileName == null ? LocalDateTime.now() + defaultSuffix : fileName;
        if (fileName == null) {
            fileName = LocalDateTime.now() + defaultSuffix;
        } else {
            for (var split_candidates : Arrays.asList("/", "\\")) {
                if (fileName.contains(split_candidates)) {
                    fileName = fileName.substring(fileName.lastIndexOf(split_candidates) + 1);
                }
            }
        }
        try {
            File targetLocation = new File(dir, fileName);
            file.transferTo(targetLocation);
            String relative_location = targetLocation.getAbsolutePath().replace(
                    getUploadRootDir().toString(), ""
            ).replace("\\", "/");
            return relative_location;
        } catch (Exception e) {
            throw new CampusAidException("上传文件失败", e);
        }
    }


}
