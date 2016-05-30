package blog.groundhao.com.mygroundhao.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import blog.groundhao.com.mygroundhao.R;
import blog.groundhao.com.mygroundhao.cache.NothingCache;
import blog.groundhao.com.mygroundhao.callback.LoadFinishListener;
import blog.groundhao.com.mygroundhao.callback.LoadingSuccessListener;
import blog.groundhao.com.mygroundhao.model.NewsThingInfo;
import blog.groundhao.com.mygroundhao.model.PostsBean;
import blog.groundhao.com.mygroundhao.ui.itemactivity.NewsThingDetailsActivity;
import blog.groundhao.com.mygroundhao.utils.ImageLoadUtils;
import blog.groundhao.com.mygroundhao.utils.NetWorkUtils;
import blog.groundhao.com.mygroundhao.utils.ShareUtils;
import blog.groundhao.com.mygroundhao.utils.ShowToastUtils;
import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * 新鲜事adpater
 * Created by user on 2016/4/8.
 */
public class NewsThingAdapter extends RecyclerView.Adapter<NewsThingAdapter.ViewHolder> {

    private final Context context;
    private int page;
    private final ArrayList<PostsBean> newsThingInfos;
    private LoadFinishListener loadFinishListener;
    private LoadingSuccessListener loadSuccessListener;
    //    private boolean isSave;
    private Uri uri;

    public NewsThingAdapter(Context context, LoadFinishListener loadFinishListener, LoadingSuccessListener loadSuccessListener) {
        this.context = context;
        newsThingInfos = new ArrayList<>();
        this.loadFinishListener = loadFinishListener;
        this.loadSuccessListener = loadSuccessListener;

    }

    @Override
    public NewsThingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = View.inflate(context, R.layout.newsthing_item, parent);
//        Fresco.initialize(context);
        View view = LayoutInflater.from(context).inflate(R.layout.newsthing_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsThingAdapter.ViewHolder holder, final int position) {
        final PostsBean newsThingInfo = newsThingInfos.get(position);
//        if (isSave) {
//            holder.tv_author.setText(newsThingInfo.getAuthor().getName() + "@");
//            holder.tv_look_num.setText("浏览" + newsThingInfo.getCustomFields().getViews() + "次");
//            holder.text_title.setText(newsThingInfo.getTitle());
//        } else {
        holder.tv_author.setText(newsThingInfo.getAuthor().getName() + "@" + newsThingInfo.getTags().get(0).getTitle());
        holder.tv_look_num.setText("浏览" + newsThingInfo.getComment_count() + "次");
        holder.text_title.setText(newsThingInfo.getTitle());
//        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toJumpActivity(position);
            }
        });
        holder.tv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareUtils.shareText((Activity) context, "哈哈,文字分享哦" + newsThingInfo.getTitle() + " " + newsThingInfo.getUrl());
            }
        });
        //设置JPG渐进式清晰度  废流量啊
//        DraweeController controller = Fresco.newDraweeControllerBuilder()
//                .setLowResImageRequest(ImageRequest.fromUri(newsThingInfo.getCustom_fields().getThumb_c().get(0)))
//                .setImageRequest(ImageRequest.fromUri(newsThingInfo.getCustom_fields().getThumb_c().get(0).replace("custom", "medium")))
//                .setOldController(holder.image_icon.getController())
//                .build();
//        holder.image_icon.setController(controller);
//        if (isSave) {
//            uri = Uri.parse(newsThingInfo.getCustomFields().getThumb_m().replace("custom", "medium"));
//        } else {
        String url = newsThingInfo.getCustom_fields().getThumb_c().get(0).replace("custom", "medium");
        uri = Uri.parse(url);
//        }

//        holder.image_icon.setImageURI(uri);
        ImageLoadUtils.loadImage(context,url,holder.image_icon);
    }

    private void toJumpActivity(int position) {
        Intent intent = new Intent(context, NewsThingDetailsActivity.class);
        intent.putExtra(NewsThingDetailsActivity.DATA_NEWSTHING, newsThingInfos);
        intent.putExtra(NewsThingDetailsActivity.DATA_NEWS_POSITION, position);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return newsThingInfos.size();
    }

    public void loadFirst() {

        page = 1;
        loadDataFromServer();
    }

    public void loadNextData() {
        page++;
        loadDataFromServer();
    }

    private void loadDataFromServer() {
        if (NetWorkUtils.isNetWorkConnected(context)) {
//            isSave = false;
            Logger.e("url:" + NewsThingInfo.URL_FRESH_NEWS + page);
            OkHttpUtils.get().url(NewsThingInfo.URL_FRESH_NEWS + page).build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e) {
                    if (loadSuccessListener != null) {
                        loadSuccessListener.onFaliListener();
                    }
                }

                @Override
                public void onResponse(String response) {
                    NewsThingInfo newsThingInfo = new Gson().fromJson(response, NewsThingInfo.class);
                    List<PostsBean> posts = newsThingInfo.getPosts();
                    if (!posts.isEmpty()) {
                        if (page == 1) {
                            newsThingInfos.clear();
                            NothingCache.getInstance(context).clearAllCache();
                        }
                        newsThingInfos.addAll(posts);
                        notifyDataSetChanged();
                        if (loadFinishListener != null) {
                            loadFinishListener.finishDataFormServer();
                        }
                        if (loadSuccessListener != null) {
                            loadSuccessListener.onSuccessListener();
                        }

                    }
                    NothingCache.getInstance(context).addResultCache(response, page);
                }
            });
        } else {
//            isSave = true;
            if (loadFinishListener != null) {
                loadFinishListener.finishDataFormServer();

            }
            if (loadSuccessListener != null) {
                loadSuccessListener.onSuccessListener();
            }
            if (page == 1) {
                newsThingInfos.clear();
            }
            newsThingInfos.addAll(NothingCache.getInstance(context).getCacheByPage(page));
            ShowToastUtils.Short("无网络，当前为缓存数据");
            notifyDataSetChanged();

        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.card)
        CardView cardView;
        @Bind(R.id.image_icon)
        ImageView image_icon;
        @Bind(R.id.text_title)
        TextView text_title;
        @Bind(R.id.tv_author)
        TextView tv_author;
        @Bind(R.id.tv_look_num)
        TextView tv_look_num;
        @Bind(R.id.tv_share)
        TextView tv_share;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
