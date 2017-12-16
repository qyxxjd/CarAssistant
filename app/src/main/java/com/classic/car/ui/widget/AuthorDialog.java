package com.classic.car.ui.widget;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;

import com.classic.car.R;

public class AuthorDialog extends Dialog {
    private Context context;

    public AuthorDialog(Context context) {
        super(context, R.style.Dialog);
        this.context = context;
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_author, null);
        setContentView(view);
        Window window = getWindow();
        if (null != window) {
            window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.BOTTOM);
        }
    }
}
