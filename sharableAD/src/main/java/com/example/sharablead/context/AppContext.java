package com.example.sharablead.context;

import lombok.Data;

import java.util.List;

@Data
public class AppContext {
    private static final String APP_CONTEXT_KEY = "APP_CONTEXT_KEY";
    private Long userId;
    private String address;
    private List<String> roleNames;


    public static AppContext getContext() {
        AppContext context = (AppContext) ThreadContext.getContext().getValue(APP_CONTEXT_KEY);
        if (null == context) {
            context = new AppContext();
            ThreadContext.getContext().setValue(APP_CONTEXT_KEY, context);
        }
        return context;
    }

    public static void setCurrentContext(AppContext context) {
        ThreadContext.getContext().setValue(APP_CONTEXT_KEY, context);
    }

    public static void removeContext() {
        ThreadContext.getContext().reset();
    }
}
