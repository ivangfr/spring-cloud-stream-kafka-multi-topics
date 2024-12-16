package com.ivanfranchin.producerkafka.news;

import java.util.UUID;

public record News(String id, String source, String title) {

    public static News from(CreateNewsRequest createNewsRequest) {
        return new News(UUID.randomUUID().toString(), createNewsRequest.source(), createNewsRequest.title());
    }
}
