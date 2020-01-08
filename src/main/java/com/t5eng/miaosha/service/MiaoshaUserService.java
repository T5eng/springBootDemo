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

import javax.servlet.http.Cookie; //servlet自带的cookie类
import javax.servlet.http.HttpServletResponse; //servlet的response
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

    public String login(HttpServletResponse response, LoginVo loginVo) {

        if (null==loginVo){
            throw new GlobalException( CodeMsg.SERVER_ERROR ); //自定义一个RuntimeException类
        }
        String mobile = loginVo.getMobile();
        String formPass = loginVo.getPassword();
        System.out.println("------------login mobile---------" + mobile);
        System.out.println("------------login pass---------" + formPass);

        MiaoshaUser user = getById(Long.parseLong(mobile)); //连接sql，获取user对象
        if(null==user){
            System.out.println("------------返回空user---------");
            throw new GlobalException( CodeMsg.MOBILE_NOT_EXIST);
        }

        //从user对象密码
        String dbPass = user.getPassword();
        String saltDB = user.getSalt();
        System.out.println("------------database password---------" + dbPass);
        System.out.println("------------database salt---------" + dbPass);

        String calcPass = MD5Util.PassToDBPass(formPass, saltDB); //计算加密后的密码
        System.out.println("------------calcPass password---------" + calcPass);

        if(!calcPass.equals(dbPass)){//验证输入的密码是否与数据库中的一致
            System.out.println("------------密码验证失败---------");
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }

        System.out.println("------------密码验证通过---------");

        //生成cookie
        String token = UUIDUtil.uuid(); //随机生成uuid
        addCookie(response, token, user);
        System.out.println("------------log token---------"+token);
        return token;
    }

    private void addCookie(HttpServletResponse response, String token, MiaoshaUser user){
        redisService.set(MiaoshaUserKey.tocken, token, user); //在redis中缓存（前缀，uuid，用户对象）

        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token); //创建cookie
        cookie.setMaxAge(MiaoshaUserKey.tocken.expireSecond());//设置cookie的有效期 为 该用户在redis缓存的有效期
        cookie.setPath("/");
        response.addCookie(cookie); //response中插入cookie
    }
}
