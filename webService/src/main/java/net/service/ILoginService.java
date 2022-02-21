package net.service;

import net.pojo.User;

import java.util.Map;

public interface ILoginService {
    int queryLogin(Map<String, Object> map);
}
