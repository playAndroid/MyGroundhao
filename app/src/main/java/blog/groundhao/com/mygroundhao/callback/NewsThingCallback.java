package blog.groundhao.com.mygroundhao.callback;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import blog.groundhao.com.mygroundhao.model.NewsThingInfo;
import okhttp3.Response;

/**
 * Created by user on 2016/4/11.
 */
public abstract class NewsThingCallback extends Callback<NewsThingInfo> {


    @Override
    public NewsThingInfo parseNetworkResponse(Response response) throws Exception {
        String string = response.body().toString();
//        List list = new Gson().fromJson(string, new TypeToken<List<NewsThingInfo>>() {
//        }.getType());
        NewsThingInfo newsThingInfo = new Gson().fromJson(string, NewsThingInfo.class);
        return newsThingInfo;
    }
}
