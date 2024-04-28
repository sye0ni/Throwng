package com.sieum.notification.service;

import static com.sieum.notification.exception.CustomExceptionStatus.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.sieum.notification.dto.FcmMessage;
import com.sieum.notification.exception.BadRequestException;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FcmService {
    private final String API_URL =
            "https://fcm.googleapis.com/v1/projects/throwng-a72e9/messages:send";
    private final ObjectMapper objectMapper;

    public void sendMessageTo(final String targetToken, final String title, final String body) {

        final String message = makeMessage(targetToken, title, body);

        final OkHttpClient client = new OkHttpClient();
        final RequestBody requestBody =
                RequestBody.create(message, MediaType.get("application/json; charset=utf-8"));
        final Request request =
                new Request.Builder()
                        .url(API_URL)
                        .post(requestBody)
                        .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                        .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                        .build();

        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            throw new BadRequestException(FCM_SENDING_ERROR);
        }
    }

    private String getAccessToken() {
        final String firebaseConfigPath = "firebase/throwng-firebase-adminsdk.json";
        try {
            final GoogleCredentials googleCredentials =
                    GoogleCredentials.fromStream(
                                    new ClassPathResource(firebaseConfigPath).getInputStream())
                            .createScoped(
                                    List.of("https://www.googleapis.com/auth/cloud-platform"));
            googleCredentials.refreshIfExpired();
            return googleCredentials.getAccessToken().getTokenValue();
        } catch (IOException e) {
            throw new BadRequestException(GOOGLE_REQUEST_TOKEN_ERROR);
        }
    }

    private String makeMessage(final String targetToken, final String title, final String body) {
        final FcmMessage fcmMessage =
                FcmMessage.builder()
                        .message(
                                FcmMessage.Message.builder()
                                        .token(targetToken)
                                        .notification(
                                                FcmMessage.Notification.builder()
                                                        .title(title)
                                                        .body(body)
                                                        .image(null)
                                                        .build())
                                        .build())
                        .validateOnly(false)
                        .build();

        try {
            return objectMapper.writeValueAsString(fcmMessage);
        } catch (JsonProcessingException e) {
            throw new BadRequestException(CONVERTING_JSON_ERROR);
        }
    }
}
