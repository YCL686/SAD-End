package com.example.sharablead.request;

import lombok.Data;

@Data
public class OperateFocusRequest {

    private Long userId;

    private Long focusedId;
}
