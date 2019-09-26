package com.xteamstudio.exam.oms.lottery.dto;

import java.util.List;
import java.util.Optional;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * this lottery can be choose from range  1 to 52.
 */
public class In52NumberValidator implements ConstraintValidator<In52Number, List<Integer>> {

    @Override
    public boolean isValid(List<Integer> value, ConstraintValidatorContext context) {
        Optional<Integer> any = value.stream().filter(o -> o > 52 || o < 1).findAny();
        return !any.isPresent();
    }
}
