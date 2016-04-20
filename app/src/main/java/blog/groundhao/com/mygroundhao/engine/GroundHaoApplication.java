package blog.groundhao.com.mygroundhao.engine;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig;
import com.orhanobut.logger.Logger;
import com.zhy.http.okhttp.OkHttpUtils;

/**
 * Created by user on 2016/3/9.
 */
public class GroundHaoApplication extends Application {
    private static final String TAG = "hlk";

    private static Context context;
    private static GroundHaoApplication mApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.init(TAG);
        context = this;
        OkHttpUtils.getInstance();
        //初始化并配置Fresco
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
                .setProgressiveJpegConfig(new SimpleProgressiveJpegConfig())
                .build();
        Fresco.initialize(this, config);
//        initData();
    }

    public synchronized static GroundHaoApplication getInstance() {
        return mApplication;
    }

    public static Context getContext() {
        return context;
    }

    private void initData() {
        //当程序发生Uncaught异常的时候,由该类来接管程序,一定要在这里初始化
//        CashHandler.getInstance().init(this);
    }
}
