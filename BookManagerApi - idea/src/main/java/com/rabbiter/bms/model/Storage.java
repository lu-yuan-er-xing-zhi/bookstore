package com.rabbiter.bms.model;

import lombok.Data;

import java.util.Date;
@Data
public class Storage {
    public Integer warehouseid;
    public Integer capacity;
    public Integer bookid;
    public Integer workerid;
    public String bookname;
    public String site;
    public Date inputtime;
    public long buy_num;
}
