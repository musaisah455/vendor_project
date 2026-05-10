package com.vendor.vendorapp.dtos;

import com.vendor.vendorapp.entity.VendorEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VendorResponseDto {
    private UUID id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private VendorEntity.Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}