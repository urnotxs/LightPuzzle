package com.xs.lightpuzzle.puzzle.view.label.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Point;
import android.graphics.Rect;
import android.text.InputFilter;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.xs.lightpuzzle.R;
import com.xs.lightpuzzle.imagedecode.JaneBitmapFactory;
import com.xs.lightpuzzle.imagedecode.core.ImageSize;
import com.xs.lightpuzzle.puzzle.util.Utils;


/**
 * Created by urnot_XS on 2018/4/11.
 */
public class EditLabelView extends RelativeLayout {
    private LABEL_TYPE label_type;

    private ICON_TYPE icon_type;

    private ImageView mInputBgIv;

    private ImageView mIcon;

    private Bitmap mIconBmp;

    private Bitmap mInputBmp;

    private EditText label_edit;

    private String text;

    private boolean edit_type = false;

    public EditLabelView(Context context, LABEL_TYPE label_type, ICON_TYPE icon_type) {
        super(context);
        assert null != label_type;
        assert null != icon_type;
        this.label_type = label_type;
        this.icon_type = icon_type;
        initView();
    }

    private void initView() {
        RelativeLayout mRelative = new RelativeLayout(getContext());
        LayoutParams mRparams = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mRparams.addRule(CENTER_VERTICAL, TRUE);
        mRparams.addRule(CENTER_HORIZONTAL, TRUE);
        this.addView(mRelative, mRparams);
        {
            //add bg
            mInputBgIv = new ImageView(getContext());
            if (mInputBmp != null && !mInputBmp.isRecycled()) {
                mInputBmp.recycle();
            }
            mInputBmp = JaneBitmapFactory.decodeResource(getContext(),
                    Input_Res[label_type.ordinal()],
                    new ImageSize(Utils.getRealPixel3(620),
                            Utils.getRealPixel3(192)));
            mInputBgIv.setImageBitmap(mInputBmp);
            mRparams = new LayoutParams(Utils.getRealPixel3(620),
                    Utils.getRealPixel3(192));
            mRelative.addView(mInputBgIv, mRparams);

            //add icon
            mIcon = new ImageView(getContext());
            if (mIconBmp != null && !mIconBmp.isRecycled()) {
                mIconBmp.recycle();
            }
            mIconBmp = JaneBitmapFactory.decodeResource(getContext(),
                    Icon_Res[icon_type.ordinal()],
                    new ImageSize(Utils.getRealPixel3(52),
                            Utils.getRealPixel3(52)));
            mIcon.setImageBitmap(mIconBmp);
            mRparams = new LayoutParams(Utils.getRealPixel3(52),
                    Utils.getRealPixel3(52));
            mRparams.setMargins(
                    Utils.getRealPixel3(Input_point[label_type.ordinal()].x),
                    Utils.getRealPixel3(Input_point[label_type.ordinal()].y), 0, 0);
            mRelative.addView(mIcon, mRparams);

            //add editView
            label_edit = new EditText(getContext());
            mRparams = new LayoutParams(Utils.getRealPixel3(496),
                    Utils.getRealPixel3(82));
            mRparams.setMargins(
                    Utils.getRealPixel3(Input_editPoint[label_type.ordinal()].x),
                    Utils.getRealPixel3(Input_editPoint[label_type.ordinal()].y), 0, 0);
            label_edit.setGravity(Gravity.CENTER);
            label_edit.setHint(R.string.editlabelpage_hint);
            label_edit.setBackgroundDrawable(null);
            label_edit.setHintTextColor(
                    getResources().getColor(R.color.puzzle_label_color));
            label_edit.setTextColor(Color.WHITE);
            label_edit.setMaxLines(1);
            label_edit.setFilters(new InputFilter[]
                    {new InputFilter.LengthFilter(20)});
            label_edit.setTextSize(15);
            label_edit.setSingleLine(true);
            if (!TextUtils.isEmpty(this.text)) {
                label_edit.setText(this.text);
            }
            mRelative.addView(label_edit, mRparams);
        }
    }

    public static Bitmap DecodeLabel(Context context, String text, ICON_TYPE icon_type, LABEL_TYPE label_type, boolean xinvert) {
        TextPaint paint = new TextPaint();
        paint.setAntiAlias(true);
        int count = text.length();
        float[] widths = new float[count];
        Matrix matrix = new Matrix();
        paint.setTextSize(53);
        paint.setColor(Color.WHITE);
        paint.getTextWidths(text, widths);
        float TextW = 0;
        for (int c = 0; c < count; c++) {
            TextW += widths[c];
        }
        int textLenngth = (int) TextW;
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), DrawInput_Res[label_type.ordinal()]);
        NinePatch patch = new NinePatch(bmp, bmp.getNinePatchChunk(), null);
        Bitmap resultBmp = Bitmap.createBitmap(bmp.getWidth() + textLenngth, bmp.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(resultBmp);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        patch.draw(canvas, new Rect(0, 0, resultBmp.getWidth(), resultBmp.getHeight()));
        Bitmap mDrawBmp = JaneBitmapFactory.decodeResource(context,
                Icon_Draw_Res[icon_type.ordinal()],
                new ImageSize(76, 76));
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        canvas.drawBitmap(mDrawBmp,
                InputDraw_point[label_type.ordinal()].x,
                InputDraw_point[label_type.ordinal()].y, paint);

        if (!xinvert) {
            int x = InputDraw_editPoint[label_type.ordinal()].x;
            int y = InputDraw_editPoint[label_type.ordinal()].y
                    + Math.abs((int) paint.getFontMetrics().ascent);
            canvas.drawText(text, x, y, paint);
            return resultBmp;
        } else {
            matrix.reset();
            matrix.postScale(-1, 1);
            Bitmap currentBitmap = Bitmap.createBitmap(resultBmp, 0, 0, resultBmp.getWidth(), resultBmp.getHeight(), matrix, true);
            canvas = new Canvas(currentBitmap);
            canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
            int x = bmp.getWidth() - InputDraw_editPoint[label_type.ordinal()].x; //反向
            int y = InputDraw_editPoint[label_type.ordinal()].y
                    + Math.abs((int) paint.getFontMetrics().ascent);
            canvas.drawText(text, x, y, paint);
            return currentBitmap;
        }
    }

    public Bitmap decodeLabel(boolean xinvert) {
        String text = getEditLabelText();
        TextPaint paint = new TextPaint();
        paint.setAntiAlias(true);
        int count = text.length();
        float[] widths = new float[count];
        Matrix matrix = new Matrix();
        paint.setTextSize(53);
        paint.setColor(Color.WHITE);
        paint.getTextWidths(text, widths);
        float TextW = 0;
        for (int c = 0; c < count; c++) {
            TextW += widths[c];
        }
        int textLenngth = (int) TextW;
        Bitmap bmp = BitmapFactory.decodeResource(
                getResources(), DrawInput_Res[label_type.ordinal()]);
        NinePatch patch = new NinePatch(
                bmp, bmp.getNinePatchChunk(), null);
        Bitmap resultBmp = Bitmap.createBitmap(
                bmp.getWidth() + textLenngth, bmp.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(resultBmp);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(
                0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        patch.draw(canvas, new Rect(
                0, 0, resultBmp.getWidth(), resultBmp.getHeight()));
        Bitmap mDrawBmp = JaneBitmapFactory.decodeResource(getContext(),
                Icon_Draw_Res[icon_type.ordinal()],
                new ImageSize(76, 76));
        paint.setFilterBitmap(true);
        canvas.drawBitmap(mDrawBmp,
                InputDraw_point[label_type.ordinal()].x,
                InputDraw_point[label_type.ordinal()].y, paint);
        if (xinvert) {
            matrix.reset();
            matrix.postScale(-1, 1);
            Bitmap currentBitmap = Bitmap.createBitmap(
                    resultBmp, 0, 0, resultBmp.getWidth(),
                    resultBmp.getHeight(), matrix, true);
            canvas = new Canvas(currentBitmap);
            canvas.setDrawFilter(new PaintFlagsDrawFilter(0,
                    Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
            int x = bmp.getWidth() - InputDraw_editPoint[label_type.ordinal()].x; //反向
            int y = InputDraw_editPoint[label_type.ordinal()].y
                    + Math.abs((int) paint.getFontMetrics().ascent);
            canvas.drawText(text, x, y, paint);
            return currentBitmap;
        } else {
            int x = InputDraw_editPoint[label_type.ordinal()].x;
            int y = InputDraw_editPoint[label_type.ordinal()].y
                    + Math.abs((int) paint.getFontMetrics().ascent);
            canvas.drawText(text, x, y, paint);
        }
        return resultBmp;
    }

    //类型
    public void changeLabelType(ICON_TYPE icon_type) {
        assert null != icon_type;
        this.icon_type = icon_type;
        if (mIconBmp != null && !mIconBmp.isRecycled()) {
            mIconBmp.recycle();
        }
        mIconBmp = JaneBitmapFactory.decodeResource(getContext(),
                Icon_Res[icon_type.ordinal()],
                new ImageSize(Utils.getRealPixel3(52),
                        Utils.getRealPixel3(52)));
        mIcon.setImageBitmap(mIconBmp);
    }

    //样式
    public void changeLabelStyle(LABEL_TYPE label_type) {
        assert null != label_type;
        this.label_type = label_type;
        if (mInputBmp != null && !mInputBmp.isRecycled()) {
            mInputBmp.recycle();
        }
        mInputBmp = JaneBitmapFactory.decodeResource(getContext(),
                Input_Res[label_type.ordinal()],
                new ImageSize(Utils.getRealPixel3(620),
                        Utils.getRealPixel3(192)));
        mInputBgIv.setImageBitmap(mInputBmp);

        LayoutParams mRparams = (LayoutParams) mIcon.getLayoutParams();
        mRparams.setMargins(
                Utils.getRealPixel3(Input_point[label_type.ordinal()].x),
                Utils.getRealPixel3(Input_point[label_type.ordinal()].y), 0, 0);
        mIcon.setLayoutParams(mRparams);
        invalidate();

    }

    public String getEditLabelText() {
        if (label_edit != null && label_edit.getText().toString().trim().length() != 0) {
            return label_edit.getText().toString().trim();
        } else {
            return null;
        }
    }

    public ICON_TYPE getIcon_type() {
        return this.icon_type;
    }

    public LABEL_TYPE getLabel_type() {
        return this.label_type;
    }

    public void setEditText(String editText) {
        if (label_edit != null && !TextUtils.isEmpty(editText)) {
            this.text = editText;
            label_edit.getText().insert(0, this.text);
            edit_type = true;
        }
    }

    public boolean isEdit_type() {
        return edit_type;
    }

    public enum LABEL_TYPE {
        LABEL_TYPE_1,
        LABEL_TYPE_2,
        LABEL_TYPE_3,
        LABEL_TYPE_4,
        DEFAULT
    }

    //img width: 646px height 200px
    public static int Input_Res[] = {
            R.drawable.puzzle_input_type_1,
            R.drawable.puzzle_input_type_2,
            R.drawable.puzzle_input_type_3,
            R.drawable.puzzle_input_type_4,
    };

    public enum ICON_TYPE {
        BRAND,
        LOCATION,
        CHARACTER,
        PRICE,
        WEATHER,
        CUSTOMIZE,
        DEFAULT
    }

    //img width: 52px height 52px
    public static int Icon_Res[] = {
            R.drawable.puzzle_label_brand,
            R.drawable.puzzle_label_location,
            R.drawable.puzzle_label_character,
            R.drawable.puzzle_label_price,
            R.drawable.puzzle_label_weather,
            R.drawable.puzzle_label_customize
    };

    public static Point Input_point[] = {
            new Point(-1, 71),
            new Point(-1, 71 + 46),
            new Point(-1, 5),
            new Point(-1, 71),
    };

    public static Point Input_editPoint[] = {
            new Point(100, 55),//原y值60 anson
            new Point(100, 55),
            new Point(100, 55),
            new Point(100, 55),
    };

    public static int[] DrawInput_Res = {
            R.drawable.puzzle_draw_input_type_1,
            R.drawable.puzzle_draw_input_type_2,
            R.drawable.puzzle_draw_input_type_3,
            R.drawable.puzzle_draw_input_type_4
    };

    public static Point[] InputDraw_editPoint = {
            new Point(192, 132),
            new Point(178, 132),
            new Point(136, 132),
            new Point(165, 132)
    };

    public static Point InputDraw_point[] = {
            new Point(0, 122),
            new Point(0, 248),
            new Point(0, 23),
            new Point(0, 124),
    };

    //img width: 76px height 76px
    public static int Icon_Draw_Res[] = {
            R.drawable.puzzle_label_draw_brand,
            R.drawable.puzzle_label_draw_location,
            R.drawable.puzzle_label_draw_character,
            R.drawable.puzzle_label_draw_price,
            R.drawable.puzzle_label_draw_weather,
            R.drawable.puzzle_label_draw_customize
    };
}
