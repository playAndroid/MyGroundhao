package blog.groundhao.com.mygroundhao.model;

/**
 * Created by user on 2016/4/14.
 */
public class ResponseInfo {
    //用于获取评论数量
    public static final String URL_COMMENT_COUNTS = "http://jandan.duoshuo.com/api/threads/counts.json?threads=";
    public static final String COMMENTS = "comments";

    public static final String THREAD_ID = "thread_id";
    public static final String THREAD_KEY = "thread_key";
    /**
     * comments : 0
     * dislikes : 0
     * likes : 0
     * reposts : 0
     * thread_id : 1185181175765836758
     * thread_key : 1
     * views : 0
     */

    private int comments;
    private String dislikes;
    private String likes;
    private String reposts;
    private String thread_id;
    private String thread_key;
    private int views;

    public ResponseInfo() {
    }

    public ResponseInfo(String thread_id, String thread_key, int comments) {
        this.thread_id = thread_id;
        this.thread_key = thread_key;
        this.comments = comments;
    }

    public static String getUrlCommentCounts(String thread_id) {

        return URL_COMMENT_COUNTS + thread_id;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public String getDislikes() {
        return dislikes;
    }

    public void setDislikes(String dislikes) {
        this.dislikes = dislikes;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getReposts() {
        return reposts;
    }

    public void setReposts(String reposts) {
        this.reposts = reposts;
    }

    public String getThread_id() {
        return thread_id;
    }

    public void setThread_id(String thread_id) {
        this.thread_id = thread_id;
    }

    public String getThread_key() {
        return thread_key;
    }

    public void setThread_key(String thread_key) {
        this.thread_key = thread_key;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }
}
