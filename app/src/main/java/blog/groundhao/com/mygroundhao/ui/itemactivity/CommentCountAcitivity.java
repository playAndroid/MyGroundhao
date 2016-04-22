package blog.groundhao.com.mygroundhao.ui.itemactivity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import blog.groundhao.com.mygroundhao.R;
import blog.groundhao.com.mygroundhao.callback.LoadFinishListener;
import blog.groundhao.com.mygroundhao.engine.uibest.BestActivity;
import blog.groundhao.com.mygroundhao.model.PostsBean;
import blog.groundhao.com.mygroundhao.ui.adapter.CommentCountAdapter;
import butterknife.Bind;
import butterknife.ButterKnife;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;

/**
 * Created by user on 2016/4/20.
 */
public class CommentCountAcitivity extends BestActivity implements LoadFinishListener {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.swipe_view)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.loading)
    CircularProgressBar loading;
    private boolean isFromNewsThing;
    private CommentCountAdapter commentCountAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        initView();
        initData();
    }

    private void initData() {
        PostsBean postsBean = (PostsBean) getIntent().getSerializableExtra(DATA_NEWSTHING);
        String thread_key = getIntent().getStringExtra(DATA_PICTURE);
        isFromNewsThing = getIntent().getBooleanExtra(IS_FROM_NEWSTHING, true);
        String id = postsBean.getId();
        if (isFromNewsThing) {
            commentCountAdapter = new CommentCountAdapter(this, id, isFromNewsThing);
        } else {
            commentCountAdapter = new CommentCountAdapter(this, thread_key, isFromNewsThing);
        }
        recyclerView.setAdapter(commentCountAdapter);
        commentCountAdapter.setLoadFinishListener(this);
        commentCountAdapter.load4Newsthing();
    }

    private void initView() {
        ButterKnife.bind(this);
        toolbar.setTitle("评论");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_actionbar_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright
                , android.R.color.holo_green_light
                , android.R.color.holo_red_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                commentCountAdapter.load4Newsthing();
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finishDataFormServer() {
        loading.setVisibility(View.GONE);
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
