package com.t5eng.miaosha.service;

import com.t5eng.miaosha.dao.UserDao;
import com.t5eng.miaosha.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    UserDao userDao;

    public User getById(int id){
        return userDao.getById(id);
    }

    @Transactional
    public boolean tx(){
        User u1 = new User();
        u1.setId(2);
        u1.setName("John");
        userDao.insert(u1);

//        User u2 = new User();
//        u1.setId(3);
//        u1.setName("Bobby");
//        userDao.insert(u2);

        return true;
    }
}
