package com.sieum.music.dto.response;

import com.sieum.music.domain.Throw;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ThrownMusicDetailResponse {

    @Schema(description = "thrown music id")
    private Long throwId;

    @Schema(description = "title")
    private String title;

    @Schema(description = "artist")
    private String artist;

    @Schema(description = "albumImage")
    private String albumImage;

    @Schema(description = "itemImage")
    private String itemImage;

    @Schema(description = "content")
    private String content;

    @Schema(description = "thrown date")
    private LocalDateTime thrownDate;

    public static ThrownMusicDetailResponse of(final Throw thrown) {
        return ThrownMusicDetailResponse.builder()
                .throwId(thrown.getId())
                .title(thrown.getSong().getTitle())
                .artist(thrown.getSong().getArtist().getName())
                .albumImage(thrown.getSong().getAlbumImage())
                .itemImage(thrown.getItemImage())
                .content(thrown.getContent())
                .thrownDate(thrown.getCreatedAt())
                .build();
    }
}
