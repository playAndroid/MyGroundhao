package blog.groundhao.com.mygroundhao.model;

import java.util.List;

/**
 * Created by user on 2016/4/14.
 */
public class JokeInfo {

    public static final String URL_JOKE = "http://jandan.net/?oxwlxojflwblxbsapi=jandan" +
            ".get_duan_comments&page=";

    private String count;
    private String current_page;
    private String page_count;
    private String status;
    private String total_comments;
    private List<Comments> comments;

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

    public List<Comments> getCommentses() {
        return comments;
    }

    public void setCommentses(List<Comments> commentses) {
        this.comments = commentses;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
