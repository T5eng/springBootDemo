package com.t5eng.miaosha.controller;

import com.t5eng.miaosha.redis.RedisService;
import com.t5eng.miaosha.result.Result;
import com.t5eng.miaosha.service.MiaoshaUserService;
import com.t5eng.miaosha.vo.LoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
        return "login"; //返回页面名
    }

    @RequestMapping("/do_login")
    @ResponseBody
    public Result<String> doLogin(HttpServletResponse response, @Valid LoginVo loginVo){ //校验输入的是否符合规定 修饰符@Valid , 在该类的变量中定义具体验证规则
        log.info(loginVo.toString());

        //登录
        String token = miaoshaUserService.login(response, loginVo);
        System.out.println("miaoshaUserService返回的tocken： "+token);
        return Result.success(token);
    }

}
