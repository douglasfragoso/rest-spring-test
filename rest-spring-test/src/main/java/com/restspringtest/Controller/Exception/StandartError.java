package com.restspringtest.Controller.Exception;

import java.io.Serializable;
import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter 
public class StandartError implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "Hour and date the error occurs", example = "2024-11-02T16:35:34Z", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "America/Sao_Paulo")
    private Instant timestamp;

    @Schema(description = "HTTP Status code", example = "404", required = true)
    private Integer status;

    @Schema(description = "Error message", example = "Resource not found", required = true)
    private String error;

    @Schema(description = "Error message", example = "Resource not found", required = true)
    private String message;

    @Schema(description = "Path of the request", example = "/person/id/1", required = true)
    private String path;
    
   

}
