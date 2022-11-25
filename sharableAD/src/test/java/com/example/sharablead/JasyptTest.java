package com.example.sharablead;

import com.ulisesbocchio.jasyptspringboot.encryptor.DefaultLazyEncryptor;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.core.env.StandardEnvironment;

public class JasyptTest {
    @Test
    public void test() {
        //your password
        System.setProperty("jasypt.encryptor.password", "xxx");
        StringEncryptor stringEncryptor = new DefaultLazyEncryptor(new StandardEnvironment());
        String originStr = "xxx";
        String encyptStr = stringEncryptor.encrypt(originStr);
        System.out.println("origin： " + originStr);
        System.out.println("encypt： " + encyptStr);
        System.out.println("decypt： " + stringEncryptor.decrypt(encyptStr));
    }
}

