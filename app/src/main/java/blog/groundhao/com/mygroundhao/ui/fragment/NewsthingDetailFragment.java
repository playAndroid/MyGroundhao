package blog.groundhao.com.mygroundhao.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.orhanobut.logger.Logger;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import blog.groundhao.com.mygroundhao.R;
import blog.groundhao.com.mygroundhao.model.NewsThingInfo;
import blog.groundhao.com.mygroundhao.model.PostsBean;
import blog.groundhao.com.mygroundhao.ui.itemactivity.CommentCountAcitivity;
import blog.groundhao.com.mygroundhao.utils.ShareUtils;
import blog.groundhao.com.mygroundhao.utils.TimeUtils;
import butterknife.Bind;
import butterknife.ButterKnife;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import okhttp3.Call;

/**
 * Created by user on 2016/4/18.
 */
public class NewsthingDetailFragment extends BaseFragment {

    @Bind(R.id.web_view)
    WebView web_view;
    @Bind(R.id.loading)
    CircularProgressBar loading;

    //    private NewsthingDetailFragment detailFragment;
    private PostsBean postsBean;

    public NewsthingDetailFragment() {
    }

    public static NewsthingDetailFragment getInstence(PostsBean postsBean) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(DATA_NEWSTHING, postsBean);
        NewsthingDetailFragment fragment = new NewsthingDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_news_detail, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        postsBean = (PostsBean) getArguments().getSerializable(DATA_NEWSTHING);
        web_view.getSettings().setJavaScriptEnabled(true);
//        web_view.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        web_view.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress > 80) {
                    loading.setVisibility(View.GONE);
                }
            }
        });
        Logger.e("url:" + NewsThingInfo.getUrlFreshNewsDetail(postsBean.getId() + ""));

        OkHttpUtils.get().url(NewsThingInfo.getUrlFreshNewsDetail(postsBean.getId() + "")).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                Logger.e("loadDataWithBaseURL", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.opt("status").equals("ok")) {
                        JSONObject content = jsonObject.getJSONObject("post");
                        String string = content.optString("content");
                        Logger.e("loadDataWithBaseURL--------->", getHtml(postsBean, string));
                        web_view.loadDataWithBaseURL("", getHtml(postsBean, string), "text/html", "utf-8", "");

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
        loading.setVisibility(View.VISIBLE);
        loading.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (loading.getVisibility() == View.VISIBLE) {
                    loading.setVisibility(View.GONE);
                }
            }
        }, 10 * 1000);
    }

    private static String getHtml(PostsBean postsBean, String content) {
        final StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>");
        sb.append("<html dir=\"ltr\" lang=\"zh\">");
        sb.append("<head>");
        sb.append("<meta name=\"viewport\" content=\"width=100%; initial-scale=1.0; maximum-scale=1.0; user-scalable=0;\" />");
        sb.append("<link rel=\"stylesheet\" href='file:///android_asset/style.css' type=\"text/css\" media=\"screen\" />");
        sb.append("</head>");
        sb.append("<body style=\"padding:0px 8px 8px 8px;\">");
        sb.append("<div id=\"pagewrapper\">");
        sb.append("<div id=\"mainwrapper\" class=\"clearfix\">");
        sb.append("<div id=\"maincontent\">");
        sb.append("<div class=\"post\">");
        sb.append("<div class=\"posthit\">");
        sb.append("<div class=\"postinfo\">");
        sb.append("<h2 class=\"thetitle\">");
        sb.append("<a>");
        sb.append(postsBean.getTitle());
        sb.append("</a>");
        sb.append("</h2>");
        sb.append(postsBean.getAuthor().getName() + " @ " + TimeUtils.dateStringFormatGoodExperienceDate(postsBean.getDate()));
        sb.append("</div>");
        sb.append("<div class=\"entry\">");
        sb.append(content);
        sb.append("</div>");
        sb.append("</div>");
        sb.append("</div>");
        sb.append("</div>");
        sb.append("</div>");
        sb.append("</div>");
        sb.append("</body>");
        sb.append("</html>");
        return sb.toString();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (web_view != null) {
            web_view.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (web_view != null) {
            web_view.onPause();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_news_detail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.action_chat:
                //跳转吐槽页
                Intent intent = new Intent(getActivity(), CommentCountAcitivity.class);
                intent.putExtra(DATA_NEWSTHING, postsBean);
                getActivity().startActivity(intent);
                break;
            case R.id.action_share:
                //分享 文字
                ShareUtils.shareText(getActivity(), "新鲜事详情页的分享哦:" + postsBean.getTitle() + postsBean.getUrl());
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
