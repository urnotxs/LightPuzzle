package com.xs.lightpuzzle.demo.a_tactics_demo;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xs.lightpuzzle.R;
import com.xs.lightpuzzle.demo.a_tactics_demo.adapter.TacticsFormationsAdapter;
import com.xs.lightpuzzle.demo.a_tactics_demo.data.TeamUserPositionVO;
import com.xs.lightpuzzle.demo.a_tactics_demo.view.TacticsBoard;

import java.util.ArrayList;

/**
 * @author xs
 * @description
 * @since 2019/2/21
 */

public class TestTacticsBoard extends FrameLayout implements View.OnClickListener{

    private Context mContext;
    private TextView teamLocationText;
    public TacticsBoard tacticsBoard;
    private String teamLocation = "";
    private Handler handler;

    public TestTacticsBoard(@NonNull Context context) {
        super(context);
        mContext = context;
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.test_tactics_board_layout, null);
        addView(view);
        {
            teamLocationText = view.findViewById(R.id.formation_tactics_txt);
            tacticsBoard = view.findViewById(R.id.formation_tactics_board);
            tacticsBoard.setItems(TacticsBoardHelper.TotalFormationsItems);
            handler = new MyHandler(this);
            tacticsBoard.setHandler(handler);

            txtTactics = findViewById(R.id.tactics_txt);
            checkboxFive = findViewById(R.id.tactics_five_checkbox);
            checkboxFive.setOnClickListener(this);
            checkboxSeven = findViewById(R.id.tactics_seven_checkbox);
            checkboxSeven.setOnClickListener(this);
            checkboxEight = findViewById(R.id.tactics_eight_checkbox);
            checkboxEight.setOnClickListener(this);
            rlSelect = findViewById(R.id.tactics_select_bg);
            tacticsGridView = findViewById(R.id.tactics_gridview);
            tacticsGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ArrayList<TeamUserPositionVO> formationsItems = null;
                    if (checkboxFive.isChecked()){
                        txtTactics.setText(TacticsBoardHelper.FiveFormations[position]);
                        formationsItems = TacticsBoardHelper.FiveFormationsItems.get(position);
                    }
                    if (checkboxSeven.isChecked()){
                        txtTactics.setText(TacticsBoardHelper.SevenFormations[position]);
                        formationsItems = TacticsBoardHelper.SevenFormationsItems.get(position);
                    }
                    if (checkboxEight.isChecked()){
                        txtTactics.setText(TacticsBoardHelper.EightFormations[position]);
                        formationsItems = TacticsBoardHelper.EightFormationsItems.get(position);
                    }
                    checkboxFive.setChecked(false);
                    checkboxSeven.setChecked(false);
                    checkboxEight.setChecked(false);
                    rlSelect.setVisibility(View.GONE);
                    selectItems = formationsItems;
                    tacticsBoard.removeAllViews();
                    tacticsBoard.setItems(formationsItems);
                    handler.sendEmptyMessage(0);
                }
            });
        }
    }
    private ArrayList<TeamUserPositionVO> selectItems;// 选择的阵型
    private TextView txtTactics;
    private CheckBox checkboxFive;
    private CheckBox checkboxSeven;
    private CheckBox checkboxEight;
    private RelativeLayout rlSelect;
    private GridView tacticsGridView;
    private TacticsFormationsAdapter formationsAdapter;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tactics_five_checkbox:
                if (checkboxFive.isChecked()) {
                    checkboxFive.setChecked(true);
                    rlSelect.setVisibility(View.VISIBLE);
                    formationsAdapter = new TacticsFormationsAdapter(mContext, TacticsBoardHelper.FiveFormations);
                    tacticsGridView.setAdapter(formationsAdapter);
                } else {
                    checkboxFive.setChecked(false);
                    rlSelect.setVisibility(View.GONE);
                }
                checkboxSeven.setChecked(false);
                checkboxEight.setChecked(false);
                break;
            case R.id.tactics_seven_checkbox:
                if (checkboxSeven.isChecked()) {
                    checkboxSeven.setChecked(true);
                    rlSelect.setVisibility(View.VISIBLE);
                    formationsAdapter = new TacticsFormationsAdapter(mContext, TacticsBoardHelper.SevenFormations);
                    tacticsGridView.setAdapter(formationsAdapter);
                } else {
                    checkboxSeven.setChecked(false);
                    rlSelect.setVisibility(View.GONE);
                }
                checkboxEight.setChecked(false);
                checkboxFive.setChecked(false);
                break;
            case R.id.tactics_eight_checkbox:
                if (checkboxEight.isChecked()) {
                    checkboxEight.setChecked(true);
                    rlSelect.setVisibility(View.VISIBLE);
                    formationsAdapter = new TacticsFormationsAdapter(mContext, TacticsBoardHelper.EightFormations);
                    tacticsGridView.setAdapter(formationsAdapter);
                } else {
                    checkboxEight.setChecked(false);
                    rlSelect.setVisibility(View.GONE);
                }
                checkboxFive.setChecked(false);
                checkboxSeven.setChecked(false);
                break;
        }
    }

    private static class MyHandler extends WeakHandler<TestTacticsBoard> {
        public MyHandler(TestTacticsBoard owner) {
            super(owner);
        }

        @Override
        public void handleMessage(Message msg) {
            final TestTacticsBoard act = getOwner();
            switch (msg.what) {
                case 0:
                    act.tacticsBoard.showItems();
                    break;
                default:
                    break;
            }
        }
    }

    private static class MyLocationHandler extends WeakHandler<TestTacticsBoard> {
        public MyLocationHandler(TestTacticsBoard owner) {
            super(owner);
        }

        @Override
        public void handleMessage(Message msg) {
            final TestTacticsBoard act = getOwner();
            switch (msg.what) {
                case 0:
                    act.tacticsBoard.showLocationItems();
                    break;
                case 1:
                    act.teamLocation = msg.getData().getString("Position");
                    act.teamLocationText.setText(TacticsBoardHelper.mLocationHashMap.get(act.teamLocation));
                    break;
                default:
                    break;
            }
        }
    }

}
