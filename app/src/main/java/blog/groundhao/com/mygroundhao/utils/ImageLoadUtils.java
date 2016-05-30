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

    public static void loadImage(Context context, String url, ImageView imageView) {
        Glide.with(context).load(url).centerCrop()
                .placeholder(R.drawable.ic_loading_large)
                .crossFade().into(imageView);
    }

    public static void loadImageForGIF(Context context, String url, ImageView imageView){
        Glide.with(context).load(url).asBitmap().centerCrop()
                .placeholder(R.drawable.ic_loading_large)
                .into(imageView);
    }

}
