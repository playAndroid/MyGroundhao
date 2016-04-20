package blog.groundhao.com.mygroundhao.ui.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.orhanobut.logger.Logger;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import blog.groundhao.com.mygroundhao.R;
import blog.groundhao.com.mygroundhao.callback.LoadFinishListener;
import blog.groundhao.com.mygroundhao.callback.LoadingSuccessListener;
import blog.groundhao.com.mygroundhao.model.ResponseInfo;
import blog.groundhao.com.mygroundhao.model.Video;
import blog.groundhao.com.mygroundhao.model.VideoInfo;
import blog.groundhao.com.mygroundhao.utils.NetWorkUtils;
import blog.groundhao.com.mygroundhao.utils.ShowToastUtils;
import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * 小视屏adpater
 * Created by user on 2016/4/8.
 */
public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    private final Context context;
    private int page;
    private final ArrayList<Video> videoLists;
    private LoadFinishListener loadFinishListener;
    private LoadingSuccessListener loadSuccessListener;


    public VideoAdapter(Context context) {
        this.context = context;
        videoLists = new ArrayList<>();
    }

    @Override
    public VideoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_video, parent, false);
        return new ViewHolder(view);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(VideoAdapter.ViewHolder holder, int position) {
        Video video = videoLists.get(position);
        Uri imageUri = Uri.parse(video.getImgUrl());
        holder.image_icon.setImageURI(imageUri);
        holder.tv_speck.setText("吐槽 " + video.getComment_count());
        holder.tv_oo.setText("OO " + video.getVote_positive());
        holder.tv_xx.setText("XX " + video.getVote_negative());
        holder.tv_title.setText(video.getTitle());
    }

    @Override
    public int getItemCount() {
        return videoLists.size();
    }

    public void loadFirst() {
        videoLists.clear();
        page = 1;
        loadDataFromServer();
    }

    public void loadNextData() {
        page++;
        loadDataFromServer();
    }

    private void loadDataFromServer() {
        if (NetWorkUtils.isNetWorkConnected(context)) {
            Logger.e("url:video:" + VideoInfo.URL_VIDEOS + page);
            OkHttpUtils.get().url(VideoInfo.URL_VIDEOS + page).build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e) {
                    if (loadSuccessListener != null) {
                        loadSuccessListener.onFaliListener();
                    }
                }

                @Override
                public void onResponse(String response) {
                    List<Video> dataFromJson = getDataFromJson(response);
                    getCountComment(dataFromJson);
                }
            });
        } else {
            ShowToastUtils.Short("网络异常");
        }
    }

    private void getCountComment(final List<Video> dataFromJson) {
        if (dataFromJson == null) {
            return;
        }
        final StringBuffer sb = new StringBuffer();
        for (Video video : dataFromJson) {
            sb.append("comment-" + video.getComment_ID() + ",");
        }
        OkHttpUtils.get().url(
                ResponseInfo.getUrlCommentCounts(sb.toString()))
                .build().execute(
                new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        ArrayList<Video> videos = new ArrayList<Video>();
                        try {
                            JSONObject jsonObject = new JSONObject(response).getJSONObject("response");
                            String[] split = sb.toString().split("\\,");
                            for (String comment_ID : split) {
                                if (!jsonObject.isNull(comment_ID)) {
                                    Video videoItem = new Video();
                                    videoItem.setComment_count(jsonObject.getJSONObject(comment_ID).getString(ResponseInfo.COMMENTS));
                                    videos.add(videoItem);
                                } else {
                                    Video videoItem = new Video();
                                    videoItem.setComment_count("0");
                                    videos.add(videoItem);
                                }
                            }

                            for (int i = 0; i < dataFromJson.size(); i++) {
                                dataFromJson.get(i).setComment_count(videos.get(i).getComment_count());
                            }
                            videoLists.addAll(dataFromJson);
                            notifyDataSetChanged();
                            if (loadFinishListener != null) {
                                loadFinishListener.finishDataFormServer();
                            }
                            if (loadSuccessListener != null) {
                                loadSuccessListener.onSuccessListener();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            if (loadSuccessListener != null) {
                                loadSuccessListener.onFaliListener();
                            }
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

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.image_icon)
        SimpleDraweeView image_icon;
        @Bind(R.id.tv_oo)
        TextView tv_oo;
        @Bind(R.id.tv_xx)
        TextView tv_xx;
        @Bind(R.id.tv_speck)
        TextView tv_speck;
        @Bind(R.id.img_share)
        ImageView img_share;
        @Bind(R.id.tv_title)
        TextView tv_title;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private List<Video> getDataFromJson(String string) {
        try {
            JSONObject jsonObject = new JSONObject(string);
            if ("ok".equals(jsonObject.optString("status"))) {

                JSONArray commentsArray = jsonObject.optJSONArray("comments");
                ArrayList<Video> videos = new ArrayList<>();

                for (int i = 0; i < commentsArray.length(); i++) {

                    JSONObject commentObject = commentsArray.getJSONObject(i);
                    JSONObject videoObject = commentObject.optJSONArray("videos").optJSONObject(0);

                    if (videoObject != null) {
                        Video video = new Video();
                        video.setTitle(videoObject.optString("title"));
                        String videoSource = videoObject.optString("video_source");
                        video.setComment_ID(commentObject.optString("comment_ID"));
                        video.setVote_positive(commentObject.optString("vote_positive"));
                        video.setVote_negative(commentObject.optString("vote_negative"));
                        video.setVideo_source(videoSource);

                        if (videoSource.equals("youku")) {
                            video.setUrl(videoObject.optString("link"));
                            video.setDesc(videoObject.optString("description"));
                            video.setImgUrl(videoObject.optString("thumbnail"));
                            video.setImgUrl4Big(videoObject.optString("thumbnail_v2"));
                        } else if (videoSource.equals("56")) {
                            video.setUrl(videoObject.optString("url"));
                            video.setDesc(videoObject.optString("desc"));
                            video.setImgUrl4Big(videoObject.optString("img"));
                            video.setImgUrl(videoObject.optString("mimg"));
                        } else if (videoSource.equals("tudou")) {
                            video.setUrl(videoObject.optString("playUrl"));
                            video.setImgUrl(videoObject.optString("picUrl"));
                            video.setImgUrl4Big(videoObject.optString("picUrl"));
                            video.setDesc(videoObject.optString("description"));
                        }

                        videos.add(video);
                    }
                }

                return videos;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
