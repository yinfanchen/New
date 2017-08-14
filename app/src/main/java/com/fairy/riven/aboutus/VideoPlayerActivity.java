package com.fairy.riven.aboutus;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.fairy.riven.aboutus.customView.CustomVideoView;

public class VideoPlayerActivity extends AppCompatActivity implements View.OnClickListener{

    private CustomVideoView videoview;
    private Button btn_start;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        btn_start = (Button) findViewById(R.id.btn_start);
        btn_start.setOnClickListener(this);

        videoview = (CustomVideoView) findViewById(R.id.videoview);
        //设置播放加载路径
//        videoview.setVideoURI(Uri.parse("android.resource://"+getPackageName()+"/"+ R.raw.fairy));
        //播放
        videoview.start();


        //循环播放
        videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                videoview.start();
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_start:
                Toast.makeText(this,"进入登陆页面",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(VideoPlayerActivity.this,MainActivity.class);
                startActivity(intent);
//                overridePendingTransition(R.anim.normal_right, R.anim.normal_left);

//                String saveEmails  = SharedPreferencesHelper.getString(VideoPlayerActivity.this,"email","");
//                String preEmails = Email.MYDEAR;
//                if (saveEmails.equals(preEmails)){
//                    //说明之前已经登录过了
//
//                }else {
//
//                }
                break;
        }
    }
}
