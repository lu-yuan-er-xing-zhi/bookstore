package com.rabbiter.bms.web;

import com.rabbiter.bms.model.buy;
import com.rabbiter.bms.service.BuyService;
import com.rabbiter.bms.utils.MyResult;
import com.rabbiter.bms.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/buy")
public class BuyController {
    @Autowired
    BuyService buyService;
    @GetMapping("/queryBuyByPage")
    public Map<String, Object> queryBuyByPage(@RequestParam Map<String, Object> params){
        MyUtils.parsePageParams(params);
        List<buy> buys = buyService.page_buy(params);
        return MyResult.getListResultMap(0, "success", buys.size(),buys);
    }
}
