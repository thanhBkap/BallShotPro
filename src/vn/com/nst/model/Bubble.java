package vn.com.nst.model;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;

public class Bubble{
	Drawable img;
	public Drawable getImg() {
		return img;
	}

	public void setImg(Drawable img) {
		this.img = img;
	}

	boolean clicked = false;
	public boolean isClicked() {
		return clicked;
	}

	public void setClicked(boolean clicked) {
		this.clicked = clicked;
	}

	public Bubble(Drawable img, boolean clicked) {
		super();
		this.img = img;
		this.clicked = clicked;
	}

	public Bubble() {
		super();
	}


}
