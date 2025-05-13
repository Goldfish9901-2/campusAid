package cn.edu.usst.cs.campusAid.mapper.mapstruct;

import cn.edu.usst.cs.campusAid.dto.forum.ForumPostPreview;
import cn.edu.usst.cs.campusAid.dto.forum.Visibility;
import cn.edu.usst.cs.campusAid.model.forum.Blog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;

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

    @Mapping(source = "postId", target = "id")
    @Mapping(source = "authorId", target = "creator")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "content", target = "content")
    @Mapping(source = "publishTime", target = "sendTime")
    @Mapping(source = "visibility", target = "visibility", ignore = true)
    Blog toModel(ForumPostPreview view);

    @Named("stringToVisibility")
    default Visibility stringToVisibility(String value) {
        if (value == null) return null;
        var optional = Arrays.stream(Visibility.values()).filter(
                v -> v.name().equalsIgnoreCase(value)
        ).findAny();
        return optional.orElse(null);
    }
}
