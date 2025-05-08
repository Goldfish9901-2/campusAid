package cn.edu.usst.cs.campusAid.mapper;

import cn.edu.usst.cs.campusAid.dto.forum.ReplyView;
import cn.edu.usst.cs.campusAid.model.forum.Reply;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ReplyMapperStruct {
    ReplyMapperStruct INSTANCE = Mappers.getMapper(ReplyMapperStruct.class);

    @Mapping(source = "sender", target = "senderId")
    @Mapping(target = "replies", ignore = true)
    ReplyView toView(Reply reply);

    List<ReplyView> toViews(List<Reply> replies);
}
