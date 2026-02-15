package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.AddressBook;
import com.sky.entity.Order;
import com.sky.entity.OrderDetail;
import com.sky.entity.ShoppingCart;
import com.sky.exception.AddressBookBusinessException;
import com.sky.mapper.AddressBookMapper;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.service.CartService;
import com.sky.service.OrderService;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {


    private final AddressBookMapper addressBookMapper;
    private final CartService cartService;
    private final OrderMapper orderMapper;
    private final OrderDetailMapper orderDetailMapper;

    public OrderServiceImpl(AddressBookMapper addressBookMapper, CartService cartService, OrderMapper orderMapper, OrderDetailMapper orderDetailMapper) {
        this.addressBookMapper = addressBookMapper;
        this.cartService = cartService;
        this.orderMapper = orderMapper;
        this.orderDetailMapper = orderDetailMapper;
    }

    @Transactional
    @Override
    public OrderSubmitVO submit(OrdersSubmitDTO dto) {
        // exception
        AddressBook addr = addressBookMapper.selectById(dto.getAddressBookId());
        if (addr == null) {
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }
        List<ShoppingCart> carts = cartService.list();
        if (carts == null || carts.isEmpty()) {
            throw new AddressBookBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }
        // insert into order table
        Order order = new Order();
        BeanUtils.copyProperties(dto, order);
        order.setUserId(BaseContext.getCurrentId());
        order.setStatus(Order.PENDING_PAYMENT);
        order.setPayStatus(Order.UN_PAID);
        order.setOrderTime(LocalDateTime.now());
        order.setNumber(String.valueOf(System.currentTimeMillis()));
        order.setPhone(addr.getPhone());
        order.setConsignee(addr.getConsignee());
        orderMapper.insert(order);

        // insert into order_detail table
        for (ShoppingCart cart : carts) {
            OrderDetail detail = new OrderDetail();
            BeanUtils.copyProperties(cart, detail);
            detail.setOrderId(order.getId());
            orderDetailMapper.insert(detail);
        }

        // clean the current shopping cart
        cartService.clean();

        // encapsulate the VO info
        OrderSubmitVO vo = OrderSubmitVO.builder().id(order.getId())
                .orderTime(order.getOrderTime())
                .orderAmount(order.getAmount())
                .orderNumber(order.getNumber()).build();

        return vo;
    }
}
