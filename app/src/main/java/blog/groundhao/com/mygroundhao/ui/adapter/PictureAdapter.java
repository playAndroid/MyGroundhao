package blog.groundhao.com.mygroundhao.ui.adapter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import blog.groundhao.com.mygroundhao.R;
import blog.groundhao.com.mygroundhao.callback.LoadFinishListener;
import blog.groundhao.com.mygroundhao.callback.LoadingSuccessListener;
import blog.groundhao.com.mygroundhao.engine.CommonString;
import blog.groundhao.com.mygroundhao.model.Comments;
import blog.groundhao.com.mygroundhao.model.NoThingInfo;
import blog.groundhao.com.mygroundhao.model.ResponseInfo;
import blog.groundhao.com.mygroundhao.ui.itemactivity.CommentCountAcitivity;
import blog.groundhao.com.mygroundhao.ui.itemactivity.PictureDetailActivity;
import blog.groundhao.com.mygroundhao.utils.NetWorkUtils;
import blog.groundhao.com.mygroundhao.utils.ShareUtils;
import blog.groundhao.com.mygroundhao.utils.ShowToastUtils;
import blog.groundhao.com.mygroundhao.utils.TimeUtils;
import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * 新鲜事adpater
 * Created by user on 2016/4/8.
 */
public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.ViewHolder> implements CommonString {

    private final Context context;
    private int page;
    private final ArrayList<Comments> pictureLists;
    private LoadFinishListener loadFinishListener;
    private NoThingInfo newsThingInfo;
    private GenericDraweeHierarchy hierarchy;
    private NoThingInfo.PictureType pictureType;
    private String picture;
    private LoadingSuccessListener loadSuccessListener;


    public PictureAdapter(Context context, NoThingInfo.PictureType pictureType) {
        this.context = context;
        pictureLists = new ArrayList<>();
        this.pictureType = pictureType;
    }

    @Override
    public PictureAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notthing, parent, false);
        return new ViewHolder(view);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(PictureAdapter.ViewHolder holder, int position) {
        final Comments comments = pictureLists.get(position);
        String uri = comments.getPics()[0];
        Uri imageUri = Uri.parse(uri);
        holder.image_gif.setVisibility(uri.endsWith(".gif") ? View.VISIBLE : View.GONE);
        if (hierarchy == null) {
            //对布局和本地资源进行操控?
            hierarchy = new GenericDraweeHierarchyBuilder(context.getResources())
                    .build();
        } else {
            hierarchy = holder.image_icon.getHierarchy();
        }
        hierarchy.setProgressBarImage(new ProgressBarDrawable());
        holder.image_icon.setHierarchy(hierarchy);
        holder.image_icon.setImageURI(imageUri);
//        Glide.with(context).load(uri).into(holder.image_icon);
        holder.tv_author.setText(comments.getComment_author());
        holder.tv_speck.setText("吐槽 " + comments.getCommentCount());
        holder.tv_time.setText(TimeUtils.dateStringFormatGoodExperienceDate(comments.getComment_date()));
        holder.tv_oo.setText("OO " + comments.getVote_positive());
        holder.tv_xx.setText("XX " + comments.getVote_negative());

        holder.img_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(R.drawable.ic_launcher);
                builder.setInverseBackgroundForced(true);
                builder.setItems(new String[]{"分享", "保存"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                //分享
                                ShowToastUtils.Short("分享");
                                ShareUtils.sharePicture((Activity) context, comments.getPics()[0]);
                                break;
                            case 1:
                                //保存
                                ShowToastUtils.Short("保存");
                                break;
                        }
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转图片详情页
                Intent intent = new Intent(context, PictureDetailActivity.class);
                intent.putExtra(DATA_NEWSTHING, comments);
                context.startActivity(intent);
            }
        });

        holder.tv_speck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CommentCountAcitivity.class);
                intent.putExtra(THEARD_KEY, "comment-" + comments.getComment_ID());
                intent.putExtra(IS_FROM_NEWSTHING, false);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pictureLists.size();
    }

    public void loadFirst() {
        pictureLists.clear();
        page = 1;
        loadDataFromServer();
    }

    public void loadNextData() {
        page++;
        loadDataFromServer();
    }

    private void loadDataFromServer() {
        if (NetWorkUtils.isNetWorkConnected(context)) {
            if (pictureType == NoThingInfo.PictureType.NOTHING) {
                picture = NoThingInfo.URL_BORING_PICTURE;
            } else if (pictureType == NoThingInfo.PictureType.GIRL) {
                picture = NoThingInfo.URL_SISTER;
            }
            Logger.e("url:gril:" + picture + page);
            OkHttpUtils.get().url(picture + page).build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e) {
                    if (loadSuccessListener != null) {
                        loadSuccessListener.onFaliListener();
                    }
                }

                @Override
                public void onResponse(String response) {
                    newsThingInfo = new Gson().fromJson(response, NoThingInfo.class);
                    getCountComment(newsThingInfo);

                }
            });
        } else {
            ShowToastUtils.Short("网络异常");
        }
    }

    private void getCountComment(NoThingInfo newsThingInfo) {
        if (newsThingInfo == null) {
            return;
        }
        final List<Comments> comments = newsThingInfo.getComments();
        final StringBuffer sb = new StringBuffer();
        for (Comments comment : comments) {
            sb.append("comment-" + comment.getComment_ID() + ",");
        }
        Logger.e("url:评论数:" + ResponseInfo.getUrlCommentCounts(sb.toString()));
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
                            JSONObject jsonObject = null;
                            jsonObject = new JSONObject(response).optJSONObject("response");
                            String s = sb.toString();
                            String[] split = s.split("\\,");
                            for (String comment_ID : split) {
                                if (!jsonObject.isNull(comment_ID)) {
                                    ResponseInfo callback = new ResponseInfo();
                                    callback.setComments(jsonObject.getJSONObject(comment_ID).optInt(ResponseInfo.COMMENTS));
                                    callback.setThread_id(jsonObject.getJSONObject(comment_ID).optString(ResponseInfo.THREAD_ID));
                                    callback.setThread_key(jsonObject.getJSONObject(comment_ID).optString(ResponseInfo.THREAD_KEY));
                                    commentNumbers.add(callback);
                                } else {
                                    //可能会出现没有对应id的数据的情况，为了保证条数一致，添加默认数据
                                    commentNumbers.add(new ResponseInfo("0", "0", 0));
                                }
                            }
                        } catch (JSONException e) {
                            //TODO Json会出现解析异常
                            e.printStackTrace();
//                                AppManager.getInstance().exitApp(context);
                            Logger.e("---------------------->" + "JSon解析出错");
                            if (loadSuccessListener != null) {
                                loadSuccessListener.onFaliListener();
                            }
                            return;
                        }
                        Logger.e("添加长度:" + comments.size() + "commentNumbers长度" + commentNumbers.size());


                        for (int i = 0; i < comments.size(); i++) {
                            comments.get(i).setCommentCount(commentNumbers.get(i).getComments());
                        }

                        Logger.e("添加长度:" + comments.size() + "commentNumbers长度" + commentNumbers.size());
                        pictureLists.addAll(comments);
                        notifyDataSetChanged();
                        if (loadFinishListener != null) {
                            loadFinishListener.finishDataFormServer();
                        }
                        if (loadSuccessListener != null) {
                            loadSuccessListener.onSuccessListener();
                        }
                    }
                });
    }

    public void setLoadFinishListener(LoadFinishListener loadFinishListener) {
        this.loadFinishListener = loadFinishListener;
    }

    public void setLoadSuccessListener(LoadingSuccessListener loadSuccessListener) {
        this.loadSuccessListener = loadSuccessListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.card)
        CardView cardView;
        @Bind(R.id.tv_author)
        TextView tv_author;
        @Bind(R.id.tv_time)
        TextView tv_time;
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
        @Bind(R.id.image_gif)
        ImageView image_gif;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
