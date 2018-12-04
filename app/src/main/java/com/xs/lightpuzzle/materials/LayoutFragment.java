package com.xs.lightpuzzle.materials;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Pair;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xs.lightpuzzle.R;

import butterknife.BindView;

/**
 * Created by xs on 2018/12/4.
 */

public class LayoutFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    LayoutAdapter adapter;
    @BindView(R.id.material_list_fg_rv)
    RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_material_list, container, false);
        initRecyclerView();
        return view;
    }

    private void initRecyclerView() {
        adapter = new LayoutAdapter();
        mRecyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(
                        2, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(adapter);
        ((SimpleItemAnimator) mRecyclerView.getItemAnimator())
                .setSupportsChangeAnimations(false);
    }

    private int getDrawableResId(String drawableName) {
        return getContext().getResources().getIdentifier(drawableName, "drawable", getContext().getPackageName());
    }

    private class LayoutAdapter extends RecyclerView.Adapter<LayoutAdapter.ViewHolder> {

        //定义一个内部类ViewHolder，继承自RecyclerView.ViewHolder，用来缓存子项的各个实例，提高效率
        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;

            public ViewHolder(View view) {
                super(view);
                imageView = view.findViewById(R.id.template_layout_item_iv);
            }
        }

        public LayoutAdapter() {
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_grid_template_layout_by_the_right,
                            viewGroup, false);
            final ViewHolder holder = new ViewHolder(view);

            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            viewHolder.imageView.setImageResource(getDrawableResId(MAP.get(i).first));
        }

        @Override
        public int getItemCount() {
            return MAP.size();
        }
    }

    private static final SparseArray<Pair<String, Integer>> MAP = new SparseArray<>();

    static {
        MAP.put(0, new Pair<>("ic_layout_entrance_1_1", ASPECT._1_1));
        MAP.put(1, new Pair<>("ic_layout_entrance_9_16", ASPECT._9_16));
        MAP.put(2, new Pair<>("ic_layout_entrance_3_4", ASPECT._3_4));
        MAP.put(3, new Pair<>("ic_layout_entrance_16_9", ASPECT._16_9));
        MAP.put(4, new Pair<>("ic_layout_entrance_4_3", ASPECT._4_3));
        MAP.put(5, new Pair<>("ic_layout_entrance_3_2", ASPECT._3_2));
        MAP.put(6, new Pair<>("ic_layout_entrance_2_1", ASPECT._2_1));
        MAP.put(7, new Pair<>("ic_layout_entrance_2_3", ASPECT._2_3));
        MAP.put(8, new Pair<>("ic_layout_entrance_1_2", ASPECT._1_2));
    }

    public interface ASPECT {
        int _1_1 = 1;
        int _9_16 = 2;
        int _3_4 = 3;
        int _16_9 = 4;
        int _4_3 = 5;
        int _3_2 = 6;
        int _2_1 = 7;
        int _2_3 = 8;
        int _1_2 = 9;
    }

}
