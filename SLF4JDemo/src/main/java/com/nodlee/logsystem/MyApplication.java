package com.nodlee.logsystem;

import android.app.Application;
import android.os.Environment;

import org.apache.log4j.Level;

import java.io.File;

import de.mindpipe.android.logging.log4j.LogConfigurator;

/**
 * 输出格式参考地址：http://logging.apache.org/log4j/1.2/apidocs/org/apache/log4j/PatternLayout.html
 * Created by nodlee on 2015/9/23.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        configLogging();
    }

    private void configLogging() {
        final LogConfigurator logConfigurator = new LogConfigurator();

        // 日志输出文件目录以及文件名称
        logConfigurator.setFileName(Environment.getExternalStorageDirectory()
                + File.separator + "taotie.log");
        // 是否在LogCat中显示信息
        logConfigurator.setUseLogCatAppender(true);
        // LogCat输出格式
        logConfigurator.setLogCatPattern("%m%n");
        // 是否写入文件中
        logConfigurator.setUseFileAppender(true);
        // 文件中日志显示格式
        logConfigurator.setFilePattern("%d{yyyy-MM-dd HH:mm:ss} %-5p %C{1} - %m%n");
//        logConfigurator.setInternalDebugging(true);
        logConfigurator.setRootLevel(Level.INFO);
        logConfigurator.configure();
    }
}
