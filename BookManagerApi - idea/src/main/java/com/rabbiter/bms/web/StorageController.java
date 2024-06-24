package com.rabbiter.bms.web;

import com.rabbiter.bms.model.BookInfo;
import com.rabbiter.bms.service.StorageService;
import com.rabbiter.bms.model.Storage;
import com.rabbiter.bms.utils.MyResult;
import com.rabbiter.bms.utils.MyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/storage")
@Slf4j
public class StorageController {

    @Autowired
    StorageService storageService;

    // 获取仓库数量
    @GetMapping(value = "/getCount")
    public Integer getCount() {
        return storageService.getCount();
    }

    //查询所有的库存信息
    @GetMapping(value = "/queryStorage")
    public List<Storage> queryStorage() {
        return storageService.queryStorage();
    }

    // 分页搜索查询图书信息 params: {page, limit, warehouseid, capacity, bookname}
    @GetMapping(value = "/queryStoragesByPage")
    public Map<String, Object> queryStoragesByPage(@RequestParam Map<String, Object> params) {
        log.info("进行分页查询"+params);
        MyUtils.parsePageParams(params);
        int count = storageService.getSearchCount(params);  // 获得总数
        List<Storage> storages = storageService.searchStorageByPage(params);  // 分页查询
        log.info(storages.toString());
        return MyResult.getListResultMap(0, "success", count, storages);
    }

    // 添加库存信息
    @PostMapping(value = "/addStorage")
    public Integer addStorage(@RequestBody Storage storage) {
        return storageService.addStorage(storage);
    }

    // 删除
    @DeleteMapping(value = "/deleteStorage")
    public Integer deleteStorage(@RequestBody Storage storage) {
        return storageService.deleteStorage(storage);
    }

    // 更新库存信息
    @PostMapping(value = "/updateStorage")
    public Integer updateBookInfo(@RequestBody Storage storage) {
        log.info(storage.toString());
        return storageService.updateStorage(storage);
    }
}
