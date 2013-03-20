package com.infteh.comboseekbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;

/**
 * seekbar background with text on it.
 * 
 * @author sazonov-adm
 * 2013-3-20
 *     dauglas-tang �޸�
 * 
 * ��ӻ��Ʒ�֧�����ͼƬ������
 */
public class CustomThumbDrawable extends Drawable {
	/**
	 * paints.
	 */
	private Paint circlePaint;
	private Context mContext;
	private float mRadius;
	
	private Drawable mBitmapDrawable = null;
	private boolean mUseDrawable = false;

	public CustomThumbDrawable(Context context, int color) {
		mContext = context;
		mRadius = toPix(16);
		setColor(color);
	}
	
	public CustomThumbDrawable(Context context, Drawable drawable) {
		mBitmapDrawable = drawable;
		mContext = context;
		mUseDrawable = true;
		mRadius = toPix(16);
		mBitmapDrawable.setBounds(0, 0, (int)(mRadius*2), (int)(mRadius*2));

		invalidateSelf();
	}

	public void setColor(int color) {
		circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		circlePaint.setColor((0xA0 << 24) + (color & 0x00FFFFFF));
		invalidateSelf();
	}
	
	public float getRadius() {
		return mRadius;
	}

	@Override
	protected final boolean onStateChange(int[] state) {
		invalidateSelf();
		return false;
	}

	@Override
	public final boolean isStateful() {
		return true;
	}

	@Override
	public final void draw(Canvas canvas) {
		
		int height = this.getBounds().centerY();
		int width = this.getBounds().centerX();
		if (mUseDrawable) {
			canvas.save();
			
			float gap = Math.abs(mRadius - height);
			
			canvas.translate(width, gap);
			mBitmapDrawable.draw(canvas);
			canvas.restore();
		} else {
			
			canvas.drawCircle(width + mRadius, height, mRadius, circlePaint);
		}
	}

	@Override
	public int getIntrinsicHeight() {
		return (int) (mRadius * 2);
	}

	@Override
	public int getIntrinsicWidth() {
		return (int) (mRadius * 2);
	}

	@Override
	public final int getOpacity() {
		return PixelFormat.TRANSLUCENT;
	}

	@Override
	public void setAlpha(int alpha) {
	}

	@Override
	public void setColorFilter(ColorFilter cf) {
	}

	private float toPix(int size) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size,
				mContext.getResources().getDisplayMetrics());
	}
}