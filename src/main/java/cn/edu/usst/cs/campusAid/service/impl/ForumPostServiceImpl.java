package cn.edu.usst.cs.campusAid.service.impl;

import cn.edu.usst.cs.campusAid.dto.forum.*;
import cn.edu.usst.cs.campusAid.mapper.db.LikeBlogMapper;
import cn.edu.usst.cs.campusAid.mapper.db.ReplyMapper;
import cn.edu.usst.cs.campusAid.model.forum.LikeBlog;
import cn.edu.usst.cs.campusAid.model.forum.Reply;
import cn.edu.usst.cs.campusAid.model.forum.ReplyTreeNode;
import cn.edu.usst.cs.campusAid.service.CampusAidException;
import cn.edu.usst.cs.campusAid.service.ForumPostService;
import cn.edu.usst.cs.campusAid.service.UploadFileSystemService;
import cn.edu.usst.cs.campusAid.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ForumPostServiceImpl implements ForumPostService {
    @Autowired
    private UserService userService;
    @Autowired
    private UploadFileSystemService uploadFileSystemService;
    @Autowired
    private LikeBlogMapper likeBlogMapper;
    @Autowired
    private ReplyMapper replyMapper;

    @Override
    public List<ForumPostPreview> getPostsSorted(Long userId, KeywordType type, String keyword, PostSortOrder sortBy) {
        // TODO: 实现获取排序后的帖子列表逻辑
        return List.of();
    }



    @Override
    public void createPost(Long userId, ForumPostPreview post) {
        // TODO: 实现创建帖子逻辑
    }

    @Override
    public void deletePost(Long postId, Long userId) {
        // TODO: 实现删除帖子逻辑
    }

    /**
     * 点赞帖子
     */
    @Transactional
    @Override
    public void likePost(Long postId, Long userId) {
        if (isLikedByUser(postId, userId)) {
            throw new RuntimeException("您已点赞过该帖子");
        }
        LikeBlog likeBlog = new LikeBlog();
        likeBlog.setBlogId(postId);
        likeBlog.setLiker(userId);
        likeBlogMapper.insertLike(likeBlog);
    }

    /**
     * 取消点赞帖子
     */
    @Transactional
    @Override
    public void unlikePost(Long postId, Long userId) {
        likeBlogMapper.deleteLike(postId, userId);
    }

    /**
     * 检查用户是否已点赞该帖子
     */
    @Override
    public boolean isLikedByUser(Long postId, Long userId) {
        return likeBlogMapper.countLikesByUserAndPost(postId, userId) > 0;
    }

    /**
     * 获取帖子的点赞数量
     */
    @Override
    public int getPostLikeCount(Long postId) {
        return likeBlogMapper.countLikes(postId);
    }

    /**
     * 回复帖子
     * @param userId
     * @param postId 帖子ID
     * @param reply  回复内容
     */
    @Transactional
    @Override
    public void replyPost(Long userId, Long postId, ReplyView reply) {
        Reply newReply = new Reply();
        newReply.setBlogId(postId);
        newReply.setSender(userId);
        newReply.setContent(reply.getContent());
        replyMapper.insertReply(newReply);
    }

    /**
     * 获取帖子的回复列表
     * @param postId 帖子ID
     * @return
     */
    @Override
    public List<Reply> getRepliesByPostId(Long postId) {
        return replyMapper.selectByBlogId(postId);
    }
    /**
     * 获取帖子的回复树结构
     * @param postId 帖子ID
     * @return
     */
    @Override
    public List<ReplyTreeNode> getRepliesTreeByPostId(Long postId) {
        List<Reply> replies = replyMapper.selectByBlogId(postId);
        Map<Long, ReplyTreeNode> nodeMap = new HashMap<>();
        List<ReplyTreeNode> rootNodes = new ArrayList<>();

        // 初始化所有节点
        for (Reply r : replies) {
            nodeMap.put(r.getId(), new ReplyTreeNode(r));
        }

        // 构建父子关系
        for (Reply r : replies) {
            ReplyTreeNode node = nodeMap.get(r.getId());
            if (r.getParentId() == null) {
                rootNodes.add(node);
            } else {
                ReplyTreeNode parent = nodeMap.get(r.getParentId());
                if (parent != null) {
                    parent.getChildren().add(node);
                }
            }
        }

        return rootNodes;
    }
    /**
     * 删除回复
     * @param replyId 回复ID
     */
    @Transactional
    @Override
    public void deleteReply(Long replyId, Long userId) {
        Reply reply = replyMapper.selectById(replyId);
        if (reply == null) {
            throw new RuntimeException("回复不存在");
        }
        // 检查用户是否是回复的作者或管理员
        if (!isUserOrAdmin(userId, reply.getSender())) {
            throw new RuntimeException("无权删除该回复");
        }

        replyMapper.deleteById(replyId);
    }

    @Override
    public void reportPost(Long userID, ReportRequest reportRequest) {
        // TODO: 实现举报帖子逻辑
    }

    @Override
    public String uploadImage(Long userId, Long postId, MultipartFile file) {
        File dir = uploadFileSystemService.getBlogsUploadDir(postId);
        String newFileName = file.getOriginalFilename();
        if (newFileName == null) {
            newFileName = LocalDateTime.now() + ".bin";
        }
        File dest = new File(dir, newFileName);
        try {
            file.transferTo(dest);
        } catch (Exception e) {
            throw new CampusAidException(e);
        }
        return dest.getAbsolutePath();
    }
    /**
     * 检查用户是否是帖子的作者或管理员
     * @param userId
     * @param targetUserId
     * @return
     */
    private boolean isUserOrAdmin(Long userId, Long targetUserId) {
        if (userId.equals(targetUserId)) return true;
        return userService.isAdmin(userId);
    }

}
