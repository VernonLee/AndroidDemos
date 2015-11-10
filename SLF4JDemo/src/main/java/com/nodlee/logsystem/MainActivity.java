package com.nodlee.logsystem;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MainActivity extends Activity {
    private final Logger log = LoggerFactory.getLogger(MainActivity.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_generate_log).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateLog();
            }
        });
    }

    private void generateLog() {
        log.info("在logcat和本地文件中找到此文件");
    }
}
