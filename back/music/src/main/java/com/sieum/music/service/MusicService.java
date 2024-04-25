package com.sieum.music.service;

import static com.sieum.music.exception.CustomExceptionStatus.*;

import com.sieum.music.controller.feign.TokenAuthClient;
import com.sieum.music.domain.*;
import com.sieum.music.domain.ThrowItem;
import com.sieum.music.domain.enums.ThrowStatus;
import com.sieum.music.dto.request.NearItemPointRequest;
import com.sieum.music.dto.response.PlaylistItemResponse;
import com.sieum.music.dto.response.PoiResponse;
import com.sieum.music.dto.response.ThrownMusicDetailResponse;
import com.sieum.music.exception.BadRequestException;
import com.sieum.music.repository.*;
import com.sieum.music.util.GeomUtil;
import com.sieum.music.util.RedisUtil;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MusicService {

    private final TokenAuthClient tokenAuthClient;
    private final RedisUtil redisUtil;
    private final MusicRepository musicRepository;
    private final ThrowQueryDSLRepository throwQueryDSLRepository;
    private final ThrowHistoryRepository throwHistoryRepository;
    private final PlaylistRepository playlistRepository;
    private final PlaylistHistoryRepository playlistHistoryRepository;
    private final PlaylistQueryDSLRepository playlistQueryDSLRepository;

    public long getCurrentUserId(String authorization) {
        return tokenAuthClient.getUserId(authorization);
    }

    public ThrownMusicDetailResponse getDetailOfThrownMusic(final long userId, final long throwId) {
        final ThrowItem throwItem =
                musicRepository
                        .findById(throwId)
                        .orElseThrow(() -> new BadRequestException(NOT_FOUND_THROW_ITEM_ID));
        return ThrownMusicDetailResponse.of(throwItem, userId);
    }

    @Transactional
    public void createPickup(final long userId, final long throwId) {
        final ThrowItem throwItem =
                musicRepository
                        .findById(throwId)
                        .orElseThrow(() -> new BadRequestException(NOT_FOUND_THROW_ITEM_ID));
        createThrowHistory(userId, throwItem);
        findPlaylist(userId, throwItem.getSong(), true)
                .orElseGet(
                        () -> {
                            createPlaylistHistory(createPlaylist(userId, throwItem.getSong()));
                            return null;
                        });
    }

    private void createThrowHistory(final long userId, final ThrowItem throwItem) {
        final String key = "user_" + userId + "_pickup_count";
        final Object value = redisUtil.getData(key);
        int pickupCount = 0;

        if (value != null) {
            redisUtil.deleteData(key);
            pickupCount = Integer.valueOf((String) value);
        }

        pickupCount++;
        redisUtil.setData(key, String.valueOf(pickupCount));

        final ThrowHistory throwHistory =
                throwHistoryRepository.save(
                        ThrowHistory.builder().userId(userId).throwItem(throwItem).build());

        throwHistory.setThrowItem(throwItem);
    }

    private Playlist createPlaylist(final long userId, final Song song) {
        return playlistRepository.save(
                Playlist.builder().userId(userId).song(song).status(true).build());
    }

    private void createPlaylistHistory(final Playlist playlist) {
        playlistHistoryRepository.save(
                PlaylistHistory.builder().playlist(playlist).status(true).build());
    }

    private Optional<Playlist> findPlaylist(
            final long userId, final Song song, final boolean status) {
        return playlistRepository.findByUserIdAndSongIdAndStatus(userId, song.getId(), status);
    }

    public Slice<PlaylistItemResponse> getPlaylist(
            final long userId, final LocalDateTime modifiedAt) {
        Pageable pageable = PageRequest.of(0, 20);
        return playlistQueryDSLRepository.getPlaylist(userId, modifiedAt, pageable);
    }

    public List<PoiResponse> findNearItemsPoints(final NearItemPointRequest nearItemPointRequest) {
        final Point point =
                GeomUtil.createPoint(
                        nearItemPointRequest.getLongitude(), nearItemPointRequest.getLatitude());
        return throwQueryDSLRepository
                .findNearItemsPointsByDistance(
                        point,
                        nearItemPointRequest.getDistance(),
                        nearItemPointRequest.getInnerDistance())
                .stream()
                .filter(item -> item.getStatus().equals(ThrowStatus.valueOf("VISIBLE")))
                .map(PoiResponse::fromItemPoint)
                .collect(Collectors.toList());
    }
}
