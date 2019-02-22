package com.xs.lightpuzzle.demo.a_tactics_demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xs.lightpuzzle.R;

/**
 * @author xs
 * @description
 * @since 2019/2/22
 */

public class TacticsFormationsAdapter extends BaseAdapter {

    private ViewHolder holder;
    public String[] formations;
    private Context context;

    public TacticsFormationsAdapter(Context context, String[] formations) {
        this.formations = formations;
        this.context = context;
    }

    @Override
    public int getCount() {
        return formations.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return formations[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.v2_item_tactics_formations, null, false);
            holder.txtFormations = convertView.findViewById(R.id.item_tactics_formations);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txtFormations.setText(formations[position]);
        return convertView;
    }

    private static class ViewHolder {
        TextView txtFormations;
    }

}
