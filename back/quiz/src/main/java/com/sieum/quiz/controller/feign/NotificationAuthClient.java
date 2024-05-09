package com.sieum.quiz.controller.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "NOTIFICATION")
public interface NotificationAuthClient {

    @PostMapping("/notifications/coupons")
    void sendCouponExpirationUserIdList(@RequestBody final List<Long> userIdList);
}
