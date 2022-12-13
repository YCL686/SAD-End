package com.example.sharablead.util;

import cn.hutool.core.lang.ObjectId;
import cn.hutool.core.lang.Singleton;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.lang.UUID;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicLong;

public class IDUtil {
    private static AtomicLong id;
    public static void main(String[] args) throws InterruptedException {
        Set<Long> set = new TreeSet<>();
        for (int i = 0; i < 100; i++) {
            new Thread(() ->
            {
                Long id = nextId();
                set.add(id);
                System.out.println(id);
            }
            ).start();
        }
        Thread.sleep(3000);
        int size = set.size();
        System.out.println(size);
    }

    /**
     * generate Long and Unique Id
     */
    public synchronized static Long nextId() {
        //generate 100 non-repeat ids in 1ms, once need more or longer, multiple *1000, *10000... may cause overflow
        long time = Long.parseLong(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()))*100;
        if (id == null) {
            id = new AtomicLong(time);
            return id.get();
        }
        if (time <= id.get()) {
            id.addAndGet(1);
        } else {
            id = new AtomicLong(time);
        }
        return id.get();
    }
    // ------------------------------------------------------------------- UUID

    /**
     * generate random Id
     *
     * @return
     */
    public static String randomUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * generate simplified random Id
     *
     * @return
     */
    public static String simpleUUID() {
        return UUID.randomUUID().toString(true);
    }

    /**
     * generate random Id faster
     *
     * @return
     */
    public static String fastUUID() {
        return UUID.fastUUID().toString();
    }

    /**
     * generate simplified random Id faster
     *
     * @return
     */
    public static String fastSimpleUUID() {
        return UUID.fastUUID().toString(true);
    }

    //TODO now is single system, one day we level up to distributed system and need more kinds of id generate methods such as snowflake etc.

}
