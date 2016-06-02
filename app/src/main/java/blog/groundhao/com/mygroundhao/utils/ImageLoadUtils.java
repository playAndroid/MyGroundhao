package blog.groundhao.com.mygroundhao.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.orhanobut.logger.Logger;

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

    /**
     * 加载GIF图片
     *
     * @param context
     * @param url
     * @param imageView
     */
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

    /**
     * 加载缩略图
     *
     * @param context
     * @param url
     * @param imageView
     * @param thumbnail
     */
    public static void loadImageForthumbnail(Context context, String url, ImageView imageView, float thumbnail) {
        Glide.with(context).load(url).listener(requestListener).thumbnail(thumbnail).into(imageView);
    }

    private static RequestListener<String, GlideDrawable> requestListener = new RequestListener<String, GlideDrawable>() {
        @Override
        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
            // todo log exception

            // important to return false so the error placeholder can be placed
            //处理错误日志
            Logger.e("glide onException");
            return false;
        }

        @Override
        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
            return false;
        }
    };

}
