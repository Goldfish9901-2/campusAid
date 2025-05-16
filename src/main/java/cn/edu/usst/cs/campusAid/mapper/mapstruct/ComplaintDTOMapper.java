package cn.edu.usst.cs.campusAid.mapper.mapstruct;

import cn.edu.usst.cs.campusAid.dto.complaint.ComplaintRequest;
import cn.edu.usst.cs.campusAid.model.complaint.Complaint;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper
public interface ComplaintDTOMapper {
    @Mappings({
            @Mapping(source = "userId", target = "senderId"),
            @Mapping(target = "id", ignore = true),
            @Mapping(source = "content", target = "description"),
            @Mapping(target = "result", ignore = true),
    })
    Complaint toDetail(ComplaintRequest request);

}
