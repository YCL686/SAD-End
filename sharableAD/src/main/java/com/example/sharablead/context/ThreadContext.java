package com.example.sharablead.context;

import java.util.HashMap;
import java.util.Map;

public class ThreadContext {
    private Map<String, Object> valueMao = new HashMap<>();
    private static ThreadLocal<ThreadContext> LOCAL = new ThreadLocal<>();


    public static ThreadContext getContext() {
        ThreadContext ctx = LOCAL.get();
        if (null == ctx) {
            ctx = new ThreadContext();
            LOCAL.set(ctx);
        }
        return ctx;
    }

    public void setValue(String name, Object value) {
        valueMao.put(name, value);
    }

    public Object getValue(String name) {
        return valueMao.get(name);
    }

    public void reset() {
        LOCAL.remove();
    }
}
