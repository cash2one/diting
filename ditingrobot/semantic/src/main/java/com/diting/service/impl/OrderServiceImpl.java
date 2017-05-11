package com.diting.service.impl;

import com.diting.dao.OrderMapper;
import com.diting.model.Order;
import com.diting.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/2/22.
 */
@SuppressWarnings("ALL")
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderMapper orderMapper;

    @Override
    public void create(Order order) {
        orderMapper.create(order);
    }

    @Override
    public void update(Order order) {
        orderMapper.update(order);
    }

    @Override
    public Order findByOrderNumber(Order order) {
        return orderMapper.findByOrderNumber(order);
    }
}
