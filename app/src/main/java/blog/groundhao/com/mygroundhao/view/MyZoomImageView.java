package blog.groundhao.com.mygroundhao.view;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

/**
 * Created by user on 2016/5/30.
 */
public class MyZoomImageView extends ImageView implements ViewTreeObserver.OnGlobalLayoutListener, ScaleGestureDetector.OnScaleGestureListener
        , View.OnTouchListener {
    /**
     * Created by Administrator on 2016/5/25.
     */
    private boolean mOnce;

    /**
     * 初始化的缩放值
     */
    private float mInitScale;
    /**
     * 双击放大的缩放值
     */
    private float mMidScale;
    /**
     * 最大的放大值
     */
    private float mMaxScale;

    /**
     * 矩阵
     */
    private Matrix mScaleMatrix;

    /**
     * 手势缩放捕获,捕获用户多指触控时的比例,判断用户想要放大还是缩小
     */
    private ScaleGestureDetector gestureDetector;

    //------------

    /**
     * 记录手指触摸的点的数量
     */
    private int mLastPointerCount;

    /**
     * 最后的x y 值
     *
     * @param context
     */
    private int mLastX, mLastY;

    /**
     * 触控溢出的值
     */
    private int mTouchSlop;
    /**
     * 是否可以拖拽
     */
    private boolean isCanDrag;

    /**
     * 是否进行上下,  左右 边界检测
     *
     * @param context
     */
    private boolean isLeftAndRightCheck, isTopAndBottom;

    /**
     * 双击缩放
     *
     * @param context
     */
    private GestureDetector mGestureDetector;
    /**
     * 是否自动缩放
     */
    boolean isAutoScale;

    /**
     * 是否可以缩放
     */
    private boolean isCanScale = true;


    public MyZoomImageView(Context context) {
        this(context, null);
    }

    public MyZoomImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyZoomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScaleMatrix = new Matrix();
        setScaleType(ScaleType.MATRIX);
        gestureDetector = new ScaleGestureDetector(context, this);
        setOnTouchListener(this);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            /**
             * 双击回调
             * @param e
             * @return
             */
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if (!isCanScale) return false;
                float x = e.getX();
                float y = e.getY();

                if (isAutoScale) return true;
                if (getScale() < mMidScale) {
                    postDelayed(new AutoScaleRunnable(mMidScale, x, y), 16);
                    isAutoScale = true;
                } else {
                    postDelayed(new AutoScaleRunnable(mInitScale, x, y), 16);
                    isAutoScale = true;
                }
                return true;
            }
        });
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        /**
         * 当View附加到窗口上时调用
         */
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        /**
         * 当View从窗口上分离的时候调用
         */
        getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }

    @Override
    public void onGlobalLayout() {
        /**
         * 全局的布局结束时调用这个方法
         */
        //1 获取ImageView加载完成的图片
        if (!mOnce) {
            //获得控件的宽和高
            int width = getWidth();
            int height = getHeight();
            //得到图片以及宽和高
            Drawable d = getDrawable();
            if (d == null) return;
            int dh = d.getIntrinsicHeight();
            int dw = d.getIntrinsicWidth();

            float scale = 1.0f;
            /**
             * 如果屏幕的宽度大于控件宽度,高度小于控件高度,将其缩小
             */
            if (dw > width && dh < height) {
                scale = width * 1.0f / dw;
            }
            /**
             * 如果屏幕的高度大于控件高度,宽度小于控件宽度,将其缩小
             */
            if (dh > height && dw < width) {
                scale = height * 1.0f / dh;
            }

            if ((dh > height && dw > width) || (dh < height && dw < width)) {
                scale = Math.min(width * 1.0f / dw, height * 1.0f / dh);
            }

            mInitScale = scale;
            mMidScale = scale * 2;
            mMaxScale = scale * 4;

            //移动图片到控件的中心
            int dx = getWidth() / 2 - dw / 2;
            int dy = getHeight() / 2 - dh / 2;

            mScaleMatrix.postTranslate(dx, dy);//平移
            mScaleMatrix.postScale(mInitScale, mInitScale, width / 2, height / 2);//缩放
            setImageMatrix(mScaleMatrix);//设置矩阵

            mOnce = true;
        }


    }

    private float getScale() {
        float[] values = new float[9];
        mScaleMatrix.getValues(values);
        return values[Matrix.MSCALE_X];
    }

    public void setCanScale(boolean canScale) {
        isCanScale = canScale;
    }

    public boolean isCanScale() {
        return isCanScale;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        float scale = getScale();
        float scaleFactor = detector.getScaleFactor();//拿到缩放值
        //缩放范围  大于最小的 还想缩小  小于最大的 且还想 放大


        Drawable d = getDrawable();
        if (d == null) {
            return false;
        }
        if ((scale > mInitScale && scaleFactor < 1.0f) || (scale < mMaxScale && scaleFactor > 1.0f)) {
            //想缩特别小的时候设置为最小值
            if (scale * scaleFactor < mInitScale) {
                scaleFactor = mInitScale / scale;
            }
            if (scale * scaleFactor > mMaxScale) {
                scaleFactor = mMaxScale / scale;
            }
            //缩放  以屏幕中心为中心点
//            mScaleMatrix.postScale(scaleFactor, scaleFactor, getWidth() / 2, getHeight() / 2);
            //以手势的缩放点为中心
            mScaleMatrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(), detector.getFocusY());
            checkBorderAndCenterWhenScale();
            setImageMatrix(mScaleMatrix);
        }
        return true;
    }


    private RectF getMatrixRectf() {
        Matrix matrix = mScaleMatrix;
        RectF rectF = new RectF();
        Drawable drawable = getDrawable();
        //获取图片的矩形宽高 和 l,t,r,b
        if (drawable != null) {
            rectF.set(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            matrix.mapRect(rectF);
        }
        return rectF;
    }

    /**
     * 控制图片边界以及内容边界
     */
    private void checkBorderAndCenterWhenScale() {
        RectF matrixRectf = getMatrixRectf();
        float dx = 0;
        float dy = 0;

        int width = getWidth();
        int height = getHeight();

        //缩放时 进行边界控制 防止出现白边
        if (matrixRectf.width() >= width) {
            if (matrixRectf.left > 0) {
                dx = -matrixRectf.left;
            }
            if (matrixRectf.right < width) {
                dx = width - matrixRectf.right;
            }
        }

        if (matrixRectf.height() >= height) {
            if (matrixRectf.top > 0) {
                dy = -matrixRectf.top;
            }
            if (matrixRectf.bottom < height) {
                dy = height - matrixRectf.bottom;
            }
        }

        //如果宽度或者高度小于控件的宽或者高 让其居中

        if (matrixRectf.width() < width) {
            dx = width / 2f - matrixRectf.right + matrixRectf.width() / 2f;
        }

        if (matrixRectf.height() < height) {
            dy = height / 2f - matrixRectf.bottom + matrixRectf.height() / 2f;
        }
        mScaleMatrix.postTranslate(dx, dy);
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (!isCanScale) return false;
        /**
         * 接管双击
         */
        if (mGestureDetector.onTouchEvent(event)) return true;

        gestureDetector.onTouchEvent(event);

        int pointerCount = event.getPointerCount();
        int x = 0;
        int y = 0;
        for (int i = 0; i < pointerCount; i++) {
            x += event.getX(i);
            y += event.getY(i);
        }

        x /= pointerCount;
        y /= pointerCount;

        if (mLastPointerCount != pointerCount) {
            mLastX = x;
            mLastY = y;
            isCanDrag = false; //手指发生改变 重新进行判断
        }

        mLastPointerCount = pointerCount;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                /**
                 * 如果图片的宽高 大于控件的宽高 请求父控件不要拦截触摸事件
                 */
                if (getMatrixRectf().width() > getWidth() + 0.01 || getMatrixRectf().height() > getHeight() + 0.01) {
                    if (getParent() instanceof ViewPager)
                        getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (getMatrixRectf().width() > getWidth() + 0.01 || getMatrixRectf().height() > getHeight() + 0.01) {
                    if (getParent() instanceof ViewPager)
                        getParent().requestDisallowInterceptTouchEvent(true);
                }
                int dx = x - mLastX;
                int dy = y - mLastY;

                if (!isCanDrag) {
                    isCanDrag = isMoveAction(dx, dy);
                }
                if (isCanDrag) {
                    RectF rectF = getMatrixRectf();
                    if (getDrawable() != null) {
                        //如果 图片的宽度 或高度 小于控件的宽度和高度 完全显示的情况下 不需要进行移动
                        isLeftAndRightCheck = isTopAndBottom = true;
                        if (rectF.width() < getWidth()) {
                            dx = 0;
                            isLeftAndRightCheck = false;
                        }
                        if (rectF.height() < getHeight()) {
                            dy = 0;
                            isTopAndBottom = false;
                        }
                        mScaleMatrix.postTranslate(dx, dy);
                        checkBorderAndCenterWhenDrag();
                        setImageMatrix(mScaleMatrix);
                    }
                    mLastX = x;
                    mLastY = y;

                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mLastPointerCount = 0;
                break;
        }

        return true;
    }

    /**
     * 移动时 进行边界检测
     */
    private void checkBorderAndCenterWhenDrag() {
        RectF rectF = getMatrixRectf();
        float dx = 0;
        float dy = 0;

        int width = getWidth();
        int heigth = getHeight();
        /**
         * 如果边界 小于控件边界且可进行检测时 进行相应的移动
         */
        if (rectF.left > 0 && isLeftAndRightCheck) {
            dx = -rectF.left;
        }
        if (rectF.right < width && isLeftAndRightCheck) {
            dx = width - rectF.right;
        }

        if (rectF.top > 0 && isTopAndBottom) {
            dy = -rectF.top;
        }

        if (rectF.bottom < heigth && isTopAndBottom) {
            dy = heigth - rectF.bottom;
        }

        mScaleMatrix.postTranslate(dx, dy);
    }

    private boolean isMoveAction(int dx, int dy) {


        /**
         * 返回平方根 double
         */
        return Math.sqrt(dx * dx + dy * dy) > mTouchSlop;
    }

    class AutoScaleRunnable implements Runnable {
        /**
         * 目标缩放值
         */
        private float tagScale;
        /**
         * 缩放中心点
         */
        private float x;
        private float y;

        private final float BIGSCALE = 1.09f;
        private final float SMALLSCALE = 0.93f;
        private float tmpScale;

        public AutoScaleRunnable(float tagScale, float x, float y) {
            this.tagScale = tagScale;
            this.x = x;
            this.y = y;
            if (getScale() < tagScale) {
                tmpScale = BIGSCALE;
            }
            if (getScale() > tagScale) {
                tmpScale = SMALLSCALE;
            }
        }

        @Override
        public void run() {
            mScaleMatrix.postScale(tmpScale, tmpScale, x, y);
            checkBorderAndCenterWhenScale();
            setImageMatrix(mScaleMatrix);
            float currentScale = getScale();
            if ((tmpScale > 1.0f && currentScale < tagScale) || (tmpScale < 1.0f && currentScale > tagScale)) {
                postDelayed(this, 16);
            } else {
                //设置为目标
                float sale = tagScale / currentScale;
                mScaleMatrix.postScale(sale, sale, x, y);
                checkBorderAndCenterWhenScale();
                setImageMatrix(mScaleMatrix);
                isAutoScale = false;
            }
        }
    }
}
