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
import com.zxy.tiny.callback.FileWithBitmapBatchCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class BatchFileWithReturnBitmapCompressTestActivity extends BaseActivity {

    private ImageView mOriginImg1;

    private ImageView mOriginImg2;

    private ImageView mOriginImg3;

    private ImageView mOriginImg4;

    private ImageView mCompressImg1;

    private ImageView mCompressImg2;

    private ImageView mCompressImg3;

    private ImageView mCompressImg4;

    private TextView mOriginTv;

    private TextView mCompressTv;

    private Bitmap.Config mConfig = Bitmap.Config.ARGB_8888;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_file_with_return_bitmap_compress_test);

        mOriginImg1 = (ImageView) findViewById(R.id.img_origin1);
        mOriginImg2 = (ImageView) findViewById(R.id.img_origin2);
        mOriginImg3 = (ImageView) findViewById(R.id.img_origin3);
        mOriginImg4 = (ImageView) findViewById(R.id.img_origin4);
        mCompressImg1 = (ImageView) findViewById(R.id.img_compress1);
        mCompressImg2 = (ImageView) findViewById(R.id.img_compress2);
        mCompressImg3 = (ImageView) findViewById(R.id.img_compress3);
        mCompressImg4 = (ImageView) findViewById(R.id.img_compress4);

        mOriginTv = (TextView) findViewById(R.id.tv_origin);
        mCompressTv = (TextView) findViewById(R.id.tv_compress);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_batch, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

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
            case R.id.action_file:
                free();
                gcAndFinalize();
                testFile();
                break;
            case R.id.action_bitmap:
                free();
                gcAndFinalize();
                testBitmap();
                break;
            case R.id.action_res:
                free();
                gcAndFinalize();
                testResource();
                break;
            case R.id.action_uri:
                free();
                gcAndFinalize();
                testUri();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void free() {
        mOriginImg1.setImageBitmap(null);
        mOriginImg2.setImageBitmap(null);
        mOriginImg3.setImageBitmap(null);
        mOriginImg4.setImageBitmap(null);
        mCompressImg1.setImageBitmap(null);
        mCompressImg2.setImageBitmap(null);
        mCompressImg3.setImageBitmap(null);
        mCompressImg4.setImageBitmap(null);
    }

    private void setupOriginInfo(Bitmap bitmap1, Bitmap bitmap2, Bitmap bitmap3, Bitmap bitmap4
            , long sizeBytes1, long sizeBytes2, long sizeBytes3, long sizeBytes4) {
        mOriginImg1.setImageBitmap(bitmap1);
        mOriginImg2.setImageBitmap(bitmap2);
        mOriginImg3.setImageBitmap(bitmap3);
        mOriginImg4.setImageBitmap(bitmap4);

        mOriginTv.setText("origin file size:\nfile[1,2,3,4]:" + Formatter.formatFileSize(this, sizeBytes1)
                + "," + Formatter.formatFileSize(this, sizeBytes2)
                + "," + Formatter.formatFileSize(this, sizeBytes3)
                + "," + Formatter.formatFileSize(this, sizeBytes4)
                + "\nwidth[1,2,3,4]:" + bitmap1.getWidth()
                + "," + bitmap2.getWidth()
                + "," + bitmap3.getWidth()
                + "," + bitmap4.getWidth()
                + "\nheight[1,2,3,4]:" + bitmap1.getHeight()
                + "," + bitmap2.getHeight()
                + "," + bitmap3.getHeight()
                + "," + bitmap4.getHeight()
                + "\nconfig:" + mConfig);
    }

    private void setupCompressInfo(
            Bitmap bitmap1, Bitmap bitmap2, Bitmap bitmap3, Bitmap bitmap4,
            String outfile1, String outfile2, String outfile3, String outfile4,
            long sizeBytes1, long sizeBytes2, long sizeBytes3, long sizeBytes4) {
        mCompressImg1.setImageBitmap(bitmap1);
        mCompressImg2.setImageBitmap(bitmap2);
        mCompressImg3.setImageBitmap(bitmap3);
        mCompressImg4.setImageBitmap(bitmap4);
        mCompressTv.setText("compress file size:\nfile[1,2,3,4]:" + Formatter.formatFileSize(this, sizeBytes1)
                + "," + Formatter.formatFileSize(this, sizeBytes2)
                + "," + Formatter.formatFileSize(this, sizeBytes3)
                + "," + Formatter.formatFileSize(this, sizeBytes4)
                + "\nwidth[1,2,3,4]:" + bitmap1.getWidth()
                + "," + bitmap2.getWidth()
                + "," + bitmap3.getWidth()
                + "," + bitmap4.getWidth()
                + "\nheight[1,2,3,4]:" + bitmap1.getHeight()
                + "," + bitmap2.getHeight()
                + "," + bitmap3.getHeight()
                + "," + bitmap4.getHeight()
                + "\n\noutfile1:" + outfile1
                + "\n\noutfile2:" + outfile2
                + "\n\noutfile3:" + outfile3
                + "\n\noutfile4:" + outfile4
                + "\n\nconfig:" + mConfig);
    }

    private void testFile() {
        try {
            final InputStream is1 = getResources().getAssets()
                    .open("test_4.png");
            final InputStream is2 = getResources().getAssets()
                    .open("test-3.jpg");
            final InputStream is3 = getResources().getAssets()
                    .open("test_2.png");
            final InputStream is4 = getResources().getAssets()
                    .open("test_1.png");

            long fileSize1 = is1.available();
            long fileSize2 = is2.available();
            long fileSize3 = is3.available();
            long fileSize4 = is4.available();
            final File outfile1 = new File(getExternalFilesDir(null), "batch-test-2.jpg");
            final File outfile2 = new File(getExternalFilesDir(null), "batch-test-3.jpg");
            final File outfile3 = new File(getExternalFilesDir(null), "batch-test-4.jpg");
            final File outfile4 = new File(getExternalFilesDir(null), "batch-test-5.jpg");
            FileOutputStream fos = new FileOutputStream(outfile1);
            byte[] buffer = new byte[4096];
            int len = -1;
            while ((len = is1.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            fos = new FileOutputStream(outfile2);
            buffer = new byte[4096];
            len = -1;
            while ((len = is2.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            fos = new FileOutputStream(outfile3);
            buffer = new byte[4096];
            len = -1;
            while ((len = is3.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            fos = new FileOutputStream(outfile4);
            buffer = new byte[4096];
            len = -1;
            while ((len = is4.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            fos.close();

            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = mConfig;
            Bitmap originBitmap1 = BitmapFactory.decodeFile(outfile1.getAbsolutePath(), options);
            Bitmap originBitmap2 = BitmapFactory.decodeFile(outfile2.getAbsolutePath(), options);
            Bitmap originBitmap3 = BitmapFactory.decodeFile(outfile3.getAbsolutePath(), options);
            Bitmap originBitmap4 = BitmapFactory.decodeFile(outfile4.getAbsolutePath(), options);
            setupOriginInfo(originBitmap1, originBitmap2, originBitmap3, originBitmap4
                    , fileSize1, fileSize2, fileSize3, fileSize4);

            Tiny.FileCompressOptions compressOptions = new Tiny.FileCompressOptions();
            compressOptions.config = mConfig;

            File[] files = new File[]{outfile1, outfile2, outfile3, outfile4};
            Tiny.getInstance().source(files).batchAsFile().withOptions(compressOptions).batchCompress(new FileWithBitmapBatchCallback() {
                @Override
                public void callback(boolean isSuccess, Bitmap[] bitmaps, String[] outfile) {
                    if (!isSuccess) {
                        mCompressTv.setText("batch compress file failed!");
                        return;
                    }
                    setupCompressInfo(bitmaps[0], bitmaps[1], bitmaps[2], bitmaps[3], outfile[0], outfile[1], outfile[2], outfile[3]
                            , new File(outfile[0]).length(), new File(outfile[1]).length()
                            , new File(outfile[2]).length(), new File(outfile[3]).length());
                }
            });

            is1.close();
            is2.close();
            is3.close();
            is4.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void testBitmap() {
        try {
            final InputStream is1 = getResources().getAssets()
                    .open("test-2.jpg");
            final InputStream is2 = getResources().getAssets()
                    .open("test_1.png");
            final InputStream is3 = getResources().getAssets()
                    .open("test_3.png");
            final InputStream is4 = getResources().getAssets()
                    .open("test_4.png");
            long fileSize1 = is1.available();
            long fileSize2 = is2.available();
            long fileSize3 = is3.available();
            long fileSize4 = is4.available();
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = mConfig;
            Bitmap originBitmap1 = BitmapFactory.decodeStream(is1, null, options);
            Bitmap originBitmap2 = BitmapFactory.decodeStream(is2, null, options);
            Bitmap originBitmap3 = BitmapFactory.decodeStream(is3, null, options);
            Bitmap originBitmap4 = BitmapFactory.decodeStream(is4, null, options);
            setupOriginInfo(originBitmap1, originBitmap2, originBitmap3, originBitmap4
                    , fileSize1, fileSize2, fileSize3, fileSize4);

            Bitmap[] bitmaps = new Bitmap[]{originBitmap1, originBitmap2, originBitmap3, originBitmap4};
            Tiny.FileCompressOptions compressOptions = new Tiny.FileCompressOptions();
            compressOptions.config = mConfig;

            Tiny.getInstance().source(bitmaps).batchAsFile().withOptions(compressOptions).batchCompress(new FileWithBitmapBatchCallback() {
                @Override
                public void callback(boolean isSuccess, Bitmap[] bitmaps, String[] outfile) {
                    if (!isSuccess) {
                        mCompressTv.setText("batch compress file failed!");
                        return;
                    }
                    setupCompressInfo(bitmaps[0], bitmaps[1], bitmaps[2], bitmaps[3], outfile[0], outfile[1], outfile[2], outfile[3]
                            , new File(outfile[0]).length(), new File(outfile[1]).length()
                            , new File(outfile[2]).length(), new File(outfile[3]).length());
                }
            });
            is1.close();
            is2.close();
            is3.close();
            is4.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void testResource() {
        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = mConfig;
            Bitmap originBitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.test_1, options);
            Bitmap originBitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.test_2, options);
            Bitmap originBitmap3 = BitmapFactory.decodeResource(getResources(), R.drawable.test_3, options);
            Bitmap originBitmap4 = BitmapFactory.decodeResource(getResources(), R.drawable.test, options);
            setupOriginInfo(originBitmap1, originBitmap2, originBitmap3, originBitmap4
                    , (long) (1.65 * 1024), (long) (2.28 * 1024), 371 * 1024, 226 * 1024);

            int[] resIds = new int[]{R.drawable.test_1, R.drawable.test_2, R.drawable.test_3, R.drawable.test};
            Tiny.FileCompressOptions compressOptions = new Tiny.FileCompressOptions();
            compressOptions.config = mConfig;

            Tiny.getInstance().source(resIds).batchAsFile().withOptions(compressOptions).batchCompress(new FileWithBitmapBatchCallback() {
                @Override
                public void callback(boolean isSuccess, Bitmap[] bitmaps, String[] outfile) {
                    if (!isSuccess) {
                        mCompressTv.setText("batch compress file failed!");
                        return;
                    }
                    setupCompressInfo(bitmaps[0], bitmaps[1], bitmaps[2], bitmaps[3], outfile[0], outfile[1], outfile[2], outfile[3]
                            , new File(outfile[0]).length(), new File(outfile[1]).length()
                            , new File(outfile[2]).length(), new File(outfile[3]).length());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void testUri() {
        String url1 = "http://7xswxf.com2.z0.glb.qiniucdn.com//blog/deec2ac0373d08eb85a.jpg";
        String url2 = "http://7xswxf.com2.z0.glb.qiniucdn.com/IMG_1439.JPG";
        String url3 = "http://7xswxf.com2.z0.glb.qiniucdn.com/IMG_1698.JPG";
        String url4 = "http://7xswxf.com2.z0.glb.qiniucdn.com/IMG_1694.JPG";
        try {
            final InputStream is = getResources().getAssets()
                    .open("enjoy.JPG");
            Bitmap enjoyBitmap = BitmapFactory.decodeStream(is);
            mOriginImg1.setImageBitmap(enjoyBitmap);
            mOriginImg2.setImageBitmap(enjoyBitmap);
            mOriginImg3.setImageBitmap(enjoyBitmap);
            mOriginImg4.setImageBitmap(enjoyBitmap);
            mOriginTv.setText("省略一万字~");

            Uri[] uris = new Uri[]{Uri.parse(url1), Uri.parse(url2), Uri.parse(url3), Uri.parse(url4)};
            Tiny.FileCompressOptions compressOptions = new Tiny.FileCompressOptions();
            compressOptions.config = mConfig;

            Tiny.getInstance().source(uris).batchAsFile().withOptions(compressOptions).batchCompress(new FileWithBitmapBatchCallback() {
                @Override
                public void callback(boolean isSuccess, Bitmap[] bitmaps, String[] outfile) {
                    if (!isSuccess) {
                        mCompressTv.setText("batch compress file failed!");
                        return;
                    }
                    setupCompressInfo(bitmaps[0], bitmaps[1], bitmaps[2], bitmaps[3], outfile[0], outfile[1], outfile[2], outfile[3]
                            , new File(outfile[0]).length(), new File(outfile[1]).length()
                            , new File(outfile[2]).length(), new File(outfile[3]).length());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
