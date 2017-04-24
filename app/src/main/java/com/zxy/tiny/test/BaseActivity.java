package com.zxy.tiny.test;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by zhengxiaoyong on 2017/4/2.
 */
public class BaseActivity extends AppCompatActivity {

    //take the initiative to invoke gc to release the memory to avoid oom when load big origin bitmap.

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        gcAndFinalize();
    }

    void startActivity(Class clazz) {
        startActivity(new Intent(this, clazz));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gcAndFinalize();
    }

    void gcAndFinalize() {
        Runtime runtime = Runtime.getRuntime();
        System.gc();
        runtime.runFinalization();
        System.gc();
    }
}
