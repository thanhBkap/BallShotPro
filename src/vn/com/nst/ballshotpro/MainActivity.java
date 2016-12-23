package vn.com.nst.ballshotpro;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import vn.com.nst.model.Bubble;

@SuppressLint("NewApi")
public class MainActivity extends Activity {
	TextView txtScore;
	Button btnPlay;
	LinearLayout layoutBubble,layoutTong;
	LayoutParams params;
	Random rd;
	int score = 0;
	boolean gameOver = false;
	ObjectAnimator objectAnimator;
	Timer timer = null;
	TimerTask timertask = null;
	int turn = 1;
	int speedTimer = 3000;
	int speedBubble = 1000;
	int speedRandom = 2000;
	Menu _menu=null;
	int _id=R.id.menulv1;
	String color="#ddffcc";
	/*
	 * Drawable nowBubble = null; Drawable lastBubble = null;
	 */
	Bubble bubble = new Bubble();
	Bubble olderBubble = new Bubble();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// turn = 1;
		setContentView(R.layout.activity_main);
		addControls();
		addEvents();
	}

	private void resetData() {
		score = 0;
		gameOver = false;
		bubble = new Bubble();
		olderBubble = new Bubble();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		_menu=menu;
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(_menu!=null){
			_menu.findItem(_id).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		}
		_id=item.getItemId();
		switch (_id) {
		case R.id.menulv1:
			speedTimer = 3000;
			speedBubble = 1600;
			speedRandom = 1400;
			color="#ddffcc";
			break;

		case R.id.menulv2:
			speedTimer = 2500;
			speedBubble = 1350;
			speedRandom = 1150;
			color="#cceeff";
			break;
		case R.id.menulv3:
			speedTimer = 2000;
			speedBubble = 1100;
			speedRandom = 900;
			color="#f2ccff";

			break;
		case R.id.menulv4:
			color="#ffd6cc";
			speedTimer = 1000;
			speedBubble = 600;
			speedRandom = 400;
			color="#ffd6cc";
			break;
		default:
			color="#ddffcc";
			speedTimer = 3000;
			speedBubble = 1600;
			speedRandom = 1400;
			break;
		}
		
		objectAnimator.pause();
		gameOver = true;
		timer.cancel();
		SystemClock.sleep(300);
		
		turn=1;
		resetData();
		setContentView(R.layout.activity_main);
		addControls();
		addEvents();

		
		
		return super.onOptionsItemSelected(item);
	}

	private void addEvents() {
		btnPlay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				btnPlay.setVisibility(View.INVISIBLE);
				autoPlay();
				/*
				 * for (int i = 0; i < rd.nextInt(5); i++) { xuLyPlay(); }
				 */

				/*
				 * while (gameOver == false) { SystemClock.sleep(1000);
				 * BubbleTask bubbleTask = new BubbleTask();
				 * bubbleTask.execute(); }
				 */
			}
		});
	}

	private void addControls() {
		layoutTong = (LinearLayout) findViewById(R.id.layoutTong);
		txtScore = (TextView) findViewById(R.id.txtScore);
		btnPlay = (Button) findViewById(R.id.btnPlay);
		btnPlay.setTextColor(Color.YELLOW);
		layoutBubble = (LinearLayout) findViewById(R.id.layoutBubble);
		params = new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		rd = new Random();
		if (turn > 1) {
			btnPlay.setText("Replay");
		}
		//Becare for value null
		if(_menu!=null){
			_menu.findItem(_id).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		}
		layoutTong.setBackgroundColor(Color.parseColor(color));
	}

	private ImageView getImageView() {
		ImageView img = new ImageView(MainActivity.this);
		img.setX(rd.nextInt(400));
		return img;
	}

	/*
	 * private int getDrawable() { int i = rd.nextInt(5); switch (i) { case 0:
	 * return R.drawable.blue; case 1: return R.drawable.gold; case 2: return
	 * R.drawable.green; case 3: return R.drawable.grey; case 4: return
	 * R.drawable.light_brown; case 5: return R.drawable.orange_dark; case 6:
	 * return R.drawable.pink; case 7: return R.drawable.purple; case 8: return
	 * R.drawable.red; case 9: return R.drawable.dark_blue; case 10: return
	 * R.drawable.bomb; case 11: return R.drawable.black; default:
	 * getDrawable(); } return 0; }
	 */
	public Drawable getDrawable() {
		Drawable draw;
		int i = rd.nextInt(5);
		switch (i) {
		case 0:
			draw = getResources().getDrawable(R.drawable.black);
			break;
		case 1:
			draw = getResources().getDrawable(R.drawable.blue);
			break;
		case 2:
			draw = getResources().getDrawable(R.drawable.bomb);
			break;
		case 3:
			draw = getResources().getDrawable(R.drawable.dark_blue);
			break;
		case 4:
			draw = getResources().getDrawable(R.drawable.gold);
			break;
		case 5:
			draw = getResources().getDrawable(R.drawable.grey);
			break;
		default:
			draw = getResources().getDrawable(R.drawable.wrong);
			break;
		}
		return draw;
	}

	private void autoPlay() {
		timertask = new TimerTask() {

			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					public void run() {
						if (gameOver == false) {
							new BubbleTask().execute();
						}
					}
				});
			}
		};
		timer = new Timer();
		timer.schedule(timertask, 0, speedTimer);
	}

	private boolean compareTwoDrawables(Drawable img1, Drawable img2) {
		Bitmap bmImg1, bmImg2;
		bmImg1 = ((BitmapDrawable) img1).getBitmap();
		bmImg2 = ((BitmapDrawable) img2).getBitmap();
		if (bmImg1 == bmImg2) {
			return true;
		}
		return false;
	}

	@SuppressLint("NewApi")
	class BubbleTask extends AsyncTask<Void, Void, Void> {
		ImageView img;
		// boolean clicked;

		@Override
		protected void onPreExecute() {
			// clicked = false;
			img = getImageView();
			/*
			 * lastBubble=nowBubble; nowBubble = getDrawable();
			 */
			olderBubble.setClicked(bubble.isClicked());
			olderBubble.setImg(bubble.getImg());
			bubble.setImg(getDrawable());
			// img.setBackground(nowBubble);
			img.setBackground(bubble.getImg());
			img.setLayoutParams(params);
			objectAnimator = (ObjectAnimator) AnimatorInflater.loadAnimator(MainActivity.this, R.animator.bubble);
			objectAnimator.setDuration(rd.nextInt(speedRandom) + speedBubble);
			objectAnimator.setTarget(img);
			layoutBubble.addView(img);
			img.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// objectAnimator.cancel();
					/*
					 * runOnUiThread(new Runnable() {
					 * 
					 * @Override public void run() { arg0.clearAnimation(); }
					 * });
					 */
					arg0.clearAnimation();
					// clicked = true;
					bubble.setClicked(true);
					layoutBubble.removeView(arg0);

					// Compare two drawable use bitmap or hashcode
					/*
					 * if (compareTwoDrawables(nowBubble,
					 * getResources().getDrawable(R.drawable.bomb))) {
					 * txtScore.setText("Score : " + (score -= 20)); } else if
					 * (compareTwoDrawables(nowBubble,
					 * getResources().getDrawable(R.drawable.gold))) {
					 * txtScore.setText("Score : " + (score += 10)); } else {
					 * txtScore.setText("Score : " + (score += 1)); }
					 */
					if (compareTwoDrawables(bubble.getImg(), getResources().getDrawable(R.drawable.bomb))) {
						txtScore.setText("Score : " + (score -= 20));
					} else if (compareTwoDrawables(bubble.getImg(), getResources().getDrawable(R.drawable.gold))) {
						txtScore.setText("Score : " + (score += 10));
					} else {
						txtScore.setText("Score : " + (score += 1));
					}
				}
			});
			objectAnimator.addListener(new Animator.AnimatorListener() {

				@Override
				public void onAnimationStart(Animator animation) {
					bubble.setClicked(false);
				}

				@Override
				public void onAnimationRepeat(Animator animation) {
					// TODO Auto-generated method stub
				}

				@Override
				public void onAnimationEnd(Animator animation) {
					// TODO Auto-generated method stub
					/*
					 * layoutBubble.removeView((View)
					 * ((ObjectAnimator)animation).getTarget());
					 */
					/*
					 * if (clicked == false) { if
					 * (compareTwoDrawables(nowBubble,
					 * getResources().getDrawable(R.drawable.bomb)) == false){
					 * objectAnimator.pause(); gameOver = true; timer.cancel();
					 * Toast.makeText(MainActivity.this, "---Game over---"
					 * +compareTwoDrawables(nowBubble,
					 * getResources().getDrawable(R.drawable.bomb)),
					 * Toast.LENGTH_SHORT).show();
					 * btnPlay.setVisibility(View.VISIBLE);
					 * btnPlay.setText("Replay"); gameOver = false; score = 0;
					 * turn++; txtScore.setText("Score: " + score);
					 * setContentView(R.layout.activity_main); addControls();
					 * addEvents(); } }
					 */
					if (olderBubble.isClicked() == false) {
						if (olderBubble.getImg() != null) {
							if (compareTwoDrawables(olderBubble.getImg(),
									getResources().getDrawable(R.drawable.bomb)) == false) {
								objectAnimator.pause();
								gameOver = true;
								timer.cancel();
								Toast.makeText(MainActivity.this, "---Game over---", Toast.LENGTH_SHORT).show();
								SystemClock.sleep(300);
								btnPlay.setVisibility(View.VISIBLE);
								btnPlay.setText("Replay");
								resetData();
								turn++;
								txtScore.setText("Score: " + score);
								setContentView(R.layout.activity_main);
								addControls();
								addEvents();
							}
						}

					}
				}

				@Override
				public void onAnimationCancel(Animator animation) {

				}
			});

			objectAnimator.start();
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {

			return null;
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);

		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
		}
	}
	////////////////////////////////////////////////////////////////////

	protected void ProcessAnim() {
		// TODO Auto-generated method stub
		// Drawing a bubble
		ImageView img = getImageView();

		img.setBackground(getDrawable());
		img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				layoutBubble.removeView(arg0);
				txtScore.setText("Score : " + (score += 1));
			}
		});

		objectAnimator = (ObjectAnimator) AnimatorInflater.loadAnimator(MainActivity.this, R.animator.bubble);
		objectAnimator.setDuration(rd.nextInt(1000) + 2000);
		objectAnimator.setTarget(img);

		layoutBubble.addView(img, params);

		objectAnimator.addListener(new Animator.AnimatorListener() {

			@Override
			public void onAnimationStart(Animator animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animator animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animator animation) {
				// TODO Auto-generated method stub

				layoutBubble.removeView((View) ((ObjectAnimator) animation).getTarget());

			}

			@Override
			public void onAnimationCancel(Animator animation) {
				// TODO Auto-generated method stub

			}
		});

		objectAnimator.start();
	}

	private void xuLyPlay() {
		// btnPlay.setVisibility(View.INVISIBLE);
		objectAnimator = (ObjectAnimator) AnimatorInflater.loadAnimator(MainActivity.this, R.animator.bubble);
		// ImageView bubble = new ImageView(MainActivity.this);
		ImageView bubble = getImageView();
		// bubble.setImageResource(getDrawable());
		bubble.setBackground(getDrawable());
		layoutBubble.addView(bubble, params);
		objectAnimator.setDuration(rd.nextInt(1000) + 2000);
		objectAnimator.setTarget(bubble);
		bubble.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				score++;
				layoutBubble.removeView(v);
				txtScore.setText("Score: " + score);
			}
		});
		objectAnimator.addListener(new Animator.AnimatorListener() {

			@Override
			public void onAnimationStart(Animator animation) {

			}

			@Override
			public void onAnimationRepeat(Animator animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animator animation) {
				layoutBubble.removeView((View) ((ObjectAnimator) animation).getTarget());
				gameOver = true;

			}

			@Override
			public void onAnimationCancel(Animator animation) {
				// TODO Auto-generated method stub

			}
		});
		objectAnimator.start();
	}

}
