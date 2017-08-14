package com.fairy.riven.aboutus;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.view.ViewGroup.LayoutParams;
import java.util.Random;

public class IndexActivity extends AppCompatActivity implements Animator.AnimatorListener {

    private static final int ANIM_COUNT = 4;
    private static final int[] PHOTOS = new int[] { R.drawable.photo1,
            R.drawable.photo2, R.drawable.photo3, R.drawable.photo4,
            R.drawable.photo5, R.drawable.photo6 };

    private FrameLayout mContainer;
    private ImageView mView;
    private Random mRandom = new Random();
    private int mIndex = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContainer = new FrameLayout(this);
        mContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));

        mView = createNewView();
        mContainer.addView(mView);

        setContentView(mContainer);
    }

    private ImageView createNewView() {
        ImageView ret = new ImageView(this);
        ret.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        ret.setScaleType(ImageView.ScaleType.FIT_XY);
        ret.setImageResource(PHOTOS[mIndex]);
        mIndex = (mIndex + 1 < PHOTOS.length) ? mIndex + 1 : 0;
        return ret;
    }

    @Override
    protected void onResume() {
        super.onResume();
        nextAnimation();
    }

    private void nextAnimation() {
        AnimatorSet anim = new AnimatorSet();
        final int index = mRandom.nextInt(ANIM_COUNT);

        switch (index) {
            case 0:
                anim.playTogether(
                        ObjectAnimator.ofFloat(mView, "scaleX", 1.5f, 1f),
                        ObjectAnimator.ofFloat(mView, "scaleY", 1.5f, 1f));
                break;

            case 1:
                anim.playTogether(ObjectAnimator.ofFloat(mView, "scaleX", 1, 1.5f),
                        ObjectAnimator.ofFloat(mView, "scaleY", 1, 1.5f));

                break;

            case 2:
                anim.playTogether(
                        ObjectAnimator.ofFloat(mView, "scaleX", 1.5f, 1f),
                        ObjectAnimator.ofFloat(mView, "scaleY", 1.5f, 1f));
                break;

            case 3:
            default:
                anim.playTogether(
                        ObjectAnimator.ofFloat(mView, "scaleX", 1f, 2f),
                        ObjectAnimator.ofFloat(mView, "scaleY", 1f, 2f));
                break;
        }
        anim.setDuration(5000);
        anim.addListener(this);
        anim.start();
    }

    @Override
    public void onAnimationCancel(Animator arg0) {
    }

    @Override
    public void onAnimationEnd(Animator animator) {
        mContainer.removeView(mView);
        mView = createNewView();
        mContainer.addView(mView);
        nextAnimation();
    }

    @Override
    public void onAnimationRepeat(Animator arg0) {
    }

    @Override
    public void onAnimationStart(Animator arg0) {
    }
}
