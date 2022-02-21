package net.service;

import net.pojo.User;

import java.util.Map;

public interface IQueryService {
    User queryUserByUsername(Map<String, Object> map);
    User queryUserById(Map<String, Object> map);
}
