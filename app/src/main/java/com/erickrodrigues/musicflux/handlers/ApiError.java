package com.erickrodrigues.musicflux.handlers;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class ApiError {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime timestamp;

    private Integer status;

    private String error;

    private String message;

    private String path;
}
