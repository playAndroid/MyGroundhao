package blog.groundhao.com.mygroundhao.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by user on 2016/4/27.
 */
    public  class CustomFieldsBean implements Serializable {
        private List<String> thumb_c;

        public List<String> getThumb_c() {
            return thumb_c;
        }

        public void setThumb_c(List<String> thumb_c) {
            this.thumb_c = thumb_c;
        }
    }
