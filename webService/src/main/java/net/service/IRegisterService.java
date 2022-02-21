package net.service;

import java.util.Map;

public interface IRegisterService {
    int insertUser(Map<String, Object> map);
    int updateUserInfo(Map<String, Object> map);
    int updateUserAvatar(Map<String, Object> map);
}
