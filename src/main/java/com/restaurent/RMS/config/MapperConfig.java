package com.restaurent.RMS.config;

import com.restaurent.RMS.mappers.RolePrivilegeMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public RolePrivilegeMapper rolePrivilegeMapper() {
        return Mappers.getMapper(RolePrivilegeMapper.class);
    }
}
