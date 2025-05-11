package cn.edu.usst.cs.campusAid.mapper;

import cn.edu.usst.cs.campusAid.dto.errand.ErrandOrderPreview;
import cn.edu.usst.cs.campusAid.dto.errand.ErrandOrderView;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ErrandPreviewMapper {
    ErrandPreviewMapper INSTANCE = Mappers.getMapper(ErrandPreviewMapper.class);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "startLocation", source = "startLocation")
    @Mapping(target = "endLocation", source = "endLocation")
    ErrandOrderPreview getPreview(ErrandOrderView view);
}
