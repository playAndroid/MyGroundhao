package blog.groundhao.com.mygroundhao.ui.itemactivity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.ArrayList;

import blog.groundhao.com.mygroundhao.R;
import blog.groundhao.com.mygroundhao.engine.AppManager;
import blog.groundhao.com.mygroundhao.engine.uibest.BestActivity;
import blog.groundhao.com.mygroundhao.model.PostsBean;
import blog.groundhao.com.mygroundhao.ui.fragment.NewsthingDetailFragment;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by user on 2016/4/15.
 */
public class NewsThingDetailsActivity extends BestActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.page_view)
    ViewPager page_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsthing);
        AppManager.getInstance().addActivity(this);

        initView();
        initData();
    }

    private void initView() {
        ButterKnife.bind(this);
        toolbar.setTitle("新鲜事");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_actionbar_back);
    }

    private void initData() {
        ArrayList<PostsBean> newsThingInfos = (ArrayList<PostsBean>) getIntent().getSerializableExtra(DATA_NEWSTHING);
        int position = getIntent().getIntExtra(DATA_NEWS_POSITION, 0);
        page_view.setAdapter(new NewsThingPageAdapter(getSupportFragmentManager(), newsThingInfos));
        page_view.setCurrentItem(position);
    }

    private class NewsThingPageAdapter extends FragmentPagerAdapter {

        private final ArrayList<PostsBean> newsThingInfos;

        public NewsThingPageAdapter(FragmentManager fm, ArrayList<PostsBean> newsThingInfos) {
            super(fm);
            this.newsThingInfos = newsThingInfos;
        }

        @Override
        public Fragment getItem(int position) {
            return NewsthingDetailFragment.getInstence(newsThingInfos.get(position));
        }

        @Override
        public int getCount() {
            return newsThingInfos.size();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
