package cn.edu.usst.cs.campusAid.service.impl.forum;

import cn.edu.usst.cs.campusAid.dto.forum.*;
import cn.edu.usst.cs.campusAid.mapper.db.forum.BlogMapper;
import cn.edu.usst.cs.campusAid.mapper.db.forum.LikeBlogMapper;
import cn.edu.usst.cs.campusAid.mapper.db.forum.ReplyMapper;
import cn.edu.usst.cs.campusAid.mapper.mapstruct.BlogToForumPostPreview;
import cn.edu.usst.cs.campusAid.mapper.mapstruct.ReplyMapperStruct;
import cn.edu.usst.cs.campusAid.model.forum.Blog;
import cn.edu.usst.cs.campusAid.model.forum.LikeBlog;
import cn.edu.usst.cs.campusAid.model.forum.Reply;
import cn.edu.usst.cs.campusAid.model.forum.ReplyTreeNode;
import cn.edu.usst.cs.campusAid.service.CampusAidException;
import cn.edu.usst.cs.campusAid.service.UploadFileSystemService;
import cn.edu.usst.cs.campusAid.service.auth.UserService;
import cn.edu.usst.cs.campusAid.service.forum.ForumPostService;
import com.github.houbb.sensitive.word.core.SensitiveWordHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ForumPostServiceImpl implements ForumPostService {
    private final UserService userService;
    private final UploadFileSystemService uploadFileSystemService;
    private final LikeBlogMapper likeBlogMapper;
    private final ReplyMapper replyMapper;
    private final BlogMapper blogMapper;
    private final BlogToForumPostPreview blogToForumPostPreview;

    // 构造器注入
    public ForumPostServiceImpl(
            UserService userService,
            UploadFileSystemService uploadFileSystemService,
            LikeBlogMapper likeBlogMapper,
            ReplyMapper replyMapper,
            BlogMapper blogMapper,
            BlogToForumPostPreview blogToForumPostPreview) {
        this.userService = userService;
        this.uploadFileSystemService = uploadFileSystemService;
        this.likeBlogMapper = likeBlogMapper;
        this.replyMapper = replyMapper;
        this.blogMapper = blogMapper;
        this.blogToForumPostPreview = blogToForumPostPreview;
    }

    /**
     * 获取排序后的帖子列表，并附带每个帖子的点赞数、回复数及是否已点赞状态。
     *
     * <p>该方法支持以下功能：</p>
     * <ul>
     *     <li>按关键词类型（标题/发帖人/标签）进行过滤</li>
     *     <li>按时间/点赞量/回复量排序</li>
     *     <li>分页加载数据</li>
     *     <li>批量查询点赞数和回复数，避免N+1查询问题</li>
     *     <li>判断当前用户是否已经对每篇帖子点赞</li>
     * </ul>
     *
     * @param userId    当前登录用户ID，用于判断点赞状态
     * @param type      关键词匹配类型（TITLE: 标题, TAG: 内容中的标签, CREATOR: 发帖人）
     * @param keyword   搜索关键词
     * @param sortBy    排序方式（TIME: 时间, LIKE_COUNT: 点赞量, REPLY_COUNT: 回复量）
     * @param rowBounds 分页参数，控制偏移量和每页条目数
     * @return 返回经过筛选、排序和补充信息后的帖子预览列表
     */
    @Override
    public List<ForumPostPreview> getPostsSorted(Long userId, KeywordType type, String keyword, PostSortOrder sortBy, RowBounds rowBounds) {
        // Step 1：调用统一SQL方法进行搜索与排序
        List<Blog> blogs = blogMapper.selectBlogs(
                type.name(),       // 枚举转字符串传给 MyBatis
                keyword,
                sortBy.name(),     // 枚举转字符串传给 MyBatis
                rowBounds
        );

        if (blogs.isEmpty()) return List.of();

        // Step 2：提取所有博客ID，用于批量查询点赞数和回复数
        List<Long> blogIds = blogs.stream()
                .map(Blog::getId)
                .filter(Objects::nonNull)
                .toList();

        // Step 3：批量查询点赞数和回复数，并转换为 Map<Long, Integer>
        Map<Long, Integer> likeCountMap = convertToIdCountMap(likeBlogMapper.countLikesByBlogIds(blogIds), "blogId", "likeCount");
        Map<Long, Integer> replyCountMap = convertToIdCountMap(replyMapper.countRepliesByBlogIds(blogIds), "blogId", "replyCount");

        // Step 4：转换为 DTO 并填充额外字段
        return blogs.stream()
                .map(blog -> {
                    ForumPostPreview preview = BlogToForumPostPreview.INSTANCE.toView(blog);
                    preview.setAuthorName(userService.getUserById(preview.getAuthorId()).getName());
                    preview.setLikeCount(likeCountMap.getOrDefault(preview.getPostId(), 0));
                    preview.setReplyCount(replyCountMap.getOrDefault(preview.getPostId(), 0));
                    preview.setLiked(isLikedByUser(blog.getId(), userId));
                    return preview;
                })
                .toList();
    }

    @Override
    public ForumPostPreview getPostById(Long postId) {
        //获取指定帖子blog
        Blog blog = blogMapper.selectById(postId);
        if (blog == null) throw new CampusAidException("帖子不存在");
        //转换成dto返回
        return BlogToForumPostPreview.INSTANCE.toView(blog);
    }


    @Override
    public void createPost(Long userId, ForumPostPreview post) {
        Blog blog = new Blog();
        blog.setTitle(post.getTitle());
        blog.setContent(
                SensitiveWordHelper.replace(post.getContent(), '*')
        );

        blog.setCreator(userId);
        blog.setVisibility(Visibility.VISIBLE.getValue());
        blog.setSendTime(LocalDateTime.now());
        blogMapper.insertBlog(blog);
    }


    @Override
    public void deletePost(Long postId, Long userId) {
        Blog blog = blogMapper.selectById(postId);
        if (blog == null) throw new CampusAidException("帖子不存在");
        log.info("👤 当前用户ID={}, 帖子作者={}, 是否是管理员={}", userId, blog.getCreator(), userService.isAdmin(userId));

        //权限控制
        if (!blog.getCreator().equals(userId) && !userService.isAdmin(userId))
            throw new CampusAidException("无权删除此帖子");

        blogMapper.deleteById(postId);
        likeBlogMapper.deleteByBlogId(postId);
        replyMapper.deleteByBlogId(postId);
    }

    /**
     * 点赞帖子
     */
    @Transactional
    @Override
    public void likePost(Long postId, Long userId) {
        if (isLikedByUser(postId, userId)) {
            throw new CampusAidException("您已点赞过该帖子");
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
     *
     * @param userId 发出回复的用户ID
     * @param postId 帖子ID
     * @param reply  回复内容
     */
    @Transactional
    @Override
    public void replyPost(Long userId, Long postId, ReplyView reply) {
        Reply newReply = new Reply();
        newReply.setBlogId(postId);
        newReply.setSender(userId);
        newReply.setContent(
                SensitiveWordHelper.replace(reply.getContent(), '*')
        );
        replyMapper.insertReply(newReply);
    }

    /**
     * 获取帖子的回复列表并转换为 DTO
     *
     * @param postId 帖子ID
     * @return 返回 ReplyView 列表
     */
    @Override
    public List<ReplyView> getRepliesByPostId(Long userId, Long postId) {
        List<Reply> replies = replyMapper.selectByBlogId(userId, postId);
        return ReplyMapperStruct.INSTANCE.toViews(replies);
    }

    /**
     * 获取帖子的回复数量
     */
    @Override
    public int getReplyCountByPostId(Long userId, Long postId) {
        return replyMapper.countRepliesByBlogId(postId);
    }

    /**
     * 获取帖子的回复树结构
     *
     * @param postId 帖子ID
     * @return 返回回复树结构
     */
    @Override
    public List<ReplyTreeNode> getRepliesTreeByPostId(Long userID, Long postId) {
        List<Reply> replies = replyMapper.selectByBlogId(userID, postId);
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
     *
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
    public List<Map<String, Object>> getLikeCountsByPosts(List<Long> blogIds) {
        return likeBlogMapper.countLikesByBlogIds(blogIds);
    }

    @Override
    public List<Map<String, Object>> countRepliesByPosts(List<Long> blogIds) {
        return replyMapper.countRepliesByBlogIds(blogIds);
    }

    @Override
    public Long getAuthorID(Long postId) {
        Blog blog = blogMapper.selectById(postId);
        return blog.getCreator();
    }

    @Override
    public Long submitPost(Long userId, ForumPostPreview post) {
        Blog blog = blogToForumPostPreview.toModel(post);
        blog.setCreator(userId);
        blog.setVisibility(Visibility.VISIBLE.getValue());
        blogMapper.insertBlog(blog);
        Blog inserted = blogMapper.selectBlogs(
                "TITLE",
                post.getTitle(),
                "TIME",
                new RowBounds(0, 1)
        ).get(0);
        return inserted.getId();
    }

    @Override
    public void reportPost(Long userID, ReportRequest reportRequest) {
        // TODO: 实现举报帖子逻辑
    }

    @Override
    public String uploadImage(Long userId, Long postId, MultipartFile file) {
        Blog preview = blogMapper.selectById(postId);
        if (preview == null)
            throw new CampusAidException("帖子不存在");
        if (!Objects.equals(preview.getCreator(), userId))
            throw new CampusAidException("不是发布者 无权上传图片");
        File dir = uploadFileSystemService.getBlogsUploadDir(postId);
        return uploadFileSystemService.uploadFile(dir, file);
    }

    /**
     * 修改帖子可见性
     *
     * @param userId     当前用户ID，用于权限校验
     * @param postId     帖子ID
     * @param visibility 新的可见性状态
     */
    @Override
    public void updatePostVisibility(Long userId, Long postId, Visibility visibility) {
        Blog blog = blogMapper.selectById(postId);
        if (blog == null) throw new CampusAidException("帖子不存在");

        // 权限控制：只有管理员或发帖人可以修改可见性
        if (!blog.getCreator().equals(userId) && !userService.isAdmin(userId)) {
            throw new CampusAidException("无权修改此帖子的可见性");
        }

        // 验证visibility参数是否合法
        if (visibility == null) {
            throw new CampusAidException("无效的可见性状态");
        }

        // 如果是管理员操作且不是设置为隐藏状态，拒绝操作
        if (userService.isAdmin(userId) && visibility != Visibility.ADMIN) {
            throw new CampusAidException("管理员只能将帖子设置为隐藏状态");
        }

        // 如果是发帖人操作且尝试设置为管理员隐藏状态，拒绝操作
        if (blog.getCreator().equals(userId) && visibility == Visibility.ADMIN) {
            throw new CampusAidException("发帖人不能将帖子设置为管理员隐藏状态");
        }

        blogMapper.updateVisibility(postId, visibility);
    }

    /**
     * 检查用户是否是帖子的作者或管理员
     *
     * @param userId       当前操作用户的ID
     * @param targetUserId 目标用户ID（如帖子作者或回复者）
     * @return 如果是本人或管理员返回true，否则返回false
     */
    private boolean isUserOrAdmin(Long userId, Long targetUserId) {
        if (userId.equals(targetUserId)) return true;
        return userService.isAdmin(userId);
    }


    /**
     * 用于将 List<Map<String, Object>> 转换为 Map<Long, Integer>：
     *
     * @param list       包含键值对的数据列表
     * @param keyField   每个map中作为key的字段名（应为Long类型）
     * @param valueField 每个map中作为value的字段名（应为数值类型Integer/Long等）
     * @return 转换后的Map<Long, Integer>
     */
    private Map<Long, Integer> convertToIdCountMap(List<Map<String, Object>> list, String keyField, String valueField) {
        if (list == null || list.isEmpty()) {
            return Map.of(); // 返回不可变空 map，避免 null 异常
        }

        return list.stream().collect(Collectors.toMap(
                map -> (Long) map.get(keyField),
                map -> ((Number) map.get(valueField)).intValue(),
                (existing, replacement) -> existing // 若有重复 key，保留旧值
        ));
    }


}
