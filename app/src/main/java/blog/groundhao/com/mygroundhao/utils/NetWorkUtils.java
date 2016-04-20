package blog.groundhao.com.mygroundhao.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * Created by user on 2016/3/8.
 */
public class NetWorkUtils {
    /**
     * 判断当前网络是否已连接
     *
     * @param context
     * @return
     */
    public static boolean isNetWorkConnected(Context context) {
        boolean result;
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        result = netInfo != null && netInfo.isConnected();
        return result;
    }

    /**
     * 判断当前的网络连接方式是否为WIFI
     *
     * @param context
     * @return
     */
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return wifiNetworkInfo.isConnected();
    }

    /**
     * 获取网络连接类型
     *
     * @return -1表示没有网络
     */
    public static final int TYPE_WIFI = 0;
    public static final int TYPE_3G = 1;
    public static final int TYPE_GPRS = 2;

    public static final int getNetWorkType(Context c) {
        ConnectivityManager conn = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conn == null) {
            return -1;
        }
        NetworkInfo info = conn.getActiveNetworkInfo();
        if (info == null || !info.isAvailable()) {
            return -1;
        }

        int type = info.getType(); // MOBILE（GPRS）;WIFI
        if (type == ConnectivityManager.TYPE_WIFI) {
            return TYPE_WIFI;
        } else {
            TelephonyManager tm = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);
            switch (tm.getNetworkType()) {
                case TelephonyManager.NETWORK_TYPE_CDMA:
                    return TYPE_GPRS;
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    return TYPE_GPRS;
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    return TYPE_GPRS;
                default:
                    return TYPE_3G;
            }
        }
    }
}
