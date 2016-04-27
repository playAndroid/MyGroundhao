package blog.groundhao.com.mygroundhao.model;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by user on 2016/4/18.
 */
public class AuthorBean implements Serializable{
        private int id;
        private String slug;
        private String name;
        private String first_name;
        private String last_name;
        private String nickname;
        private String url;
        private String description;

    public static AuthorBean parse(final JSONObject jsonObject) {
        AuthorBean author;
        if (jsonObject == null) {
            author = null;
        } else {
            author = new AuthorBean();
            author.id = jsonObject.optInt("id");
            author.slug = jsonObject.optString("slug");
            author.name = jsonObject.optString("name");
            author.first_name = jsonObject.optString("first_name");
            author.last_name = jsonObject.optString("last_name");
            author.nickname = jsonObject.optString("nickname");
            author.url = jsonObject.optString("url");
            author.description = jsonObject.optString("description");
        }
        return author;
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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getFirst_name() {
            return first_name;
        }

        public void setFirst_name(String first_name) {
            this.first_name = first_name;
        }

        public String getLast_name() {
            return last_name;
        }

        public void setLast_name(String last_name) {
            this.last_name = last_name;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
}
