package com.zhengcq.srv.client1.srv.client1;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        while(sc.hasNextInt())
        {
            int a=sc.nextInt();
            int b=sc.nextInt();
            System.out.println(a+b);
        }

    }
}
