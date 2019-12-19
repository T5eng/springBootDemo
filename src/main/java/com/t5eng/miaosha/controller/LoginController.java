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
        return "Login";
    }

    @RequestMapping("/do_login")
    @ResponseBody
    public Result<Boolean> doLogin(LoginVo loginVo){
        log.info(loginVo.toString());
        String passInput = loginVo.getPassword();
        String mobileNum = loginVo.getMobile();
        if(StringUtils.isEmpty(passInput)){
            return Result.error(CodeMsg.PASSWORD_EMPTY);
        }
        if(StringUtils.isEmpty(mobileNum)){
            return Result.error(CodeMsg.MOBILE_EMPTY);
        }
        if(!ValidatorUtil.isMobile(mobileNum)){
            return Result.error(CodeMsg.MOBILE_ERROR);
        }
        //登录
        CodeMsg cm = miaoshaUserService.login(loginVo);
        if(cm.getCode()==0){
            return Result.success(true);
        }else{
            return Result.error(cm);
        }
    }

    @RequestMapping("/hello")
    @ResponseBody
    public Result<String> home(){
        return Result.success("Hello, World!");
    }

    @RequestMapping("/error")
    @ResponseBody
    public Result<String> error(){
        return Result.error(CodeMsg.SESSION_ERROR);
    }

    @RequestMapping("/hello/thymeleaf")
    public String thymeleaf(Model model){
        model.addAttribute("name", "Joshua");
        return "hello";
    }

}
