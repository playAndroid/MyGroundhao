package blog.groundhao.com.mygroundhao.model;

import org.json.JSONObject;

/**
 * Created by user on 2016/4/27.
 */
public class TagsBean {
    private int id;
    private String slug;
    private String title;
    private String description;
    private int post_count;
    public static TagsBean parseCache(final JSONObject jsonObject) {
        TagsBean tags;
        if (jsonObject == null) {
            tags = null;
        } else {
            tags = new TagsBean();
            tags.id = jsonObject.optInt("id");
            tags.title = jsonObject.optString("title");
            tags.description = jsonObject.optString("description");
        }
        return tags;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPost_count() {
        return post_count;
    }

    public void setPost_count(int post_count) {
        this.post_count = post_count;
    }
}
