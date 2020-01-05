package com.t5eng.miaosha.controller;

import com.sun.org.apache.bcel.internal.classfile.Code;
import com.t5eng.miaosha.domain.User;
import com.t5eng.miaosha.redis.RedisService;
import com.t5eng.miaosha.redis.UserKey;
import com.t5eng.miaosha.result.CodeMsg;
import com.t5eng.miaosha.result.Result;
import com.t5eng.miaosha.service.MiaoshaUserService;
import com.t5eng.miaosha.service.UserService;
import com.t5eng.miaosha.util.ValidatorUtil;
import com.t5eng.miaosha.vo.LoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequestMapping("/login") /**每个@RequestMapping都会增加一个层级 **/
public class LoginController {

    @Autowired
    MiaoshaUserService miaoshaUserService;
    @Autowired
    RedisService redisService;

    private static Logger log = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping("/to_login")
    public String toLogin(){
        return "login";
    }

    @RequestMapping("/do_login")
    @ResponseBody
    public Result<String> doLogin(HttpServletResponse response, @Valid LoginVo loginVo){ //校验输入的是否符合规定
        log.info(loginVo.toString());
        //登录
        String token = miaoshaUserService.login(response, loginVo);
        return Result.success(token);
    }


}
