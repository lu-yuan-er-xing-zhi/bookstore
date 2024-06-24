package com.rabbiter.bms.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class buy {
    private String buyId;
    private String bookName;
    private BigDecimal bookPrice;
    private LocalDateTime buyTime;
    private int buyNum;
    private int capacity;
}
