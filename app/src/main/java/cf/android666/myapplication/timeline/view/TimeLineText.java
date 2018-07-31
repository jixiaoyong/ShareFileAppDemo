package cf.android666.myapplication.timeline.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by jixiaoyong on 2018/7/24.
 * email:jixiaoyong1995@gmail.com
 */
public class TimeLineText extends TextView {

    private final int PADDING_RIGHT = 10;
    private final int POINT_WIDTH = 5;
    private Paint linePaint;
    private Paint pointPaint;
    private boolean needTop = true;
    private boolean needBottom = true;

    public TimeLineText(Context context) {
        this(context, null, 0);
    }

    public TimeLineText(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimeLineText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TimeLineText(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        linePaint = new Paint();
        pointPaint = new Paint();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setPadding(getPaddingLeft() + POINT_WIDTH * 3, getPaddingTop(),
                getPaddingRight(), getPaddingBottom());

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        linePaint.setColor(Color.BLACK);
        linePaint.setStrokeWidth(POINT_WIDTH / 2);
        linePaint.setAntiAlias(true);

        if (needTop) {
            canvas.drawLine(getLeft() + POINT_WIDTH * 2, 0, getLeft() + POINT_WIDTH * 2, getHeight()/2, linePaint);
        }
        if (needBottom) {
            canvas.drawLine(getLeft() + POINT_WIDTH * 2, getHeight()/2, getLeft() + POINT_WIDTH * 2, getHeight(), linePaint);
        }

        pointPaint.setColor(Color.BLACK);
        pointPaint.setAntiAlias(true);
        canvas.drawCircle(getLeft() + POINT_WIDTH * 2, getHeight() / 2, POINT_WIDTH * 2, pointPaint);
        pointPaint.setColor(Color.WHITE);
        canvas.drawCircle(getLeft() + POINT_WIDTH * 2, getHeight() / 2, POINT_WIDTH, pointPaint);

        //获取到的view坐标都是相对于父容器
        //canvas.drawxxx()都是相对于子容器的(0,0)

    }

    public void setNeedBottom(boolean needBottom) {
        this.needBottom = needBottom;
        invalidate();
    }

    public void setNeedTop(boolean needTop) {
        this.needTop = needTop;
        invalidate();
    }


}
