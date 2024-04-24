package com.sieum.music.service;

import static com.sieum.music.exception.CustomExceptionStatus.*;

import com.sieum.music.domain.Throw;
import com.sieum.music.dto.response.ThrownMusicDetailResponse;
import com.sieum.music.exception.BadRequestException;
import com.sieum.music.repository.MusicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MusicService {

    private final MusicRepository musicRepository;

    public ThrownMusicDetailResponse getDetailOfThrownMusic(final long throwId) {
        Throw thrown =
                musicRepository
                        .findById(throwId)
                        .orElseThrow(() -> new BadRequestException(NOT_FOUND_THROW_ID));
        return ThrownMusicDetailResponse.of(thrown);
    }
}
