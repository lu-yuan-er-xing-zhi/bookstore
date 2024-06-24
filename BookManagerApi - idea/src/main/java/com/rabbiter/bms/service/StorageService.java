package com.rabbiter.bms.service;

import com.rabbiter.bms.mapper.BookInfoMapper;
import com.rabbiter.bms.model.BookInfo;
import com.rabbiter.bms.model.Storage;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

public interface StorageService {
     Integer getCount();
     List<Storage> queryStorage();
     List<Storage> searchStorageByPage(Map<String, Object> params);

     Integer addStorage(Storage storage);

     Integer getSearchCount(Map<String, Object> params);

     Integer deleteStorage(Storage storage);

     Integer updateStorage(Storage storage);
}
