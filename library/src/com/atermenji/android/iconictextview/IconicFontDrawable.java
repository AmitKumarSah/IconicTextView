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

    // TODO add padding here
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

        Log.v(TAG, "initial text size = " + textSize);

        mPaint.setTextSize(textSize);

        Path path = new Path();
        mPaint.getTextPath(mIconUtfChars, 0, mIconUtfChars.length, 0, bounds.height(), path);

        RectF pathBounds = new RectF();
        path.computeBounds(pathBounds, true);

        Log.v(TAG, "basic bounds : left =  " + bounds.left + " right = " + bounds.right +
                " bottom = " + bounds.bottom + " top = " + bounds.top + " ; witdh = "
                + bounds.width() + " , height = " + bounds.height());

        Log.v(TAG, "Text bounds before : left =  " + pathBounds.left + " right = " + pathBounds.right +
                " bottom = " + pathBounds.bottom + " top = " + pathBounds.top + " ; witdh = "
                + pathBounds.width() + " , height = " + pathBounds.height());

        // calculate text size
        float deltaWidth = ((float) bounds.width() / (float) pathBounds.width());
        float deltaHeight = ((float) bounds.height() / (float) pathBounds.height());
        float delta = (deltaWidth < deltaHeight) ? deltaWidth : deltaHeight;
        textSize *= delta;

        Log.v(TAG, "delta width = " + deltaWidth);
        Log.v(TAG, "delta height = " + deltaHeight);
        Log.v(TAG, "delta = " + delta);
        Log.v(TAG, "new text size = " + textSize);

        mPaint.setTextSize(textSize);

        mPaint.getTextPath(mIconUtfChars, 0, mIconUtfChars.length, 0, bounds.height(), path);
        path.computeBounds(pathBounds, true);

        Log.v(TAG, "Text bounds after : left =  " + pathBounds.left + " right = " + pathBounds.right +
                " bottom = " + pathBounds.bottom + " top = " + pathBounds.top + " ; witdh = "
                + pathBounds.width() + " , height = " + pathBounds.height());

        if (mIcon != null) {
            Log.v(TAG, "Path bounds  before offset: left =  " + pathBounds.left + " right = " + pathBounds.right +
                    " bottom = " + pathBounds.bottom + " top = " + pathBounds.top + " ; witdh = "
                    + pathBounds.width() + " , height = " + pathBounds.height());
            offsetIcon(path, bounds, pathBounds);
            path.computeBounds(pathBounds, true);
            path.close();
            Log.v(TAG, "Path bounds  after offset: left =  " + pathBounds.left + " right = " + pathBounds.right +
                    " bottom = " + pathBounds.bottom + " top = " + pathBounds.top + " ; witdh = "
                    + pathBounds.width() + " , height = " + pathBounds.height());

            if (mDrawStroke) {
                canvas.drawPath(path, mStrokePaint);
            }

            canvas.drawPath(path, mPaint);
        }
    }

    private void offsetIcon(Path path, Rect viewBounds, RectF pathBounds) {
        float startX = viewBounds.centerX() - (pathBounds.width() / 2);
        float offsetX = startX - pathBounds.left;

        float startY = viewBounds.centerY() - (pathBounds.height() / 2);
        float offsetY = startY - (pathBounds.top);

        Log.v(TAG, "offset x = " + offsetX + " offset y = " + offsetY);

        path.offset(offsetX, offsetY);
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
    }
}
