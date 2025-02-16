package com.mick.chatopapi.service;

import com.mick.chatopapi.dto.NewRentalDto;
import com.mick.chatopapi.dto.RentalDto;
import com.mick.chatopapi.dto.UpdateRentalDto;

import java.util.List;

public interface RentalService {
    List<RentalDto> getAllRentals();
    RentalDto getRentalById(Integer id);
    void createRental(NewRentalDto rentalDto);
    void updateRental(Integer id, UpdateRentalDto updateRentalDto);
}
