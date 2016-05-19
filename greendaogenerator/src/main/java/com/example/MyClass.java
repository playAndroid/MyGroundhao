package com.example;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class MyClass {

    //辅助文件生成的相对路径
    public static final String DAO_PATH = "/MyGroundhao/app/src/main/java-gen";
    //辅助文件的包名
    public static final String PACKAGE_NAME = "blog.groundhao.com.mygroundhao";
    //数据库的版本号
    public static final int DATA_VERSION_CODE = 1;


    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(DATA_VERSION_CODE, PACKAGE_NAME);
        addNote(schema, "JokeCache");
        addNote(schema, "NothingCache");
        addNote(schema, "PictureCache");
        addNote(schema, "SisterCache");
        addNote(schema, "VideoCache");
        new DaoGenerator().generateAll(schema, DAO_PATH);
    }

    /**
     * @param schema
     */
    private static void addNote(Schema schema, String tableName) {
        Entity joke = schema.addEntity(tableName);

        //主键id自增长
        joke.addIdProperty().primaryKey().autoincrement();
        //请求结果
        joke.addStringProperty("result");
        //页数
        joke.addIntProperty("page");
        //插入时间，暂时无用
        joke.addLongProperty("time");
    }
}

