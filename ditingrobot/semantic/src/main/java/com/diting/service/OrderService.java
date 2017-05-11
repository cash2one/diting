package com.diting.service;

import com.diting.model.Order;

/**
 * Created by Administrator on 2017/2/22.
 */
public interface OrderService {
    void create(Order order);
    void update(Order order);
    Order findByOrderNumber(Order order);
}
