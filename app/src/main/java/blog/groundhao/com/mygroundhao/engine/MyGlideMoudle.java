package blog.groundhao.com.mygroundhao.engine;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.module.GlideModule;
import com.bumptech.glide.request.target.ViewTarget;

import blog.groundhao.com.mygroundhao.R;

/**
 * Created by user on 2016/5/30.
 */
public class MyGlideMoudle implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        ViewTarget.setTagId(R.id.glide_tag_id);
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
