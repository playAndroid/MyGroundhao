package blog.groundhao.com.mygroundhao.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import blog.groundhao.com.mygroundhao.R;

/**
 * 图片加载工具类
 * Created by user on 2016/5/30.
 */
public class ImageLoadUtils {

    /**
     * 加载静态图片
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadImage(Context context, String url, ImageView imageView) {
        Glide.with(context).load(url).asBitmap().centerCrop().error(R.drawable.error)
                .placeholder(R.drawable.ic_loading_large)
                .into(imageView);//crossFade 淡入淡出
    }

    public static void loadImageForGIF(Context context, String url, ImageView imageView) {
        Glide.with(context).load(url).asGif().centerCrop().error(R.drawable.error)
                .placeholder(R.drawable.ic_loading_large).crossFade()
                .into(imageView);
    }

    public static void loadImageDef(Context context, String url, ImageView imageView) {
        Glide.with(context).load(url).into(imageView);
    }

    /**
     * 加载静态图片
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadImageForGIFforBitmap(Context context, String url, ImageView imageView) {
        Glide.with(context).load(url).asBitmap().centerCrop().error(R.drawable.error)
                .placeholder(R.drawable.ic_loading_large)
                .into(imageView);
    }

}
