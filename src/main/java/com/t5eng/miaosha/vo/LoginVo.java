package com.t5eng.miaosha.vo;

import com.t5eng.miaosha.validator.IsMobile; //自定义的修饰符
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class LoginVo {

    @NotNull
    @IsMobile //@Valid @自定义的修饰符
    private String mobile;
    @NotNull
    @Length(min=32)//@Valid @Length(min)验证长度
    private String password;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginVo[" +
                "mobile='" + mobile + '\'' +
                ", password='" + password + '\'' +
                "]";
    }
}
