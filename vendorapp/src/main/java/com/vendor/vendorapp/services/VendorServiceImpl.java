package com.vendor.vendorapp.services;

import com.vendor.vendorapp.dtos.VendorRequestDto;
import com.vendor.vendorapp.dtos.VendorResponseDto;
import com.vendor.vendorapp.entity.VendorEntity;
import com.vendor.vendorapp.exceptions.VendorNotFoundException;
import com.vendor.vendorapp.mapper.VendorMapper;
import com.vendor.vendorapp.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class VendorServiceImpl implements VendorService {

    private final VendorRepository repository;
    private final VendorMapper mapper;
    private final RestClient restClient;        // ← Injected

  //  private static final String POSTS_URL = "https://jsonplaceholder.typicode.com/posts";

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


    // ================== Combined Method using RestClient ==================
    @Override
    public Map<String, Object> getVendorsAndPosts() {

        // Get your vendors
        Page<VendorResponseDto> vendorsPage = getAllVendors(Pageable.unpaged(), null);
        List<VendorResponseDto> vendors = vendorsPage.getContent();

        // Fetch posts using RestClient (clean way)
        List<Object> posts = restClient.get()
                .uri("https://jsonplaceholder.typicode.com/posts")
                .retrieve()
                .body(new ParameterizedTypeReference<List<Object>>() {});

        // Combine both
        Map<String, Object> response = new HashMap<>();
        response.put("vendors", vendors);
        response.put("posts", posts);
        response.put("totalVendors", vendors.size());
        response.put("totalPosts", posts != null ? posts.size() : 0);

        return response;
    }

    @Override
    public Object getExternalPostById(Long id) {
        return restClient.get()
                .uri("https://jsonplaceholder.typicode.com/posts" + "/{id}", id)
                .retrieve()
                .body(Object.class);
    }
/**
    @Override
    public Map<String, Object> getVendorsAndPosts() {
        return Map.of();
    }*/
/**
        // ================== New Clean RestClient Methods ==================

        @Override
        public List<Object> getExternalPosts() {
            return restClient.get()
                    .uri(POSTS_URL)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });
        }

        @Override
        public Object getExternalPostById(Long id) {
            return restClient.get()
                    .uri(POSTS_URL + "/{id}", id)
                    .retrieve()
                    .body(Object.class);
        }*/

        @Override
        public List<Object> getExternalBooks() {
            return restClient.get()
                    .uri("https://restful-booker.herokuapp.com/booking")
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });
        }

    @Override
    public Map<String, Object> getVendorsAndBooks() {

        // Get your vendors
        Page<VendorResponseDto> vendorsPage = getAllVendors(Pageable.unpaged(), null);
        List<VendorResponseDto> vendors = vendorsPage.getContent();

        // Fetch posts using RestClient (clean way)
        List<Object> books = restClient.get()
                .uri("https://restful-booker.herokuapp.com/booking")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        // Combine both
        Map<String, Object> response = new HashMap<>();
        response.put("vendors", vendors);
        response.put("books", books);
        response.put("totalVendors", vendors.size());
        response.put("totalBooks", books != null ? books.size() : 0);

        return response;
    }

}