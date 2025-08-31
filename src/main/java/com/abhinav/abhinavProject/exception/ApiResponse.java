package com.abhinav.abhinavProject.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse {
    int status;
    String message;
    Object details;

    public ApiResponse(String message) {
        this.message = message;
    }

    public ApiResponse(String message, Object details) {
        this.message = message;
        this.details = details;
    }
}
