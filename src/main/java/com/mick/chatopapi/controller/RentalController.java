package com.mick.chatopapi.controller;

import com.mick.chatopapi.dto.*;
import com.mick.chatopapi.service.RentalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.NoSuchElementException;

@Tag(name = "Rentals", description = "Rental management APIs")
@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }


    @Operation(
        summary = "Get all rentals",
        security = @SecurityRequirement(name = "bearerAuth"),
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "List of rentals retrieved successfully",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RentalsResponse.class),
                    examples = {
                        @ExampleObject(
                            name = "RentalsResponseExample",
                            value = """
                            {
                              "rentals": [
                                {
                                  "id": 1,
                                  "name": "test house 1",
                                  "surface": 432.0,
                                  "price": 300.0,
                                  "description": "Lorem ipsum dolor sit amet",
                                  "picture": "https://blog.technavio.org/wp-content/uploads/2018/12/Online-House-Rental-Sites.jpg",
                                  "owner_id": 1,
                                  "createdAt": "2012-12-02T00:00:00",
                                  "updatedAt": "2014-12-02T00:00:00"
                                },
                                {
                                  "id": 2,
                                  "name": "test house 2",
                                  "surface": 154.0,
                                  ...
                                }
                              ]
                            }
                            """
                        )
                    }
                )
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Unauthorized: User not authenticated",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = {
                        @ExampleObject(
                            name = "UnauthorizedExample",
                            value = """
                            {
                              "error": "UNAUTHORIZED",
                              "status": 401,
                              "message": "Unauthenticated user."
                            }
                            """
                        )
                    }
                )
            )
        }
    )
    @GetMapping
    public ResponseEntity<RentalsResponse> getAllRentals() {
        return ResponseEntity.ok(new RentalsResponse(rentalService.getAllRentals()));
    }

    @Operation(
        summary = "Get a rental by ID",
        security = @SecurityRequirement(name = "bearerAuth"),
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Rental found",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RentalDto.class),
                    examples = {
                        @ExampleObject(
                            name = "RentalFoundExample",
                            value = """
                            {
                              "id": 1,
                              "name": "Dream house",
                              "surface": 24,
                              "price": 30,
                              "description": "Lorem ipsum",
                              "picture": "myrental.jpg",
                              "owner_id": 2,
                              "createdAt": "2023-02-01T10:00:00",
                              "updatedAt": "2023-02-05T15:30:00"
                            }
                            """
                        )
                    }
                )
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Unauthorized: User not authenticated",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = {
                        @ExampleObject(
                            name = "UnauthorizedExample",
                            value = """
                            {
                              "error": "UNAUTHORIZED",
                              "status": 401,
                              "message": "Unauthenticated user."
                            }
                            """
                        )
                    }
                )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Rental not found",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = {
                        @ExampleObject(
                            name = "NotFoundExample",
                            value = """
                            {
                              "error": "NOT_FOUND",
                              "status": 404,
                              "message": "Rental not found"
                            }
                            """
                        )
                    }
                )
            )
        }
    )
    @GetMapping("/{id}")
    public ResponseEntity<?> getRentalById(@PathVariable Integer id) {
        RentalDto rental = rentalService.getRentalById(id);
        if (rental == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(
                            "NOT_FOUND",
                            HttpStatus.NOT_FOUND.value(),
                            "Rental not found"
                    ));
        }
        return ResponseEntity.ok(rental);
    }


    @Operation(
        summary = "Create a new rental",
        security = @SecurityRequirement(name = "bearerAuth"),
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "Rental created successfully",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RentalMessageResponse.class),
                    examples = {
                        @ExampleObject(
                            name = "CreatedResponseExample",
                            value = """
                            {
                              "message": "Rental created !"
                            }
                            """
                        )
                    }
                )
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Bad Request: Validation error",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = {
                        @ExampleObject(
                            name = "ValidationErrorExample",
                            value = """
                            {
                              "error": "BAD_REQUEST",
                              "status": 400,
                              "message": "The name field is required; The surface field is required"
                            }
                            """
                        )
                    }
                )
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Unauthorized: user not authenticated",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = {
                        @ExampleObject(
                            name = "UnauthorizedExample",
                            value = """
                            {
                              "error": "UNAUTHORIZED",
                              "status": 401,
                              "message": "Unauthenticated user"
                            }
                            """
                        )
                    }
                )
            )
        }
    )
    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> createRental(@ModelAttribute @Valid NewRentalDto rentalDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .reduce((e1, e2) -> e1 + "; " + e2)
                    .orElse("Validation error");

            return ResponseEntity.badRequest().body(
                    new ErrorResponse(
                            "BAD_REQUEST",
                            HttpStatus.BAD_REQUEST.value(),
                            errorMessage
                    )
            );
        }
        try {
            rentalService.createRental(rentalDto);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new RentalMessageResponse("Rental created !"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(
                            "UNAUTHORIZED",
                            HttpStatus.UNAUTHORIZED.value(),
                            "Erreur: " + e.getMessage()
                    ));
        }
    }

    @Operation(
        summary = "Get a rental image by filename",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Image retrieved successfully",
                content = @Content(
                        mediaType = "image/jpeg"
                )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Image not found or not readable",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = {
                        @ExampleObject(
                            name = "NotFoundResponse",
                            value = """
                            {
                              "error": "NOT_FOUND",
                              "status": 404,
                              "message": "Image not found"
                            }
                            """
                        )
                    }
                )
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Server error when reading the file",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = {
                        @ExampleObject(
                            name = "ServerErrorResponse",
                            value = """
                            {
                              "error": "INTERNAL_SERVER_ERROR",
                              "status": 500,
                              "message": "An error occurred while reading the image file"
                            }
                            """
                        )
                    }
                )
            )
        }
    )
    @GetMapping(value = "/image/{filename}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody ResponseEntity<?> getRentalImage(@PathVariable String filename) {
        try {
            Path filePath = Paths.get("uploads").resolve(filename).normalize();
            if (!Files.exists(filePath) || !Files.isReadable(filePath)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse(
                                "NOT_FOUND",
                                HttpStatus.NOT_FOUND.value(),
                                "Image not found"
                        ));
            }
            byte[] imageBytes = Files.readAllBytes(filePath);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(imageBytes);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(
                            "INTERNAL_SERVER_ERROR",
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "An error occurred while reading the image file"
                    ));
        }
    }

    @Operation(
        summary = "Update a rental by ID",
        security = @SecurityRequirement(name = "bearerAuth"),
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Rental updated successfully",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RentalMessageResponse.class),
                    examples = {
                        @ExampleObject(
                            name = "SuccessResponseExample",
                            value = """
                            {
                              "message": "Rental updated !"
                            }
                            """
                        )
                    }
                )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Rental not found",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = {
                        @ExampleObject(
                            name = "NotFoundResponse",
                            value = """
                            {
                              "error": "NOT_FOUND",
                              "status": 404,
                              "message": "Rental not found"
                            }
                            """
                        )
                    }
                )
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Unauthorized or error while updating the rental",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = {
                        @ExampleObject(
                            name = "UnauthorizedExample",
                            value = """
                            {
                              "error": "UNAUTHORIZED",
                              "status": 401,
                              "message": "Unauthenticated user"
                            }
                            """
                        )
                    }
                )
            )
        }
    )
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateRental(
            @PathVariable Integer id,
            @ModelAttribute @Valid UpdateRentalDto updateRentalDto
    ) {
        try {
            rentalService.updateRental(id, updateRentalDto);
            return ResponseEntity.ok(new RentalMessageResponse("Rental updated !"));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(
                            "NOT_FOUND",
                            HttpStatus.NOT_FOUND.value(),
                            "Rental not found"
                    ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(
                            "UNAUTHORIZED",
                            HttpStatus.UNAUTHORIZED.value(),
                            "Erreur: " + e.getMessage()
                    ));
        }
    }

}
