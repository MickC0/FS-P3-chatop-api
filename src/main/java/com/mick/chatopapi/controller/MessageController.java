package com.mick.chatopapi.controller;

import com.mick.chatopapi.dto.ErrorResponse;
import com.mick.chatopapi.dto.MessageRequestDto;
import com.mick.chatopapi.dto.SuccessResponse;
import com.mick.chatopapi.service.MessageService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@Tag(name = "Messages", description = "Message management APIs")
@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }


    @Operation(
        summary = "Create a new message",
        security = @SecurityRequirement(name = "bearerAuth"),
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Message created successfully",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SuccessResponse.class),
                    examples = {
                        @ExampleObject(
                            name = "SuccessResponse",
                            value = """
                            {
                              "message": "Message send with success"
                            }
                            """
                        )
                    }
                )
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Bad Request: Validation error or illegal argument",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = {
                        @ExampleObject(
                            name = "ValidationErrorResponse",
                            value = """
                            {
                              "error": "BAD_REQUEST",
                              "status": 400,
                              "message": "rental_id must not be null"
                            }
                            """
                        )
                    }
                )
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Unauthorized: User not authenticated or runtime error",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = {
                        @ExampleObject(
                            name = "UnauthorizedResponse",
                            value = """
                            {
                              "error": "UNAUTHORIZED",
                              "status": 401,
                              "message": "Utilisateur non authentifié"
                            }
                            """
                        )
                    }
                )
            )
        }
    )
    @PostMapping
    public ResponseEntity<?> createMessage(
            @Valid @RequestBody MessageRequestDto messageRequestDto,
            BindingResult bindingResult,
            Authentication authentication) {
        try {
            if (bindingResult.hasErrors()) {
                String errorMessage = bindingResult.getAllErrors()
                        .stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .reduce((e1, e2) -> e1 + "; " + e2)
                        .orElse("Validation error");

                return ResponseEntity
                        .badRequest()
                        .body(new ErrorResponse(
                            "BAD_REQUEST",
                            HttpStatus.BAD_REQUEST.value(),
                            errorMessage
                        ));
            }
            messageService.createMessage(messageRequestDto);
            return ResponseEntity.ok(new SuccessResponse("Message sent with success"));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse(
                        "BAD_REQUEST",
                        HttpStatus.BAD_REQUEST.value(),
                        e.getMessage()
                    )
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new ErrorResponse(
                        "UNAUTHORIZED",
                        HttpStatus.UNAUTHORIZED.value(),
                        e.getMessage()
                    )
            );
        }
    }
}

