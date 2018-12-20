package com.ming.usesystemutils;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * Author MingRuQi
 * E-mail mingruqi@sina.cn
 * DateTime 2018/12/12 17:20
 */
public class CircleImageView extends AppCompatImageView {

    private Paint paint;
    BitmapShader shader;


    public CircleImageView(Context context) {
        this(context, null);
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
    }

    @Override
    public void draw(Canvas canvas) {
        Bitmap bitmap = getBitmap(getDrawable());
        if (bitmap != null) {
            int viewWidth = getWidth();//控件宽度
            int viewHeight = getHeight();//控件高度
            int viewMinSize = Math.min(viewHeight, viewWidth);//控件最小尺寸
            int width = bitmap.getWidth();//图片宽度
            int height = bitmap.getHeight();//图片高度
            float dstWidth = viewMinSize;//圆形图片宽度
            float dstHeight = viewMinSize;//圆形图片高度
            if (shader == null) {
                shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);//新建着色器
            }
            if (shader != null) {
                Matrix matrix = new Matrix();
                matrix.setScale(dstWidth / width, dstHeight / height);
                shader.setLocalMatrix(matrix);
            }
            paint.setShader(shader);
            float radius = viewMinSize / 2.0f;
            canvas.drawCircle(radius, radius, radius, paint);
        } else {
            super.draw(canvas);
        }

    }

    private Bitmap getBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof ColorDrawable) {
            Rect rect = drawable.getBounds();
            int width = rect.right - rect.left;
            int height = rect.bottom - rect.top;
            int color = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
                color = ((ColorDrawable) drawable).getColor();
            }
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            canvas.drawARGB(Color.alpha(color), Color.red(color), Color.green(color), Color.blue(color));
            return bitmap;
        } else {
            return null;
        }
    }
}

