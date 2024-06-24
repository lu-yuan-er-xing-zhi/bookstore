package com.rabbiter.bms.service.impl;

import com.rabbiter.bms.mapper.BookInfoMapper;
import com.rabbiter.bms.mapper.BuyMapper;
import com.rabbiter.bms.mapper.StorageMapper;
import com.rabbiter.bms.model.BookInfo;
import com.rabbiter.bms.model.buy;
import com.rabbiter.bms.service.BuyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
@Slf4j
public class BuyServiceImpl implements BuyService {
    @Autowired
    StorageMapper storageMapper;
    @Autowired
    BuyMapper buyMapper;
    @Autowired
    BookInfoMapper bookInfoMapper;
    @Override
    public Integer buy_book(BookInfo bookInfo) {
        System.out.println("bookInfo = " + bookInfo.getBookid().toString());
            //获取库存
            int capacity = storageMapper.get_capacity(bookInfo.getBookid().toString());
            log.info("当前库存"+capacity);
            if (capacity==0){
                return 0;
            }
            //建立购买信息
            int bookNum=capacity-1;
            buy buy = new buy();
            buy.setBuyTime(LocalDateTime.now());
            buy.setBuyNum(1);
            buy.setBookName(bookInfo.getBookname());
            buy.setBookPrice(bookInfo.getBookprice());
            buy.setCapacity(bookNum);
            buy.setBuyId(generateRandomString(6));
            buyMapper.buy_book(buy);
            //更新库存
            storageMapper.updateCapacity(bookNum,bookInfo.getBookid());
            //同步书籍信息
            bookInfoMapper.update_storage(bookNum,bookInfo.getBookid());
            //更新购买总量
            storageMapper.updateBuy_num(bookInfo.getBookid(),1);

        return 1;
    }

    @Override
    public List<buy> page_buy(Map<String, Object> params) {
        List<buy> buys = buyMapper.page_buy(params);
        log.info(buys.toString());
        return buys;
    }

    private  String generateRandomString(int length) {
        // 定义可能的字符集合
        String charPool = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            // 随机选择一个字符并添加到StringBuilder中
            int randomIndex = random.nextInt(charPool.length());
            char randomChar = charPool.charAt(randomIndex);
            sb.append(randomChar);
        }

        return sb.toString();
    }
}
