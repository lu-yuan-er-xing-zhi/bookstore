package com.rabbiter.bms.service;

import com.rabbiter.bms.model.BookInfo;
import com.rabbiter.bms.model.buy;

import java.util.List;
import java.util.Map;

public interface BuyService {
    Integer buy_book(BookInfo bookInfo);
    List<buy> page_buy(Map<String, Object> params);
}
