package com.mick.chatopapi.service.impl;

import com.mick.chatopapi.dto.NewRentalDto;
import com.mick.chatopapi.dto.RentalDto;
import com.mick.chatopapi.dto.UpdateRentalDto;
import com.mick.chatopapi.entity.RentalEntity;
import com.mick.chatopapi.entity.UserEntity;
import com.mick.chatopapi.mapper.RentalMapper;
import com.mick.chatopapi.repository.RentalRepository;
import com.mick.chatopapi.repository.UserRepository;
import com.mick.chatopapi.service.RentalService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RentalServiceImpl implements RentalService {

    private final RentalRepository rentalRepository;
    private final RentalMapper rentalMapper;
    private final UserRepository userRepository;

    @Value("${spring.servlet.multipart.location}")
    private String uploadDir;

    public RentalServiceImpl(RentalRepository rentalRepository, RentalMapper rentalMapper, UserRepository userRepository) {
        this.rentalRepository = rentalRepository;
        this.rentalMapper = rentalMapper;
        this.userRepository = userRepository;
    }

    @Override
    public List<RentalDto> getAllRentals() {
        List<RentalEntity> rentals = rentalRepository.findAll();
        return rentals.stream().map(rentalMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public RentalDto getRentalById(Integer id) {
        Optional<RentalEntity> rentalEntity = rentalRepository.findById(id);
        return rentalEntity.map(rentalMapper::toDTO).orElse(null);
    }

    @Override
    public void createRental(NewRentalDto rentalDto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity owner = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Unauthenticated user"));

        RentalEntity rentalEntity = rentalMapper.toEntity(rentalDto);
        rentalEntity.setOwner(owner);

        try{
            rentalEntity.setPicture(getImageUrl(saveFile(rentalDto.picture())));
        } catch (IOException e) {
            throw new RuntimeException("There was a problem with the photo " + e);
        }
        rentalEntity.setCreated_at(LocalDateTime.now());
        rentalEntity.setUpdated_at(LocalDateTime.now());
        rentalRepository.save(rentalEntity);
        rentalMapper.toDTO(rentalEntity);
    }

    @Override
    public void updateRental(Integer id, UpdateRentalDto updateRentalDto) {
        RentalEntity existingRental  = rentalRepository.findById(id).orElseThrow(
                ()->new NoSuchElementException("Rental not found")
        );
        existingRental .setName(updateRentalDto.name());
        existingRental .setSurface(updateRentalDto.surface());
        existingRental .setPrice(updateRentalDto.price());
        existingRental .setDescription(updateRentalDto.description());
        existingRental .setUpdated_at(LocalDateTime.now());
        if (updateRentalDto.picture() != null && !updateRentalDto.picture().isEmpty()) {
            removeOldFileIfNeeded(existingRental.getPicture());
            try {
                String savedFilename = saveFile(updateRentalDto.picture());
                existingRental.setPicture(getImageUrl(savedFilename));
            } catch (IOException e) {
                throw new RuntimeException("There was a problem with the new photo : " + e.getMessage());
            }
        }
        rentalRepository.save(existingRental);
        rentalMapper.toDTO(existingRental);
    }

    private String getImageUrl(String filename) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/rentals/image/")
                .path(filename)
                .toUriString();
    }
    private String saveFile(MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }

    private void removeOldFileIfNeeded(String oldImageUrl) {
        if (oldImageUrl == null) return;
        String filename = Paths.get(URI.create(oldImageUrl).getPath()).getFileName().toString();

        Path uploadPath = Paths.get(uploadDir);
        Path fileToDelete = uploadPath.resolve(filename);
        try {
            Files.deleteIfExists(fileToDelete);
        } catch (IOException e) {
            System.err.println("Unable to delete old photo : " + e.getMessage());
        }
    }
}
