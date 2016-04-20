package blog.groundhao.com.mygroundhao.model;

import android.support.v4.app.Fragment;

/**
 * Created by user on 2016/4/7.
 */
public class MenuItem {

    private String title;
    private int iconId;
    private FragmentType fragmentType;

    private Class<? extends Fragment> fragment;

    public enum FragmentType {
        NEWSTHING, NOTHING, PICEC, VIDEO, GRIL
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public FragmentType getFragmentType() {
        return fragmentType;
    }

    public void setFragmentType(FragmentType fragmentType) {
        this.fragmentType = fragmentType;
    }

    public MenuItem(String title, int iconId, FragmentType fragmentType) {
        this.title = title;
        this.iconId = iconId;
        this.fragmentType = fragmentType;
    }

    public MenuItem(String title, int iconId, FragmentType fragmentType, Class<? extends Fragment> fragment) {
        this.title = title;
        this.iconId = iconId;
        this.fragmentType = fragmentType;
        this.fragment = fragment;
    }

    public Class<? extends Fragment> getFragment() {

        return fragment;
    }

    public void setFragment(Class<? extends Fragment> fragment) {
        this.fragment = fragment;
    }
}
