package blog.groundhao.com.mygroundhao.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import blog.groundhao.com.mygroundhao.model.NoThingInfo;

/**
 * 妹子
 * Created by user on 2016/4/8.
 */
public class GrilFragment extends NothingFragment{
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        pictureType = NoThingInfo.PictureType.GIRL;
    }
}
