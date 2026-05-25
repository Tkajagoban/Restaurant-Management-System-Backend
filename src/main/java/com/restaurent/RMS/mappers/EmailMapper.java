package com.restaurent.RMS.mappers;

import com.restaurent.RMS.dtos.request.EmailRequestDto;
import com.restaurent.RMS.dtos.response.EmailResponseDto;
import com.restaurent.RMS.entities.Email;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import java.util.List;

@Mapper(componentModel = "spring")
public interface  EmailMapper {
    @Mapping(target = "id", ignore = true )
    Email toEntity(EmailRequestDto emailRequestDto);

    EmailResponseDto toResponseDto(Email email);

    List<EmailResponseDto> toResponseDtoList(List<Email> entities);

    //FOR UPDATE
    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(EmailRequestDto dto, @MappingTarget Email entity);
}
