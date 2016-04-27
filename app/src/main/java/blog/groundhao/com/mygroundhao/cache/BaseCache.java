package blog.groundhao.com.mygroundhao.cache;

import java.util.ArrayList;

import blog.groundhao.com.mygroundhao.DaoSession;

/**
 * Created by user on 2016/4/27.
 */
public abstract class BaseCache<T> {
    public static final String DB_NAME = "grund-db";
    protected static DaoSession mDaoSession;

    public abstract void clearAllCache();

    public abstract ArrayList<T> getCacheByPage(int page);

    public abstract void addResultCache(String result, int page);
}
