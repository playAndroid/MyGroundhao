package blog.groundhao.com.mygroundhao.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 自定义控件，用于显示宽度和ImageView相同，高度自适应的图片显示模式.
 * 除此之外，还添加了最大高度限制，若图片长度大于等于屏幕长度，则高度显示为屏幕的1/3
 */
public class ShowMaxImageView extends ImageView {

    private float mHeight = 0;

    public ShowMaxImageView(Context context) {
        super(context);
    }

    public ShowMaxImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ShowMaxImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        if (bm != null) {
            getHeight(bm);
        }
        super.setImageBitmap(bm);
        requestLayout();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        if (drawable != null) {
            getHeight(drawableToBitamp(drawable));
        }
        super.setImageDrawable(drawable);
        requestLayout();
    }

    private Bitmap drawableToBitamp(Drawable drawable) {
        if (drawable != null) {
            BitmapDrawable bitmap = (BitmapDrawable) drawable;
            return bitmap.getBitmap();
        }
        return null;
    }

    private void getHeight(Bitmap bm) {
        int bmWidth = bm.getWidth();
        int bmHeight = bm.getHeight();
        if (bmWidth > 0 && bmHeight > 0) {
            int scaleWidth = getWidth() / bmWidth;
            mHeight = bmHeight * scaleWidth;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mHeight != 0) {
            int widthSize = MeasureSpec.getSize(widthMeasureSpec);
            int heightSize = MeasureSpec.getSize(heightMeasureSpec);
            int resultHeight = (int) Math.max(heightSize, mHeight);
            Activity activity = (Activity) getContext();
            int screenHeight = activity.getWindowManager().getDefaultDisplay().getHeight();
            if (resultHeight > screenHeight) {
                resultHeight = screenHeight / 3;
            }
            setMeasuredDimension(widthSize, resultHeight);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }


    }
}
