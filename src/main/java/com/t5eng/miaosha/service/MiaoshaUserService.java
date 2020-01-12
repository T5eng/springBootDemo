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
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.Cookie; //servlet自带的cookie类
import javax.servlet.http.HttpServletResponse; //servlet的response
import java.util.UUID;

@Service
public class MiaoshaUserService {

    public static final String COOKIE_NAME_TOKEN = "token"; //为cookie命名（nickname）

    @Autowired
    MiaoshaUserDao miaoshaUserDao; //与数据库打交道的

    @Autowired
    RedisService redisService;

    public MiaoshaUser getById(long id){
        return miaoshaUserDao.getById(id); //从数据库中返回MiaoshaUser类
    }

    public MiaoshaUser getByToken(HttpServletResponse httpServletResponse, String token){
        if(StringUtils.isEmpty(token)){
            return null;
        }
        MiaoshaUser user = redisService.get(MiaoshaUserKey.token, token, MiaoshaUser.class);

        if(user!=null){//刷新cookie有效期
            addCookie(httpServletResponse, token, user);
        }
        return user;

    }

    public String login(HttpServletResponse response, LoginVo loginVo) { //HttpServletResponse处理cookie信息，LoginVo读取前端传来的信息

        if (null==loginVo){
            throw new GlobalException( CodeMsg.SERVER_ERROR ); //自定义的一个RuntimeException类
        }
        String mobile = loginVo.getMobile();
        String formPass = loginVo.getPassword();
        //判断手机号是否存在
        MiaoshaUser user = getById(Long.parseLong(mobile)); //连接sql，根据id获取user对象
        if(null==user){
            throw new GlobalException( CodeMsg.MOBILE_NOT_EXIST);
        }


        // 验证数据库对象的密码
        String dbPass = user.getPassword();
        String saltDB = user.getSalt();
        String calcPass = MD5Util.formPassToDBPass(formPass, saltDB); //计算加密后的密码
        if(!calcPass.equals(dbPass)){//验证输入的密码是否与数据库中的一致
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
        System.out.println("------------密码验证通过--------------");

        //生成cookie
        String token = UUIDUtil.uuid(); //随机生成uuid
        addCookie(response, token, user); //cookie插入到response中
        System.out.println("------------log token---------"+token);
        return token;
    }

    private void addCookie(HttpServletResponse response, String token, MiaoshaUser user){
        redisService.set(MiaoshaUserKey.token, token, user); //在redis中缓存（前缀，uuid，用户对象）
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token); //创建cookie
        cookie.setMaxAge(MiaoshaUserKey.token.expireSecond());//cookie的有效期与redis缓存一致
        cookie.setPath("/");
        response.addCookie(cookie); //response中插入cookie
    }
}
