package com.vendor.vendorapp.services;

import com.vendor.vendorapp.dtos.VendorRequestDto;
import com.vendor.vendorapp.dtos.VendorResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface VendorService {
    VendorResponseDto createVendor(VendorRequestDto request);
    VendorResponseDto getVendorById(UUID id);
    Page<VendorResponseDto> getAllVendors(Pageable pageable, String search);
    VendorResponseDto updateVendor(UUID id, VendorRequestDto request);
    void deleteVendor(UUID id);
    void deleteAllVendors();

    // ================== Combined Method using RestClient ==================
    Map<String, Object> getVendorsAndPosts();

    Object getExternalPostById(Long id);

   // List<Object> getExternalPosts();

    //Object getExternalPostById(Long postId);
}