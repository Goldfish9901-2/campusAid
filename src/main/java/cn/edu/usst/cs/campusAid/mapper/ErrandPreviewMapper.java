package cn.edu.usst.cs.campusAid.mapper;

import cn.edu.usst.cs.campusAid.dto.errand.ErrandOrderPreview;
import cn.edu.usst.cs.campusAid.dto.errand.ErrandOrderView;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ErrandPreviewMapper {
    ErrandPreviewMapper INSTANCE = Mappers.getMapper(ErrandPreviewMapper.class);

    @Mappings({})
    ErrandOrderPreview getPreview(ErrandOrderView view);
}
