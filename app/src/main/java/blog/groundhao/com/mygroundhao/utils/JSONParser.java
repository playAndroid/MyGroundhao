package blog.groundhao.com.mygroundhao.utils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;

/**
 * Created by user on 2016/4/21.
 */
public class JSONParser {
    protected static Gson gson = new Gson();


    public static String toString(Object json) {

        return gson.toJson(json);
    }


    public static Object fromJson(String json, Class<?> c) {

        return gson.fromJson(json, c);
    }

    public static Object toObject(String jsonString, Object type) {
        jsonString = jsonString.replace("&nbsp", "");
        jsonString = jsonString.replace("﹠nbsp", "");
        jsonString = jsonString.replace("nbsp", "");
        jsonString = jsonString.replace("&amp;", "");
        jsonString = jsonString.replace("&amp", "");
        jsonString = jsonString.replace("amp", "");
        if (type instanceof Type) {
            try {
                return gson.fromJson(jsonString, (Type) type);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
                return null;
            }

        } else if (type instanceof Class<?>) {
            try {
                return gson.fromJson(jsonString, (Class<?>) type);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            throw new RuntimeException("只能是Class<?>或者通过TypeToken获取的Type类型");
        }
    }
}
