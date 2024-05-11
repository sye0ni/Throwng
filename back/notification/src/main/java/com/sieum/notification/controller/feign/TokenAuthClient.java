package com.sieum.notification.controller.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "USER")
public interface TokenAuthClient {

    @GetMapping("/users/user/{userId}/fcm")
    String getUserFcm(@PathVariable final long userId);
}
