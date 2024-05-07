package com.sieum.user.controller.feign;

import com.sieum.user.dto.response.CouponeInquiryResponse;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "QUIZ")
public interface QuizFeignClient {

    @GetMapping("/quizzes/coupons/{userId}/history")
    List<CouponeInquiryResponse> getCouponHistory(@PathVariable("userId") long userId);
}
