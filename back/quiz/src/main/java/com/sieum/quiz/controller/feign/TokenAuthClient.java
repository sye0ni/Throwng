package com.sieum.quiz.controller.feign;

import com.sieum.quiz.dto.request.UpdateExperiencePointRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "USER")
public interface TokenAuthClient {

    @GetMapping("/users/auth/id")
    long getUserId(@RequestHeader("Authorization") String accessToken);

    @PostMapping("/users/user/experience-point")
    void upgradeExperiencePoint(
            @RequestBody final UpdateExperiencePointRequest updateExperiencePointRequest);
}
