package com.ruiqin.intercepthomedemo;

import android.content.Intent;
import android.device.DeviceManager;
import android.os.Bundle;
import android.view.View;

import com.speedata.postest.PosC;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.textView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SecondActivity.class));
            }
        });

        try {
            new DeviceManager().enableHomeKey(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        PosC.home(false, this);
    }


    @Override
    public void onBackPressed() {
    }
}
