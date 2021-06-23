package com.miljanpeles.lib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.miljanpeles.lib.R;

import java.util.ArrayList;

public class PaintView extends View {

    private int brushColor;
    private float brushWidth;

    private Path path = new Path();
    private Paint brush = new Paint();

    private ArrayList<Path> putanje = new ArrayList<>();
    private ArrayList<Integer> boje = new ArrayList<>();
    private ArrayList<Float> debljina = new ArrayList<>();

    public PaintView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // uzima vrednosti atributa
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.PaintView, 0, 0);

        try {
            //get the text and colors specified using the names in attrs.xml
            brushColor = a.getInteger(R.styleable.PaintView_brushColor, 0);//0 is default
            brushWidth = a.getDimension(R.styleable.PaintView_brushWidth, 8);
        } finally {
            a.recycle();
        }

        initBrush();
    }

    private void initBrush() {
        brush.setAntiAlias(true);
        brush.setColor(brushColor == 0 ? Color.MAGENTA : brushColor);
        brush.setStyle(Paint.Style.STROKE);
        brush.setStrokeJoin(Paint.Join.ROUND);
        brush.setStrokeWidth(brushWidth);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float pointX = event.getX();
        float pointY = event.getY();

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                path = new Path();
                path.moveTo(pointX, pointY);
                putanje.add(path);
                boje.add(brushColor);
                debljina.add(brushWidth);
                return true;

            case MotionEvent.ACTION_MOVE:
                path.lineTo(pointX, pointY);
                break;

            case MotionEvent.ACTION_UP:
                break;

            default:
                return false;
        }
        postInvalidate();
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        for(int i = 0; i < putanje.size(); i++) {
            brush.setColor(boje.get(i));
            brush.setStrokeWidth(debljina.get(i));
            canvas.drawPath(putanje.get(i), brush);
        }
    }

    public int getBrushColor() {
        return brushColor;
    }

    public void setBrushColor(int brushColor) {
        this.brushColor = brushColor;

        //invalidate();
        //requestLayout();
    }

    public float getBrushWidth() {
        return brushWidth;
    }

    public void setBrushWidth(float brushWidth) {
        this.brushWidth = brushWidth;

        //invalidate();
        //requestLayout();
    }

    public void clear() {
        putanje = new ArrayList<>();
        boje = new ArrayList<>();
        debljina = new ArrayList<>();
        invalidate();
    }


}
