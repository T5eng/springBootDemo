package com.t5eng.miaosha.controller;

import com.t5eng.miaosha.domain.User;
import com.t5eng.miaosha.redis.RedisService;
import com.t5eng.miaosha.redis.UserKey;
import com.t5eng.miaosha.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.t5eng.miaosha.result.Result;
import com.t5eng.miaosha.result.CodeMsg;

@Controller // 用于定义控制类 负责将用户的url请求转发到相应的服务接口(service层)
@RequestMapping("/demo") /**提供路由信息, 负责url到Controller中具体函数的映射. 每个@RequestMapping都会增加一个层级**/
public class SampleController {

    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

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

    @RequestMapping("/db/get")
    @ResponseBody
    public Result<String> dbGet(){
        User user = userService.getById(1);
        return Result.success(user.getName());
    }

    @RequestMapping("/db/tx")
    @ResponseBody
    public Result<Boolean> dbTx(){
        userService.tx();
        return Result.success(true);
    }

    @RequestMapping("/redis/get")
    @ResponseBody
    public Result<User> redisGet(){
        User user = redisService.get(UserKey.getById,""+1, User.class);
        return Result.success(user);
    }

    @RequestMapping("/redis/set")
    @ResponseBody
    public Result<Boolean> redisSet(){
        User user = new User();
        user.setId(0000);
        user.setName("000000");
        redisService.set(UserKey.getById, ""+1, user);
        return Result.success(true);
    }
}
