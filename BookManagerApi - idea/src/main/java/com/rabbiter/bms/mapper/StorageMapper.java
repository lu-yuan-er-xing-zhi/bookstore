package com.rabbiter.bms.mapper;

import com.rabbiter.bms.model.Storage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
@Mapper()
public interface StorageMapper {
    List<Storage> selectAll();

    List<Storage> selectBySearch(Map<String, Object> searchParam);


    int insertSelective(Storage record);

    int selectCountBySearch(Map<String, Object> searchParam);

    Integer selectCount();

    int deleteByPrimaryKey(Integer warehouseid);

    int updateByPrimaryKeySelective(Storage record);
    int get_capacity(@Param("book_id") String book_id);
    void updateCapacity(@Param("capacity") int capacity,@Param("bookId") int bookId);
    void updateBuy_num(@Param("bookId") int bookId,@Param("buy_num") int buy_num);
}
