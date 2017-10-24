package dk.org.macallan.kidznumbers.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;

import dk.org.macallan.kidznumbers.R;

public class SmileyView extends android.support.v7.widget.AppCompatImageView {

	private BitmapDrawable smiley;
	
	public SmileyView(Context context, AttributeSet attrs) {
		super(context, attrs);
		loadSmiley();
		//setAlpha(0);
		
	}
	private void loadSmiley() {
		smiley= new BitmapDrawable(getResources(),
				getResources().getResourceName(R.drawable.smiley));
	
		this.setImageDrawable(smiley);
	}
	
	
	
	public void setAlphaValue(int aValue){
		
		//smiley.setAlpha(aValue);
		//invalidate();
	}
	
	protected void onDraw(Canvas canvas){

        smiley.draw(canvas);
	}
	
	
}
