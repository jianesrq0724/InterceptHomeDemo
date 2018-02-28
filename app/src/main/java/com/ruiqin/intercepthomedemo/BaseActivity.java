package com.ruiqin.intercepthomedemo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * @version 1.0
 * @author：ruiqin.shen
 * @since：2018/2/27
 */

public class BaseActivity extends AppCompatActivity {

    public Context context;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
    }


}
