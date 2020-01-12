package com.t5eng.miaosha.controller;

import com.t5eng.miaosha.domain.MiaoshaUser;
import com.t5eng.miaosha.redis.RedisService;
import com.t5eng.miaosha.service.MiaoshaUserService;
import org.codehaus.groovy.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/goods")
public class GoodController {

    @Autowired
    MiaoshaUserService miaoshaUserService;
    @Autowired
    RedisService redisService;

    @RequestMapping("/to_list")
    public String list(HttpServletResponse httpServletResponse, Model model,
                          @CookieValue(value=MiaoshaUserService.COOKIE_NAME_TOKEN,required = false) String cookieToken, //从cookie中获取用户uuid（即Token）
                          @RequestParam(value=MiaoshaUserService.COOKIE_NAME_TOKEN,required = false) String paramToken,//部分浏览器将用户uuid（即Token）写在param中
                       MiaoshaUser user)

    {
    if(StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken) ){
            return "lgoin"; //登录失败，返回登录页
        }

        String token = StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
        user = miaoshaUserService.getByToken(httpServletResponse, token);
        model.addAttribute("user", user);
        return "good_list"; //返回页面名
    }

}
