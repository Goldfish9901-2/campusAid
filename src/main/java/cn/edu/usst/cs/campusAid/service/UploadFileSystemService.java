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
        return blogsDir;
    }

    default File getBlogsUploadDir(Long blogId) {
        File blogsDir = new File(getBlogsUploadDir(), String.valueOf(blogId));
        if (!blogsDir.exists()) {
            if (!blogsDir.mkdir())
                throw new CampusAidException("创建 " + blogsDir + " 失败");
        }
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
        if (!shopDir.exists()) {
            if (!shopDir.mkdir())
                throw new CampusAidException("创建 " + shopDir + " 失败");
        }
        return shopDir;
    }

    /**
     * @param shopName    商户名
     * @param productName 商品名（同一商户不能够拥有多个同名商品）
     * @return 文件夹
     */
    default File getProductDir(String shopName, String productName) {
        File productDir = new File(getShopDir(shopName), productName);
        if (!productDir.exists()) {
            if (!productDir.mkdir())
                throw new CampusAidException("创建 " + productDir + " 失败");
        }
        return productDir;
    }
}
