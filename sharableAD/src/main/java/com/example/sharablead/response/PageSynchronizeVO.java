package com.example.sharablead.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class PageSynchronizeVO {

    private Long id;

    private LocalDate synchronizeDate;

    private BigDecimal synchronizeAmount;

    private LocalDateTime gmtCreated;

    private LocalDateTime gmtModified;
}
