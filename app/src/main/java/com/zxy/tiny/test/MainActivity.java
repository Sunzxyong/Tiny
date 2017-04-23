package com.zxy.tiny.test;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends BaseActivity {

    private TextView mInfoTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mInfoTv = (TextView) findViewById(R.id.tv_info);
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();

        mInfoTv.setText("sdk version:" + Build.VERSION.SDK_INT + "\ndensity:" + metrics.density + ",densityDpi:" + metrics.densityDpi + "\nwidth:" + metrics.widthPixels + ",height:" + metrics.heightPixels);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ic_delete_forever_white_24dp);
        mInfoTv.append(",  width:"+bitmap.getWidth()+",height:"+bitmap.getHeight());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_delete) {
            new AlertDialog.Builder(this)
                    .setTitle("Tip")
                    .setMessage("Do you want to clear \"tiny\" compressed directory?")
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setPositiveButton("Sure", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //for test,so simple impl.
                            try {
                                File dir = new File(getExternalFilesDir(null).getParent() + File.separator + "tiny");
                                File[] files = dir.listFiles();
                                for (int i = 0; i < files.length; i++) {
                                    files[i].delete();
                                }
                                Toast.makeText(MainActivity.this.getApplication(), "Clear success!", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {

                            }
                        }
                    }).show();
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_one:
                startActivity(BitmapCompressTestActivity.class);
                break;
            case R.id.btn_two:
                startActivity(FileCompressTestActivity.class);
                break;
            case R.id.btn_three:
                startActivity(FileWithReturnBitmapCompressTestActivity.class);
                break;
            case R.id.btn_four:
                startActivity(BatchBitmapCompressTestActivity.class);
                break;
            case R.id.btn_five:
                startActivity(BatchFileCompressTestActivity.class);
                break;
            case R.id.btn_six:
                startActivity(BatchFileWithReturnBitmapCompressTestActivity.class);
                break;
            default:
                break;
        }
    }

}
