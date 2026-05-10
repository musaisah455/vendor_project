package com.vendor.vendorapp.mapper;

import com.vendor.vendorapp.dtos.VendorRequestDto;
import com.vendor.vendorapp.dtos.VendorResponseDto;
import com.vendor.vendorapp.entity.VendorEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VendorMapper {

    VendorEntity toEntity(VendorRequestDto dto);

    VendorResponseDto toResponseDto(VendorEntity entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(VendorRequestDto dto, @MappingTarget VendorEntity entity);
}