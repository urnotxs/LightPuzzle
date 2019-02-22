package com.xs.lightpuzzle.demo.a_circle_progress_bar_demo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.xs.lightpuzzle.R;

/**
 * @ClassName: ChildProgressBar 
 * @Description:圆环动态加载进度条自定义控件
 * @author urnot_XS
 * @date 2015-10-10 下午2:00:33 
 *  
 */
public class ChildProgressBar extends View {
	private boolean isShowTextHint = true;

	/**
	 * 圆环内字体和与圆顶部平行的字体大小的差值
	 * */
	private int lessSize;

	/**
	 * 圆环内的字符内容
	 * */
	private String text = "";

	/**
	 * 画笔对象的引用
	 */
	private Paint paint;

	/**
	 * 圆环的颜色
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
	 * 中间进度百分比的字符串的字体
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
	 * 是否显示中间的进度
	 */
	private boolean textIsDisplayable;

	/**
	 * 进度的风格，实心或者空心
	 */
	private int style;

	private int distance = 15;

	public static final int STROKE = 0;
	public static final int FILL = 1;

	/**
	 * 记录当前所画的每小块圆弧个数
	 * */
	private int count = 0 ;

	/**
	 * 记录还没画出的圆弧进度
	 * */
	private int reverse_pro;

	/**
	 * 圆环动画速度
	 * */
	private int rate;

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				if(reverse_pro!=0)
					count++;
				reverse_pro--;
				postInvalidate();// progress每增加一格就刷新一次界面，用count来记录弧度单元格个数
				if (reverse_pro > 0) {
					Message message = handler.obtainMessage(1);
					handler.sendMessageDelayed(message, rate);
				}
				break;
			}
			super.handleMessage(msg);
		}
	};

	public ChildProgressBar(Context context) {
		this(context, null);
	}

	public ChildProgressBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ChildProgressBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		paint = new Paint();

		TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
				R.styleable.ChildProgressBar);

		// 获取自定义属性和默认值
		roundColor = mTypedArray.getColor(
				R.styleable.ChildProgressBar__roundColor, Color.GRAY);
		roundProgressColor = mTypedArray.getColor(
				R.styleable.ChildProgressBar__roundProgressColor, Color.RED);
		textColor = mTypedArray.getColor(
				R.styleable.ChildProgressBar__textColor, Color.WHITE);
		roundWidth = mTypedArray.getDimension(
				R.styleable.ChildProgressBar__roundWidth, 10);
		max = mTypedArray.getInteger(R.styleable.ChildProgressBar__max, 100);
		textIsDisplayable = mTypedArray.getBoolean(
				R.styleable.ChildProgressBar__textIsDisplayable, true);
		style = mTypedArray.getInt(R.styleable.ChildProgressBar__style, 0);

		mTypedArray.recycle();

	}

	/**
	 * 将圆形进度框设置为动态增长
	 * */
	public void updateBar() {

		Message message = handler.obtainMessage(1);
		handler.sendMessageDelayed(message, rate);

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int centre = getWidth() / 2; // 获取圆心的x坐标
		int hcentre = getHeight() / 2;
		int radius;
		if (centre>hcentre)
		{
			radius = (int) (hcentre - roundWidth); // 圆环的半径 减10的目的是为了让字体
		}
		else
		{
			radius = (int) (centre - roundWidth) ; // 圆环的半径 减10的目的是为了让字体
		}
		lessSize = 20;
		textSize = roundWidth / 2 + 35;//根据画笔宽度改变字体大小
		/**
		 * 画最外层的大圆环
		 */
		paint.setColor(roundColor); // 设置圆环的颜色
		paint.setStyle(Paint.Style.STROKE); // 设置空心
		paint.setStrokeWidth(roundWidth); // 设置圆环的宽度
		paint.setAntiAlias(true); // 消除锯齿
		canvas.drawCircle(centre, hcentre, radius, paint); // 画出圆环

		Log.e("log", centre + "");

		/**
		 * 画进度百分比
		 */
		paint.setStrokeWidth(2);
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		paint.setColor(Color.WHITE);
		paint.setTextSize(textSize-10);
		paint.setTypeface(Typeface.DEFAULT); // 设置字体

		float p = 0;
		if (max != 0)
			p = ((float) progress / (float) max) * 100.0f;
		
		String percent = String.valueOf(p); // 中间的进度百分比，先转换成float在进行除法运算，不然都为0
		percent = percent.substring(0, percent.indexOf(".") + 1)
				+ percent.substring(percent.indexOf(".") + 1,
						percent.indexOf(".") + 2) + "%";
		float percentWidth = paint.measureText(percent);// 测量字体宽度，我们需要根据字体的宽度设置在圆环中间

		if (textIsDisplayable && style == STROKE) {
			canvas.drawText(percent, centre - percentWidth / 2, hcentre, paint); // 画出进度百分比
		}

		paint.setTextSize(textSize - lessSize); // 改变画笔字体大小格式
		paint.setStrokeWidth(0);
		String s = text + "率";
		float textWidth = paint.measureText(s); // 测量字体宽度，我们需要根据字体的宽度设置在圆环中间

		if (textIsDisplayable && style == STROKE) {
			canvas.drawText(s, centre - textWidth / 2, hcentre
					+ (textSize - lessSize/2), paint); // 画出进度百分比
		}

		/**
		 * 画圆弧 ，画圆环的进度
		 */

		// 设置进度是实心还是空心
		paint.setStrokeWidth(roundWidth); // 设置圆环的宽度
		paint.setColor(roundProgressColor); // 设置进度的颜色
		RectF oval = new RectF(centre - radius, hcentre - radius, centre
				+ radius, hcentre + radius); // 用于定义的圆弧的形状和大小的界限

		switch (style) {
		case STROKE: {
			paint.setStyle(Paint.Style.STROKE);
			if (max != 0) {
				if(progress==0){
					canvas.drawArc(oval, -90, 1, false, paint);
				}
				canvas.drawArc(oval, -90, (count) * 360 / max, false, paint); // 根据进度画圆弧

			} else
				canvas.drawArc(oval, 0, 0, false, paint); // 根据进度画圆弧
			break;
		}
		case FILL: {
			paint.setStyle(Paint.Style.FILL_AND_STROKE);
			if (progress != 0 && max != 0){
				if(progress==0){
					canvas.drawArc(oval, -90, 1,true, paint);
				}
				canvas.drawArc(oval, -90, (count) * 360 / max, true, paint); // 根据进度画圆弧
			}
			else
				canvas.drawArc(oval, 0, 0, true, paint); // 根据进度画圆弧
			break;
		}
		}
		if (isShowTextHint) {
			/**
			 * 画与进度弧颜色相同的圆点
			 * */
			paint.setColor(roundProgressColor);
			paint.setStyle(Paint.Style.FILL);
			paint.setStrokeWidth(roundWidth / 2);


			canvas.drawCircle(centre - 6* distance,
					hcentre - radius + distance / 2, distance /3, paint);


			/**
			 * 画比赛状态：比赛场数
			 * */
			paint.setStrokeWidth(0);
			paint.setColor(textColor);// 设置字体
			canvas.drawText(text + progress, centre - 5 * distance, hcentre
					+ (textSize - lessSize) / 2 - radius, paint); // 画出进度百分比

		}
	}

	public synchronized int getMax() {
		return max;
	}

	/**
	 * 设置进度的最大值
	 * 
	 * @param max
	 */
	public synchronized void setMax(int max) {
		if (max < 0) {
			throw new IllegalArgumentException("max not less than 0");
		}
		this.max = max;
	}

	/**
	 * 获取进度.需要同步
	 * 
	 * @return
	 */
	public synchronized int getProgress() {
		return progress;
	}

	/**
	 * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步 刷新界面调用postInvalidate()能在非UI线程刷新
	 * 
	 * @param progress
	 */
	public synchronized void setProgress(int progress) {
		if (progress < 0) {
			throw new IllegalArgumentException("progress not less than 0");
		}
		if (progress > max) {
			progress = max;
		}
		if (progress <= max) {
			this.progress = progress;
			reverse_pro = progress;// 将传进来的进程数传给用来记录当前圆环的比率
			postInvalidate();
		}
	}

	public int getCricleColor() {
		return roundColor;
	}

	public void setCricleColor(int cricleColor) {
		this.roundColor = cricleColor;
	}

	public int getCricleProgressColor() {
		return roundProgressColor;
	}

	public void setCricleProgressColor(int cricleProgressColor) {
		this.roundProgressColor = cricleProgressColor;
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

	public void setText(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
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

	public boolean isShowTextHint() {
		return isShowTextHint;
	}

	public void setShowTextHint(boolean showTextHint) {
		isShowTextHint = showTextHint;
	}
}
