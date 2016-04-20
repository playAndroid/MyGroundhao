package blog.groundhao.com.mygroundhao.engine.uibest;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import blog.groundhao.com.mygroundhao.engine.CommonString;

/**
 * Activity Best
 * Created by hlk on 2016/3/11.
 */
public class BestActivity extends AppCompatActivity implements CommonString {

    protected Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
    }

    public void replaceFragment(int id_content, Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(id_content, fragment);
        transaction.commit();
    }
}
