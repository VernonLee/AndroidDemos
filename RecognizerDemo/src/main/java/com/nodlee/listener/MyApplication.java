package com.nodlee.listener;

import android.app.Application;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

/**
 * Created by nodlee on 2015/11/30.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initSpeechUtility();
    }

    private void initSpeechUtility() {
        StringBuffer param = new StringBuffer();
        param.append(SpeechConstant.APPID + "=" + Constants.APP_ID);
        param.append(",");
        param.append(SpeechConstant.ENGINE_MODE + "=" + SpeechConstant.MODE_MSC);
        SpeechUtility.createUtility(this, param.toString());
    }
}
