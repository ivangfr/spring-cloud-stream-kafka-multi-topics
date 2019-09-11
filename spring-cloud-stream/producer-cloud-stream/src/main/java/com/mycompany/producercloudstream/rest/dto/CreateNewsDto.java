package com.mycompany.producercloudstream.rest.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CreateNewsDto {

    @NotBlank
    private String source;

    @NotBlank
    private String title;

}
