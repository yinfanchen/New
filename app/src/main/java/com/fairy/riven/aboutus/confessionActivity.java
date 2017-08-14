package com.fairy.riven.aboutus;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.fairy.riven.aboutus.customView.LoveLayout;

public class confessionActivity extends AppCompatActivity {

    private LoveLayout mLoveLayout;
    private Handler handler=new Handler(){
        public void handleMessage(android.os.Message msg) {
            if (msg.what==0x123) {
                mLoveLayout.addLove();
            }
        };
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confession);
        initView();
    }

    private void initView() {
        mLoveLayout=(LoveLayout)findViewById(R.id.id_love_layout);
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    while(true){
                        Thread.sleep(400);
                        handler.sendEmptyMessage(0x123);
                    }
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
