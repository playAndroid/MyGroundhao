package blog.groundhao.com.mygroundhao.ui.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import blog.groundhao.com.mygroundhao.R;
import blog.groundhao.com.mygroundhao.callback.LoadFinishListener;
import blog.groundhao.com.mygroundhao.callback.LoadingSuccessListener;
import blog.groundhao.com.mygroundhao.model.CommentNewsthingInfo;
import blog.groundhao.com.mygroundhao.model.Commentator;
import blog.groundhao.com.mygroundhao.utils.JSONParser;
import blog.groundhao.com.mygroundhao.utils.ShowToastUtils;
import blog.groundhao.com.mygroundhao.utils.TimeUtils;
import blog.groundhao.com.mygroundhao.view.floorview.FloorView;
import blog.groundhao.com.mygroundhao.view.floorview.SubComments;
import blog.groundhao.com.mygroundhao.view.floorview.SubFloorFactory;
import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by user on 2016/4/20.
 */
public class CommentCountAdapter extends RecyclerView.Adapter<CommentCountAdapter.ViewHolder> {


    private final Context context;
    private final String id;
    private final boolean isFromNewsThing;
    ArrayList<CommentNewsthingInfo> commentsList;
    private LoadFinishListener loadFinishListener;
    private LoadingSuccessListener loadingSuccessListener;

    public CommentCountAdapter(Context context, String id, boolean isFromNewsThing) {
        this.context = context;
        this.id = id;
        this.isFromNewsThing = isFromNewsThing;
        commentsList = new ArrayList<>();
    }


    @Override
    public CommentCountAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case Commentator.TYPE_HOT:
            case Commentator.TYPE_NEW:
                return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_comment_flag, parent, false));
            case Commentator.TYPE_NORMAL:
                return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_comments_list, parent, false));
            default:
                return null;
        }


    }

    @Override
    public void onBindViewHolder(CommentCountAdapter.ViewHolder holder, int position) {
        CommentNewsthingInfo commentNewsthingInfo = commentsList.get(position);
        switch (commentNewsthingInfo.getType()) {
            case CommentNewsthingInfo.TYPE_HOT:
                holder.tv_flag.setText("热门评论");
                break;
            case CommentNewsthingInfo.TYPE_NEW:
                holder.tv_flag.setText("最新评论");
                break;
            case Commentator.TYPE_NORMAL:
                holder.tv_name.setText(commentNewsthingInfo.getName());


                if (isFromNewsThing) {
                    String url = commentNewsthingInfo.getUrl();
                    Uri uri = Uri.parse(url);
                    holder.image_icon.setImageURI(uri);
                    holder.tv_time.setText(TimeUtils.dateStringFormatGoodExperienceDate(commentNewsthingInfo.getDate()));
                    holder.tv_content.setText(getOnlyContent(commentNewsthingInfo.getContent()));
                } else {

                }

                if (commentNewsthingInfo.getFloorNum() > 1) {
                    SubComments subComments;
//                    if (isFromNewsThing) {
                    subComments = new SubComments(commentNewsthingInfo.getParentComments());
//                    } else {
//
//                    }

                    holder.floorView.setComments(subComments);
                    holder.floorView.setFactory(new SubFloorFactory());
                    holder.floorView.setBoundDrawer(context.getResources().getDrawable(
                            R.drawable.bg_comment));
                    holder.floorView.init();
                } else {
                    holder.floorView.setVisibility(View.GONE);
                }


                break;
        }
    }


    public void setLoadFinishListener(LoadFinishListener loadFinishListener) {
        this.loadFinishListener = loadFinishListener;
    }

    public void setLoadingSuccessListener(LoadingSuccessListener loadingSuccessListener) {
        this.loadingSuccessListener = loadingSuccessListener;
    }


    @Override
    public int getItemViewType(int position) {

        return commentsList.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return commentsList.size();
    }


    public void loadDataFrom() {
        Logger.e("url-----loadDataFrom:" + Commentator.getUrlCommentList(id));
        OkHttpUtils.get().url(Commentator.getUrlCommentList(id)).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {

            }
        });
    }

    public void load4Newsthing() {
        Logger.e("url------------>:" + CommentNewsthingInfo.getUrlComments(id));
        OkHttpUtils.get().url(CommentNewsthingInfo.getUrlComments(id)).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                if (loadingSuccessListener != null) {
                    loadingSuccessListener.onFaliListener();
                }
            }

            @Override
            public void onResponse(String response) {

                ArrayList<CommentNewsthingInfo> dataFromServer = getDataFromServer(response);
                if (dataFromServer == null) {
                    return;
                } else {
                    if (dataFromServer.size() == 0) {
                        ShowToastUtils.Short("暂无评论");
                    } else {
                        commentsList.clear();
                        if (dataFromServer.size() > 6) {
                            CommentNewsthingInfo commentNewsthingInfo = new CommentNewsthingInfo();
                            commentNewsthingInfo.setType(Commentator.TYPE_HOT);
                            commentsList.add(commentNewsthingInfo);
                            Collections.sort(dataFromServer, new Comparator<CommentNewsthingInfo>() {
                                @Override
                                public int compare(CommentNewsthingInfo lhs, CommentNewsthingInfo rhs) {
                                    return lhs.getVote_positive() <= rhs.getVote_positive() ? 1 : -1;
                                }
                            });
                            List<CommentNewsthingInfo> subList = dataFromServer.subList(0, 6);
                            for (CommentNewsthingInfo info : subList) {
                                info.setTag(info.TAG_HOT);
                            }
                            commentsList.addAll(subList);
                        }

                        CommentNewsthingInfo newsthingInfo = new CommentNewsthingInfo();
                        newsthingInfo.setType(newsthingInfo.TYPE_NEW);
                        commentsList.add(newsthingInfo);

                        Collections.sort(dataFromServer);
                        for (CommentNewsthingInfo infos : dataFromServer) {
                            if (infos.getTag().equals(infos.TAG_NORMAL)) {
                                commentsList.add(infos);
                            }
                        }

                        notifyDataSetChanged();
                        if (loadFinishListener != null) {
                            loadFinishListener.finishDataFormServer();
                        }
                        if (loadingSuccessListener != null) {
                            loadingSuccessListener.onSuccessListener();
                        }
                    }
                }
            }
        });
    }

    private ArrayList<CommentNewsthingInfo> getDataFromServer(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            String string = jsonObject.optString("status");
            if (string.equals("ok")) {
                String commentsString = jsonObject.optJSONObject("post").optJSONArray("comments").toString();
                Logger.e("评论内容:" + commentsString);
                ArrayList<CommentNewsthingInfo> parentComments = (ArrayList<CommentNewsthingInfo>) JSONParser.toObject(commentsString, new TypeToken<ArrayList<CommentNewsthingInfo>>() {
                }.getType());

                Pattern pattenrn = Pattern.compile("\\d{7}");//[0-9] 匹配7次
                for (CommentNewsthingInfo info : parentComments) {
                    Matcher matcher = pattenrn.matcher(info.getContent());
                    boolean isHas7Num = matcher.find();
                    boolean isHasComment = info.getContent().contains("#comment-");
                    if (isHas7Num && isHasComment || info.getParentId() != 0) {
                        ArrayList<CommentNewsthingInfo> tempCommentList = new ArrayList<>();
                        int parentId = getParentId(info.getContent());
                        info.setParentId(parentId);
                        getParentInfo(tempCommentList, parentComments, parentId);
                        Collections.reverse(tempCommentList);
                        info.setParentComments(tempCommentList);
                        info.setFloorNum(tempCommentList.size() + 1);
                        info.setContent(getContentParent(info.getContent()));
                    } else {
                        info.setContent(getContentParent(info.getContent()));
                    }
                }
                return parentComments;
            } else {
                ShowToastUtils.Short("status 不OK");
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void getParentInfo(ArrayList<CommentNewsthingInfo> tempCommentList,
                               ArrayList<CommentNewsthingInfo> parentComments,
                               int parentId) {
        for (CommentNewsthingInfo commentNewsthingInfo : parentComments) {
            if (commentNewsthingInfo.getId() != parentId) continue;
            //有父评论
            tempCommentList.add(commentNewsthingInfo);
            if (commentNewsthingInfo.getParentId() != 0 && commentNewsthingInfo.getParentComments() != null) {
                commentNewsthingInfo.setContent(getContentParent(commentNewsthingInfo.getContent()));
                tempCommentList.addAll(commentNewsthingInfo.getParentComments());
                return;
            }
            commentNewsthingInfo.setContent(getOnlyContent(commentNewsthingInfo.getContent()));
        }
    }

    private String getContentParent(String content) {
        if (content.contains("</a>:")) {
            return getOnlyContent(content).split("</a>:")[1];
        }
        return content;
    }

    private String getOnlyContent(String content) {
        content = content.replace("</p>", "").replace("<p>", "").replace("<br />", "");
        return content;
    }

    private int getParentId(String content) {
        try {
            int index = content.indexOf("comment-") + 8;
            int parseID = Integer.parseInt(content.substring(index, index + 7));
            return parseID;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Nullable
        @Bind(R.id.image_icon)
        SimpleDraweeView image_icon;
        @Nullable
        @Bind(R.id.tv_name)
        TextView tv_name;
        @Nullable
        @Bind(R.id.tv_time)
        TextView tv_time;
        @Nullable
        @Bind(R.id.tv_content)
        TextView tv_content;
        @Nullable
        @Bind(R.id.tv_hot)
        TextView tv_flag;
        @Nullable
        @Bind(R.id.floors_parent)
        FloorView floorView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            setIsRecyclable(false);//不复用
        }
    }
}
