package com.doschool.aa.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.RectF;
import android.view.View;

import com.doschool.R;
import com.doschool.app.DoschoolApp;

public class CircleProgress extends View {  
  
    // 画实心圆的画笔  
    private Paint mCirclePaint;  
    // 画圆环的画笔  
    private Paint mRingPaint;  
    // 画字体的画笔  
    private Paint mTextPaint;  
    
    // 圆形颜色  
    private int mCircleColor=R.color.transparent;  
    // 圆环颜色  
    private int mRingColor=R.color.white;  

    private int mTextColor=R.color.transparent;  
    
    // 半径  
    private float mRadius;  
    // 圆环半径  
    private float mRingRadius;  
    // 圆环宽度  
    private float mStrokeWidth;  
    // 圆心x坐标  
    private int mXCenter;  
    // 圆心y坐标  
    private int mYCenter;  
    // 字的长度  
    private float mTxtWidth;  
    // 字的高度  
    private float mTxtHeight;  
    // 总进度  
    private int mTotalProgress = 100;  
    // 当前进度  
    private int mProgress;  
  
    public CircleProgress(Context context,int size) {  
        super(context);  
        // 获取自定义的属性  
//        mRadius =(float) (size*0.8);
//        mStrokeWidth =(float) (size*0.1);
//        mRingRadius = mRadius + mStrokeWidth / 2;  
        
        mRadius =0;
        mStrokeWidth =3*DoschoolApp.pxperdp;
        mRingRadius = 21*DoschoolApp.pxperdp;  
        
        initVariable();  
    }  

  
    private void initVariable() {  
        mCirclePaint = new Paint();  
        mCirclePaint.setAntiAlias(true);  
        mCirclePaint.setColor(getResources().getColor(mCircleColor));  
        mCirclePaint.setStyle(Paint.Style.FILL);  
          
        mRingPaint = new Paint();  
        mRingPaint.setAntiAlias(true);  
        mRingPaint.setColor(getResources().getColor(mRingColor));  
        mRingPaint.setStyle(Paint.Style.STROKE);  
        mRingPaint.setStrokeWidth(mStrokeWidth);  
          
        mTextPaint = new Paint();  
        mTextPaint.setAntiAlias(true);  
        mTextPaint.setStyle(Paint.Style.FILL);  
        mTextPaint.setColor(getResources().getColor(mTextColor));
        mTextPaint.setARGB(255, 255, 255, 255);  
        mTextPaint.setTextSize(mRadius / 2);  
          
        FontMetrics fm = mTextPaint.getFontMetrics();  
        mTxtHeight = (int) Math.ceil(fm.descent - fm.ascent);  
          
    }  
  
    @Override  
    protected void onDraw(Canvas canvas) {  
  
        mXCenter = getWidth() / 2;  
        mYCenter = getHeight() / 2;  
          
        canvas.drawCircle(mXCenter, mYCenter, mRadius, mCirclePaint);  
          
        if (mProgress >= 0 ) {  
            RectF oval = new RectF();  
            oval.left = (mXCenter - mRingRadius);  
            oval.top = (mYCenter - mRingRadius);  
            oval.right = mRingRadius * 2 + (mXCenter - mRingRadius);  
            oval.bottom = mRingRadius * 2 + (mYCenter - mRingRadius);  
            canvas.drawArc(oval, -90, ((float)mProgress / mTotalProgress) * 360, false, mRingPaint); //  
//          canvas.drawCircle(mXCenter, mYCenter, mRadius + mStrokeWidth / 2, mRingPaint);  
            String txt = mProgress + "%";  
            mTxtWidth = mTextPaint.measureText(txt, 0, txt.length());  
            canvas.drawText(txt, mXCenter - mTxtWidth / 2, mYCenter + mTxtHeight / 4, mTextPaint);  
        }  
    }  
      
    public void setProgress(int progress) {  
        mProgress = progress;  
//      invalidate();  
        postInvalidate();  
    }  
  
}  