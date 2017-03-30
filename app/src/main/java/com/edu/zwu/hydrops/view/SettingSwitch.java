package com.edu.zwu.hydrops.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.edu.zwu.hydrops.R;
import com.edu.zwu.hydrops.util.AppUtil;

/**
 * Created by shengwei.yi on 2016/4/12.
 */
public class SettingSwitch extends View implements View.OnClickListener {

    private Bitmap mSwitchBck, mSwitchBtn;
    private Paint mPaint;
    private boolean isSwitchOn;
    private final static float TOPOFFEST = AppUtil.convertDpToPx(0.5f);
    private final static float LEFTOFFEST = AppUtil.convertDpToPx(0.5f);
    private float mOffsetX, mLastX, mCurrentX, mFinalX, mMoveX, mUpX;

    private OnChangeListener mListener;

    public SettingSwitch(Context context) {
        this(context, null);
    }

    public SettingSwitch(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingSwitch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化相关资源
     */
    public void init() {
        mSwitchBtn = BitmapFactory.decodeResource(getResources(),
                R.drawable.switch_btn);
        mSwitchBck = BitmapFactory.decodeResource(getResources(),
                R.drawable.switch_on);
        setOnClickListener(this);
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        isSwitchOn = true;
        mOffsetX = mSwitchBck.getWidth() - mSwitchBtn.getWidth() - LEFTOFFEST;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setAlpha(255);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        setMeasuredDimension(mSwitchBck.getWidth(), mSwitchBck.getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        canvas.drawBitmap(mSwitchBck, 0, 0, mPaint);
        canvas.drawBitmap(mSwitchBtn, mOffsetX + mMoveX, TOPOFFEST, mPaint);
    }

    public void setSwitchOn(boolean isSwitchOn) {
        this.isSwitchOn = isSwitchOn;
        mSwitchBck = BitmapFactory.decodeResource(getResources(),
                isSwitchOn ? R.drawable.switch_on : R.drawable.switch_off);
        mOffsetX = isSwitchOn ? mSwitchBck.getWidth() - mSwitchBtn.getWidth() - LEFTOFFEST : LEFTOFFEST;
        invalidate();
    }

    public boolean getSwitchOn() {
        return isSwitchOn;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                mCurrentX = event.getX();
                mMoveX = mCurrentX - mLastX;
                // 如果开关开着向左滑动，或者开关关着向右滑动（这时候是不需要处理的）
                if ((isSwitchOn && mMoveX > 0) || (!isSwitchOn && mMoveX < 0)) {
                    return true;
                } else if (mMoveX > mSwitchBtn.getWidth() / 2) {
                    mSwitchBck = BitmapFactory.decodeResource(getResources(),
                            R.drawable.switch_on);
                } else if (mMoveX < mSwitchBtn.getWidth() / 2 && mMoveX > 0) {
                    mSwitchBck = BitmapFactory.decodeResource(getResources(),
                            R.drawable.switch_off);
                } else if (mMoveX < 0 && mMoveX > -mSwitchBtn.getWidth() / 2) {
                    mSwitchBck = BitmapFactory.decodeResource(getResources(),
                            R.drawable.switch_on);
                } else if (mMoveX < -mSwitchBtn.getWidth() / 2) {
                    mSwitchBck = BitmapFactory.decodeResource(getResources(),
                            R.drawable.switch_off);
                }

                if (mMoveX > mSwitchBtn.getWidth()) {
                    mMoveX = mSwitchBtn.getWidth();
                } else if (mMoveX < -mSwitchBtn.getWidth()) {
                    mMoveX = -mSwitchBtn.getWidth();
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                mFinalX = event.getX();
                mUpX = mFinalX - mLastX;
                mMoveX = 0;
                if ((isSwitchOn && mUpX > 0) || (!isSwitchOn && mUpX < 0)) {
                    return true;
                }
                if (mUpX > mSwitchBtn.getWidth() / 2) {
                    mOffsetX = mSwitchBck.getWidth() - mSwitchBtn.getWidth() - LEFTOFFEST;
                    isSwitchOn = true;
                    if (mListener != null) {
                        mListener.onChange(this, isSwitchOn);
                    }
                    invalidate();
                    return true;
                } else if (mUpX < mSwitchBtn.getWidth() / 2 && mUpX > 0) {
                    mOffsetX = LEFTOFFEST;
                    isSwitchOn = false;
                    if (mListener != null) {
                        mListener.onChange(this, isSwitchOn);
                    }
                    invalidate();
                    return true;
                } else if (mUpX < 0 && mUpX > -mSwitchBtn.getWidth() / 2) {
                    mOffsetX = mSwitchBck.getWidth() - mSwitchBtn.getWidth() - LEFTOFFEST;
                    isSwitchOn = true;
                    if (mListener != null) {
                        mListener.onChange(this, isSwitchOn);
                    }
                    invalidate();
                    return true;
                } else if (mUpX < -mSwitchBtn.getWidth() / 2) {
                    mOffsetX = LEFTOFFEST;
                    isSwitchOn = false;
                    if (mListener != null) {
                        mListener.onChange(this, isSwitchOn);
                    }
                    invalidate();
                    return true;
                }

            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    public void setOnChangeListener(OnChangeListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onClick(View v) {
        isSwitchOn = !isSwitchOn;
        mOffsetX = isSwitchOn ? mSwitchBck.getWidth() - mSwitchBtn.getWidth() - LEFTOFFEST : LEFTOFFEST;
        mSwitchBck = BitmapFactory.decodeResource(getResources(),
                isSwitchOn ? R.drawable.switch_on : R.drawable.switch_off);
        if (mListener != null) {
            mListener.onChange(this, isSwitchOn);
        }
        invalidate();
    }

    public interface OnChangeListener {
        void onChange(SettingSwitch sb, boolean state);
    }
}
