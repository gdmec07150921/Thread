package com.example.administrator.thread;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private TextView tv1;
    private int seconds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv1 = (TextView) findViewById(R.id.tv1);
        Date thelastday = new Date(117,5,123);
        Date toDay = new Date();
        seconds = (int) (thelastday.getTime()-toDay.getTime())/1000;
    }
    public void anr(View v){
        for(int i = 0;i < 100000;i++){
            BitmapFactory.decodeResource(getResources(),R.drawable.a);
        }
    }
    public void threadclass(View v){
        class ThreadSimple extends Thread{
            Random rm;
            public ThreadSimple(String tname){
                super(tname);
                rm = new Random();
            }
            public void run(){
                for (int i = 0;i<10;i++){
                    System.out.println(i+" "+getName());
                    try {
                        sleep(rm.nextInt(1000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(getName()+"完成");
            }
        }
        ThreadSimple thread1 = new ThreadSimple("线程一");
        thread1.start();
        ThreadSimple thread2 = new ThreadSimple("线程二");
        thread2.start();
    }

    public void runnableinterface(View v){
        class RunableExample implements Runnable{
            Random rm;
            String name;

            public RunableExample(String tname){
                this.name = tname;
                rm = new Random();
            }

            @Override
            public void run() {
                for(int i = 0;i < 10;i++){
                    System.out.println(i+" "+name);
                    try {
                        Thread.sleep(rm.nextInt(1000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(name+"完成");
            }
        }
        Thread thread1 = new Thread(new RunableExample("线程一"));
        thread1.start();
        Thread thread2 = new Thread(new RunableExample("线程二"));
        thread2.start();
    }

    public void timertask(View v){
        class MyThread extends TimerTask{
            Random rm;
            String name;

            public MyThread(String tname){
                this.name = tname;
                rm = new Random();
            }

            @Override
            public void run() {
                for (int i = 0;i < 10;i++){
                    System.out.println(i+" "+name);
                    try {
                        Thread.sleep(rm.nextInt(1000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(name+"完成");
            }
        }
        Timer timer1 = new Timer();
        Timer timer2 = new Timer();
        MyThread myThread1 = new MyThread("线程一");
        MyThread myThread2 = new MyThread("线程二");
        timer1.schedule(myThread1,0);
        timer2.schedule(myThread2,0);
    }

    public void handlermessage(View v){
        final Handler myhandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch(msg.what){
                    case 1:
                        showmsg(String.valueOf(msg.arg1+msg.getData().get("attach").toString()));
                }
            }
        };
        class MyTask extends TimerTask{
            int countdown;
            double achievement1 = 1,achievement2 = 2;
            public MyTask(int seconds){
                this.countdown = seconds;
            }
            @Override
            public void run() {
                Message msg = Message.obtain();
                msg.what=1;
                msg.arg1 = countdown--;
                achievement1 = achievement1+1.01;
                achievement2 = achievement2+1.02;

                Bundle bundle = new Bundle();
                bundle.putString("attach","\n努力多1％:"+achievement1+"\n努力多2%:"+achievement2);
                msg.setData(bundle);
                myhandler.sendMessage(msg);
            }
        }
        Timer timer = new Timer();
        timer.schedule(new MyTask(seconds),1,1000);

    }

    private void showmsg(String msg) {
        tv1.setText(msg);
    }

    public void asynctask(View v){
        class LearHard extends AsyncTask<Long,String,String> {
            private Context context;
            final int duration = 10;
            int count = 0;

            public LearHard(Activity context){
                this.context = context;
            }

            @Override
            protected String doInBackground(Long... params) {
                long num = params[0].longValue();
                while (count<duration){
                    num--;
                    count++;
                    String status = "离毕业还有"+num+"秒,努力学习"+count+"秒";
                    publishProgress(status);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return "这"+duration+"秒有收获";
            }

            @Override
            protected void onProgressUpdate(String... values) {
                ((MainActivity)context).tv1.setText(values[0]);
                super.onProgressUpdate(values);
            }

            @Override
            protected void onPostExecute(String s) {
                showmsg(s);
                super.onPostExecute(s);
            }
        }
        LearHard learhard = new LearHard(this);
        learhard.execute((long)seconds);
    }

}
