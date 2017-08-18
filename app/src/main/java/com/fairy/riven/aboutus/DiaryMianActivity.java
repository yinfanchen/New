package com.fairy.riven.aboutus;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.fairy.riven.aboutus.bean.DiaryBean;
import com.fairy.riven.aboutus.bean.MessageEvent;
import com.fairy.riven.aboutus.db.DiaryDatabaseHelper;
import com.fairy.riven.aboutus.event.StartUpdateDiaryEvent;
import com.fairy.riven.aboutus.utils.AppManager;
import com.fairy.riven.aboutus.utils.GetDate;
import com.fairy.riven.aboutus.utils.SpHelper;
import com.fairy.riven.aboutus.utils.StatusBarCompat;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DiaryMianActivity extends AppCompatActivity {


    private static final String TAG="DiaryMianActivity";
    @Bind(R.id.common_tv_title)
    TextView mCommonTvTitle;
    @Bind(R.id.common_iv_test)
    ImageView mCommonIvTest;
    @Bind(R.id.common_title_ll)
    LinearLayout mCommonTitleLl;
    @Bind(R.id.main_iv_circle)
    ImageView mMainIvCircle;
    @Bind(R.id.main_tv_date)
    TextView mMainTvDate;
    @Bind(R.id.main_tv_content)
    TextView mMainTvContent;
    @Bind(R.id.item_ll_control)
    LinearLayout mItemLlControl;

    @Bind(R.id.main_rv_show_diary)
    RecyclerView mMainRvShowDiary;
    @Bind(R.id.main_fab_enter_edit)
    FloatingActionButton mMainFabEnterEdit;
    @Bind(R.id.main_rl_main)
    RelativeLayout mMainRlMain;
    @Bind(R.id.item_first)
    LinearLayout mItemFirst;
    @Bind(R.id.main_ll_main)
    LinearLayout mMainLlMain;
    private List<DiaryBean> mDiaryBeanList;

    private DiaryDatabaseHelper mHelper;

    private static String IS_WRITE = "true";

    private int mEditPosition = -1;

    DiaryAdapter adapter;
    /**
     * 标识今天是否已经写了日记
     */
    private boolean isWrite = false;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, DiaryMianActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_main);
        AppManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        StatusBarCompat.compat(this, Color.parseColor("#161414"));
        mHelper = new DiaryDatabaseHelper(this, "Diary.db", null, 1);
        EventBus.getDefault().register(this);
        SpHelper spHelper = SpHelper.getInstance(this);
        getDiaryBeanList();
        initTitle();
        mMainRvShowDiary.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DiaryAdapter(this, mDiaryBeanList);
        mMainRvShowDiary.setAdapter(adapter);
    }

    private void initTitle() {
        mMainTvDate.setText("今天，" + GetDate.getDate());
        mCommonTvTitle.setText("日记");
        mCommonIvTest.setVisibility(View.INVISIBLE);

    }

    private List<DiaryBean> getDiaryBeanList() {

        mDiaryBeanList = new ArrayList<>();
        List<DiaryBean> diaryList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = mHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query("Diary", null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String date = cursor.getString(cursor.getColumnIndex("date"));
                String dateSystem = GetDate.getDate().toString();
                if (date.equals(dateSystem)) {
                    mMainLlMain.removeView(mItemFirst);
                    break;
                }
            } while (cursor.moveToNext());
        }


        if (cursor.moveToFirst()) {
            do {
                String date = cursor.getString(cursor.getColumnIndex("date"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                String tag = cursor.getString(cursor.getColumnIndex("tag"));
                mDiaryBeanList.add(new DiaryBean(date, title, content, tag));
            } while (cursor.moveToNext());
        }
        cursor.close();

        for (int i = mDiaryBeanList.size() - 1; i >= 0; i--) {
            diaryList.add(mDiaryBeanList.get(i));
        }

        mDiaryBeanList = diaryList;
        return mDiaryBeanList;
    }

    @Subscribe
    public void startUpdateDiaryActivity(StartUpdateDiaryEvent event) {
        String title = mDiaryBeanList.get(event.getPosition()).getTitle();
        String content = mDiaryBeanList.get(event.getPosition()).getContent();
        String tag = mDiaryBeanList.get(event.getPosition()).getTag();
        UpdateDiaryActivity.startActivity(this, title, content, tag);

    }


    /**
     * 在后台线程中执行，如果当前线程是子线程，则会在当前线程执行，如果当前线程是主线程，则会创建一个新的子线程来执行
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void  onEventBackgroundThread(MessageEvent event){
        Log.d(TAG,"onEventBackgroundThread::"+" "+Thread.currentThread().getName());
    }

    /**
     * 创建一个异步线程来执行
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventAsync(MessageEvent event){
        Log.d(TAG,"onEventAsync::"+" "+Thread.currentThread().getName());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(MessageEvent event){
        Log.d(TAG,"onEventMainThread::"+" "+Thread.currentThread().getName()+event.getMsg());
        //收到消息后  更新数据库 刷新adapter
        //这种实现效率并不好，暂时没有想到其他的方法
        getDiaryBeanList();
        mMainRvShowDiary.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DiaryAdapter(this, mDiaryBeanList);
        mMainRvShowDiary.setAdapter(adapter);

    }

    /**
     * 在主线程中运行
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMain(MessageEvent event){
        Log.d(TAG,"onEventMain::"+" "+Thread.currentThread().getName());
    }

    /**
     *默认的线程模式，在当前线程下运行。如果当前线程是子线程则在子线程中，当前线程是主线程，则在主线程中执行。
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEventPosting(MessageEvent event){
        Log.d(TAG,"onEventPosting::"+" "+Thread.currentThread().getName());
    }

















    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.main_fab_enter_edit,R.id.common_iv_back})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.common_iv_back:
                finish();
                break;
            case R.id.main_fab_enter_edit:
                Intent intent = new Intent(DiaryMianActivity.this ,AddDiaryActivity.class);
                startActivity(intent);
                break;
        }
    }


    public void onBackPressed() {
        super.onBackPressed();
        AppManager.getAppManager().AppExit(this);
    }
}