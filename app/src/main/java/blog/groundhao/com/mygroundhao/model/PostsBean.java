package blog.groundhao.com.mygroundhao.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by user on 2016/4/18.
 */
public class PostsBean implements Serializable {

    private int id;
    private String url;
    private String title;
    private String date;
    /**
     * id : 563
     * slug : siyi
     * name : 蛋花
     * first_name :
     * last_name :
     * nickname : 蛋花
     * url :
     * description :
     */

    private AuthorBean author;
    private int comment_count;
    private CustomFieldsBean custom_fields;
    /**
     * id : 808
     * slug : 3d%e6%89%93%e5%8d%b0
     * title : 3D打印
     * description :
     * post_count : 171
     */

    private List<TagsBean> tags;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public AuthorBean getAuthor() {
        return author;
    }

    public void setAuthor(AuthorBean author) {
        this.author = author;
    }

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

    public CustomFieldsBean getCustom_fields() {
        return custom_fields;
    }

    public void setCustom_fields(CustomFieldsBean custom_fields) {
        this.custom_fields = custom_fields;
    }

    public List<TagsBean> getTags() {
        return tags;
    }

    public void setTags(List<TagsBean> tags) {
        this.tags = tags;
    }



    public static class CustomFieldsBean implements Serializable{
        private List<String> thumb_c;

        public List<String> getThumb_c() {
            return thumb_c;
        }

        public void setThumb_c(List<String> thumb_c) {
            this.thumb_c = thumb_c;
        }
    }

    public static class TagsBean implements Serializable{
        private int id;
        private String slug;
        private String title;
        private String description;
        private int post_count;

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

    @Override
    public String toString() {
        return "PostsBean{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", author=" + author +
                ", comment_count=" + comment_count +
                ", custom_fields=" + custom_fields +
                ", tags=" + tags +
                '}';
    }
}


