package blog.groundhao.com.mygroundhao.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import blog.groundhao.com.mygroundhao.R;
import blog.groundhao.com.mygroundhao.callback.LoadFinishListener;
import blog.groundhao.com.mygroundhao.callback.LoadingSuccessListener;
import blog.groundhao.com.mygroundhao.engine.CommonString;
import blog.groundhao.com.mygroundhao.model.Comments;
import blog.groundhao.com.mygroundhao.model.JokeInfo;
import blog.groundhao.com.mygroundhao.model.ResponseInfo;
import blog.groundhao.com.mygroundhao.ui.itemactivity.CommentCountAcitivity;
import blog.groundhao.com.mygroundhao.utils.NetWorkUtils;
import blog.groundhao.com.mygroundhao.utils.ShareUtils;
import blog.groundhao.com.mygroundhao.utils.ShowToastUtils;
import blog.groundhao.com.mygroundhao.utils.TimeUtils;
import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * 段子
 * Created by user on 2016/4/14.
 */
public class JokeAdapter extends RecyclerView.Adapter<JokeAdapter.ViewHolder> implements CommonString {


    private final Context context;
    private LoadFinishListener loadFinishListener;
    private LoadingSuccessListener loadSuccessListener;
    private int page;
    private final ArrayList<Comments> jokeInfos;

    public JokeAdapter(Context context) {
        this.context = context;
        jokeInfos = new ArrayList<>();
    }

    @Override
    public JokeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_joke, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(JokeAdapter.ViewHolder holder, int position) {
        final Comments comments = jokeInfos.get(position);
        holder.tv_author.setText(comments.getComment_author());
        holder.tv_oo.setText("OO " + comments.getVote_positive());
        holder.tv_xx.setText("XX " + comments.getVote_negative());
        holder.tv_joke.setText(comments.getComment_content());
        holder.tv_speck.setText("吐槽 " + comments.getCommentCount());
        holder.tv_time.setText(TimeUtils.dateStringFormatGoodExperienceDate(comments.getComment_date()));
        holder.img_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareUtils.shareText((Activity) context, "段子分享:" + comments.getComment_content());
            }
        });
        holder.tv_speck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CommentCountAcitivity.class);
                intent.putExtra(THEARD_KEY, "comment-" + comments.getComment_ID());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return jokeInfos.size();
    }

    public void loadFirst() {
        jokeInfos.clear();
        page = 1;
        loadDataFromServer();
    }

    public void loadNextData() {
        page++;
        loadDataFromServer();
    }

    private void loadDataFromServer() {
        if (NetWorkUtils.isNetWorkConnected(context)) {
            OkHttpUtils.get().url(JokeInfo.URL_JOKE + page).build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e) {
                    if (loadSuccessListener != null) {
                        loadSuccessListener.onFaliListener();
                    }
                }

                @Override
                public void onResponse(String response) {
                    JokeInfo jokeInfo = new Gson().fromJson(response, JokeInfo.class);
                    getCountComment(jokeInfo);
                }
            });
        } else {
            ShowToastUtils.Short("网络异常");
        }
    }

    private void getCountComment(JokeInfo jokeInfo) {
        if (jokeInfo == null) {
            return;
        }
        final List<Comments> comments = jokeInfo.getCommentses();
        final StringBuffer sb = new StringBuffer();
        for (Comments comment : comments) {
            sb.append("comment-" + comment.getComment_ID() + ",");
        }
        OkHttpUtils.get().url(
                ResponseInfo.getUrlCommentCounts(sb.toString()))
                .build().execute(
                new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        if (loadSuccessListener != null) {
                            loadSuccessListener.onFaliListener();
                        }
                    }

                    @Override
                    public void onResponse(String response) {
                        ArrayList<ResponseInfo> commentNumbers = new ArrayList<>();
                        try {
                            JSONObject jsonObject = new JSONObject(response).getJSONObject("response");
                            String s = sb.toString();
                            String[] split = s.split("\\,");
                            for (String comment_ID : split) {
                                Log.e("hlk", "comment_ID" + comment_ID);
                                if (!jsonObject.isNull(comment_ID)) {
                                    ResponseInfo callback = new ResponseInfo();
                                    callback.setComments(jsonObject.getJSONObject(comment_ID).getInt(ResponseInfo.COMMENTS));
                                    callback.setThread_id(jsonObject.getJSONObject(comment_ID).getString(ResponseInfo.THREAD_ID));
                                    callback.setThread_key(jsonObject.getJSONObject(comment_ID).getString(ResponseInfo.THREAD_KEY));
                                    commentNumbers.add(callback);
                                } else {
                                    //可能会出现没有对应id的数据的情况，为了保证条数一致，添加默认数据
                                    commentNumbers.add(new ResponseInfo("0", "0", 0));
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            if (loadSuccessListener != null) {
                                loadSuccessListener.onFaliListener();
                            }
                            return;
                        }
                        for (int i = 0; i < comments.size(); i++) {
                            comments.get(i).setCommentCount(commentNumbers.get(i).getComments());
                        }
                        jokeInfos.addAll(comments);
                        notifyDataSetChanged();
                        if (loadFinishListener != null) {
                            loadFinishListener.finishDataFormServer();
                        }
                        if (loadSuccessListener != null) {
                            loadSuccessListener.onSuccessListener();
                        }
                    }
                }

        );
    }

    public void setLoadFinishListener(LoadFinishListener loadFinishListener) {
        this.loadFinishListener = loadFinishListener;
    }

    public void setLoadSuccessListener(LoadingSuccessListener loadSuccessListener) {
        this.loadSuccessListener = loadSuccessListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_author)
        TextView tv_author;
        @Bind(R.id.tv_time)
        TextView tv_time;
        @Bind(R.id.tv_oo)
        TextView tv_oo;
        @Bind(R.id.tv_xx)
        TextView tv_xx;
        @Bind(R.id.tv_speck)
        TextView tv_speck;
        @Bind(R.id.img_share)
        ImageView img_share;
        @Bind(R.id.tv_joke)
        TextView tv_joke;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
