package blog.groundhao.com.mygroundhao.engine;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig;
import com.facebook.stetho.Stetho;
import com.orhanobut.logger.Logger;
import com.zhy.http.okhttp.OkHttpUtils;

import blog.groundhao.com.mygroundhao.DaoMaster;
import blog.groundhao.com.mygroundhao.DaoSession;
import blog.groundhao.com.mygroundhao.cache.BaseCache;

/**
 * Created by user on 2016/3/9.
 */
public class GroundHaoApplication extends Application {
    private static final String TAG = "hlk";

    private static Context context;
    private static GroundHaoApplication mApplication;
    private static DaoMaster daoMaster;
    private static DaoSession daoSession;

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
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                        .build());
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


    public static DaoMaster getDaoMaster(Context context) {
        if (daoMaster == null) {
            DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(context, BaseCache.DB_NAME, null);
            daoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        }
        return daoMaster;

    }

    public static DaoSession getDaoSession(Context context) {
        if (daoSession == null) {
            if (daoMaster == null) {
                daoMaster = getDaoMaster(context);
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }
}
