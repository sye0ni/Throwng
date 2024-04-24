package com.sieum.music.controller;

import com.sieum.music.service.MusicService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MusicController {

    private final MusicService musicService;

    @Operation(summary = "show detail of a thrown music")
    @GetMapping("/thrown/{throwId}")
    public ResponseEntity<?> getDetailOfThrownMusic(@PathVariable("throwId") long throwId) {
        return ResponseEntity.ok().body(musicService.getDetailOfThrownMusic(throwId));
    }
}
