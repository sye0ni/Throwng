package com.sieum.notification.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Usage {
    CONTENT_QUIZ("coupon_content_notification", "컨텐츠");

    private String value;
    private String kr;
}
