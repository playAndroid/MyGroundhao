package blog.groundhao.com.mygroundhao.model;

/**
 * 网络状态
 * Created by user on 2016/4/8.
 */
public class NetEvent {
    /**
     * 可用
     */
    public static final int AVAILABLE = 1;
    /**
     * 不可用
     */
    public static final int UNAVAILABLE = -1;

    private int type;

    public NetEvent(int type) {
        this.type = type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
