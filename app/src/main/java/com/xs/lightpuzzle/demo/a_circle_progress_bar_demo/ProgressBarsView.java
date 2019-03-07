package com.xs.lightpuzzle.demo.a_circle_progress_bar_demo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.xs.lightpuzzle.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author urnot_XS
 * @ClassName: ChildProgressBar
 * @Description:圆环动态加载进度条自定义控件，封装控件
 * @date 2015-10-12 下午2:00:33
 */
public class ProgressBarsView extends RelativeLayout {

    /**
     * 是否绘制与圆顶部齐平的文字和圆点
     */
    private boolean isShowTopTextAndPoint;
    /**
     * 是否只显示第一条进度条的中间文字
     */
    private boolean onlyFirstShowCenter;

    /**
     * 圆环的底部轨道颜色
     */
    private int progressTrackColor;

    /**
     * 中间进度百分比的字符串的颜色
     */
    private int textColor;

    /**
     * 中间进度百分比的字符串的字体 大小
     */
//	private float textSize;

    /**
     * 圆环的宽度
     */
    private float roundWidth;

    /**
     * 每条子圆环间距
     */
    private float progressGap;

    /**
     * 圆环动画速度
     */
    private int rate;

    /**
     * 圆环内的字符内容
     */
    private String text = "";
    /**
     * 圆环进度的颜色
     */
    private int roundProgressColor;

    /**
     * 最大进度
     */
    private int max;

    /**
     * 当前进度
     */
    private int progress;

    private Context mContext;

    private List<ViewHolder> list = new ArrayList<>();

    public ProgressBarsView(Context context) {
        this(context, null);
    }

    public ProgressBarsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressBarsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.ProgressBarsView);
        // 获取自定义属性和默认值
        progressTrackColor = mTypedArray.getColor(R.styleable.ProgressBarsView_progressTrackColor, Color.GRAY); // 每条进度底部轨道颜色一致
        textColor = mTypedArray.getColor(R.styleable.ProgressBarsView_textColor, Color.WHITE); // 每条进度文字颜色一致
        rate = mTypedArray.getInteger(R.styleable.ProgressBarsView_rate, 50); // 每条进度刷新速率一致
        progressGap = mTypedArray.getDimension(R.styleable.ProgressBarsView_progressGap, 10); // 每条进度彼此间隔一致
        roundWidth = mTypedArray.getDimension(R.styleable.ProgressBarsView_roundWidth, 20); // 每条进度宽度一致
        onlyFirstShowCenter = mTypedArray.getBoolean(R.styleable.ProgressBarsView_onlyFirstShowCenter, true); // 总体是否只显示第一条进度的中心文案
        isShowTopTextAndPoint = mTypedArray.getBoolean(R.styleable.ProgressBarsView_isShowTopTextAndPoint, true); // 每条进度是否显示顶部齐平的文案和圆点
    }

    public void addProgressBar(String text, int progressColor, int maxProgress, int progress) {
        ViewHolder holder = new ViewHolder();
        holder.rate = rate;
        holder.roundWidth = roundWidth;
        holder.progressTrackColor = progressTrackColor;
        holder.isShowTopTextAndPoint = isShowTopTextAndPoint;
        holder.textColor = textColor;

        holder.max = maxProgress;
        holder.progress = progress;
        holder.text = text;
        holder.roundProgressColor = progressColor;
        list.add(holder);
    }

    public void showProgress() {
        this.removeAllViews();

        for (int i = 0; i < list.size(); i++) {
            ViewHolder holder = list.get(i);
            ChildProgressBar progressBar = new ChildProgressBar(mContext);
            progressBar.setText(holder.text);
            progressBar.setTextColor(holder.textColor);
            progressBar.setCricleColor(holder.progressTrackColor);
            progressBar.setCricleProgressColor(holder.roundProgressColor);
            progressBar.setRoundWidth(holder.roundWidth);
            progressBar.setRate(holder.rate);
            progressBar.setMax(holder.max);
            progressBar.setProgress(holder.progress);
            progressBar.setTextIsDisplayable(i == 0 && onlyFirstShowCenter); // 是否绘制圆中心文案
            progressBar.setShowTextHint(holder.isShowTopTextAndPoint); // 是否绘制与圆顶部平行的文字和圆点

            float totalMargin = (roundWidth + progressGap) * i;
            LayoutParams params = new LayoutParams(
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT);
            params.setMargins((int) totalMargin, (int) totalMargin,
                    (int) totalMargin, (int) totalMargin);
            this.addView(progressBar, params);

            progressBar.updateBar();
        }
    }

    public void clearProgressBar() {
        list.clear();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getProgressTrackColor() {
        return progressTrackColor;
    }

    public void setProgressTrackColor(int progressTrackColor) {

        this.progressTrackColor = progressTrackColor;
    }

    public int getRoundProgressColor() {
        return roundProgressColor;
    }

    public void setRoundProgressColor(int roundProgressColor) {
        this.roundProgressColor = roundProgressColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public float getRoundWidth() {
        return roundWidth;
    }

    public void setRoundWidth(float roundWidth) {
        this.roundWidth = roundWidth;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    private final class ViewHolder {
        int rate;
        float roundWidth;
        int progressTrackColor;
        boolean isShowTopTextAndPoint;
        int textColor;
        float textSize;

        int max;
        int progress;
        String text = "";
        int roundProgressColor;
    }
}
