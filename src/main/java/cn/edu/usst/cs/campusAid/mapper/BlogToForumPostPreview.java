package cn.edu.usst.cs.campusAid.mapper;

import cn.edu.usst.cs.campusAid.dto.forum.ForumPostPreview;
import cn.edu.usst.cs.campusAid.dto.forum.Visibility;
import cn.edu.usst.cs.campusAid.model.forum.Blog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BlogToForumPostPreview {

    BlogToForumPostPreview INSTANCE = Mappers.getMapper(BlogToForumPostPreview.class);

    @Mapping(source = "id", target = "postId")
    @Mapping(source = "creator", target = "authorId")
    @Mapping(target = "authorName", ignore = true)
    @Mapping(source = "title", target = "title")
    @Mapping(source = "content", target = "content")
    @Mapping(source = "sendTime", target = "publishTime")
    @Mapping(target = "likeCount", constant = "0")//后续service中赋值
    @Mapping(target = "replyCount", constant = "0")//后续service中赋值
    @Mapping(target = "liked", constant = "false")
    @Mapping(target = "tags", ignore = true)
    @Mapping(source = "visibility", target = "visibility", qualifiedByName = "stringToVisibility")
    ForumPostPreview toView(Blog blog);

    @Named("stringToVisibility")
    default Visibility stringToVisibility(String value) {
        if (value == null) return null;
        for (Visibility v : Visibility.values()) {
            if (v.getValue().equalsIgnoreCase(value)) {
                return v;
            }
        }
        throw new IllegalArgumentException("No matching Visibility for value: " + value);
    }
}
