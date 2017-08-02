package com.example.jonathanmaldonado.w3d3_ex01;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.Pools;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName()+"_TAG";
    private static final String MESSAGE_EXTRA= "com.example.jonathanmaldonado.w3d3_ex01.MESSAGE_EXTRA";

    private TextView resultTV;
    private MyThread myThread;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            String message =msg.getData().getString(MESSAGE_EXTRA);
            setResultTextview(message);

        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultTV= (TextView) findViewById(R.id.tv_result);
        setResultTextview("");
        myThread = new MyThread();
        myThread.start();
    }

    public void doWithLooper (View view){
        myThread.handler.post(new Runnable() {


            @Override
            public void run() {

                String thread = Thread.currentThread().getName();
                String message= "From thread with looper "+thread;
                //setResultTextview(message);
                Log.d(TAG, "run: "+ message);
            }
        });
    }

    public void doWithAsync(View view){

        new AsyncTask<Void, Void, String>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //read values etc before task
            }

            @Override
            protected String doInBackground(Void... voids) {
                // you do the background work
                synchronized (this) {
                    try {

                        wait(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    String message = "Done with AsyncTask";
                    return message;
                }
            }
            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
            }

            @Override
            protected void onPostExecute(String s) {
                //super.onPostExecute(s);
                setResultTextview(s);
            }
        }.execute();

    }

    private void setResultTextview (String message){
        String result = String.format(getString(R.string.lbl_result),message);
        resultTV.setText(result);
    }

    //this is a bad idea
    public void doMainThread(View view) {
        try{
            String message = "From Main thread";
            Thread.sleep(5000);
            setResultTextview(message);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    public void separateThread(View view) {
        final String message = "From thread";
        Thread thread =  new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (this){
                    try {
                        wait(1500);
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
                   // setResultTextview(message);
                    Log.d(TAG, "run: "+ message);
                }
            }
        });
        thread.start();
    }

    public void doWithHandler(View view) {
        final String message = "From thread with handler";
        Thread thread = new Thread(){
            @Override
            public void run() {
                Message msg =handler.obtainMessage();
                super.run();
                Bundle data =new Bundle();
                data.putString(MESSAGE_EXTRA,message);
                msg.setData(data);
                try{
                    sleep(1500);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                handler.sendMessage(msg);
            }

        };
        thread.start();


    }


}
