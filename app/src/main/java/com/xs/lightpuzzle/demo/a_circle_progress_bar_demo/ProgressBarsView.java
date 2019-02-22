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
 * @ClassName: ChildProgressBar 
 * @Description:圆环动态加载进度条自定义控件，封装控件
 * @author urnot_XS
 * @date 2015-10-12 下午2:00:33 
 *  
 */
public class ProgressBarsView extends RelativeLayout {

	/**
	 * 圆环内的字符内容
	 * */
	private String text = "";

	/**
	 * 圆环的颜色 ，底色
	 */
	private int roundColor;

	/**
	 * 圆环进度的颜色
	 */
	private int roundProgressColor;

	/**
	 * 中间进度百分比的字符串的颜色
	 */
	private int textColor;

	/**
	 * 中间进度百分比的字符串的字体 大小
	 */
	private float textSize;

	/**
	 * 圆环的宽度
	 */
	private float roundWidth;

	/**
	 * 最大进度
	 */
	private int max;

	/**
	 * 当前进度
	 */
	private int progress;

	/**
	 * 圆环动画速度
	 * */
	private int rate;
	/**
	 * 是否显示中间的进度
	 */
	private boolean textIsDisplayable;

	private ChildProgressBar progressBar;
	private Context mContext;
	
	List<ViewHolder> list = new ArrayList<ViewHolder>();

	public ProgressBarsView(Context context) {
		this(context, null);
	}

	public ProgressBarsView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);

	}

	public ProgressBarsView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
				R.styleable.ProgressBarsView);

		// 获取自定义属性和默认值
		roundColor = mTypedArray.getColor(R.styleable.ProgressBarsView_roundcolor,
				Color.GRAY);
		textColor = mTypedArray.getColor(R.styleable.ProgressBarsView_textcolor,
				Color.WHITE);
		rate = mTypedArray.getInteger(R.styleable.ProgressBarsView_rate, 50);

		mContext = context;
	}

	private final class ViewHolder {

		boolean isShowTextHint;
		String text = "";
		int roundColor;
		int roundProgressColor;
		int textColor;
		float textSize;
		float roundWidth;
		int max;
		int progress;
		int rate;
		boolean textIsDisplayable;
	}

	public void addProgressBar(String text, int roundProgressColor,
			float roundWidth, int max, int progress, boolean textIsDisplayable, boolean isShowTextHint) {

		ViewHolder holder = new ViewHolder();
		holder.isShowTextHint = isShowTextHint;
		holder.text = text;
		holder.roundColor = roundColor;
		holder.roundProgressColor = roundProgressColor;
		holder.textColor = textColor;
		holder.textSize = textSize;
		holder.roundWidth = roundWidth;
		holder.max = max;
		holder.progress = progress;
		holder.rate = rate;
		holder.textIsDisplayable = textIsDisplayable;
		list.add(holder);
		
	}

	public void clearProgressBar()
	{
		list.clear();
	}
	
	public void showProgress() {

		this.removeAllViews();
		
		for (int i = 0; i < list.size(); i++) {
			ViewHolder holder = list.get(i);
			progressBar = new ChildProgressBar(mContext);

			progressBar.setShowTextHint(holder.isShowTextHint); // 绘制与圆顶部平行的文字和圆点
			progressBar.setText(holder.text);
			progressBar.setTextColor(holder.textColor);
			progressBar.setTextSize(holder.textSize);
			progressBar.setCricleColor(holder.roundColor);
			progressBar.setCricleProgressColor(holder.roundProgressColor);
			progressBar.setRoundWidth(holder.roundWidth);
			progressBar.setProgress(holder.progress);
			progressBar.setMax(holder.max);
			progressBar.setRate(holder.rate);
			progressBar.setTextIsDisplayable(holder.textIsDisplayable);

			float width = 0;// 用来记录前两者总宽度
			if (i != 0) {
				for (int j = 0; j < i; j++) {
					ViewHolder viewholder = list.get(j);
					width = viewholder.roundWidth + width;
				}
			}

			LayoutParams lp1 = new LayoutParams(
					android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT);

			lp1.setMargins((int) width + 10 * i, (int) width + 10 * i,
					(int) width + 10 * i, (int) width + 10 * i);

			this.addView(progressBar, lp1);
			
			progressBar.updateBar();
		}
	}

	

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getRoundColor() {
		return roundColor;
	}

	public void setRoundColor(int roundColor) {

		this.roundColor = roundColor;
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

	public float getTextSize() {
		return textSize;
	}

	public void setTextSize(float textSize) {
		this.textSize = textSize;
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

	public boolean isTextIsDisplayable() {
		return textIsDisplayable;
	}

	public void setTextIsDisplayable(boolean textIsDisplayable) {
		this.textIsDisplayable = textIsDisplayable;
	}
}
