package com.atermenji.android.iconictextview;

import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.Drawable;

import android.util.Log;
import com.atermenji.android.iconictextview.icon.Icon;

public class IconicFontDrawable extends Drawable {

    private static final String TAG = "IconFontDrawable";

    private Context mContext;
    
    private Paint mPaint;
    private Paint mStrokePaint;

    private Icon mIcon;
    private char[] mIconUtfChars;

    private boolean mDrawStroke;
    
    public IconicFontDrawable(Context context) {
        mContext = context.getApplicationContext();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }
    
    public IconicFontDrawable(Context context, Icon icon) {
        this(context);
        init(icon);
    }
    
    public void setIcon(final Icon icon) {
        init(icon);
        invalidateSelf();
    }

    public void setStroke(final int strokeColor) {
        mStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mStrokePaint.setStyle(Paint.Style.STROKE);
        mStrokePaint.setStrokeWidth(10);
        mStrokePaint.setColor(strokeColor);
        mStrokePaint.setTextAlign(Paint.Align.CENTER);
    }
    
    public void drawStroke(boolean drawStroke) {
        this.mDrawStroke = drawStroke;
        invalidateSelf();
    }

    public void setStokeColor(final int color) {
        mStrokePaint.setColor(color);
        invalidateSelf();
    }

    public void setIconColor(final int color) {
        mPaint.setColor(color);
        invalidateSelf();
    }

    private void drawWithPath(Canvas canvas) {

        Rect bounds = getBounds();
        float textSize = (float) bounds.height() * 2;
//        float textSize = 10.0f;

        Log.v(TAG, "initial text size = " + textSize);

        mPaint.setTextSize(textSize);
        Rect textBounds = new Rect();
        mPaint.getTextBounds(mIconUtfChars, 0, mIconUtfChars.length, textBounds);


        Log.v(TAG, "basic bounds : left =  " + bounds.left + " right = " + bounds.right +
                " bottom = " + bounds.bottom + " top = " + bounds.top + " ; witdh = "
                + bounds.width() + " , height = " + bounds.height());

        Log.v(TAG, "Text bounds before : left =  " + textBounds.left + " right = " + textBounds.right +
                " bottom = " + textBounds.bottom + " top = " + textBounds.top + " ; witdh = "
                + textBounds.width() + " , height = " + textBounds.height());

        // calculate text size
        float deltaWidth = ((float) bounds.width() / (float) textBounds.width());
        float deltaHeight = ((float) bounds.height() / (float) textBounds.height());
        float delta = (deltaWidth < deltaHeight) ? deltaWidth : deltaHeight;
        textSize *= delta;

        Log.v(TAG, "delta width = " + deltaWidth);
        Log.v(TAG, "delta height = " + deltaHeight);
        Log.v(TAG, "delta = " + delta);
        Log.v(TAG, "new text size = " + textSize);

        mPaint.setTextSize(textSize);

        mPaint.getTextBounds(mIconUtfChars, 0, mIconUtfChars.length, textBounds);

        Log.v(TAG, "Text bounds after : left =  " + textBounds.left + " right = " + textBounds.right +
                " bottom = " + textBounds.bottom + " top = " + textBounds.top + " ; witdh = "
                + textBounds.width() + " , height = " + textBounds.height());


        // calculate text size 2
        float deltaWidth2 = ((float) bounds.width() / (float) textBounds.width());
        float deltaHeight2 = ((float) bounds.height() / (float) textBounds.height());
        float delta2 = (deltaWidth2 < deltaHeight2) ? deltaWidth2 : deltaHeight2;
        textSize *= delta2;

        Log.v(TAG, "delta width 2 = " + deltaWidth2);
        Log.v(TAG, "delta height 2 = " + deltaHeight2);
        Log.v(TAG, "delta 2 = " + delta2);
        Log.v(TAG, "new text size = " + textSize);

        mPaint.setTextSize(textSize);

        mPaint.getTextBounds(mIconUtfChars, 0, mIconUtfChars.length, textBounds);

        Log.v(TAG, "Text bounds after second: left =  " + textBounds.left + " right = " + textBounds.right +
                " bottom = " + textBounds.bottom + " top = " + textBounds.top + " ; witdh = "
                + textBounds.width() + " , height = " + textBounds.height());


        // calculate text size 3
        float deltaWidth3 = ((float) bounds.width() / (float) textBounds.width());
        float deltaHeight3 = ((float) bounds.height() / (float) textBounds.height());
        float delta3 = (deltaWidth3 < deltaHeight3) ? deltaWidth3 : deltaHeight3;
        textSize *= delta3;

        Log.v(TAG, "delta width 3 = " + deltaWidth3);
        Log.v(TAG, "delta height 3 = " + deltaHeight3);
        Log.v(TAG, "delta 3 = " + delta3);
        Log.v(TAG, "new text size = " + textSize);

        mPaint.setTextSize(textSize);

        mPaint.getTextBounds(mIconUtfChars, 0, mIconUtfChars.length, textBounds);

        Log.v(TAG, "Text bounds after third 333: left =  " + textBounds.left + " right = " + textBounds.right +
                " bottom = " + textBounds.bottom + " top = " + textBounds.top + " ; witdh = "
                + textBounds.width() + " , height = " + textBounds.height());



        Path path = new Path();
        mPaint.getTextPath(mIconUtfChars, 0, mIconUtfChars.length, 0, bounds.height(), path);


        if (mIcon != null) {

            offsetIcon(path, bounds, textBounds);

            RectF pathBounds = new RectF();
            path.computeBounds(pathBounds, true);
            path.close();
            Log.v(TAG, "Path bounds  after: left =  " + pathBounds.left + " right = " + pathBounds.right +
                    " bottom = " + pathBounds.bottom + " top = " + pathBounds.top + " ; witdh = "
                    + pathBounds.width() + " , height = " + pathBounds.height());

            if (mDrawStroke) {
                canvas.drawPath(path, mStrokePaint);
            }

            canvas.drawPath(path, mPaint);
        }
    }

    private void offsetIcon(Path path, Rect viewBounds, Rect textBounds) {
        int startX = viewBounds.centerX() - (textBounds.width() / 2);
        int offsetX = startX - textBounds.left;

        int startY = viewBounds.centerY() - (textBounds.height() / 2);
        int offsetY = startY - (-1 * textBounds.bottom);

        path.offset(offsetX, -offsetY);
    }

    @Override
    public void draw(Canvas canvas) {
        drawWithPath(canvas);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mPaint.setColorFilter(cf);
    }
    
    private void init(final Icon icon) {
        mIcon = icon;
        mIconUtfChars = Character.toChars(icon.getIconUtfValue());
        mPaint.setTypeface(mIcon.getIconicTypeface().getTypeface(mContext));
        setStroke(Color.RED);
//        mPaint.setStyle(Paint.Style.FILL);
//        mPaint.setTextAlign(Paint.Align.CENTER);
    }
}
