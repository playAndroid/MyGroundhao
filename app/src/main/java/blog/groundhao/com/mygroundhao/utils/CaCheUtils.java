package blog.groundhao.com.mygroundhao.utils;

import java.io.File;
import java.util.Random;

/**
 * Created by user on 2016/4/15.
 */
public class CaCheUtils {

    /**
     * 获取保存图片的名称
     *
     * @param cacheFile
     * @param urls
     * @return
     */
    public static String getShareFileName(File cacheFile, String[] urls) {
        return cacheFile.getAbsolutePath() + new Random().nextInt(100000) + "." + urls[urls
                .length - 1];
    }
}
