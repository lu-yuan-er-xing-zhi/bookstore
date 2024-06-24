package com.rabbiter.bms.service.impl;

import com.rabbiter.bms.mapper.StorageMapper;
import com.rabbiter.bms.model.BookInfo;
import com.rabbiter.bms.model.Storage;
import com.rabbiter.bms.service.StorageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StorageServiceImpl implements StorageService {
    @Resource
    private StorageMapper storageMapper;

    @Override
    public Integer getCount() {
        return storageMapper.selectCount();
    }

    @Override
    public List<Storage> queryStorage() {
        return storageMapper.selectAll();
    }

    @Override
    public List<Storage> searchStorageByPage(Map<String, Object> params) {
        return storageMapper.selectBySearch(params);
    }

    @Override
    public Integer getSearchCount(Map<String, Object> params) {
        return storageMapper.selectCountBySearch(params);
    }

    @Override
    public Integer deleteStorage(Storage storage) {
        int count = 0;
        try{
            Map<String, Object> map = new HashMap<>();
            map.put("warehouseid", storage.getWarehouseid());
//            if(borrowMapper.selectCountBySearch(map) > 0) {
//                return -1;
//            }
            count = storageMapper.deleteByPrimaryKey(storage.getWarehouseid());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    @Override
    public Integer updateStorage(Storage storage) {
        return storageMapper.updateByPrimaryKeySelective(storage);
    }


    @Override
    public Integer addStorage(Storage storage) {
        return storageMapper.insertSelective(storage);
    }

}
