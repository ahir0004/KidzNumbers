package dk.org.macallan.kidznumbers.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

import dk.org.macallan.kidznumbers.KidzNumberzActivity;
import dk.org.macallan.kidznumbers.R;

public class GameView extends View {
	
	

	int deltaX;
	int deltaY;

	int x;
	int y;
	
	private ArrayList<Bitmap> fruits;
	private int[] img ={R.drawable.apple,
						R.drawable.banana,
						R.drawable.kiwi,
						R.drawable.lemon,
						R.drawable.orange,
						R.drawable.pear,
						R.drawable.strawberry,
						R.drawable.water_melon};
	
	private int theKey=1;
	private int numberOfFruits=0;
	private Bitmap sourceImage;
	private Random generator;
	private int randomIndex;
	private boolean lockTouch;
	
	public GameView(Context context, AttributeSet as) {
        super(context,as);
        this.setFocusable(true);
        this.setFocusableInTouchMode(true);
        
        fruits=new ArrayList<Bitmap>();
        lockTouch=false;
	}
	public int setTheKey(int value){
		
		theKey=value;
		
		if(theKey==numberOfFruits){
			lockTouch=false;
		}
			
		return numberOfFruits;
		
	}
	
	private void loadImages()
	  {
		
		
	       
	       
	  }
	
	@Override
    protected void onDraw(Canvas canvas) {
		x= getWidth();
		y = getHeight();
		
		sourceImage = BitmapFactory.decodeStream(getResources().openRawResource(
                 img[this.randomIndex]));
		sourceImage=Bitmap.createScaledBitmap(sourceImage,x/4,x/4,true) ;
		deltaX=sourceImage.getWidth();
		deltaY=sourceImage.getHeight();
		int padX=deltaX/2;
		int padY=deltaY/2;
				
		int j=padY;
		int i=padX;
		for(int k=0;k<numberOfFruits;k++){
				canvas.drawBitmap(sourceImage, i, j, null);
				
				if( i> x-(2*deltaX)){
					i=padX;
					j= j+deltaY;
				}
				else
					i= i+deltaX;
				
		}
		sourceImage.recycle();
		System.out.println("NumberOfFruits: "+numberOfFruits);
    }
	
	@Override
	   public boolean onTouchEvent(MotionEvent event) {
	    
		if (event.getAction() != MotionEvent.ACTION_DOWN)
	         return super.onTouchEvent(event);
		else
			if(!lockTouch){
				
				lockTouch=true;
				generator = new Random();
				randomIndex = generator.nextInt(7);
				numberOfFruits=generator.nextInt(9)+1;
				
				((KidzNumberzActivity)getContext()).runOnUiThread(new Runnable() {
					public void run(){
						((KidzNumberzActivity)getContext()).getSecondPic().setVisibility(GONE);
						((KidzNumberzActivity)getContext()).soundNumberOfFruits(randomIndex);
					}
				});
				postInvalidate();
			}
		return true;
	   }
}
