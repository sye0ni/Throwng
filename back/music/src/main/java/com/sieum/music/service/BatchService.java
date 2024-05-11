package com.sieum.music.service;

import com.sieum.music.repository.BatchQueryDSLRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BatchService {
    private final BatchQueryDSLRepository batchQueryDSLRepository;

    private final float PERCENT = 0.05f;
    private final int MIN_COUNT = 20;

    @Transactional
    public void popularMusic() {
        List<Integer> zipCodeIds = batchQueryDSLRepository.getZipCodeId();
        for (int zipCodeId : zipCodeIds) {
            final Long legalDongPickUpCnt =
                    batchQueryDSLRepository.getLegalDongPickUpCount(zipCodeId);
            if (legalDongPickUpCnt < MIN_COUNT) {
                continue;
            }
            List<Long> throwIds =
                    batchQueryDSLRepository.getPopularThrowItems(
                            (int) (legalDongPickUpCnt * PERCENT), zipCodeId);
            batchQueryDSLRepository.popularMusic(throwIds);
        }
    }
}
