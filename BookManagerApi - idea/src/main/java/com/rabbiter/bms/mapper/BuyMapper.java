package com.rabbiter.bms.mapper;

import com.rabbiter.bms.model.buy;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BuyMapper {
    void buy_book(buy buy);
    List<buy> page_buy(Map<String, Object> params);
}
