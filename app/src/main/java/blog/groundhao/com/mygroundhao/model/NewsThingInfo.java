package blog.groundhao.com.mygroundhao.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by user on 2016/4/8.
 */
public class NewsThingInfo implements Serializable {
    public static final String URL_FRESH_NEWS = "http://jandan.net/?oxwlxojflwblxbsapi=get_recent_posts&include=url,date,tags,author,title,comment_count,custom_fields&custom_fields=thumb_c,views&dev=1&page=";

    public static final String URL_FRESH_NEWS_DETAIL = "http://i.jandan.net/?oxwlxojflwblxbsapi=get_post&include=content&id=";
    private String status;
    private int count;
    private int count_total;
    private int pages;


    /**
     * id : 77723
     * url : http://jandan.net/2016/04/12/using-mars-dust.html
     * title : 这个用火星和月球尘埃来做3D打印的方案，获得了NASA竞赛头奖
     * date : 2016-04-12 14:00:04
     * tags : [{"id":808,"slug":"3d%e6%89%93%e5%8d%b0","title":"3D打印","description":"","post_count":171}]
     * author : {"id":563,"slug":"siyi","name":"蛋花","first_name":"","last_name":"","nickname":"蛋花","url":"","description":""}
     * comment_count : 0
     * custom_fields : {"thumb_c":["http://tankr.net/s/custom/JXDY.jpg"]}
     */

    private List<PostsBean> posts;

    public static String getUrlFreshNewsDetail(String id) {
        return URL_FRESH_NEWS_DETAIL + id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount_total() {
        return count_total;
    }

    public void setCount_total(int count_total) {
        this.count_total = count_total;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<PostsBean> getPosts() {
        return posts;
    }

    public void setPosts(List<PostsBean> posts) {
        this.posts = posts;
    }

}
