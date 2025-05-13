package cn.edu.usst.cs.campusAid.mapper.mapstruct;

import cn.edu.usst.cs.campusAid.dto.complaint.ComplaintDetail;
import cn.edu.usst.cs.campusAid.dto.complaint.ComplaintRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

@Mapper
public interface ComplaintMapper {
    @Mappings({})
    ComplaintDetail toDetail(ComplaintRequest request);
}
