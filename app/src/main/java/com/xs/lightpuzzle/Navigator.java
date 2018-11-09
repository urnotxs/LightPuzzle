package com.xs.lightpuzzle;

import android.content.Context;
import android.content.Intent;

import com.xs.lightpuzzle.materials.DownloadedListActivity;
import com.xs.lightpuzzle.materials.MaterialListActivity;

/**
 * Created by xs on 2018/11/2.
 */

public class Navigator {

    public static void navigatorToMaterrailListActivity(Context context){
        Intent intent = new Intent(context, MaterialListActivity.class);
        intent.putExtra(MaterialListActivity.EXTRA_STATE, MaterialListActivity.STATE.NORMAL);
        context.startActivity(intent);
    }

    public static void navigateToDownloadedListActivity(Context context){
        Intent intent = new Intent(context, DownloadedListActivity.class);
        context.startActivity(intent);
    }
}
