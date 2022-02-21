package net.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import net.dao.IUserDao;
import net.pojo.User;
import net.service.IQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class QueryServiceImpl  implements IQueryService{

    @Autowired
    private IUserDao userMapper;

    @Override
    public User queryUserByUsername(Map<String, Object> map)
    {
        return userMapper.queryUserByUsername(map);
    }

    @Override
    public User queryUserById(Map<String, Object> map)
    {
        return userMapper.queryUserById(map);
    }



}
