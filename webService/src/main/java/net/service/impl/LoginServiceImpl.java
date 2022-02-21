
package net.service.impl;

import net.dao.IUserDao;
import net.pojo.User;
import net.service.ILoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class LoginServiceImpl implements ILoginService {
    @Autowired
    private IUserDao userMapper;

    @Override
    public int queryLogin(Map<String, Object> map){
        return userMapper.queryUserLogin(map);
    }


}


