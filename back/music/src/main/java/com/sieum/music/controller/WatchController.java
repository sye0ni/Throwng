package com.sieum.music.controller;

import com.sieum.music.service.WatchService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/watch")
@RequiredArgsConstructor
public class WatchController {

    private final WatchService watchService;

    @Operation(summary = "Show playlist")
    @GetMapping("/playlists")
    public ResponseEntity<?> getPlaylist(
            @RequestHeader("Authorization") final String authorization) {
        final long userId = watchService.getCurrentUserId(authorization);
        return ResponseEntity.ok().body(watchService.getPlaylist(userId));
    }
}
