package com.sieum.music.service;

import com.sieum.music.controller.feign.TokenAuthClient;
import com.sieum.music.dto.response.WatchPlaylistItemResponse;
import com.sieum.music.repository.PlaylistRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WatchService {

    private final TokenAuthClient tokenAuthClient;
    private final PlaylistRepository playlistRepository;

    public long getCurrentUserId(String authorization) {
        return tokenAuthClient.getUserId(authorization);
    }

    @Transactional(readOnly = true)
    public List<WatchPlaylistItemResponse> getPlaylist(final long userId) {
        return playlistRepository.findByUserId(userId).stream()
                .map(WatchPlaylistItemResponse::of)
                .collect(Collectors.toList());
    }
}
