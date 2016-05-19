package blog.groundhao.com.mygroundhao.cache;

import android.content.Context;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import blog.groundhao.com.mygroundhao.DaoSession;
import blog.groundhao.com.mygroundhao.NothingCacheDao;
import blog.groundhao.com.mygroundhao.engine.GroundHaoApplication;
import blog.groundhao.com.mygroundhao.model.AuthorBean;
import blog.groundhao.com.mygroundhao.model.CustomFields;
import blog.groundhao.com.mygroundhao.model.NewsThingInfo;
import blog.groundhao.com.mygroundhao.model.PostsBean;
import blog.groundhao.com.mygroundhao.model.TagsBean;
import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by user on 2016/4/27.
 */
public class NothingCache extends BaseCache {

    private static NothingCache instance;
    private static DaoSession daoSession;
    private static NothingCacheDao nothingCacheDao;

    private NothingCache() {

    }


    public static NothingCache getInstance(Context context) {
        if (instance == null) {
            synchronized (NothingCache.class) {
                if (instance == null) {
                    instance = new NothingCache();
                }
            }
            daoSession = GroundHaoApplication.getDaoSession(context);
            nothingCacheDao = daoSession.getNothingCacheDao();

        }
        return instance;
    }

    @Override
    public void clearAllCache() {
        nothingCacheDao.deleteAll();
    }

    @Override
    public ArrayList<PostsBean> getCacheByPage(int page) {
        QueryBuilder<blog.groundhao.com.mygroundhao.NothingCache> query = nothingCacheDao.queryBuilder()
                .where(NothingCacheDao.Properties.Page.eq("" + page));
        if (query.list().size() > 0) {
            try {
                Logger.e("ssssssssssss=>>>"+query.list().get(0).getResult());
                return parseCacheFromGson(query.list().get(0).getResult());
            } catch (Exception e) {
                e.printStackTrace();
                return new ArrayList<>();
            }
        } else {
            return new ArrayList<>();
        }

    }

    @Override
    public void addResultCache(String result, int page) {
        blog.groundhao.com.mygroundhao.NothingCache nothingCache = new blog.groundhao.com.mygroundhao.NothingCache();
        nothingCache.setResult(result);
        nothingCache.setPage(page);
        nothingCache.setTime(System.currentTimeMillis());
        nothingCacheDao.insert(nothingCache);
    }


    private ArrayList<PostsBean> parseCacheFromGson(String response) {
        NewsThingInfo newsThingInfo = new Gson().fromJson(response, NewsThingInfo.class);
        List<PostsBean> posts = newsThingInfo.getPosts();
        return (ArrayList<PostsBean>) posts;
    }


    private ArrayList<PostsBean> parseCache(JSONArray jsonArray) {
        ArrayList<PostsBean> postsBeenLists = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            PostsBean postsBean = new PostsBean();
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            postsBean.setId(jsonObject.optString("id"));
            postsBean.setUrl(jsonObject.optString("url"));
            postsBean.setTitle(jsonObject.optString("title"));
            postsBean.setDate(jsonObject.optString("date"));
            postsBean.setComment_count(jsonObject.optInt("comment_count"));
            postsBean.setAuthor(AuthorBean.parse(jsonObject.optJSONObject("author")));
            postsBean.setCustomFields(CustomFields.parseCache(jsonObject.optJSONObject("custom_fields")));
            postsBean.setTagsBean(TagsBean.parseCache(jsonObject.optJSONObject("tags")));

            postsBeenLists.add(postsBean);
        }
        return postsBeenLists;
    }
}
