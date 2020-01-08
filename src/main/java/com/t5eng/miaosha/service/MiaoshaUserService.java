package com.t5eng.miaosha.service;

import com.t5eng.miaosha.dao.MiaoshaUserDao;
import com.t5eng.miaosha.domain.MiaoshaUser;
import com.t5eng.miaosha.exception.GlobalException;
import com.t5eng.miaosha.redis.MiaoshaUserKey;
import com.t5eng.miaosha.redis.RedisService;
import com.t5eng.miaosha.result.CodeMsg;
import com.t5eng.miaosha.util.MD5Util;
import com.t5eng.miaosha.util.UUIDUtil;
import com.t5eng.miaosha.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Service
public class MiaoshaUserService {

    private static final String COOKIE_NAME_TOKEN = "token";

    @Autowired
    MiaoshaUserDao miaoshaUserDao;

    @Autowired
    RedisService redisService;

    public MiaoshaUser getById(long id){
        return miaoshaUserDao.getById(id);
    }

    public boolean login(HttpServletResponse response, LoginVo loginVo) {

        if (null==loginVo){
            throw new GlobalException( CodeMsg.SERVER_ERROR); //自定义一个RuntimeException类
        }
        String mobile = loginVo.getMobile();
        String formPass = loginVo.getPassword();

        MiaoshaUser user = getById(Long.parseLong(mobile)); //访问sql, 根据手机id获取用户对象

        if(null==user){
            throw new GlobalException( CodeMsg.MOBILE_NOT_EXIST);
        }
        //验证密码
        String dbPass = user.getPassword();
        String saltDB = user.getSalt();
        String calcPass = MD5Util.PassToDBPass(formPass, saltDB);
        if(!calcPass.equals(dbPass)){
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }

        //生成cookie
        String token = UUIDUtil.uuid();
        redisService.set(MiaoshaUserKey.tocken, token, user);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        cookie.setMaxAge(MiaoshaUserKey.tocken.expireSecond());//设置cookie的有效期 为 该用户在redis缓存的有效期
        cookie.setPath("/");
        response.addCookie(cookie);
        // session, 跳转到登录后页面


        return true;
    }
}
