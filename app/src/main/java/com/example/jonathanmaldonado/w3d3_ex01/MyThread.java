package com.example.jonathanmaldonado.w3d3_ex01;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by Jonathan Maldonado on 8/2/2017.
 */


public class MyThread  extends Thread{

    Handler handler;

    public MyThread() {
        // here the context is of where you instance
        // MyThread...

        //handler = new Handler;

    }

    @Override
    public void run() {
        Looper.prepare();// this attaches to the MessageQueve (MQ)
        handler=new Handler();
        Looper.loop();//Is looping for messages
    }
}
