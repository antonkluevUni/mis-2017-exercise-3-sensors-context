
package com.uni.antonkluev.app;

import android.graphics.Color;
import android.view.View;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;

import java.util.ArrayList;

// https://examples.javacodegeeks.com/android/core/graphics/canvas-graphics/android-canvas-example/
public class CanvasView extends View {
    private int width;
    private int height;
    private int padding = 10;
    public int windowSize = 1024;
    public int maxWindowSize = 1024;
    public ArrayList <Axis> axis = new ArrayList<Axis>();
    // methods
    public CanvasView(Context c, AttributeSet attrs) {
        super(c, attrs);
        axis.add(new Axis(Color.RED));
        axis.add(new Axis(Color.GREEN));
        axis.add(new Axis(Color.BLUE));
        axis.add(new Axis(Color.GRAY));
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // print plane
        drawCoordinatePlane(canvas);
        // print axis
        for (int i = 0; i < axis.size(); i ++)
            canvas.drawPath(axis.get(i).path, axis.get(i).paint);
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width  = w;
        height = h;
        super.onSizeChanged(w, h, oldw, oldh);
    }
    void drawCoordinatePlane (Canvas canvas) {
        // draw line
        Path  path  = new Path();
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.LTGRAY);
        int [] values = new int []{-2, -1, 0, 1, 2};
        int size = 5;
        float x = width - 2 * padding;
        float h  = (height - padding * 2) / (size - 1);
        for (int i = 0; i < size; i ++) {
            // draw numbers
            String text = String.valueOf(values[i]);
            float y = height - padding - i * h;
            Paint tp = new Paint();
            canvas.drawText(text,
                    x - tp.measureText(text) / 2,
                    y - (tp.descent() + tp.ascent()) / 2, tp);
            // draw line
            path.moveTo(0, y);
            path.lineTo(width - 3 * padding, y);
            canvas.drawPath(path, paint);
        }
    }
    public class Axis {
        public double data [] = new double[maxWindowSize];
        public Path  path  = new Path();
        public Paint paint = new Paint();
        private float min, max;
        // methods
        public Axis (int color) {
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(color);
        }
        public void setRange (float min, float max) {
            this.min = min;
            this.max = max;
        }
        public void add (double value) {
            double vNew = value;
            for (int i = 0; i < maxWindowSize; i ++) {
                // shift everything down
                double t = vNew;
                vNew = data[i];
                data[i] = t;
            }
            update();
        }
        public void update () {
            for (int i = 0; i < windowSize; i ++) {
                double vNew = (data[i] - min) / (max - min) * (height - 2 * padding);
                // draw line
                float left = width - i * width / (windowSize - 1f);
                float top  = height - padding - (float)vNew;
                if (i == 0) {
                    path.reset();
                    path.moveTo(left, top);
                } else
                    path.lineTo(left, top);
            }
            postInvalidate();
        }
    }
}