package com.vendor.vendorapp.services;

import com.vendor.vendorapp.dtos.VendorRequestDto;
import com.vendor.vendorapp.dtos.VendorResponseDto;
import com.vendor.vendorapp.entity.VendorEntity;
import com.vendor.vendorapp.exceptions.VendorNotFoundException;
import com.vendor.vendorapp.mapper.VendorMapper;
import com.vendor.vendorapp.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class VendorServiceImpl implements VendorService {

    private final VendorRepository repository;
    private final VendorMapper mapper;

    @Override
    public VendorResponseDto createVendor(VendorRequestDto request) {
        if (repository.findByEmail(request.getEmail()).isPresent()) {
            throw new VendorNotFoundException("Email already exists");
        }
        VendorEntity vendorEntity = mapper.toEntity(request);
        vendorEntity = repository.saveAndFlush(vendorEntity);
        return mapper.toResponseDto(vendorEntity);
    }

    @Override
    public VendorResponseDto getVendorById(UUID id) {
        VendorEntity vendorEntity = repository.findById(id)
                .orElseThrow(() -> new VendorNotFoundException("VendorEntity not found with id: " + id));
        return mapper.toResponseDto(vendorEntity);
    }

    @Override
    public Page<VendorResponseDto> getAllVendors(Pageable pageable, String search) {
        if (search != null && !search.isBlank()) {
            return repository.findByNameContainingIgnoreCase(search, pageable)
                    .map(mapper::toResponseDto);
        }
        return repository.findAll(pageable).map(mapper::toResponseDto);
    }

    @Override
    public VendorResponseDto updateVendor(UUID id, VendorRequestDto request) {
        VendorEntity vendorEntity = repository.findById(id)
                .orElseThrow(() -> new VendorNotFoundException("VendorEntity not found with id: " + id));

        mapper.updateEntityFromDto(request, vendorEntity);
        vendorEntity = repository.save(vendorEntity);
        return mapper.toResponseDto(vendorEntity);
    }

    @Override
    public void deleteVendor(UUID id) {
        if (!repository.existsById(id)) {
            throw new VendorNotFoundException("VendorEntity not found with id: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public void deleteAllVendors() {
        repository.deleteAll();
    }

}