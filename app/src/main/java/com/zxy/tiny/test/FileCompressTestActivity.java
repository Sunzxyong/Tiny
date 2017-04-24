package com.zxy.tiny.test;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.FileCallback;
import com.zxy.tiny.core.HttpUrlConnectionFetcher;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileCompressTestActivity extends BaseActivity {

    private ImageView mOriginImg;

    private TextView mOriginTv;

    private TextView mCompressTv;

    private Bitmap.Config mConfig = Bitmap.Config.ARGB_8888;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_compress_test);

        mOriginImg = (ImageView) findViewById(R.id.img_origin);

        mOriginTv = (TextView) findViewById(R.id.tv_origin);
        mCompressTv = (TextView) findViewById(R.id.tv_compress);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_testcase, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        mOriginImg.setImageBitmap(null);
        switch (id) {
            case R.id.action_config:
                if (mConfig == Bitmap.Config.ARGB_8888) {
                    item.setTitle("RGB_565");
                    mConfig = Bitmap.Config.RGB_565;
                } else if (mConfig == Bitmap.Config.RGB_565) {
                    item.setTitle("ARGB_8888");
                    mConfig = Bitmap.Config.ARGB_8888;
                }

                break;
            case R.id.action_bytes:
                //free memory for test
                gcAndFinalize();
                testBytes();
                break;
            case R.id.action_file:
                gcAndFinalize();
                testFile();
                break;
            case R.id.action_bitmap:
                gcAndFinalize();
                testBitmap();
                break;
            case R.id.action_stream:
                gcAndFinalize();
                testStream();
                break;
            case R.id.action_res:
                gcAndFinalize();
                testResource();
                break;
            case R.id.action_uri:
                gcAndFinalize();
                testUri();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupOriginInfo(Bitmap bitmap, long sizeBytes) {
        mOriginImg.setImageBitmap(bitmap);
        mOriginTv.setText("origin file size:" + Formatter.formatFileSize(this, sizeBytes)
                + "\nwidth:" + bitmap.getWidth() + ",height:" + bitmap.getHeight() + ",config:" + bitmap.getConfig());
    }

    private void setupCompressInfo(String outfile, long sizeBytes) {
        mCompressTv.setText("compress file size:" + Formatter.formatFileSize(this, sizeBytes)
                + "\noutfile: " + outfile);
    }

    private void testBytes() {
        try {
            final InputStream is = getResources().getAssets()
                    .open("test-3.jpg");

            long fileSize = is.available();

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int len = -1;
            while ((len = is.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
            os.close();
            is.close();
            byte[] bitmapBytes = os.toByteArray();

            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = mConfig;
            Bitmap originBitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length, options);

            setupOriginInfo(originBitmap, fileSize);

            Tiny.FileCompressOptions compressOptions = new Tiny.FileCompressOptions();
            compressOptions.config = mConfig;
            Tiny.getInstance().source(bitmapBytes).asFile().withOptions(compressOptions).compress(new FileCallback() {
                @Override
                public void callback(boolean isSuccess, String outfile) {
                    if (!isSuccess) {
                        mCompressTv.setText("compress file failed!");
                        return;
                    }
                    File file = new File(outfile);
                    setupCompressInfo(outfile, file.length());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void testFile() {
        try {
            final InputStream is = getResources().getAssets()
                    .open("test-6.jpg");
            File outfile = new File(getExternalFilesDir(null), "test-6-file.jpg");
            FileOutputStream fos = new FileOutputStream(outfile);
            byte[] buffer = new byte[4096];
            int len = -1;
            while ((len = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            fos.close();
            is.close();

            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = mConfig;
            Bitmap originBitmap = BitmapFactory.decodeFile(outfile.getAbsolutePath(), options);

            setupOriginInfo(originBitmap, outfile.length());

            Tiny.FileCompressOptions compressOptions = new Tiny.FileCompressOptions();
            compressOptions.config = mConfig;
            Tiny.getInstance().source(outfile).asFile().withOptions(compressOptions).compress(new FileCallback() {
                @Override
                public void callback(boolean isSuccess, String outfile) {
                    if (!isSuccess) {
                        mCompressTv.setText("compress file failed!");
                        return;
                    }
                    File file = new File(outfile);
                    setupCompressInfo(outfile, file.length());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void testBitmap() {
        try {
            final InputStream is = getResources().getAssets()
                    .open("test-5.jpg");

            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = mConfig;
            long fileSize = is.available();
            Bitmap originBitmap = BitmapFactory.decodeStream(is, null, options);

            setupOriginInfo(originBitmap, fileSize);

            Tiny.FileCompressOptions compressOptions = new Tiny.FileCompressOptions();
            compressOptions.config = mConfig;

            Tiny.getInstance().source(originBitmap).asFile().withOptions(compressOptions).compress(new FileCallback() {
                @Override
                public void callback(boolean isSuccess, String outfile) {
                    if (!isSuccess) {
                        mCompressTv.setText("compress file failed!");
                        return;
                    }
                    File file = new File(outfile);
                    setupCompressInfo(outfile, file.length());
                }
            });
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void testStream() {
        try {
            final InputStream is = getResources().getAssets()
                    .open("test-4.jpg");
            File outfile = new File(getExternalFilesDir(null), "test-4.jpg");
            FileOutputStream fos = new FileOutputStream(outfile);
            byte[] buffer = new byte[4096];
            int len = -1;
            while ((len = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            fos.close();

            InputStream is2 = new FileInputStream(outfile);

            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = mConfig;
            Bitmap originBitmap = BitmapFactory.decodeStream(is, null, options);

            setupOriginInfo(originBitmap, outfile.length());

            Tiny.FileCompressOptions compressOptions = new Tiny.FileCompressOptions();
            compressOptions.config = mConfig;
            Tiny.getInstance().source(is2).asFile().withOptions(compressOptions).compress(new FileCallback() {
                @Override
                public void callback(boolean isSuccess, String outfile) {
                    if (!isSuccess) {
                        mCompressTv.setText("compress file failed!");
                        return;
                    }
                    File file = new File(outfile);
                    setupCompressInfo(outfile, file.length());
                }
            });
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void testResource() {
        try {
//            Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
//                    + getResources().getResourcePackageName(R.drawable.test) + "/"
//                    + getResources().getResourceTypeName(R.drawable.test) + "/"
//                    + getResources().getResourceEntryName(R.drawable.test));
//            File file = new File(new URI(uri.toString()));
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = mConfig;
            Bitmap originBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test, options);

            setupOriginInfo(originBitmap, 227 * 1024);

            Tiny.FileCompressOptions compressOptions = new Tiny.FileCompressOptions();
            compressOptions.config = mConfig;
            Tiny.getInstance().source(R.drawable.test).asFile().withOptions(compressOptions).compress(new FileCallback() {
                @Override
                public void callback(boolean isSuccess, String outfile) {
                    if (!isSuccess) {
                        mCompressTv.setText("compress file failed!");
                        return;
                    }
                    File file = new File(outfile);
                    setupCompressInfo(outfile, file.length());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void testUri() {
        final String url = "http://7xswxf.com2.z0.glb.qiniucdn.com//blog/deec2ac0373d08eb85a.jpg";
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    HttpUrlConnectionFetcher.fetch(url, new HttpUrlConnectionFetcher.ResponseCallback() {
                        @Override
                        public void callback(final InputStream is) {
                            final BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inPreferredConfig = mConfig;
                            try {
                                final File outfile = new File(getExternalFilesDir(null), "test-network-img.jpg");
                                FileOutputStream fos = new FileOutputStream(outfile);
                                byte[] buffer = new byte[4096];
                                int len = -1;
                                while ((len = is.read(buffer)) != -1) {
                                    fos.write(buffer, 0, len);
                                }
                                fos.close();

                                final Bitmap originBitmap = BitmapFactory.decodeFile(outfile.getAbsolutePath(), options);

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        setupOriginInfo(originBitmap, outfile.length());
                                    }
                                });
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }).start();

            Tiny.FileCompressOptions compressOptions = new Tiny.FileCompressOptions();
            compressOptions.config = mConfig;
            Tiny.getInstance().source(Uri.parse(url)).asFile().withOptions(compressOptions).compress(new FileCallback() {
                @Override
                public void callback(boolean isSuccess, String outfile) {
                    if (!isSuccess) {
                        mCompressTv.setText("compress file failed!");
                        return;
                    }
                    File file = new File(outfile);
                    setupCompressInfo(outfile, file.length());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
