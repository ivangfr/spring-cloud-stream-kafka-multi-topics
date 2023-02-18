package com.ivanfranchin.producercloudstream.rest.news;

import jakarta.validation.constraints.NotBlank;

public record CreateNewsRequest(@NotBlank String source, @NotBlank String title) {
}
