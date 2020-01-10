package com.t5eng.miaosha.validator;

import com.t5eng.miaosha.util.ValidatorUtil;
import org.thymeleaf.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class IsMobileValidator implements ConstraintValidator <IsMobile, String>{
    private boolean required=false;
    @Override
    public void initialize(IsMobile isMobile) {
        required = isMobile.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if(!required && StringUtils.isEmpty(value)){
            return true;
        }else{
            return ValidatorUtil.isMobile(value);
        }
    }
}
