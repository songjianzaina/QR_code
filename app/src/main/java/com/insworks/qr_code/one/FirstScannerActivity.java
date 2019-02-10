package com.insworks.qr_code.one;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.zxing.Result;
import com.insworks.qr_code.R;
import com.insworks.qr_code.one.core.IViewFinder;
import com.insworks.qr_code.one.core.ViewFinderView;


/**
 * 二维码扫描界面
 */
public class FirstScannerActivity extends AppCompatActivity implements ScannerView.ResultHandler {
    private static final String TAG = "FirstScannerActivity";


    private ScannerView mScannerView;

    @Override
    public void onCreate(Bundle state) {
        setContentView(R.layout.activity_scanner_first);
        super.onCreate(state);

        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.scanner_content_frame);

        mScannerView = new ScannerView(this) {
            @Override
            protected IViewFinder createViewFinderView(Context context) {
                return new CustomViewFinderView(context);
            }
        };
        contentFrame.addView(mScannerView);
    }



    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    public void handleResult(Result rawResult) {
//        Toast.makeText(this, "Contents = " + rawResult.getText() +
//                ", Format = " + rawResult.getBarcodeFormat().toString(), Toast.LENGTH_SHORT).show();
        Log.i(TAG, "二维码扫描内容 rawResult.getText()=" + rawResult.getText());
        Toast.makeText(this, rawResult.getText(), Toast.LENGTH_LONG);

        // Note:
        // * Wait 2 seconds to resume the preview.
        // * On older devices continuously stopping and resuming camera preview can result in freezing the app.
        // * I don't know why this is the case but I don't have the time to figure out.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScannerView.resumeCameraPreview(FirstScannerActivity.this);
            }
        }, 2000);
    }


    private boolean filterResult(Result rawResult) {
        //过滤扫描结果

        return false;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private class CustomViewFinderView extends ViewFinderView {
        public static final String TRADE_MARK_TEXT = "将二维码图片对准扫描框即可自动扫描";
        public static final int TRADE_MARK_TEXT_SIZE_SP = 18;
        public final Paint PAINT = new Paint();
        private int flashWidth;
        private int flashHeight;
        private float tradeMarkTop;
        private float tradeMarkLeft;
        private float flashLeft;
        private float flashTop;


        public CustomViewFinderView(Context context) {
            super(context);
            init();
        }

        public CustomViewFinderView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        private void init() {
            PAINT.setColor(Color.WHITE);
            PAINT.setAntiAlias(true);
            float textPixelSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                    TRADE_MARK_TEXT_SIZE_SP, getResources().getDisplayMetrics());
            PAINT.setTextSize(textPixelSize);
        }

        @Override
        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);
//            drawTradeMark(canvas);
            drawFlashIcon(canvas);

        }

        private void drawTradeMark(Canvas canvas) {
            Rect framingRect = getFramingRect();
            if (framingRect != null) {
                tradeMarkTop = framingRect.bottom + PAINT.getTextSize() + 80;
                tradeMarkLeft = framingRect.left;
            } else {
                tradeMarkTop = 10;
                tradeMarkLeft = canvas.getHeight() - PAINT.getTextSize() - 10;
            }
            canvas.drawText(TRADE_MARK_TEXT, tradeMarkLeft, tradeMarkTop, PAINT);
        }

        //绘制闪光灯开图标
        private void drawFlashIcon(Canvas canvas) {
            Rect framingRect = getFramingRect();
            Bitmap flashBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.kaidengzhuangtai);
            //获取图片的宽和高
            flashWidth = flashBitmap.getWidth();
            flashHeight = flashBitmap.getHeight();
            //开始绘制
            flashLeft = (canvas.getWidth() / 2) - flashWidth / 2;
            flashTop = framingRect.bottom + PAINT.getTextSize() + 20;
            canvas.drawBitmap(flashBitmap, flashLeft, flashTop, PAINT);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            if (event.getAction() != MotionEvent.ACTION_DOWN) {
                return true;
            }
            if ((flashLeft <= event.getX() && event.getX() <= (flashLeft + flashWidth))
                    && (event.getY() >= flashTop && event.getY() <= (flashTop + flashWidth))) {
                // 闪光灯开关
                mScannerView.toggleFlash();
            }
            return false;
        }
    }
}
