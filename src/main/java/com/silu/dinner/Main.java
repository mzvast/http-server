package com.silu.dinner;

import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by silu on 15-1-11.
 */
public class Main {
    public static void main(String... args){
        AbstractXmlApplicationContext context = new ClassPathXmlApplicationContext("spring/entrance.xml");
        Runtime.getRuntime().addShutdownHook(new Thread("shutdown-hook"){
            public void run(){
                context.close();
            }
        });
    }
}
