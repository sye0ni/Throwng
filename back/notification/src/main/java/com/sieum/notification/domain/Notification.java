package com.sieum.notification.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("notification")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Notification {

    @Id private String id;
    private String title;
    private String body;
    private String image;
    private String name;
    //    private String url;
}
