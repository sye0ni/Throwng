package com.sieum.music.service;

import static com.sieum.music.exception.CustomExceptionStatus.*;

import com.sieum.music.controller.feign.TokenAuthClient;
import com.sieum.music.domain.Song;
import com.sieum.music.domain.ThrowItem;
import com.sieum.music.domain.Zipcode;
import com.sieum.music.domain.dao.ThrowCurrentDao;
import com.sieum.music.domain.enums.ThrowStatus;
import com.sieum.music.dto.request.WatchThrownItemRequest;
import com.sieum.music.dto.response.KakaoMapReverseGeoResponse;
import com.sieum.music.dto.response.UserLevelInfoResponse;
import com.sieum.music.dto.response.WatchPlaylistItemResponse;
import com.sieum.music.exception.BadRequestException;
import com.sieum.music.repository.*;
import com.sieum.music.util.GeomUtil;
import com.sieum.music.util.KakaoMapReverseGeoUtil;
import com.sieum.music.util.LocalDateUtil;
import com.sieum.music.util.RedisUtil;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WatchService {

    private final MusicRepository musicRepository;
    private final TokenAuthClient tokenAuthClient;
    private final PlaylistRepository playlistRepository;
    private final ArtistRepository artistRepository;
    private final SongRepository songRepository;
    private final ZipCodeRepository zipCodeRepository;
    private final ThrowQueryDSLRepository throwQueryDSLRepository;
    private final LocalDateUtil localDateUtil;
    private final RedisUtil redisUtil;
    private final KakaoMapReverseGeoUtil kakaoMapReverseGeoUtil;

    public long getCurrentUserId(String authorization) {
        return tokenAuthClient.getUserId(authorization);
    }

    @Transactional(readOnly = true)
    public List<WatchPlaylistItemResponse> getPlaylist(final long userId) {
        return playlistRepository.findByUserId(userId).stream()
                .map(WatchPlaylistItemResponse::of)
                .collect(Collectors.toList());
    }

    public UserLevelInfoResponse getLimitAccount(String authorization) {
        return tokenAuthClient.getLimitAccount(authorization);
    }

    @Transactional
    public void thrownSong(
            final UserLevelInfoResponse userLevelInfoResponse,
            final WatchThrownItemRequest watchThrownItemRequest) {
        final long userId = userLevelInfoResponse.getUserId();
        final String nowDate = localDateUtil.GetDate(LocalDate.now());
        final String key = "user_throw_" + userId + "_" + nowDate;
        final Object value = redisUtil.getData(key);

        int thrownCount = 0;

        if (value == null) {
            thrownCount = userLevelInfoResponse.getLevelCount();
        } else {
            if (Integer.valueOf((String) value) == 0) {
                throw new BadRequestException(NOT_THROW_SONG);
            } else {
                thrownCount = Integer.valueOf((String) value);
            }
        }

        final Point point =
                GeomUtil.createPoint(
                        watchThrownItemRequest.getLongitude(),
                        watchThrownItemRequest.getLatitude());

        final Song song =
                songRepository
                        .findById(watchThrownItemRequest.getSongId())
                        .orElseThrow(() -> new BadRequestException(NOT_FOUND_SONG_ID));

        // Verification: The same user cannot throw the same song again within 100m
        ThrowCurrentDao throwDao =
                throwQueryDSLRepository
                        .findNearItemsPointsByDistanceAndUserIdAndCreatedAtAndYoutubeId(
                                point, 100.0, userId, nowDate, song.getYoutubeId());

        if (throwDao != null) {
            if (throwDao.getStatus().equals(ThrowStatus.valueOf("VISIBLE"))) {
                throw new BadRequestException(NOT_THROW_ITEM_IN_LIMITED_RADIUS);
            }
        }

        KakaoMapReverseGeoResponse kakaoMapReverseGeoResponse =
                kakaoMapReverseGeoUtil.getReverseGeo(
                        watchThrownItemRequest.getLatitude(),
                        watchThrownItemRequest.getLongitude());

        Zipcode zipcode =
                zipCodeRepository
                        .findByCode(kakaoMapReverseGeoResponse.getDocuments().get(0).code)
                        .orElseThrow(() -> new BadRequestException(NOT_FOUND_ZIP_CODE));

        musicRepository.save(
                ThrowItem.builder()
                        .content(watchThrownItemRequest.getComment())
                        .itemImage(null)
                        .status(ThrowStatus.valueOf("VISIBLE"))
                        .locationPoint(point)
                        .userId(userId)
                        .zipcode(zipcode)
                        .song(song)
                        .build());

        thrownCount--;

        redisUtil.setData(key, String.valueOf(thrownCount));
    }
}
