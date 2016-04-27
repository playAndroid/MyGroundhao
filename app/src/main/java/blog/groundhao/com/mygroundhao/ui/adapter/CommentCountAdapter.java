package blog.groundhao.com.mygroundhao.ui.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import java.util.Arrays;
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
 * 评论适配器
 * Created by user on 2016/4/20.
 */
public class CommentCountAdapter extends RecyclerView.Adapter<CommentCountAdapter.ViewHolder> {


    private final Context context;
    private final String id;
    private final boolean isFromNewsThing;
    ArrayList<Commentator> commentatorArrayList;
    ArrayList<CommentNewsthingInfo> commentsList;
    private LoadFinishListener loadFinishListener;
    private LoadingSuccessListener loadingSuccessListener;

    public CommentCountAdapter(Context context, String id, boolean isFromNewsThing) {
        this.context = context;
        this.id = id;
        this.isFromNewsThing = isFromNewsThing;
        if (isFromNewsThing) {
            commentsList = new ArrayList<>();
        } else {
            commentatorArrayList = new ArrayList<>();
        }
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
        Commentator commentator;

        if (isFromNewsThing) {
            commentator = commentsList.get(position);
        } else {
            commentator = commentatorArrayList.get(position);
        }


        switch (commentator.getType()) {
            case Commentator.TYPE_HOT:
                holder.tv_flag.setText("热门评论");
                break;
            case Commentator.TYPE_NEW:
                holder.tv_flag.setText("最新评论");
                break;
            case Commentator.TYPE_NORMAL:

                if (isFromNewsThing) {
                    CommentNewsthingInfo info = (CommentNewsthingInfo) commentator;
                    String url = info.getUrl();
                    holder.tv_name.setText(info.getName());
                    Uri uri = Uri.parse(url);
                    holder.image_icon.setImageURI(uri);
                    holder.tv_time.setText(TimeUtils.dateStringFormatGoodExperienceDate(info.getDate()));
                    holder.tv_content.setText(getOnlyContent(info.getContent()));
                } else {
                    Uri uri = Uri.parse(commentator.getAvatar_url());
                    String timeString = commentator.getCreated_at().replace("T", " ");
                    timeString = timeString.substring(0, timeString.indexOf("+"));
                    holder.tv_time.setText(TimeUtils.dateStringFormatGoodExperienceDate(timeString));
                    holder.tv_content.setText(getOnlyContent(commentator.getMessage()));
                    holder.image_icon.setImageURI(uri);
                }

                if (commentator.getFloorNum() > 1) {
                    SubComments subComments;
                    if (isFromNewsThing) {
                        subComments = new SubComments(addFloors4FreshNews((CommentNewsthingInfo) commentator));
                    } else {
                        subComments = new SubComments(addFloors(commentator));
                    }

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

    private List<CommentNewsthingInfo> addFloors4FreshNews(CommentNewsthingInfo commentor) {
        return commentor.getParentComments();
    }

    private List<Commentator> addFloors(Commentator commentator) {
        //只有一层返回
        if (commentator.getFloorNum() == 1) {
            return null;
        }
        List<String> parents = Arrays.asList(commentator.getParents());
        ArrayList<Commentator> commentators = new ArrayList<>();

        for (Commentator comm : this.commentatorArrayList) {
            if (parents.contains(comm.getPost_id())) {
                commentators.add(comm);
            }
        }
        Collections.reverse(commentators);
        return commentators;

    }


    public void setLoadFinishListener(LoadFinishListener loadFinishListener) {
        this.loadFinishListener = loadFinishListener;
    }

    public void setLoadingSuccessListener(LoadingSuccessListener loadingSuccessListener) {
        this.loadingSuccessListener = loadingSuccessListener;
    }


    @Override
    public int getItemViewType(int position) {
        if (isFromNewsThing) {
            return commentsList.get(position).getType();
        } else {
            return commentatorArrayList.get(position).getType();
        }
    }

    @Override
    public int getItemCount() {
        if (isFromNewsThing) {
            return commentsList.size();
        } else {
            return commentatorArrayList.size();
        }

    }


    public void loadDataFrom() {
        Logger.e("url-----loadDataFrom:" + Commentator.getUrlCommentList(id));
        OkHttpUtils.get().url(Commentator.getUrlCommentList(id)).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                if (loadingSuccessListener != null) {
                    loadingSuccessListener.onFaliListener();
                }
            }

            @Override
            public void onResponse(String response) {
                ArrayList<Commentator> list = getDataLoadPicutre(response);
                if (list == null) {
                    if (loadingSuccessListener != null) {
                        loadingSuccessListener.onSuccessListener();
                        ShowToastUtils.Short("暂无评论");
                    }
                    return;
                } else {
                    if (list.size() == 0) {
                        ShowToastUtils.Short("暂无评论");
                    } else {
                        commentatorArrayList.clear();
                        ArrayList<Commentator> hotCommentator = new ArrayList<>();
                        ArrayList<Commentator> normalComment = new ArrayList<>();

                        //添加热门评论
                        for (Commentator commentator : list) {
                            if (commentator.getTag().equals(Commentator.TAG_HOT)) {
                                hotCommentator.add(commentator);
                            } else {
                                normalComment.add(commentator);
                            }
                        }

                        //添加热门评论title
                        if (hotCommentator.size() != 0) {
                            Collections.sort(hotCommentator);
                            Commentator commentatorFlag = new Commentator();
                            commentatorFlag.setType(Commentator.TYPE_HOT);
                            hotCommentator.add(0, commentatorFlag);
                            commentatorArrayList.addAll(hotCommentator);
                        }

                        //添加最新评论及标签
                        if (normalComment.size() != 0) {
                            Commentator newCommentFlag = new Commentator();
                            newCommentFlag.setType(Commentator.TYPE_NEW);
                            commentatorArrayList.add(newCommentFlag);
                            Collections.sort(normalComment);
                            commentatorArrayList.addAll(normalComment);
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
                    if (loadingSuccessListener != null) {
                        loadingSuccessListener.onSuccessListener();
                    }
                    return;
                } else {
                    if (dataFromServer.size() == 0) {
                        if (loadingSuccessListener != null) {
                            loadingSuccessListener.onSuccessListener();
                        }
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

    public ArrayList<Commentator> getDataLoadPicutre(String response) {
        try {
            JSONObject resultJson = new JSONObject(response);
            String allThreadId = resultJson.getString("response").replace("[", "").replace
                    ("]", "").replace("\"", "");
            Logger.e("=======================>" + response);
            String[] threadIds = allThreadId.split("\\,");
            if (TextUtils.isEmpty(threadIds[0])) {
                return null;
            } else {
                //然后根据thread_id再去获得对应的评论和作者信息
                JSONObject parentPostsJson = resultJson.getJSONObject("parentPosts");
                //找出热门评论
                String hotPosts = resultJson.getString("hotPosts").replace("[", "").replace
                        ("]", "").replace("\"", "");
                String[] allHotPosts = hotPosts.split("\\,");
                ArrayList<Commentator> list = new ArrayList<>();
                List<String> allHotPostsArray = Arrays.asList(allHotPosts);
                for (String threadId : threadIds) {
                    Commentator commentator = new Commentator();
                    JSONObject threadObject = parentPostsJson.getJSONObject(threadId);
                    //解析评论，打上TAG
                    if (allHotPostsArray.contains(threadId)) {
                        commentator.setTag(Commentator.TAG_HOT);
                    } else {
                        commentator.setTag(Commentator.TAG_NORMAL);
                    }
                    commentator.setPost_id(threadObject.optString("post_id"));
                    commentator.setParent_id(threadObject.optString("parent_id"));

                    String parentsString = threadObject.optString("parents").replace("[", "").replace
                            ("]", "").replace("\"", "");

                    String[] parents = parentsString.split("\\,");
                    commentator.setParents(parents);
                    //如果第一个数据为空，则只有一层
                    if (TextUtils.isEmpty(parents[0]) || "null".equals(parents[0])) {
                        commentator.setFloorNum(1);
                    } else {
                        commentator.setFloorNum(parents.length + 1);
                    }

                    commentator.setMessage(threadObject.optString("message"));
                    commentator.setCreated_at(threadObject.optString("created_at"));
                    JSONObject authorObject = threadObject.optJSONObject("author");
                    commentator.setName(authorObject.optString("name"));
                    commentator.setAvatar_url(authorObject.optString("avatar_url"));
                    commentator.setType(Commentator.TYPE_NORMAL);
                    list.add(commentator);
                }
                return list;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
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
