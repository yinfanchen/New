package com.fairy.riven.aboutus;

import android.content.Intent;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.Toast;
import java.util.Random;


public class MainActivity extends AppCompatActivity  {

    private static final String TAG="MainActivity";
    private Matrix mMatrix;
    ImageView imageView ,img_video_play_pause,img_main_bg;
    private static final int[] PHOTOS = new int[] { R.drawable.photo1,
            R.drawable.photo2, R.drawable.photo3, R.drawable.photo4,
            R.drawable.photo5, R.drawable.photo6 };
    private Random random = new Random();
    
    private int mIndex = 0;
    boolean flag =true;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.img);
        img_video_play_pause = (ImageView) findViewById(R.id.img_video_play_pause);
        img_main_bg =(ImageView) findViewById(R.id.img_main_bg);
    }

    public void ClickBtton(View view){
        Intent intent =null;
        switch (view.getId()){
            case R.id.img_video_play_pause:
                if (flag){
                    flag =false;
                    img_main_bg.setVisibility(View.INVISIBLE);

                    img_video_play_pause.setImageResource(R.drawable.btn_videoplay_pause);
                    startVideoPlay();
                }else {
                    flag = true;
                    img_main_bg.setVisibility(View.VISIBLE);
                    img_video_play_pause.setImageResource(R.drawable.btn_videoplay_play);

                }
                break;
            case R.id.btn_anniversary_icon:
                //周年纪念日
                intent = new Intent(MainActivity.this,DiaryMianActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_wish_icon:
                //愿望
                break;
            case R.id.btn_clock_icon:
                //叫醒对方
                break;
            case R.id.btn_check_icon:
                intent =new Intent(MainActivity.this ,confessionActivity.class);
                startActivity(intent);
                break;
        }

    }

    private void startVideoPlay() {
        /**
         * 动图的实现，如何实现三张图片缓慢的切换。
         */
        mIndex=random.nextInt(5);
//        Toast.makeText(MainActivity.this,""+mIndex,Toast.LENGTH_LONG).show();
        imageView.setImageResource(PHOTOS[mIndex]);
        mMatrix = new Matrix();
        mMatrix.postScale(2f, 2f, 0f, 0f);
        imageView.setImageMatrix(mMatrix);
        imageView.invalidate();
        imageView.startAnimation(getAnimation());
    }

    private Animation getAnimation() {
        MAnimation animation = new MAnimation();
        animation.setDuration(10000);
        animation.setRepeatMode(Animation.REVERSE);
        animation.setRepeatCount(Animation.INFINITE);
        return animation;
    }

    public class MAnimation extends Animation {

        private ImageView mulimageView;
        private float pre =- 0.0f;
        private int mWidth, mHeight;

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
            this.mWidth = width;
            this.mHeight = height;

        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
//            Log.d(TAG,"interpolatedTime="+interpolatedTime);
            if (pre < interpolatedTime) {
                pre = interpolatedTime;
                mMatrix.postTranslate(-interpolatedTime * 2, 0);
            }
            else {
                pre = interpolatedTime;
                mMatrix.postTranslate(interpolatedTime * 2, 0);
            }
            if (interpolatedTime == 1.0){
                mIndex = ((mIndex+1 <PHOTOS.length )? mIndex+1 :0);//三目运算
                imageView.setImageResource(PHOTOS[mIndex]);
                mMatrix = new Matrix();
                mMatrix.postScale(2f, 2f, 0f, 0f);
                imageView.setImageMatrix(mMatrix);
                imageView.invalidate();
                imageView.startAnimation(getAnimation());
            }
            // mMatrix.postScale(interpolatedTime, 1);
            imageView.setImageMatrix(mMatrix);
            imageView.invalidate();
        }
    }
}
