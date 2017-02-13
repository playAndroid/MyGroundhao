package blog.groundhao.com.mygroundhao.ui;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import blog.groundhao.com.mygroundhao.R;
import blog.groundhao.com.mygroundhao.engine.uibest.BestActivity;
import blog.groundhao.com.mygroundhao.model.NetEvent;
import blog.groundhao.com.mygroundhao.ui.fragment.ItemListFragment;
import blog.groundhao.com.mygroundhao.ui.fragment.NewsthingFragment;
import blog.groundhao.com.mygroundhao.utils.NetWorkUtils;
import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends BestActivity {

    @Bind(R.id.main_drawer_layout)
    DrawerLayout drawerLayout;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private BroadcastReceiver netWorkBroadcast;
    private AlertDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 注册默认的未捕捉异常处理类
//        Thread.setDefaultUncaughtExceptionHandler(AppException
//                .getAppExceptionHandler());
//        AppManager.getInstance().addActivity(this);
        initView();
        initData();
        replaceFragment(R.id.frage_content, new NewsthingFragment());
        replaceFragment(R.id.frage_list, new ItemListFragment());
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    private void initData() {
        netWorkBroadcast = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
//                Logger.e("onReceive");
                if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
//                    Logger.e("网络广播");
                    if (NetWorkUtils.isNetWorkConnected(context)) {
//                        Logger.e("网络可用");
                        EventBus.getDefault().post(new NetEvent(NetEvent.AVAILABLE));
                    } else {
//                        Logger.e("网络不可用");
                        EventBus.getDefault().post(new NetEvent(NetEvent.UNAVAILABLE));
                    }
                }
            }
        };

        registerReceiver(netWorkBroadcast, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventCheckNetWork(NetEvent netEvent) {
        if (netEvent.getType() == NetEvent.UNAVAILABLE) {
//            Logger.e("无网络连接");
            builder = new AlertDialog.Builder(this);
            builder.setTitle("无网络连接,是否开启?");
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                    startActivity(intent);
                }
            });
            builder.show();
        }
    }

    /**
     * 初始化布局
     */
    private void initView() {
        ButterKnife.bind(this);
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.first, R.string.second) {
            @Override
            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();//更新菜单
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();//更新菜单
            }
        };
        actionBarDrawerToggle.syncState();
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

    }


    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(netWorkBroadcast);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    public void closeDrawer() {
        drawerLayout.closeDrawers();
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Logger.e("onKeyDown");
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            dialog();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("确认要退出应用吗?");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.show();
    }

}
