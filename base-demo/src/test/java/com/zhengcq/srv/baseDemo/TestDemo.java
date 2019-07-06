package com.zhengcq.srv.baseDemo;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestDemo {

    @Test
    public void test1(){
        int i = 128;
        byte j = (byte)i;

        System.out.println(j);


        System.out.println(0.3 * 1 == 0.3);
    }

}
