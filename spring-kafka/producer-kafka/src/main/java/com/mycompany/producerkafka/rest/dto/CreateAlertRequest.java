package com.mycompany.producerkafka.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAlertRequest {

    @Min(1)
    @Max(5)
    private Integer level;

    @NotBlank
    private String message;
}
