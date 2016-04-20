package blog.groundhao.com.mygroundhao.utils;

import android.widget.Toast;

import blog.groundhao.com.mygroundhao.engine.GroundHaoApplication;

/**
 * Toast工具类 快捷弹出
 * Created by user on 2016/1/21.
 */
public class ShowToastUtils {

    private static Toast toast;

    public static void Short(CharSequence charSequence) {
        if (toast == null) {
            toast = Toast.makeText(GroundHaoApplication.getContext(), charSequence, Toast.LENGTH_SHORT);
        }
        toast.setText(charSequence);
        toast.show();
    }

    public static void Long(CharSequence charSequence) {
        if (toast == null) {
            toast = Toast.makeText(GroundHaoApplication.getContext(), charSequence, Toast.LENGTH_LONG);
        }
        toast.setText(charSequence);
        toast.show();
    }
}
