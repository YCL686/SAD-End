package com.example.sharablead.request;

import lombok.Data;

@Data
public class AddLaunchRequest {

    private Long userId;

    private String launchTitle;

    private String launchLink;

    private String launchDescription;

    private String launchUrl;

    private Long launchId;
}
