package cn.edu.usst.cs.campusAid.mapper.mapstruct;

import cn.edu.usst.cs.campusAid.config.AlipayTemplate;
import com.alipay.easysdk.kernel.Config;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface AlipayConfigMapper {
    @Mappings({
            // 已有配置
            @Mapping(target = "merchantCertPath", ignore = true),
            @Mapping(target = "alipayCertPath", ignore = true),
            @Mapping(target = "alipayRootCertPath", ignore = true),
            @Mapping(target = "gatewayHost", source = "gatewayUrl"),

            // 新增忽略字段
            @Mapping(target = "protocol", expression = "java(\"https\")"),
            @Mapping(target = "encryptKey", ignore = true),
            @Mapping(target = "signProvider", ignore = true),
            @Mapping(target = "httpProxy", ignore = true),
            @Mapping(target = "ignoreSSL", ignore = true)
    })
    Config toConfig(AlipayTemplate alipayTemplate);
}

