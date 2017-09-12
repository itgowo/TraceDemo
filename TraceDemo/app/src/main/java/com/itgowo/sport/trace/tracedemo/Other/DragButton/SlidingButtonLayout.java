package com.itgowo.sport.trace.tracedemo.Other.DragButton;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.ViewDragHelper;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.itgowo.sport.trace.tracedemo.R;


/**
 * Created by admin on 2016/4/21.
 */
public class SlidingButtonLayout extends FrameLayout {

    private ViewDragHelper mViewDragHelper;
    private ImageView mImageView;
    private TextView mTextView;
    private ViewGroup parentGroup;
    private Point initPosition = new Point();
    private boolean islayout=false;

    //拖动前显示的text
    private String initText;
    //拖动完成以后显示的str
    private String dragFinishText;

    private Drawable initImageViewDrawable;
    private Drawable dragFinishDrawable;

    private Drawable initTextViewDrawable;
    private Drawable dragTextViewFinishDrawable;

    //拖动前显示的可滑动imageview背景色
    private int ivBackgroundColor;
    //拖动完成后显示的可滑动imageview背景色
    private int dragFinishIvBackgroundColor;

    //拖动前显示的底部背景色
    private int tvBackgroundColor;
    //拖动完成后显示的底部背景色
    private int dragFinishTvBackgroundColor;

    //拖动前显示的字体颜色
    private int textViewTextColor;
    //拖动后显示的字体颜色
    private int dragFinishTextViewTextColor;


    private OnFinshDragListener mOnFinshDragListener;
    //是否完成拖动 默认是没有 为false
    private boolean isFinishDragFlag = false;

    public void setOnFinshDragListener(OnFinshDragListener onFinshDragListener) {
        mOnFinshDragListener = onFinshDragListener;
    }

    /**
     * 参数可以为空
     *
     * @param dragFinishText     拖动完成以后显示的text
     * @param dragFinishDrawable 拖动完成以后 可拖动按钮显示的图片
     */
    public void initUIconfig(String dragFinishText, Drawable dragFinishDrawable) {
        this.dragFinishText = dragFinishText;
        this.dragFinishDrawable = dragFinishDrawable;
    }


    public SlidingButtonLayout(Context context) {
        super(context);
    }

    /**
     * 滑动完成以后的ui变化
     */
    private void dragFinishUiChange() {
        if (!TextUtils.isEmpty(dragFinishText)) {
            mTextView.setText(dragFinishText);
        }
        mTextView.setBackgroundColor(dragFinishTvBackgroundColor);
        mTextView.setTextColor(dragFinishTextViewTextColor);
        //对于可拖动imageview来说 如果有背景图 那肯定优先背景图 否则背景色
        if (null != dragFinishDrawable) {
            mImageView.setBackgroundDrawable(dragFinishDrawable);
        } else {
            mImageView.setBackgroundDrawable(new ColorDrawable(dragFinishIvBackgroundColor));
        }
    }

    public SlidingButtonLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.sliding_layout, this);
        parentGroup = (ViewGroup) findViewById(R.id.parent_group);
        mImageView = (ImageView) view.findViewById(R.id.drag_button);
        mTextView = (TextView) view.findViewById(R.id.content_textview);
        initStyles(context, attrs);
        mViewDragHelper = ViewDragHelper.create(parentGroup, 1.0f, new ViewDragHelper.Callback() {
                    @Override
                    public boolean tryCaptureView(View child, int pointerId) {
                        if (isFinishDragFlag) {
                            return false;
                        }

                        if (child == mImageView) {
                            return true;
                        }
                        return false;
                    }

                    @Override
                    public int clampViewPositionVertical(View child, int top, int dy) {
                        return initPosition.y;
                    }

                    @Override
                    public int clampViewPositionHorizontal(View child, int left, int dx) {

                        //取得左边界的坐标
                        final int leftBound = getPaddingLeft();
                        //取得右边界的坐标
                        final int rightBound = getWidth() - child.getWidth() - leftBound;
                        //这个地方的含义就是 如果left的值 在leftbound和rightBound之间 那么就返回left
                        //如果left的值 比 leftbound还要小 那么就说明 超过了左边界 那我们只能返回给他左边界的值
                        //如果left的值 比rightbound还要大 那么就说明 超过了右边界，那我们只能返回给他右边界的值
                        if (left > rightBound - 20) {
                            Log.v("wuyue", "触发完成动作的回掉");
                            isFinishDragFlag = true;
                        }
                        return Math.min(Math.max(left, leftBound), rightBound);
                    }

                    @Override
                    public void onViewReleased(View releasedChild, float xvel, float yvel) {
                        //松手的时候 判断如果是这个view 就让他回到起始位置
                        if (releasedChild == mImageView && !isFinishDragFlag) {
                            Log.v("wuyue", "触发回弹");
                            mViewDragHelper.settleCapturedViewAt(initPosition.x, initPosition.y);
                            invalidate();
                        } else {
                            if (mOnFinshDragListener != null) {
                                mOnFinshDragListener.onFinshDragDone();
                            }
                            mViewDragHelper.settleCapturedViewAt(initPosition.x, initPosition.y);
                            invalidate();
                            isFinishDragFlag=false;
//                            dragFinishUiChange();
                        }
                    }
                }
        );

    }

    private void initStyles(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.slidingButtonLayout);
        initText = typedArray.getString(R.styleable.slidingButtonLayout_textview_text);
        dragFinishText = typedArray.getString(R.styleable.slidingButtonLayout_textview_dragfinish_text);
        tvBackgroundColor = typedArray.getColor(R.styleable.slidingButtonLayout_textview_backgroundColor, 0);
        dragFinishTextViewTextColor = typedArray.getColor(R.styleable.slidingButtonLayout_textview_dragfinish_textcolor, 0);
        dragFinishTvBackgroundColor = typedArray.getColor(R.styleable.slidingButtonLayout_textview_dragfinish_backgroundColor, 0);
        initImageViewDrawable = typedArray.getDrawable(R.styleable.slidingButtonLayout_imageview_background);
        dragFinishDrawable = typedArray.getDrawable(R.styleable.slidingButtonLayout_imageview_dragfinish_background);
        ivBackgroundColor = typedArray.getColor(R.styleable.slidingButtonLayout_imageview_backgroundColor, 0);
        dragFinishIvBackgroundColor = typedArray.getColor(R.styleable.slidingButtonLayout_imageview_dragfinish_backgroundColor, 0);
        initTextViewDrawable = typedArray.getDrawable(R.styleable.slidingButtonLayout_textview_backgroundDrawable);
        dragTextViewFinishDrawable = typedArray.getDrawable(R.styleable.slidingButtonLayout_textview_dragfinish_backgroundDrawable);
        mTextView.setText(initText);
        textViewTextColor = typedArray.getColor(R.styleable.slidingButtonLayout_textview_textcolor, 0xffffff);
        mTextView.setTextColor(textViewTextColor);
        //textview 背景图处理 图片资源优先
        if (null != initTextViewDrawable) {
            mTextView.setBackgroundDrawable(initTextViewDrawable);
        } else {
            mTextView.setBackgroundDrawable(new ColorDrawable(tvBackgroundColor));
        }
        //可滑动imageview 背景处理 图片资源优先
        if (null != initImageViewDrawable) {
            mImageView.setBackgroundDrawable(initImageViewDrawable);
        } else {
            mImageView.setBackgroundDrawable(new ColorDrawable(ivBackgroundColor));
        }
        typedArray.recycle();
    }


    public SlidingButtonLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//    public SlidingButtonLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }

    @Override
    public void computeScroll() {
        if (mViewDragHelper.continueSettling(true)) {
            invalidate();
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        //触发事件完成以后 就不可以再layout 回到初始位置了
        if (isFinishDragFlag) {
            return;
        }
        super.onLayout(changed, left, top, right, bottom);
        if (islayout){
            return;
        }
        initPosition.x = mImageView.getLeft();
        initPosition.y = mImageView.getTop();
        islayout=true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    public interface OnFinshDragListener {
        public void onFinshDragDone();
    }

}
