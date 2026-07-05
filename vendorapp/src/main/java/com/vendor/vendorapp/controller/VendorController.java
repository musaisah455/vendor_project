package com.vendor.vendorapp.controller;

import com.vendor.vendorapp.dtos.VendorRequestDto;
import com.vendor.vendorapp.dtos.VendorResponseDto;
import com.vendor.vendorapp.services.VendorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@RestController
@RequestMapping("/api/v1/vendors")
@RequiredArgsConstructor
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
@SecurityRequirement(name = "Bearer Authentication")
public class VendorController {

    private final VendorService vendorService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Create a new vendor")
    public ResponseEntity<VendorResponseDto> create(@Valid @RequestBody VendorRequestDto request) {
        VendorResponseDto response = vendorService.createVendor(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    @Operation(summary = "Get vendor by ID")
    public ResponseEntity<VendorResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(vendorService.getVendorById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Get all vendors with pagination and search")
    public ResponseEntity<Page<VendorResponseDto>> getAll(@ParameterObject
                                                          @PageableDefault(size = 20) Pageable pageable,
                                                          @RequestParam(required = false) String search) {
        return ResponseEntity.ok(vendorService.getAllVendors(pageable, search));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Update vendor")
    public ResponseEntity<VendorResponseDto> update(
            @PathVariable UUID id,
            @Valid @RequestBody VendorRequestDto request) {
        return ResponseEntity.ok(vendorService.updateVendor(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete vendor")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        vendorService.deleteVendor(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete all vendors")
    public ResponseEntity<Void> deleteAllVendors() {
        vendorService.deleteAllVendors();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/combined")
    public Map<String, Object> getVendorsAndPosts() {
        return ResponseEntity.ok(vendorService.getVendorsAndPosts()).getBody();
    }

    @GetMapping("/combined/{id}")
    public Object getExternalPostById(@PathVariable Long id) {
        return ResponseEntity.ok(vendorService.getExternalPostById(id));
    }
/**
    @GetMapping("/posts")
    public List<Object> getExternalPosts() {
        return vendorService.getExternalPosts();
    }

    @GetMapping("/posts/{postId}")
    public Object getExternalPostById(@PathVariable Long postId) {
        return vendorService.getExternalPostById(postId);
    }*/

}