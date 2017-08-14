package com.fairy.riven.aboutus.customView;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fairy.riven.aboutus.R;

/**
 * Created by yanfa6 on 2017/7/24.
 */
public class CustomToast {



    public static void initShortToast(Context context,String text){
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 100);
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View toastView = layoutInflater.inflate(R.layout.activity_toast, null);
        toast.setView(toastView);
        TextView tv = (TextView)toastView.findViewById(R.id.ToastTextViewInfo);
        tv.setText(text+"");
        toast.show();
    }

    public static void initLongToast(Context context,String text){
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 100);
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View toastView = layoutInflater.inflate(R.layout.activity_toast, null);
        toast.setView(toastView);
        TextView tv = (TextView)toastView.findViewById(R.id.ToastTextViewInfo);
        tv.setText(text+"");
        toast.show();
    }

}
