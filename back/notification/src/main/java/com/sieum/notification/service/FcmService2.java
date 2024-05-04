package com.sieum.notification.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import com.sieum.notification.dto.FcmMessage2;
import com.sieum.notification.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import static com.sieum.notification.exception.CustomExceptionStatus.*;

/**
 * SEND NOTIFICATION TO MAJORITY
 */
@Service
@RequiredArgsConstructor
public class FcmService2 {
    private final String API_URL =
            "https://fcm.googleapis.com/v1/projects/throwng-a72e9/messages:send";
    private final ObjectMapper objectMapper;

    public void sendMessageTo(final List<String> targetTokens, final String link, final FcmMessage2 fcmMessage2) {

        final MulticastMessage message = makeMessage(targetTokens, fcmMessage2);

        try {
            FirebaseMessaging.getInstance().sendMulticast(message);
        } catch (FirebaseMessagingException e) {
            throw new BadRequestException(FCM_SENDING_ERROR);
        }
//        final OkHttpClient client = new OkHttpClient();
//        final RequestBody requestBody =
//                RequestBody.create(message, MediaType.get("application/json; charset=utf-8"));
//        final Request request =
//                new Request.Builder()
//                        .url(API_URL)
//                        .post(requestBody)
//                        .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
//                        .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
//                        .build();
//
//        try {
//            client.newCall(request).execute();
//        } catch (IOException e) {
//            throw new BadRequestException(FCM_SENDING_ERROR);
//        }
    }

//    private String getAccessToken() {
//        final String firebaseConfigPath = "firebase/throwng-firebase-adminsdk.json";
//        try {
//            final GoogleCredentials googleCredentials =
//                    GoogleCredentials.fromStream(
//                                    new ClassPathResource(firebaseConfigPath).getInputStream())
//                            .createScoped(
//                                    List.of("https://www.googleapis.com/auth/cloud-platform"));
//            googleCredentials.refreshIfExpired();
//            return googleCredentials.getAccessToken().getTokenValue();
//        } catch (IOException e) {
//            throw new BadRequestException(GOOGLE_REQUEST_TOKEN_ERROR);
//        }
//    }

    private MulticastMessage makeMessage(final List<String> targetTokens, final FcmMessage2 fcmMessage2) {
        final MulticastMessage fcmMessage =
                MulticastMessage.builder()
                        .putAllData(new HashMap<>(){{
                            put("time", LocalDateTime.now().toString());
                            put("link", fcmMessage2.getLink());
                        }})
                        .setNotification(Notification.builder()
                                .setTitle(fcmMessage2.getTitle())
                                .setBody(fcmMessage2.getBody())
                                .setImage(fcmMessage2.getImage())
                                .build())
                        .addAllTokens(targetTokens)
                        .build();
        return fcmMessage;
    }
}
