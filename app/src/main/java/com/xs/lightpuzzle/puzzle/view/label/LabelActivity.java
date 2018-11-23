package com.xs.lightpuzzle.puzzle.view.label;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by xs on 2018/11/23.
 */

public class LabelActivity extends Activity {
    public static final String LABEL_TEXT = "label_text";
    public static final String LABEL_IS_INVERT = "label_is_invert";
    public static final String LABEL_BITMAP = "label_bitmap";
    public static final String LABEL_ICON_TYPE = "label_icon_type";
    public static final String LABEL_LABEL_TYPE = "label_label_type";
    public static final String LABEL_IS_UPDATE = "label_is_update";

    private LabelPage mPage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = new LabelPage(this, getIntent(), new PageListener() {
            @Override
            public void onClickSaveBtn(int icon_type_index, int label_type_index,
                                       String text, boolean isInvert, boolean isUpdate) {
                Intent data = new Intent();
                data.putExtra(LABEL_ICON_TYPE, icon_type_index);
                data.putExtra(LABEL_LABEL_TYPE, label_type_index);
                data.putExtra(LABEL_TEXT, text);
                data.putExtra(LABEL_IS_INVERT, isInvert);
                data.putExtra(LABEL_IS_UPDATE, isUpdate);
                setResult(Activity.RESULT_OK, data);
                finish();
            }

            @Override
            public void onClickBackBtn() {
                finish();
            }
        });
        setContentView(mPage);
    }

    public interface PageListener {
        void onClickSaveBtn(int icon_type_index, int label_type_index,
                            String text, boolean isInvert, boolean isUpdate);

        void onClickBackBtn();
    }
}
