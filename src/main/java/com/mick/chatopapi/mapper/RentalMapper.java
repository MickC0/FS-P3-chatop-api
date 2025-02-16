package com.mick.chatopapi.mapper;

import com.mick.chatopapi.dto.NewRentalDto;
import com.mick.chatopapi.dto.RentalDto;
import com.mick.chatopapi.entity.RentalEntity;
import org.springframework.stereotype.Component;

@Component
public class RentalMapper {
    public RentalDto toDTO (RentalEntity rentalEntity){
        return new RentalDto(
                rentalEntity.getId(),
                rentalEntity.getName(),
                rentalEntity.getSurface(),
                rentalEntity.getPrice(),
                rentalEntity.getDescription(),
                rentalEntity.getPicture(),
                rentalEntity.getOwner().getId(),
                rentalEntity.getCreated_at(),
                rentalEntity.getUpdated_at()
        );
    }

    public RentalEntity toEntity (NewRentalDto dto){
        return new RentalEntity(dto.name(), dto.surface(), dto.price(), dto.description());
    }
}
