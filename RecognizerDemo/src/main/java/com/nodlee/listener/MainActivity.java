package com.nodlee.listener;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;


public class MainActivity extends AppCompatActivity {
    private static final boolean DEBUG = true;

    private TextView mContextTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContextTv = (TextView) findViewById(R.id.txt_content);

        final RecognizerDialog iatDialog = new RecognizerDialog(MainActivity.this, null);
        // 清空参数
        iatDialog.setParameter(SpeechConstant.PARAMS, null);
        // 设置云端听写引擎
        iatDialog.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        // 设置返回结果格式
        iatDialog.setParameter(SpeechConstant.RESULT_TYPE, "json");
        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        iatDialog.setParameter(SpeechConstant.VAD_BOS, "4000");
        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        iatDialog.setParameter(SpeechConstant.VAD_EOS, "1000");
        // 设置听写语言
        iatDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        // 设置语言区域
        iatDialog.setParameter(SpeechConstant.ACCENT, "mandarin ");
        iatDialog.setListener(mRecoListener);

        findViewById(R.id.btn_record).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iatDialog.show();
            }
        });
    }

    private RecognizerDialogListener mRecoListener = new RecognizerDialogListener() {
        @Override
        public void onResult(RecognizerResult recognizerResult, boolean b) {
            if (recognizerResult != null) {
                if (DEBUG) Log.i("xxx", recognizerResult.getResultString());
                String text = JsonParser.parseLocalGrammarResult2(
                        recognizerResult.getResultString());
                appendContent(text);
            }
        }

        @Override
        public void onError(SpeechError speechError) {
            if (DEBUG) Log.i("xxx", "error_code:" + speechError.getErrorCode());
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_clear:
                clearContent();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void appendContent(String newContent) {
        if (newContent != null) {
            mContextTv.append(newContent);
        }
    }

    private void clearContent() {
        mContextTv.setText("");
    }
}
