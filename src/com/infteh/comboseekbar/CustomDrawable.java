package com.infteh.comboseekbar;

import java.util.List;

import com.infteh.comboseekbar.ComboSeekBar.Dot;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;

/**
 * seekbar background with text on it.
 * 
 * @author sazonov-adm
 * 
 */
public class CustomDrawable extends Drawable {
	private final Context mContext;
	private boolean mUseDrawable = false;
	private Paint textUnselected = null;
	private float mThumbRadius;
	/**
	 * paints.
	 */
	private Paint unselectLinePaint = null;
	private List<Dot> mDots;
	private Paint selectLinePaint = null;
	private Paint circleLinePaint = null;
	private float mDotRadius;
	private Paint textSelected = null;
	private int mTextSize;
	private float mTextMargin;
	private int mTextHeight;
	private boolean mIsMultiline;
	
//	public CustomDrawable(Context context, float thumbRadius, List<Dot> dots, int color, int textSize, boolean isMultiline) {
//		mIsMultiline = isMultiline;
//		mContext     = context;
//		mDots        = dots;
//		mTextSize    = textSize;
//		mUseDrawable = true;
//		mThumbRadius = thumbRadius;
//
//		Rect textBounds = new Rect();
//
//		mTextHeight = textBounds.height();
//		mDotRadius = toPix(5);
//		mTextMargin = toPix(3);
//		
//		textUnselected = new Paint(Paint.ANTI_ALIAS_FLAG);
//		textUnselected.setColor(0);
//		textUnselected.setAlpha(255);
//
//		textSelected = new Paint(Paint.ANTI_ALIAS_FLAG);
//		textSelected.setTypeface(Typeface.DEFAULT_BOLD);
//		textSelected.setColor(0);
//		textSelected.setAlpha(255);
//		
//		textUnselected.setTextSize(mTextSize);
//		textSelected.setTextSize(mTextSize);
//	}


	public CustomDrawable(Context context, float thumbRadius, List<Dot> dots, int color, int textSize, boolean isMultiline) {
		mIsMultiline = isMultiline;
		mContext     = context;
		mDots        = dots;
		mTextSize    = textSize;
		textUnselected = new Paint(Paint.ANTI_ALIAS_FLAG);
		textUnselected.setColor(color);
		textUnselected.setAlpha(255);

		textSelected = new Paint(Paint.ANTI_ALIAS_FLAG);
		textSelected.setTypeface(Typeface.DEFAULT_BOLD);
		textSelected.setColor(color);
		textSelected.setAlpha(255);

		mThumbRadius = thumbRadius;

		unselectLinePaint = new Paint();
		unselectLinePaint.setColor(color);

		unselectLinePaint.setStrokeWidth(toPix(1));

		selectLinePaint = new Paint();
		selectLinePaint.setColor(color);
		selectLinePaint.setStrokeWidth(toPix(3));

		circleLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		circleLinePaint.setColor(color);

		Rect textBounds = new Rect();
		textSelected.setTextSize((int) (mTextSize * 2));
		textSelected.getTextBounds("M", 0, 1, textBounds);

		textUnselected.setTextSize(mTextSize);
		textSelected.setTextSize(mTextSize);

		mTextHeight = textBounds.height();
		mDotRadius = toPix(5);
		mTextMargin = toPix(3);
	}

	private float toPix(int size) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, mContext.getResources().getDisplayMetrics());
	}

	@Override
	protected final void onBoundsChange(Rect bounds) {
//		mStartDot.setBounds(bounds);
	}

	@Override
	protected final boolean onStateChange(int[] state) {
		if (mUseDrawable) {
//			mStartDot.setState(state);
		}
		
		invalidateSelf();
		return false;
	}

	@Override
	public final boolean isStateful() {
		
		return true;
	}

	@Override
	public final void draw(Canvas canvas) {
		// Log.d("--- draw:" + (getBounds().right - getBounds().left));
		
		int height = this.getIntrinsicHeight() / 2;
		if (mDots.size() == 0) {
//			canvas.drawLine(0, height, getBounds().right, height, unselectLinePaint);
			return;
		}
		
		for (Dot dot : mDots) {
			drawText(canvas, dot, dot.mX, height);
//			if (dot.isSelected) {
//				canvas.drawLine(mDots.get(0).mX, height, dot.mX, height, selectLinePaint);
//				canvas.drawLine(dot.mX, height, mDots.get(mDots.size() - 1).mX, height, unselectLinePaint);
//			}
//			canvas.drawCircle(dot.mX, height, mDotRadius, circleLinePaint);
			dot.draw(canvas, height, mDots.get(0), mDots.get(mDots.size() - 1), mDotRadius,
					selectLinePaint, unselectLinePaint, circleLinePaint);
		}
	}

	/**
	 * @param canvas
	 *            canvas.
	 * @param dot
	 *            current dot.
	 * @param x
	 *            x cor.
	 * @param y
	 *            y cor.
	 */
	private void drawText(Canvas canvas, Dot dot, float x, float y) {
		final Rect textBounds = new Rect();
		textSelected.getTextBounds(dot.text, 0, dot.text.length(), textBounds);
		float xres;
		if (dot.id == (mDots.size() - 1)) {
			xres = getBounds().width() - textBounds.width();
		} else if (dot.id == 0) {
			xres = 0;
		} else {
			xres = x - (textBounds.width() / 2);
		}

		float yres;
		// ��谢懈 屑薪芯谐芯���芯�薪�泄 �械泻��
		if (mIsMultiline) {
			// ��谢懈 �械�薪邪� �芯�泻邪, �芯 �胁械���
			if ((dot.id % 2) == 0) {
				yres = y - mTextMargin - mDotRadius;
			} else {
				yres = y + mTextHeight;
			}
		} else {
			yres = y - (mDotRadius * 2) + mTextMargin;
		}

		if (dot.isSelected) {
			canvas.drawText(dot.text, xres, yres, textSelected);
		} else {
			canvas.drawText(dot.text, xres, yres, textUnselected);
		}
	}

	@Override
	public final int getIntrinsicHeight() {
		if (mIsMultiline) {
			return (int) (selectLinePaint.getStrokeWidth() + mDotRadius + (mTextHeight) * 2  + mTextMargin);
		} else {
			return (int) (mThumbRadius + mTextMargin + mTextHeight + mDotRadius);
		}
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
}