package com.sieum.music.common.validator;

import com.sieum.music.annotation.IsLatitude;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LatitudeValidator implements ConstraintValidator<IsLatitude, Double> {
    private static final double MIN_LATITUDE = -90;
    private static final double MAX_LATITUDE = 90;

    @Override
    public boolean isValid(Double value, ConstraintValidatorContext context) {
        return value == null || !(value < MIN_LATITUDE) && !(value > MAX_LATITUDE);
    }
}
