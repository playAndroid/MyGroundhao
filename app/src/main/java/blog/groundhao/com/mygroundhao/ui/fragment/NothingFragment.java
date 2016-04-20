package blog.groundhao.com.mygroundhao.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import blog.groundhao.com.mygroundhao.R;
import blog.groundhao.com.mygroundhao.callback.LoadFinishListener;
import blog.groundhao.com.mygroundhao.callback.LoadingSuccessListener;
import blog.groundhao.com.mygroundhao.model.NoThingInfo;
import blog.groundhao.com.mygroundhao.ui.adapter.PictureAdapter;
import butterknife.Bind;
import butterknife.ButterKnife;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;

/**
 * 无聊图
 * Created by user on 2016/4/8.
 */
public class NothingFragment extends BaseFragment implements LoadFinishListener, LoadingSuccessListener {
    @Bind(R.id.swipe_view)
    SwipeRefreshLayout swipe_view;
    @Bind(R.id.recycler_view)
    RecyclerView recycler_view;
    @Bind(R.id.loading)
    CircularProgressBar loading;
    private PictureAdapter newsThingAdapter;
    private LinearLayoutManager linearLayoutManager;
    public NoThingInfo.PictureType pictureType;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        pictureType = NoThingInfo.PictureType.NOTHING;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_auto_load, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recycler_view.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recycler_view.setLayoutManager(linearLayoutManager);
        swipe_view.setColorSchemeResources(android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_blue_light);
        swipe_view.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                newsThingAdapter.loadFirst();
//                swipe_view.com
                swipe_view.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (swipe_view.isRefreshing())
                            swipe_view.setRefreshing(false);
                    }
                }, 2000);
            }
        });
        newsThingAdapter = new PictureAdapter(getActivity(), pictureType);
        newsThingAdapter.setLoadFinishListener(this);
        newsThingAdapter.setLoadSuccessListener(this);
        recycler_view.setAdapter(newsThingAdapter);
        newsThingAdapter.loadFirst();
        registerListener();
        loading.setVisibility(View.VISIBLE);
    }

    private void registerListener() {
        recycler_view.setOnScrollListener(new RecyclerView.OnScrollListener() {
            Boolean isScrolling = false;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                isScrolling = (dy > 0);
                int itemPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                int itemCount = linearLayoutManager.getItemCount();
                if (isScrolling && itemPosition >= itemCount - 3) {
                    isScrolling = false;
                    newsThingAdapter.loadNextData();
//                    swipe_view.setRefreshing(true);
                    loading.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_refresh, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            newsThingAdapter.loadFirst();
            swipe_view.setRefreshing(true);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finishDataFormServer() {
        if (swipe_view.isRefreshing()) {
            swipe_view.setRefreshing(false);
        }
    }

    @Override
    public void onSuccessListener() {
        loading.setVisibility(View.GONE);
        if (swipe_view.isRefreshing()) {
            swipe_view.setRefreshing(false);
        }
    }

    @Override
    public void onFaliListener() {
        loading.setVisibility(View.VISIBLE);
        if (swipe_view.isRefreshing()) {
            swipe_view.setRefreshing(false);
        }
    }
}
