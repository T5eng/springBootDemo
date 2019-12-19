package com.t5eng.miaosha.service;

import com.t5eng.miaosha.dao.MiaoshaUserDao;
import com.t5eng.miaosha.domain.MiaoshaUser;
import com.t5eng.miaosha.result.CodeMsg;
import com.t5eng.miaosha.util.MD5Util;
import com.t5eng.miaosha.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MiaoshaUserService {

    @Autowired
    MiaoshaUserDao miaoshaUserDao;

    public MiaoshaUser getById(long id){
        return miaoshaUserDao.getById(id);
    }

    public CodeMsg login(LoginVo loginVo) {
        if (null==loginVo){
            return CodeMsg.SERVER_ERROR;
        }
        String mobile = loginVo.getMobile();
        String formPass = loginVo.getPassword();
        //验证手机是否存在
        MiaoshaUser user = getById(Long.parseLong(mobile));
        if(null==user){
            return CodeMsg.MOBILE_NOT_EXIST;
        }
        //验证密码
        String dbPass = user.getPassword();
        String saltDB = user.getSalt();
        String calcPass = MD5Util.PassToDBPass(formPass, saltDB);
        if(!calcPass.equals(dbPass)){
            return CodeMsg.PASSWORD_ERROR;
        }
        return CodeMsg.SUCCESS;
    }
}
