package com.t5eng.miaosha.validator;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = {IsMobileValidator.class}//具体判断方法
)
public @interface IsMobile { //自定义修饰符
    boolean required() default true; //某些情况允许false

    String message() default "{手机号码格式错误}";

    Class<?>[] groups() default {};//默认

    Class<? extends Payload>[] payload() default {};//默认

}
