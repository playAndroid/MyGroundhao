package blog.groundhao.com.mygroundhao.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.SimpleCacheKey;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.orhanobut.logger.Logger;

import java.io.File;

import blog.groundhao.com.mygroundhao.R;

/**
 * 分享工具类
 * Created by user on 2016/4/15.
 */
public class ShareUtils {
    public static void shareText(Activity acitivty, String shareString) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, shareString);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        acitivty.startActivity(Intent.createChooser(intent, acitivty.getResources().getString(R.string.app_name)));
    }

    public static void sharePicture(Activity activity, String url) {
        Uri uri = Uri.parse(url);
        String[] split = url.split("\\.");
        FileBinaryResource resource = (FileBinaryResource) Fresco.getImagePipelineFactory().getMainDiskStorageCache().getResource(new SimpleCacheKey(uri.toString()));
        File file = resource.getFile();
        if (!file.exists()) {
            Logger.e("!file.exists()");
//            String picUrl = uri.toString();
//            picUrl = picUrl.replace("mw600", "small").replace("mw1200", "small").replace
//                    ("large", "small");
//            file = ((FileBinaryResource) Fresco.getImagePipelineFactory().getMainDiskStorageCache().getResource(new SimpleCacheKey(picUrl))).getFile();
        }
        File newFile = new File(CaCheUtils.getShareFileName(file, split));
        if (FileUtils.copyTo(file, newFile)) {
            ShareUtils.sharePicture(activity, newFile.getAbsolutePath(), "分享自简单" + url);
        } else {
            ShowToastUtils.Short("图片分享失败");
        }
    }

    public static void sharePicture(Activity activity, String imageUri, String shareText) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        File f = new File(imageUri);
        if (f != null && f.exists() && f.isFile()) {
            Logger.e("f != null && f.exists() && f.isFile()"+f.getAbsolutePath());
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f));
        } else {
            ShowToastUtils.Short("分享的图片不存在");
            return;
        }
        if (imageUri.endsWith(".gif")) {
            intent.putExtra(Intent.EXTRA_TEXT, shareText);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(Intent.createChooser(intent, activity.getResources().getString(R.string.app_name)));
    }
}
