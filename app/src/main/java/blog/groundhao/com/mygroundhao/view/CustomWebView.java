package blog.groundhao.com.mygroundhao.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * @author hlk
 *         Created by hlk on 2016/3/9.
 */
public class CustomWebView extends WebView {

    public static final String ENCODING_UTF_8 = "UTF-8";

    public CustomWebView(Context context) {
        super(context);
        init();
    }


    public CustomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {

        /**
         * 如果在自定义控件的构造函数或者其他绘制相关地方使用系统依赖的代码，
         * 会导致可视化编辑器无法报错并提示：Use View.isInEditMode() in your custom views to skip code when shown in Eclipse
         */
        if (isInEditMode()) {
            return;
        }

        WebSettings settings = getSettings();
        settings.setJavaScriptEnabled(true); //是否支持js
        /**
         1>outsideInset :  该ScrollBar显示在视图(view)的边缘,增加了view的padding. 如果可能的话,该ScrollBar仅仅覆盖这个view的背景.
         2>outsideOverlay :  该ScrollBar显示在视图(view)的边缘,不增加view的padding,该ScrollBar将被半透明覆盖
         3>insideInset :该ScrollBar显示在padding区域里面,增加了控件的padding区域,该ScrollBar不会和视图的内容重叠.
         4>insideOverlay : 该ScrollBar显示在内容区域里面,不会增加了控件的padding区域,该ScrollBar以半透明的样式覆盖在视图(view)的内容上.
         */
        setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);//有滚动条
        setScrollbarFadingEnabled(true);//定义滚动条是否会褪色时,视图不滚动。
        requestFocus();
        setDrawingCacheEnabled(true);  //启用或禁用绘图缓存。


        settings.setBuiltInZoomControls(false);
        //设置缓存模式
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //开启DOM storage API功能
        settings.setDomStorageEnabled(true);
        //开启database storage 功能
        settings.setDatabaseEnabled(true);

        String cacheDir = getContext().getFilesDir().getAbsolutePath() + "web_cache";
        settings.setAppCachePath(cacheDir);
        settings.setAppCacheEnabled(true);

        settings.setLoadsImagesAutomatically(true);//支持自动加载图片
        settings.setDefaultTextEncodingName(ENCODING_UTF_8);
        settings.setBlockNetworkImage(false);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//支持内容从新布局
        settings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        settings.setLoadWithOverviewMode(true);
        setHorizontalScrollBarEnabled(false);//横向滚动条
    }
    /**
     *  使用webview一定要处理返回键
     *  处理返回键是否返回上一个页面
     7	public boolean onKeyDown(int keyCode, KeyEvent event) {
     8	        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
     9	            mWebView.goBack();
     10	                   return true;
     11	        }
     12	        return super.onKeyDown(keyCode, event);
     13	    }

     *  6.WebView 加载界面主要调用三个方法：LoadUrl、LoadData、LoadDataWithBaseURL.
     　　1、LoadUrl            直接加载网页、图片并显示.（本地或是网络上的网页、图片、gif）
     　　2、LoadData           显示文字与图片内容 （模拟器1.5、1.6）
     　　3、LoadDataWithBase  显示文字与图片内容（支持多个模拟器版本）
     */
    /**
     *
     WebSettings 的常用方法介绍
     * 14		setJavaScriptEnabled(true);
     //支持js脚步
     15		setPluginsEnabled(true);
     //支持插件
     16		setUseWideViewPort(false);
     //将图片调整到适合webview的大小
     17		setSupportZoom(true);
     //支持缩放
     18		setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
     //支持内容从新布局
     19		supportMultipleWindows();
     //多窗口
     20		setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
     //关闭webview中缓存
     21		setAllowFileAccess(true);
     //设置可以访问文件
     22		setNeedInitialFocus(true);
     //当webview调用requestFocus时为webview设置节点
     webSettings.setBuiltInZoomControls(true);
     //设置支持缩放
     23		setJavaScriptCanOpenWindowsAutomatically(true);
     //支持通过JS打开新窗口
     24		setLoadsImagesAutomatically(true);
     //支持自动加载图片
     */
    /**
     * WebViewClient 的方法全解
     *
     25	doUpdateVisitedHistory(WebView view, String url, boolean isReload)
     (更新历史记录)

     26	onFormResubmission(WebView view, Message dontResend, Message resend)
     (应用程序重新请求网页数据)

     27	onLoadResource(WebView view, String url)
     在加载页面资源时会调用，每一个资源（比如图片）的加载都会调用一次。

     28	onPageStarted(WebView view, String url, Bitmap favicon)
     这个事件就是开始载入页面调用的，通常我们可以在这设定一个loading的页面，告
     诉用户程序在等待网络响应。

     29	onPageFinished(WebView view, String url)
     在页面加载结束时调用。同样道理，我们知道一个页面载入完成，于是我们可以关
     闭loading 条，切换程序动作。

     30	onReceivedError(WebView view, int errorCode, String description, String failingUrl)
     (报告错误信息)

     31	onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host,
     String realm)（获取返回信息授权请求）

     32	onReceivedSslError(WebView view, SslErrorHandler handler, SslError error)
     重写此方法可以让webview处理https请求。

     33	onScaleChanged(WebView view, float oldScale, float newScale)
     (WebView发生改变时调用)

     34	onUnhandledKeyEvent(WebView view, KeyEvent event)
     （Key事件未被加载时调用）

     35	shouldOverrideKeyEvent(WebView view, KeyEvent event)
     重写此方法才能够处理在浏览器中的按键事件。

     36	shouldOverrideUrlLoading(WebView view, String url)
     在点击请求的是链接是才会调用，重写此方法返回true表明点击网页里面的链接还是在当前的webview里跳转，
     不跳到浏览器那边。这个函数我们可以做很多操作，比如我们读取到某些特殊的URL，于是就可以不打开地址，
     取消这个操作，进行预先定义的其他操作，这对一个程序是非常必要的。
     */
}
