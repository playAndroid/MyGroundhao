package blog.groundhao.com.mygroundhao.model;

import java.util.List;

/**
 * 无聊图 小视屏通用Info
 * Created by hlk on 2016/4/13.
 */
public class NoThingInfo {

    public static final String URL_BORING_PICTURE = "http://jandan.net/?oxwlxojflwblxbsapi=jandan.get_pic_comments&page=";
    public static final String URL_SISTER = "http://jandan.net/?oxwlxojflwblxbsapi=jandan.get_ooxx_comments&page=";

    public enum PictureType {
        NOTHING, GIRL
    }

    private String count;
    private String current_page;
    private String page_count;
    private String status;
    private String total_comments;
    private List<Comments> comments;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(String current_page) {
        this.current_page = current_page;
    }

    public String getPage_count() {
        return page_count;
    }

    public void setPage_count(String page_count) {
        this.page_count = page_count;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotal_comments() {
        return total_comments;
    }

    public void setTotal_comments(String total_comments) {
        this.total_comments = total_comments;
    }

    public List<Comments> getComments() {
        return comments;
    }

    public void setComments(List<Comments> comments) {
        this.comments = comments;
    }
}
