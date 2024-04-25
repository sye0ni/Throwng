package com.sieum.music.controller;

import com.sieum.music.dto.response.SearchSongResponse;
import com.sieum.music.dto.request.NearItemPointRequest;
import com.sieum.music.dto.response.SearchSongResponse;
import com.sieum.music.service.MusicService;
import com.sieum.music.util.YoutubeMusicUtil;
import com.sieum.music.util.YoutubeMusicUtil;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MusicController {

    private final MusicService musicService;
    private final YoutubeMusicUtil youtubeMusicUtil;

    @Operation(summary = "show detail of a thrown music")
    @GetMapping("/thrown/{throwId}")
    public ResponseEntity<?> getDetailOfThrownMusic(
            @RequestHeader("Authorization") String authorization, @PathVariable long throwId) {
        long userId = musicService.getCurrentUserId(authorization);
        return ResponseEntity.ok().body(musicService.getDetailOfThrownMusic(userId, throwId));
    }

    @Operation(summary = "pick up a song")
    @PostMapping("/pick/{throwId}")
    public ResponseEntity<?> createPickup(
            @RequestHeader("Authorization") String authorization, @PathVariable long throwId) {
        long userId = musicService.getCurrentUserId(authorization);
        musicService.createPickup(userId, throwId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "search for songs on YouTube")
    @GetMapping("/search/{keyword}")
    public ResponseEntity<?> searchSong(@PathVariable("keyword") String keyword) {
        List<SearchSongResponse> searchSongResponse = youtubeMusicUtil.searchSongInYoutube(keyword);
        return ResponseEntity.ok().body(searchSongResponse);
    }

    @Operation(summary = "Search for list of dropped music within 600 radius")
    @GetMapping("/thrown/points")
    public ResponseEntity<?> findNearItemsPoints(
            @Valid @RequestBody NearItemPointRequest nearItemPointRequest) {
        return ResponseEntity.ok().body(musicService.findNearItemsPoints(nearItemPointRequest));
    }
}
