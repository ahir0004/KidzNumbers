package dk.org.macallan.kidznumbers;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import dk.org.macallan.kidznumbers.utils.Rotate3dAnimation;
import dk.org.macallan.kidznumbers.views.GameView;

public class KidzNumberzActivity extends Activity {

	private Handler splashHandler;
	private Runnable hideSplash;
	private static final float ROTATE_FROM = 0.0f;
	private static final float ROTATE_TO = -10.0f * 360.0f;// 3.141592654f * 32.0f;
	private View keys[] = new View[9];
	private int numberImage[]=
	{		 R.drawable.one,
			 R.drawable.two,
			 R.drawable.three,
			 R.drawable.four,
			 R.drawable.five,
			 R.drawable.six,
			 R.drawable.seven,
			 R.drawable.eight,
			 R.drawable.nine
			};
	private int numberSounds[]=
	{
			R.raw.one,
			R.raw.two,
			R.raw.three,
			R.raw.four,
			R.raw.five,
			R.raw.six,
			R.raw.seven,
			R.raw.eight,
			R.raw.nine
	};
	private int fruitSounds[]=
	{
			R.raw.aebler,
			R.raw.bananer,
			R.raw.kiwi,
			R.raw.citroner,
			R.raw.appelsiner,
			R.raw.paerer,
			R.raw.jordbaer,
			R.raw.vandmeloner
	};
	private GameView gameView;
	private ImageView smileyView;
    private ImageView secondPic;
    private ViewGroup mContainer;
    private ImageView mImageView;
    //RotateAnimation r;

	
	private int selectedKey;
	private MediaPlayer player;
	private int[] sentence;
	private AdView mAdView;


	/**
	 * Called when the activity is first created.
	 */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
		setContentView(R.layout.main);

		mAdView = (AdView) findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder()
				.tagForChildDirectedTreatment(true)
				.build();
		mAdView.loadAd(adRequest);

		findGameView();
        findViews();
		setListeners();
		hideSplash = new Runnable() {
			@Override
			public void run() {
				((ImageView)findViewById(R.id.imageViewSplash)).setVisibility(View.GONE);
				findViewById(R.id.keypad_array).setVisibility(View.VISIBLE);
				gameView.setLockTouch(false);
			}
		};
		splashHandler = new Handler();
		splashHandler.postDelayed(hideSplash, 5000);
    }
     
    private void findGameView(){
    	
    	gameView=(GameView)findViewById(R.id.imageView1);
    	smileyView = (ImageView)findViewById(R.id.imageView2);
		smileyView.setImageAlpha(150);
		secondPic = (ImageView)findViewById(R.id.imageView3);
		secondPic.setImageAlpha(225);
		mContainer = (ViewGroup) findViewById(R.id.container);
    	 //smileyView.
    }
    private void findViews() {
	      keys[0] = findViewById(R.id.keypad_1);
	      keys[1] = findViewById(R.id.keypad_2);
	      keys[2] = findViewById(R.id.keypad_3);
	      keys[3] = findViewById(R.id.keypad_4);
	      keys[4] = findViewById(R.id.keypad_5);
	      keys[5] = findViewById(R.id.keypad_6);
	      keys[6] = findViewById(R.id.keypad_7);
	      keys[7] = findViewById(R.id.keypad_8);
	      keys[8] = findViewById(R.id.keypad_9);
	}
    
    public void soundNumberOfFruits(int number){
    	int sentence[]={R.raw.hvor_mange,fruitSounds[number],R.raw.er_der};
    	initSounds(sentence);
    	
    }
    
    private void initSounds(int[] sounds){
    	
    	sentence=sounds;
    	playSounds(0);
    }
    
    private void playSounds(final int sound){
    	final KidzNumberzActivity context =this;
    	
    	if(sound<sentence.length){
    		
			player=MediaPlayer.create(context, sentence[sound]);
			player.setOnCompletionListener(new OnCompletionListener() {
				
				@Override
				public void onCompletion(MediaPlayer mp) {
					int counter =sound;
					player.release();
					playSounds(counter+1);
					
					
				}
			});
	    	player.setVolume(0.9f, 0.9f);
	        player.start();
    	}
    }
    
	private void setListeners() {
      for (int i = 0; i < keys.length; i++) {
         final int t = i ;
         
         keys[i].setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
               int nof = gameView.setTheKey(t+1);


					if (nof == t + 1) {

						setSelectedKey(t);
						//applyRotation(-1, 0,90);
						int sentence[] = {R.raw.rigtigt, numberSounds[t]};
						initSounds(sentence);

					} else {
						if(!gameView.isViewTouchEnabled()) {
							smileyView.setVisibility(View.GONE);
							secondPic.setVisibility(View.GONE);
							int sentence[] = {R.raw.naesten_rigtigt};
							initSounds(sentence);
						}
						else{
							gameView.loadFruits();
						}
					}

            }});
      }
	}

	private void setSelectedKey(final int t) {
		
		
		runOnUiThread(new Runnable() {
			
			public void run(){
				smileyView.setVisibility(View.VISIBLE);
           		smileyView.requestFocus();
   				secondPic.setImageResource(numberImage[t]);
   				secondPic.setVisibility(View.GONE);
				applyRotation(-1, 0,90);
			}
		});
	}

	/**
     * Setup a new 3D rotation on the container view.
     *
     * @param position the item that was clicked to show a picture, or -1 to show the list
     * @param start the start angle at which the rotation must begin
     * @param end the end angle of the rotation
     */
    private void applyRotation(int position, float start, float end) {
        // Find the center of the container
        final float centerX = mContainer.getWidth() / 2.0f;
        final float centerY = mContainer.getHeight() / 2.0f;

        // Create a new 3D rotation with the supplied parameter
        // The animation listener is used to trigger the next animation
        final Rotate3dAnimation rotation =
                new Rotate3dAnimation(start, end, centerX, centerY, 310.0f, true);
        rotation.setDuration(1000);
        rotation.setFillAfter(true);
        rotation.setInterpolator(new AccelerateInterpolator());
        rotation.setAnimationListener(new DisplayNextView(position));

        mContainer.startAnimation(rotation);
    }
	public ImageView getSecondPic(){
		
		return secondPic;
		
	}
		/**
     * This class listens for the end of the first half of the animation.
     * It then posts a new action that effectively swaps the views when the container
     * is rotated 90 degrees and thus invisible.
     */
    private final class DisplayNextView implements Animation.AnimationListener {
        private final int mPosition;

        private DisplayNextView(int position) {
            mPosition = position;
        }

        public void onAnimationStart(Animation animation) {
        }

        public void onAnimationEnd(Animation animation) {
            mContainer.post(new SwapViews(mPosition));
        }

        public void onAnimationRepeat(Animation animation) {
        }
    }

    /**
     * This class is responsible for swapping the views and start the second
     * half of the animation.
     */
    private final class SwapViews implements Runnable {
        private final int mPosition;

        public SwapViews(int position) {
            mPosition = position;
        }

        public void run() {
            final float centerX = mContainer.getWidth() / 2.0f;
            final float centerY = mContainer.getHeight() / 2.0f;
            Rotate3dAnimation rotation;
            smileyView.setVisibility(View.GONE);
            secondPic.setVisibility(View.VISIBLE);
            secondPic.requestFocus();

            rotation = new Rotate3dAnimation(90, 0, centerX, centerY, 310.0f, false);
           
            rotation.setDuration(500);
            rotation.setFillAfter(true);
            rotation.setInterpolator(new DecelerateInterpolator());

            mContainer.startAnimation(rotation);
        }
    }
	
}