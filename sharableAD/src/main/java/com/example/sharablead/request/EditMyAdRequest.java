package com.example.sharablead.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class EditMyAdRequest {
    @NotNull(message = "adId can not be null")
    private Long adId;

    private Long userId;

    @Size(max = 20, min = 1, message = "label size is less than 20")
    private String label;

    @NotNull(message = "resourceUrl can not be null")
    private String resourceUrl;

    @Size(max = 30, min = 1, message = "label size is less than 30")
    private String link;
}
