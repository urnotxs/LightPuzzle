package com.xs.lightpuzzle.puzzle.info.low;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;

import com.xs.lightpuzzle.LightPuzzleConstant;
import com.xs.lightpuzzle.data.DataConstant;
import com.xs.lightpuzzle.puzzle.data.editdata.TemporaryTextData;
import com.xs.lightpuzzle.puzzle.data.lowdata.TextData;
import com.xs.lightpuzzle.puzzle.info.DrawView;
import com.xs.lightpuzzle.puzzle.msgevent.PuzzlesRequestMsg;
import com.xs.lightpuzzle.puzzle.msgevent.code.PuzzlesRequestMsgName;
import com.xs.lightpuzzle.puzzle.util.PuzzleTextUtils;
import com.xs.lightpuzzle.puzzle.util.PuzzlesUtils;
import com.xs.lightpuzzle.puzzle.util.ShapeUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by xs on 2018/4/12.
 */

public class PuzzlesTextInfo implements DrawView {

    private Rect rect;
    //保存时用到的rect
    private Rect outPutRect;
    //官方数据
    private TextData textData;

    private Point[] drawPoint;

    private transient Rect drawRect, calculationRect;

    private transient boolean save;

    private int id;

    private LinkedList<String> lineStrs;

    private int scrollY;

    private transient Paint paint;

    private String alignment = "LEFT";
    //当前的行间距
    private int lineSpace;
    //最大行距
    private int maxLineSpace;
    //当前字体大小
    private int fontSize;
    //最小字体大小
    private int minFontSize;
    //最大字体大小
    private int maxFontSize;
    //当前字体颜色
    private int fontColor;
    //当前字体
    private String font;
    //是否是下载的字体
    private boolean downloadFont = true;
    //输入文本
    private String autoStr = "";

    private float fontScale;

    private transient boolean actionDown;

    public void setRect(Rect rect) {
        this.rect = rect;
    }

    public Rect getRect() {
        return rect;
    }

    public void setTextData(TextData textData) {
        this.textData = textData;
    }

    public TextData getTextData() {
        return textData;
    }

    public int getMinFontSize() {
        return minFontSize;
    }

    public int getMaxFontSize() {
        return maxFontSize;
    }

    public Point[] getDrawPoint() {
        return drawPoint;
    }

    public void setSave(boolean save) {
        this.save = save;
    }

    public void setOutPutRect(Rect outPutRect) {
        this.outPutRect = outPutRect;
    }

    public String getAutoStr() {
        return autoStr;
    }

    public void setDownloadFont(boolean downloadFont) {
        this.downloadFont = downloadFont;
    }

    public boolean isDownloadFont() {
        return downloadFont;
    }

    public String getFont() {
        return font;
    }

    public int getFontColor() {
        return fontColor;
    }

    public int getFontSize() {
        return fontSize;
    }

    public String getTextType() {
        return textData == null ? null : textData.getTextType();
    }

    public PointF[] getTextPoint() {
        return textData == null ? null : textData.getPolygons();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void changeFontColor(int fontColor) {
        if (fontColor == LightPuzzleConstant.INVALID_COLOR) {
            return;
        }
        this.fontColor = fontColor;
        if (paint != null) {
            paint.setColor(fontColor);
        }
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
        this.maxFontSize = fontSize;
        this.minFontSize = fontSize;
        if (paint != null) {
            paint.setTextSize(fontSize);
        }
        lineStrs = getLineStrs();
    }

    public void setFont(String font) {
        this.font = font;
        lineStrs = getLineStrs();
    }

    public void setFont(Context context, String font) {
        if (TextUtils.isEmpty(font) || font.equals(this.font)) {
            return;
        }
        this.font = font;
        if (!TextUtils.isEmpty(font)) {
            Typeface typeface = PuzzleTextUtils.readFont(context, font, true);
            paint.setTypeface(typeface);
        }
        lineStrs = getLineStrs();
    }

    /**
     * 有字符串输入时，需要重设最大最小字号
     *
     * @param autoStr
     */

    public void setAutoStr(String autoStr) {
        if (autoStr != null && this.autoStr != null && autoStr.equals(this.autoStr)) {
            return;
        }
        this.autoStr = autoStr;
        if (textData != null) {
            maxFontSize = (int) ((textData.getMaxFontSize() / 2) * fontScale);
            minFontSize = (int) ((textData.getMinFontSize() / 2) * fontScale);
        }
        lineStrs = getLineStrs();
    }

    @Override
    public void init() {
        if (textData == null) {
            return;
        }
        Rect finalRect = null;
        if (save) {
            if (outPutRect == null) {
                return;
            }
            finalRect = outPutRect;
            if (rect != null) {
                float scale = (float) outPutRect.width() / (float) rect.width();
                fontSize = (int) (fontSize * scale);
                maxFontSize = (int) (maxFontSize * scale);
                minFontSize = (int) (minFontSize * scale);
                lineSpace = (int) (lineSpace * scale);
                maxLineSpace = (int) (maxLineSpace * scale);
            }
        } else {
            if (rect == null) {
                return;
            }
            finalRect = rect;
            fontColor = textData.getFontColor();
            if (!TextUtils.isEmpty(textData.getFont())) {
                font = DataConstant.DIR_PATH.FONT + File.separator + textData.getTypefaceId() + File.separator + textData.getFont();
            }
            if (!TextUtils.isEmpty(textData.getAutoStr())) {
                autoStr = textData.getAutoStr();
            }
            if (textData.getAlignment() != null) {
                alignment = textData.getAlignment();
            }
            //进来的时候默认读取最大的字号
            if (textData.getMaxFontSize() != 0 && textData.getLayoutWidth() != 0) {
                fontScale = ((float) finalRect.width() / (float) textData.getLayoutWidth());
                fontSize = (int) ((textData.getMaxFontSize() / 2) * fontScale);
                maxFontSize = (int) ((textData.getMaxFontSize() / 2) * fontScale);
                minFontSize = (int) ((textData.getMinFontSize() / 2) * fontScale);
            }
            if (textData.getLineSpace() != 0 && textData.getLayoutWidth() != 0) {
                lineSpace = (int) ((textData.getLineSpace() / 2) * ((float) finalRect.width() / (float) textData.getLayoutWidth()));
                maxLineSpace = (int) ((textData.getLineSpace() / 2) * ((float) finalRect.width() / (float) textData.getLayoutWidth()));
            }
        }
        if (finalRect == null) {
            return;
        }
        //内部生成画笔
        if (paint == null) {
            paint = new Paint();
            paint.setAntiAlias(true);
        }
        //第一步, 先计算可绘制区域, 根据设计的数据进行绘图数据转换
        drawPoint = ShapeUtils.makePts(textData.getPolygons(), finalRect);
        drawRect = ShapeUtils.makeRect(drawPoint);

        setAutoStr(autoStr);
    }

    @Override
    public void initBitmap(Context context) {
        if (context == null) {
            return;
        }
        if (drawPoint == null) {
            return;
        }
        if (drawRect == null) {
            return;
        }
        if (paint == null) {
            paint = new Paint();
        }
        paint.reset();
        paint.setColor(fontColor);
        if (paint != null) {
            paint.setTextSize(fontSize);
        }
        if (!TextUtils.isEmpty(font)) {
            Typeface typeface = PuzzleTextUtils.readFont(context, font, true);
            paint.setTypeface(typeface);
        }
        //由于部分字体会出现显示不全，因此加大显示区域
        calculationRect = new Rect(drawRect.left, drawRect.top, drawRect.right,
                (int) (drawRect.bottom + paint.getFontMetrics().descent));
    }

    @Override
    public void draw(Canvas canvas) {
        if (canvas == null) {
            return;
        }
        if (drawRect == null) {
            return;
        }
        if (calculationRect == null) {
            return;
        }
        //在开启debug模式下，可以看到文字的最大绘制区域
        /*if (AppConfiguration.getDefault().isDebugMode()) {
            paint.setColor(PuzzlesUtils.strColor2Int("00ff00"));
            canvas.drawRect(drawRect, paint);
            paint.setColor(fontColor);
        }*/
        if (lineStrs == null || (lineStrs.size() == 0 && !TextUtils.isEmpty(autoStr))) {
            lineStrs = getLineStrs();
        }
        if (autoStr != null) {
            //每一行所绘制的行数
            if (lineStrs != null && lineStrs.size() > 0) {
                for (int i = 0; i < lineStrs.size(); i++) {
                    String line = lineStrs.get(i);
                    float start_x = calculationRect.left;
                    int space = lineSpace;
                    if (lineSpace < 0) {
                        // 防止有些行间距被设计设置为负数，导致文字重叠，eg:ID 118
                        space = 0;
                    }
                    int textHeight = (int) (paint.descent() - paint.ascent());
                    float start_y = calculationRect.top + (space + textHeight + paint.getFontMetrics().leading) * i + (-paint.ascent());
                    float lineWidth = paint.measureText(line);
                    if (alignment.equals("RIGHT")) {
                        start_x += calculationRect.width() - lineWidth;
                    } else if (alignment.equals("CENTER")) {
                        start_x += (calculationRect.width() - lineWidth) / 2;
                    }
                    canvas.drawText(line, start_x, start_y, paint);
                }
            }
        }
    }

    private void resetInit(){
        if (textData == null) {
            return;
        }

        if (rect == null) {
            return;
        }

        //第一步, 先计算可绘制区域, 根据设计的数据进行绘图数据转换
        drawPoint = ShapeUtils.makePts(textData.getPolygons(), rect);
        drawRect = ShapeUtils.makeRect(drawPoint);

        setAutoStr(autoStr);
    }

    /**
     *模板上下移动，切换子模板的时候调用
     * @param scrollYOffset 与原位置的偏移量
     **/
    public void translateOffset(int scrollYOffset) {
        resetInit();
        if(calculationRect != null){
            calculationRect.top += scrollYOffset;
        }
    }

    /**
     * 获取文字生成的bitmap，用于视频模板
     *
     * @param path
     * @return
     */

    public Bitmap getTextBitmap(String path) {
        LinkedList<String> lineStrs = getLineStrs();
        if (lineStrs.size() > 0) {
            if (drawRect != null) {
                Bitmap bitmap = Bitmap.createBitmap(drawRect.width(), drawRect.height(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
                        | Paint.FILTER_BITMAP_FLAG));
       /*         if (AppConfiguration.getDefault().isDebugMode()) {
                    Paint paint = new Paint();
                    paint.setColor(PuzzlesUtils.strColor2Int("00ff00"));
                    canvas.drawRect(drawRect, paint);
                }*/
                for (int i = 0; i < lineStrs.size(); i++) {
                    String line = lineStrs.get(i);
                    float start_x = 0;
                    float start_y = (lineSpace + paint.descent()) * i - paint.ascent() * (i + 1);
                    float lineWidth = paint.measureText(line);
                    if (alignment.equals("Right")) {
                        start_x += drawRect.width() - lineWidth;
                    } else if (alignment.equals("Center")) {
                        start_x += (drawRect.width() - lineWidth) / 2;
                    }
                    canvas.drawText(line, start_x, start_y, paint);
                }
//                FileUtils.write2SD(bitmap, path, true);
                return bitmap;
            }
        }
        return null;
    }

    /**
     * 获取所需要的行数
     *
     * @return
     */

    int eachLevelOffset = 5;
    public LinkedList<String> getLineStrs() {
        if (autoStr != null && calculationRect != null) {
            LinkedList<String> lineStrs = new LinkedList<>();
            //该字体大小下，一行文字的高度
            float lineHeight = paint.descent() - paint.ascent();

            String editStr = autoStr;
            float strWidth = 0;
            String lineStr = "";

            for (int i = 0; i < editStr.length(); i++) {

                char str = editStr.charAt(i);
                String charStr = String.valueOf(str);
                float charWidth = paint.measureText(charStr);

                // 是否为换行符
                if (!charStr.equals("\n")) {
                    // 若要添加当前字符，是否需要添加行数
                    if ((strWidth + charWidth) > calculationRect.width()) {
                        // 如果添加字符以后的宽度超过规定范围
                        // 先将 未加当前字符 的lineStr加入行数数组，而后判断高度是否超过最大高度
                        lineStrs.add(lineStr);// 添加行数就需要测量高度是否小于规定高度

                        int lineIndex = lineStrs.size();
                        if (lineIndex != 0) {
                            // 未加当前字符 的行数数组的总高度
                            int resultHeight = (int) (lineIndex * lineHeight + (lineIndex - 1) * lineSpace);

                            if (resultHeight > calculationRect.height()) {
                                // 如果超过了高度，调整字体大小
                                if (fontSize == minFontSize) {
                                    // 如果已经是最小字体了，去掉最后一行
                                    lineStrs.remove(lineStr);
                                    return lineStrs; // 递归算法的出口
                                } else {
                                    fontSize = fontSize - eachLevelOffset;
                                    if (fontSize < minFontSize) {
                                        fontSize = minFontSize;
                                    }
                                    lineSpace = lineSpace / 2;
                                    if (lineSpace < 2) {
                                        lineSpace = 2;
                                    }
                                    paint.setTextSize(fontSize);
                                    //重新获取队列
                                    Log.d("drawText", "1");
                                    return getLineStrs(); // 递归，以小一号的字体重新测量
                                }
                            }
                        }
                        // 另起新的一行，重置两个参数，
                        strWidth = charWidth;
                        lineStr = charStr;
                    } else {
                        // 为当前行数添加字符，累加行宽
                        strWidth += charWidth;
                        lineStr += charStr;
                    }
                    if (i == editStr.length() - 1) {
                        // 最后一个字符
                        lineStrs.add(lineStr);// 添加行数就需要测量高度是否小于规定高度

                        int lineIndex = lineStrs.size();
                        if (lineIndex != 0) {
                            // 添加了最后一行字符串的总高度
                            int resultHeight = (int) (lineIndex * lineHeight + (lineIndex - 1) * lineSpace);
                            // 如果改行字符串以后高度超过规定高度
                            if (resultHeight > calculationRect.height()) {
                                // 调整字体大小,最后一行不够位置绘制
                                if (fontSize == minFontSize) {
                                    // 如果已经是最小字体了
                                    lineStrs.remove(lineStr);
                                    return lineStrs;
                                } else {
                                    fontSize = fontSize - eachLevelOffset;
                                    if (fontSize < minFontSize) {
                                        fontSize = minFontSize;
                                    }
                                    lineSpace = lineSpace / 2;
                                    if (lineSpace < 2) {
                                        lineSpace = 2;
                                    }
                                    paint.setTextSize(fontSize);
                                    //重新获取队列
                                    Log.d("drawText", "2");
                                    return getLineStrs();// 递归，以小一号的字体重新测量
                                }
                            }
                        }
                        // 重新初始属性值
                        strWidth = 0;
                        lineStr = "";
                    }
                } else {
                    lineStrs.add(lineStr);// 添加行数就需要测量高度是否小于规定高度

                    int lineIndex = lineStrs.size();
                    if (lineIndex != 0) {
                        int resultHeight = (int) (lineIndex * lineHeight + (lineIndex - 1) * lineSpace);

                        if (resultHeight > calculationRect.height()) {
                            // 如果超过了高度
                            if (fontSize == minFontSize) {
                                // 如果已经是最小字体了
                                lineStrs.remove(lineStr);
                                return lineStrs;
                            } else {
                                fontSize = fontSize - eachLevelOffset;
                                if (fontSize < minFontSize) {
                                    fontSize = minFontSize;
                                }
                                lineSpace = lineSpace / 2;
                                if (lineSpace < 2) {
                                    lineSpace = 2;
                                }
                                paint.setTextSize(fontSize);
                                //重新获取队列
                                Log.d("drawText", "3");
                                return getLineStrs();
                            }
                        }
                    }
                    strWidth = 0;
                    lineStr = "";
                }
            }
            int lineSize = lineStrs.size();
            int resultHeight = (int) (lineSize * lineHeight + lineSpace * (lineSize - 1));
            //如果绘制区域的高度大于文字总高度
            if ((calculationRect.height() > resultHeight)) {
                if (fontSize == maxFontSize) {
                    return lineStrs;
                } else {
                    //那就先加5个size测测, 估算出新的字符串集合, 再测试行高
                    List<String> resultStrs = getDefultLines(fontSize + eachLevelOffset);
                    int endSpace = lineSpace * 2;
                    float endLineHeight = paint.descent() - paint.ascent();
                    int endHeight = (int) (resultStrs.size() * endLineHeight + endSpace * (resultStrs.size() - 1));
                    float maxWidth = 0;
                    for (int i = 0; i < resultStrs.size(); i++) {
                        if (maxWidth < paint.measureText(resultStrs.get(i))) {
                            maxWidth = paint.measureText(resultStrs.get(i));
                        }
                    }
                    if (calculationRect.height() > endHeight && calculationRect.width() > maxWidth) {
                        fontSize = fontSize + eachLevelOffset;
                        if (fontSize > maxFontSize) {
                            fontSize = maxFontSize;
                        }
                        lineSpace = lineSpace * 2 ;
                        if (lineSpace > maxLineSpace) {
                            lineSpace = maxLineSpace;
                        }
                        paint.setTextSize(fontSize);
                        //重新获取队列
                        Log.d("drawText", "4");
                        return getLineStrs();
                    } else {
                        paint.setTextSize(fontSize);
                        return lineStrs;
                    }
                }
            }
            return lineStrs;
        }
        return null;
    }

    /**
     * 根据字体大小获取字符串集合
     *
     * @param fontSize
     * @return
     */

    private LinkedList<String> getDefultLines(int fontSize) {
        if (autoStr != null) {
            paint.setTextSize(fontSize);
            LinkedList<String> lineStrs = new LinkedList<>();
            //该字体大小下一行字的高度
            String editStr = autoStr;
            float strWidth = 0;
            String lineStr = "";
            for (int i = 0; i < editStr.length(); i++) {
                char str = editStr.charAt(i);
                String charStr = String.valueOf(str);
                float charWidth = paint.measureText(charStr);
                if (!charStr.equals("\n")) {
                    if ((strWidth + charWidth) > calculationRect.width()) {
                        lineStrs.add(lineStr);
                        strWidth = charWidth;
                        lineStr = charStr;
                    } else {
                        strWidth += charWidth;
                        lineStr += charStr;
                    }
                    if (i == editStr.length() - 1) {
                        lineStrs.add(lineStr);
                        strWidth = 0;
                        lineStr = "";
                    }
                } else {
                    lineStrs.add(lineStr);
                    strWidth = 0;
                    lineStr = "";
                }
            }
            return lineStrs;
        }
        return null;
    }

    private TemporaryTextData getTemporaryTextData() {
        TextData data = new TextData();
        data.setMaxFontSize((int) ((this.textData.getMaxFontSize() / 2) * fontScale));
        data.setMinFontSize((int) ((this.textData.getMinFontSize() / 2) * fontScale));
        data.setFontSize(fontSize);
        data.setAutoStr(autoStr);
        data.setFontColor(fontColor);
        data.setFont(font);
        data.setNickname(this.textData.isNickname());
        data.setDownload(downloadFont);
        TemporaryTextData temporaryTextData = new TemporaryTextData(data, drawPoint);
        return temporaryTextData;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int actCode = event.getAction();
        List<Point> points = Arrays.asList(drawPoint);
        switch (actCode) {
            case MotionEvent.ACTION_DOWN:
                if (points != null && PuzzlesUtils.pointInRegion(points, new Point((int) event.getX(), (int) event.getY()))) {
                    actionDown = true;
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (actionDown && points != null && PuzzlesUtils.pointInRegion(points, new Point((int) event.getX(), (int) event.getY()))) {
                    actionDown = false;
                    if (System.currentTimeMillis() - lastTime > 2000) {
                        lastTime = System.currentTimeMillis();
                        TemporaryTextData temporaryTextData = getTemporaryTextData();
                        EventBus.getDefault().post(new PuzzlesRequestMsg(PuzzlesRequestMsgName.PUZZLES_TEXT_EDIT, actCode, temporaryTextData));
                    }
                    return true;
                }
                break;
        }
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return false;
    }

    @Override
    public void recycle() {

    }

    long lastTime;


}
