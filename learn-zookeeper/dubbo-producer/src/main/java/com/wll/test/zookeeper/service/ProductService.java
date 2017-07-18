package com.wll.test.zookeeper.service;

import org.springframework.stereotype.Service;

/**
 * Created by wll on 17-7-11.
 */
@Service
public class ProductService implements IProduct {

    @Override
    public String getProductName() {
        return "jim";
    }
}
