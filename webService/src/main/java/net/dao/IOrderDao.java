package net.dao;

import net.pojo.Order;

import java.util.List;
import java.util.Map;

public interface IOrderDao {
    List<Order> queryOrderList(Map<String, Object> map);
}
