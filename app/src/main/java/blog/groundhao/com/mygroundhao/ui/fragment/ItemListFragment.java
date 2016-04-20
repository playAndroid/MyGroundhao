package blog.groundhao.com.mygroundhao.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import blog.groundhao.com.mygroundhao.R;
import blog.groundhao.com.mygroundhao.model.MenuItem;
import blog.groundhao.com.mygroundhao.ui.MainActivity;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 菜单Fragment
 * Created by user on 2016/4/7.
 */
public class ItemListFragment extends Fragment {


    @Bind(R.id.recycler_view)
    RecyclerView recycler_view;
    @Bind(R.id.rl_container)
    RelativeLayout rl_container;
    private LinearLayoutManager layoutManager;
    private MenuAdapter menuAdapter;
    private MainActivity mainActivity;
    private MenuItem.FragmentType fragmentType = MenuItem.FragmentType.NEWSTHING;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            mainActivity = (MainActivity) context;
        } else {
            throw new IllegalArgumentException("The Activity must be MainActivity!!");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_item_list, container, false);
        ButterKnife.bind(this, view);
        layoutManager = new LinearLayoutManager(getActivity());
        recycler_view.setLayoutManager(layoutManager);
        rl_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转设置页面
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        menuAdapter = new MenuAdapter();
        addMenuItems(menuAdapter);
        recycler_view.setAdapter(menuAdapter);
    }

    private void addMenuItems(MenuAdapter menuAdapter) {
        menuAdapter.menuItems.clear();
        menuAdapter.menuItems.add(new MenuItem(getResources().getString(R.string.newsthing), R.drawable.ic_explore_white_24dp, MenuItem.FragmentType.NEWSTHING, NewsthingFragment.class));
        menuAdapter.menuItems.add(new MenuItem(getResources().getString(R.string.nothing), R.drawable.ic_mood_white_24dp, MenuItem.FragmentType.NOTHING, NothingFragment.class));
        menuAdapter.menuItems.add(new MenuItem(getResources().getString(R.string.gril), R.drawable.ic_local_florist_white_24dp, MenuItem.FragmentType.GRIL, GrilFragment.class));
        menuAdapter.menuItems.add(new MenuItem(getResources().getString(R.string.joke), R.drawable.ic_chat_white_24dp, MenuItem.FragmentType.PICEC, JokeFragment.class));
        menuAdapter.menuItems.add(new MenuItem(getResources().getString(R.string.video), R.drawable.ic_movie_white_24dp, MenuItem.FragmentType.VIDEO, VideoFragment.class));
    }


    private class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

        private ArrayList<MenuItem> menuItems;

        public MenuAdapter() {
            menuItems = new ArrayList<>();
        }

        @Override
        public MenuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MenuAdapter.ViewHolder holder, final int position) {
            final MenuItem menuItem = menuItems.get(position);
            holder.icon.setImageResource(menuItem.getIconId());
            holder.title.setText(menuItem.getTitle());
            holder.rl_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        if (fragmentType != menuItem.getFragmentType()) {
                            Fragment fragment = (Fragment) Class.forName(menuItem.getFragment().getName()).newInstance();
                            mainActivity.replaceFragment(R.id.frage_content, fragment);
                            fragmentType = menuItem.getFragmentType();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mainActivity.closeDrawer();
                }
            });
        }

        @Override
        public int getItemCount() {
            return menuItems.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            private ImageView icon;
            private TextView title;
            private RelativeLayout rl_container;

            public ViewHolder(View itemView) {
                super(itemView);
                icon = (ImageView) itemView.findViewById(R.id.img_menu);
                title = (TextView) itemView.findViewById(R.id.tv_title);
                rl_container = (RelativeLayout) itemView.findViewById(R.id.rl_container);
            }
        }
    }
}
