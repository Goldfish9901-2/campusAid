package cn.edu.usst.cs.campusAid.mapper.mapstruct;

import cn.edu.usst.cs.campusAid.dto.forum.ReplyView;
import cn.edu.usst.cs.campusAid.model.forum.Reply;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ReplyMapperStruct {

    ReplyMapperStruct INSTANCE = Mappers.getMapper(ReplyMapperStruct.class);

    @Mappings({
            @Mapping(source = "id", target = "senderId"),
    })
    ReplyView toView(Reply reply);

    List<ReplyView> toViews(List<Reply> replies);
}
