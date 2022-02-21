package net.dao;

import java.util.List;
import java.util.Map;
import net.pojo.User;
public interface IUserDao {
    int queryUserLogin(Map<String, Object> map);
    User queryUserByUsername(Map<String, Object> map);
    User queryUserById(Map<String, Object> map);
    int insertUser(Map<String,Object> map);
    int updateUserInfo(Map<String,Object> map);
    int updateUserAvatar(Map<String,Object> map);
    List<Integer> queryReadHistory(Map<String,Object> map);
}
