package com.sieum.user.controller.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "MUSIC")
public interface MusicFeignClient {

    @GetMapping("/music/thrown-song/{userId}")
    Long countThrownSong(@PathVariable("userId") long userId);

    @GetMapping("/music/pick-song/{userId}")
    Long countPickUpSong(@PathVariable("userId") long userId);
}
