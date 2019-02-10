package com.insworks.qr_code.two;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.insworks.qr_code.R;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 二维码扫描界面
 */
public class SecondScannerActivity extends AppCompatActivity {
    private static final String TAG = "SecondScannerActivity";
    private static final int REQUEST_CODE = 10240;
    private static final int REQUEST_IMAGE = 1025;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner_second);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.btn_first1, R.id.btn_second1, R.id.btn_third1, R.id.btn_fourth1, R.id.btn_fifth1, R.id.btn_sixth1})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_first1://集成一
                Intent intent = new Intent(this, CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.btn_second1://集成二  开启相册
                intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_IMAGE);
                break;
           /* case R.id.btn_third1://自定义普通侧滑菜单 include布局
                startActivity(new Intent(getApplicationContext(), ThirdImplementionsActivity.class));
                break;
            case R.id.btn_fourth1://使用第三方开源库SlidingMenu
                startActivity(new Intent(getApplicationContext(), FourthImplementionsActivity.class));
                break;
            case R.id.btn_fifth1://使用谷歌自带DrawerLayout 包裹NavigationView
                startActivity(new Intent(getApplicationContext(), FifthImplementionsActivity.class));
                break;
            case R.id.btn_sixth1://第六种 github成品
                startActivity(new Intent(getApplicationContext(), SixthImplementionsActivity.class));
                break;*/
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    Toast.makeText(this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(this, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }

        if (requestCode == REQUEST_IMAGE) {
            if (data != null) {
                Uri uri = data.getData();
                ContentResolver cr = getContentResolver();
                try {
                    Bitmap mBitmap = MediaStore.Images.Media.getBitmap(cr, uri);//显得到bitmap图片

                    CodeUtils.analyzeBitmap(mBitmap.toString(), new CodeUtils.AnalyzeCallback() {
                        @Override
                        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                            Toast.makeText(SecondScannerActivity.this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onAnalyzeFailed() {
                            Toast.makeText(SecondScannerActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                        }
                    });

                    if (mBitmap != null) {
                        mBitmap.recycle();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

