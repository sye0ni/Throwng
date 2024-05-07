package com.sieum.batch.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;

@FeignClient(name = "MUSIC")
public interface MusicFeignClient {
    @DeleteMapping("/music/throw-items")
    long deleteNotFamousMusic();
}
