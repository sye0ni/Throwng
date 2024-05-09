package com.sieum.quiz.controller.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;


@FeignClient(name = "USER")
public interface UserAuthClient {

    @GetMapping("/users/auth/id")
    long getUserId(@RequestHeader("Authorization") String accessToken);

    @GetMapping("/users/user/{userId}/fcm")
    String getUserFcm(@PathVariable final long userId);
}
