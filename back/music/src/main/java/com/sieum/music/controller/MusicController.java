package com.sieum.music.controller;

import com.sieum.music.dto.request.NearItemPointRequest;
import com.sieum.music.dto.response.SearchSongResponse;
import com.sieum.music.service.MusicService;
import com.sieum.music.util.YoutubeMusicUtil;
import io.swagger.v3.oas.annotations.Operation;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MusicController {

    private final MusicService musicService;
    private final YoutubeMusicUtil youtubeMusicUtil;

    @Operation(summary = "show detail of a thrown music")
    @GetMapping("/thrown/{throwId}")
    public ResponseEntity<?> getDetailOfThrownMusic(
            @RequestHeader("Authorization") final String authorization,
            @PathVariable final long throwId) {
        final long userId = musicService.getCurrentUserId(authorization);
        return ResponseEntity.ok().body(musicService.getDetailOfThrownMusic(userId, throwId));
    }

    @Operation(summary = "pick up a song")
    @PostMapping("/pick/{throwId}")
    public ResponseEntity<?> createPickup(
            @RequestHeader("Authorization") final String authorization,
            @PathVariable final long throwId) {
        final long userId = musicService.getCurrentUserId(authorization);
        musicService.createPickup(userId, throwId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "show playlist")
    @GetMapping("/playlists")
    public ResponseEntity<?> getPlaylist(
            @RequestHeader("Authorization") final String authorization,
            @RequestParam(value = "time", required = false)
                    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
                    final LocalDateTime modifiedAt) {
        final long userId = musicService.getCurrentUserId(authorization);
        return ResponseEntity.ok().body(musicService.getPlaylist(userId, modifiedAt));
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

    @Operation(summary = "delete playlist")
    @DeleteMapping("/playlists/{playlistId}")
    public ResponseEntity<?> deletePlaylist(
            @RequestHeader("Authorization") final String authorization,
            @PathVariable final int playlistId) {
        final long userId = musicService.getCurrentUserId(authorization);
        musicService.deletePlaylist(userId, playlistId);
        return ResponseEntity.noContent().build();
    }
}
