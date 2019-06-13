package com.zhengcq.srv.client1.srv.client1;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import com.zhengcq.srv.client1.service.RemoteCallService;
import org.jooq.lambda.*;
import org.jooq.lambda.tuple.Tuple2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Test1 {
    @Autowired
    private RemoteCallService remoteCallService;
    public static void main(String[] args){

        List<Integer> ls = new LinkedList<>();
        ls.add(3);
        ls.add(4);
        ls.add(2);


        List<Integer> ls1 = new LinkedList<>();
        ls1.add(3);
        ls1.add(4);
        ls1.add(5);
        Seq<Tuple2<Object,Object>> rs =   Seq.of(ls.toArray()).innerJoin(Seq.of(ls1.toArray()),(a, b)-> {
            Integer o1 = (Integer)a;
            Integer o2 = (Integer)b;
            return o1.equals(o2);
        } );

       rs.stream().forEach(vo->{
           System.out.println(vo.v1);
           System.out.println(vo.v2);
       });
//        System.out.println(rs.collect(Collectors.toList()));
        System.out.print( Seq.of(ls).collect(Collectors.toList()));
    }

    @Test
    public void test1(){

        for(int i= 0;i<100;i++){
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    StringBuilder builder = new StringBuilder();
                    builder.append("orderNo");
                    Random random  = new Random(System.currentTimeMillis());
                    builder.append(random.nextInt());
                    try {
                        System.out.println(builder.toString()+"waiting rs");
                        String rs = remoteCallService.getOrder(builder.toString());
                        System.out.println(rs);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        }

        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        remoteCallService.finishRequest();

        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
