package cn.edu.usst.cs.campusAid;

import cn.edu.usst.cs.campusAid.config.SecurityProperties;
import cn.edu.usst.cs.campusAid.dto.forum.*;
import cn.edu.usst.cs.campusAid.service.CampusAidException;
import cn.edu.usst.cs.campusAid.service.ErrandService;
import cn.edu.usst.cs.campusAid.service.ForumPostService;
import cn.edu.usst.cs.campusAid.service.UploadFileSystemService;
import cn.edu.usst.cs.campusAid.service.shop.ShopService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
@MapperScan("cn.edu.usst.cs.campusAid.mapper.db") // 只扫描真正的数据库 Mapper
@SpringBootApplication
@EnableConfigurationProperties(
        {
                SecurityProperties.class
        }
)
public class CampusAidApplication {

    public static void main(String[] args) {

        SpringApplication.run(CampusAidApplication.class, args);
    }


    private ExecutorService executorService;

    @Bean
    public ExecutorService getExecutorService() {
        if (executorService == null) {
            executorService = java.util.concurrent.Executors.newFixedThreadPool(10);
        }
        return executorService;
    }

    @Bean
    public UploadFileSystemService getUploadFileSystemService() {
        return new UploadFileSystemService() {
            final String uploadRootDir = "uploads/";

            @Override
            public File getUploadRootDir() {
                File dir = new File(uploadRootDir);
                if (!dir.exists() && !dir.mkdirs())
                    throw new CampusAidException("Failed to create upload directory");

                return dir;
            }

        };
    }

//    @Bean
//    public ForumPostService getForumPostService() {
//        return new ForumPostService() {
//            @Autowired
//            private UploadFileSystemService uploadFileSystemService;
//
//            @Override
//            public List<ForumPostPreview> getPostsSorted(Long userId, KeywordType type, String keyword, PostSortOrder sortBy) {
//                return List.of();
//            }
//
//            @Override
//            public ForumPostPreview getPostDetail(Long userId, Long postId) {
//                return null;
//            }
//
//            @Override
//            public void createPost(Long userId, ForumPostPreview post) {
//
//            }
//
//            @Override
//            public void deletePost(Long postId, Long userId) {
//
//            }
//
//            @Override
//            public void likePost(Long postId, Long userId) {
//
//            }
//
//            @Override
//            public void replyPost(Long userId, Long postId, ReplyView reply) {
//
//            }
//
//            @Override
//            public void reportPost(Long userID, ReportRequest reportRequest) {
//
//            }
//
//            @Override
//            public String uploadImage(Long userId, Long postId, MultipartFile file) {
//                File dir = uploadFileSystemService.getBlogsUploadDir(postId);
//                String newFileName = file.getOriginalFilename();
//                if (newFileName == null)
//                    newFileName = LocalDateTime.now() + ".bin";
//                File dest = new File(dir, newFileName);
//                try {
//                    file.transferTo(dest);
//                } catch (Exception e) {
//                    throw new CampusAidException(e);
//                }
//                return dest.getAbsolutePath();
//            }
//        };
//    }
}
