package net.service.impl;

import net.dao.IUserDao;
import net.service.IRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RegisterServiceImpl implements IRegisterService {
    @Autowired
    private IUserDao userMapper;

    @Override
    public int insertUser(Map<String, Object> map){
        return userMapper.insertUser(map);
    }

    @Override
    public int updateUserInfo(Map<String, Object> map) {
//        if(!map.containsKey("username")){map.put("username",null);}
//        if(!map.containsKey("password")){map.put("password",null);}
        return userMapper.updateUserInfo(map);
    }

    @Override
    public int updateUserAvatar(Map<String, Object> map) {
        return userMapper.updateUserAvatar(map);
    }
}
