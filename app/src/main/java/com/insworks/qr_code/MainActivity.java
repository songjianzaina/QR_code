package com.insworks.qr_code;

import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.insworks.qr_code.one.FirstScannerActivity;
import com.uuzuche.lib_zxing.activity.CaptureActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_first)
    Button btnFirst;
    @BindView(R.id.btn_second)
    Button btnSecond;
    @BindView(R.id.btn_third)
    Button btnThird;
    @BindView(R.id.btn_fourth)
    Button btnFourth;
    @BindView(R.id.btn_fifth)
    Button btnFifth;
    @BindView(R.id.btn_sixth)
    Button btnSixth;
    @BindView(R.id.activity_main)
    LinearLayout activityMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.btn_first, R.id.btn_second, R.id.btn_third, R.id.btn_fourth, R.id.btn_fifth, R.id.btn_sixth})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_first://普通带闪光灯
                if (isCameraPermission()) {
                    startActivity(new Intent(getApplicationContext(), FirstScannerActivity.class));
                } else {
                    Toast.makeText(this,"您的相机权限已被禁用,请手动开启!",Toast.LENGTH_SHORT);
                }
                break;
            case R.id.btn_second://第三方集成
//                startActivity(new Intent(getApplicationContext(), SecondScannerActivity.class));
                startActivity(new Intent(getApplicationContext(), CaptureActivity.class));
                break;
     /*       case R.id.btn_third://自定义普通侧滑菜单 include布局
                startActivity(new Intent(getApplicationContext(), ThirdImplementionsActivity.class));
                break;
            case R.id.btn_fourth://使用第三方开源库SlidingMenu
                startActivity(new Intent(getApplicationContext(), FourthImplementionsActivity.class));
                break;
            case R.id.btn_fifth://使用谷歌自带DrawerLayout 包裹NavigationView
                startActivity(new Intent(getApplicationContext(), FifthImplementionsActivity.class));
                break;
            case R.id.btn_sixth://第六种 github成品
                startActivity(new Intent(getApplicationContext(), SixthImplementionsActivity.class));
                break;*/
        }
    }
    /**
     * 作用：用户是否同意打开相机权限
     *
     * @return true 同意 false 拒绝
     */
    public boolean isCameraPermission() {

        try {
            Camera.open().release();
            return true;
        } catch (Exception e) {
            return false;
        }

    }
}
