package blog.groundhao.com.mygroundhao.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * Created by user on 2016/4/15.
 */
public class FileUtils {
    /**
     * 复制文件
     *
     * @param src 源文件
     * @param dst 目标文件
     * @return
     */
    public static boolean copyTo(File src, File dst) {
        FileInputStream fi = null;
        FileOutputStream fo = null;
        FileChannel in = null;
        FileChannel out = null;

        try {
            fi = new FileInputStream(src);
            fo = new FileOutputStream(dst);

            in = fi.getChannel();
            out = fo.getChannel();

            in.transferTo(0, in.size(), out);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (fi != null) {
                    fi.close();
                }
                if (fo != null) {
                    fo.close();
                }
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }
}
