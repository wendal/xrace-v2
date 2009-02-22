///:~
/**
 * XRace V2
 * <p/>Use for ... ...
 * <p/>ID: $Id: $
 * Last Commit:  $Author: $
 * @version $Revision: $
 * 
 */
package com.sa.xrace.client;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Region;


/**
 * @author jlin
 * @version $Id: Sprite.java,v 1.1 2008-11-17 07:32:26 cpan Exp $
 */
public class Sprite {
	public float x,y;
	public Bitmap img,bakc_img;
	public int frameWidth,frameHeight;
	public Region region;
	public Paint paint;
	public float left,top,right,bottom;
	public double speadX,speadY;
	public int rgb[];
	public float beginX,beginY;
	public boolean visible = true;
	public float t_collisionRectX,t_collisionRectY,t_collisionRectWidth,t_collisionRectHeight;
	
	public Sprite(){
		
	}
	public Sprite(Bitmap img, int frameWidth, int frameHeight,float x,float y) {
		this.img = img;
		this.frameWidth = frameWidth;
		this.frameHeight = frameHeight;
		this.x =x;
		this.y = y;
		
		t_collisionRectX=0;
		t_collisionRectY=0;
		t_collisionRectWidth = frameWidth;
		t_collisionRectHeight=  frameHeight;
		
		beginX = x;
		beginY = y;
		rgb= new int[frameWidth*frameHeight];
		img.getPixels(rgb, 0,frameWidth, 0, 0, frameWidth, frameHeight );
		
	}
	public void setPaint(Paint paint){
		this.paint = paint;
	}
	public float getX(){
		return 0;
	}
	public float getY(){
		return 0;
	}
	public void setPosition(float x,float y){
		this.x = x;
		this.y = y;
	}
	public void setVisible(boolean temvisible){
		this.visible = temvisible;
	}
	public void move(float dx, float dy){
		x+=dx;
		y+=dy;
	}
	public void automove(){
		x+=speadX;
		y+=speadY;
	}
	public void setImage(Bitmap img){ 
		this.img = img;
		img.getPixels(rgb, 0,frameWidth, 0, 0, frameWidth, frameHeight );
	}
	
	
	public boolean collidesWith(Sprite s){
		setRegion();
		s.setRegion();
		return region.op(s.region, Region.Op.INTERSECT );
	}
	//////////////////////////////////////////////////////////////////////////////
	
	
	 public boolean collideWith_Sprite(Sprite sprite,int type){
		 switch(type){
			 case 0:{  //up
				 int startY=0;
				 int k=0;
				 for( ;k<frameWidth*frameHeight;k++){
					 if(rgb[k]!=0){
						 startY = k/frameWidth*frameWidth;    
						 break;
					 }
				 }
				 
///////////////////map move				 
				 int intX = (int)x+(int)((sprite.beginX-sprite.x));
				 int intY = (int)y+k/frameWidth-1+(int)((sprite.beginY-sprite.y));    //-1 is Spead
				 
//////////////car move
//				 int intX = (int)x;
//				 int intY = (int)y+k/frameWidth-1;    //-1 is Spead
				 
				 
				 for(int i=intY*sprite.frameWidth+intX,j=startY;i<intY*sprite.frameWidth+intX+frameWidth ;i++,j++){
					 if(i<=0 || i>sprite.frameWidth*sprite.frameHeight-1){
						 return true;
					 }
					 if((sprite.rgb[i]& rgb[j])!=0){
						 return true;
					 }
				 }
				 
				 return false;
			 }
			 case 1:{  //down
				 int startY=0;
				 int k=frameWidth*frameHeight-1;
				 for(;k>=0;k--){
					 if(rgb[k]!=0){
						 startY =  k/frameWidth*frameWidth;
						 break;
					 }
				 }
				 int intX = (int)x+(int)((sprite.beginX-sprite.x));
				 int intY = (int)y+k/frameWidth+1+(int)((sprite.beginY-sprite.y));  //1 is Spead
//				 int intX = (int)x;
//				 int intY = (int)y+k/frameWidth+1;  //1 is Spead
				 
				 for(int i=intY*sprite.frameWidth+intX,j=startY;i<intY*sprite.frameWidth+intX+frameWidth ;i++,j++){
					 if(i<=0 || i>sprite.frameWidth*sprite.frameHeight-1){
						 return true;
					 }
					 if((sprite.rgb[i]& rgb[j])!=0){
						 return true;
					 }
				 }
				 return false;
			 } 
			 
			 case 2:{  //left
				 int startX=0;
				 boolean isBreak = false;
				 int n=0;
				 for(;n<frameWidth;n++){
					 for(int m=0;m<frameHeight;m++){
						 if(rgb[m*frameWidth+n]!=0){
							 startX = n;
							 isBreak= true;
							 break;
						 }
					 }
					 if(isBreak){
						 isBreak = false;
						 break;
					 }
				 }
				 int intX = (int)x+n-1+(int)((sprite.beginX-sprite.x));
				 int intY = (int)y+(int)((sprite.beginY-sprite.y));
//				 int intX = (int)x+n-1;
//				 int intY = (int)y;
				 
				 for(int i=intY*sprite.frameWidth+intX,j=startX;i<intY*sprite.frameWidth+intX+sprite.frameWidth*frameWidth ;i+=sprite.frameWidth,j+=frameWidth){
					 if(i<0 || i>sprite.frameWidth*sprite.frameHeight-1){
						 return true;
					 }
					 if((sprite.rgb[i]& rgb[j])!=0){
						 return true;
					 }
				 }
				 break;
			 }
			 case 3:{  //right
				 int startX=0;
				 boolean isBreak = false;
				 int n=frameWidth-1;
				 
				 for(;n>=0;n--){
					 for(int m=0;m<frameHeight;m++){
						 if(rgb[m*frameWidth+n]!=0){
							 startX = n;
							 isBreak= true;
							 break;
						 }
					 }
					 if(isBreak){
						 isBreak= false;
						 break;
					 }
				 }
				 int intX = (int)x+n+1+(int)((sprite.beginX-sprite.x));
				 int intY = (int)y+(int)((sprite.beginY-sprite.y));
//				 int intX = (int)x+n+1;
//				 int intY = (int)y;
				 for(int i=intY*sprite.frameWidth+intX,j=startX;i<intY*sprite.frameWidth+intX+sprite.frameWidth*frameWidth ;i+=sprite.frameWidth,j+=frameWidth){
					 if(i<0 || i>sprite.frameWidth*sprite.frameHeight-1){
						 return true;
					 }
					 if((sprite.rgb[i]& rgb[j])!=0){
						 return true;
					 }
				 }
				 break;
			 }
		 }
		 return false;
	 }

	public final boolean collidesWith(Sprite s, boolean pixelLevel) {
		if (!s.visible || !visible)
			return false;
		int otherLeft =(int)(s.x + s.t_collisionRectX);
		int otherTop = (int)(s.y + s.t_collisionRectY);
		int otherRight =(int) (otherLeft + s.t_collisionRectWidth);
		int otherBottom = (int) (otherTop + s.t_collisionRectHeight);
		int left = (int)(x + t_collisionRectX);
		int top = (int)(y + t_collisionRectY);
		int right = (int)(left + t_collisionRectWidth);
		int bottom = (int)(top + t_collisionRectHeight);
		
		if (intersectRect(otherLeft, otherTop, otherRight, otherBottom, left,
				top, right, bottom)) {
			if (pixelLevel) {
				if (t_collisionRectX < 0)
					left = (int)x;
				if (t_collisionRectY < 0)
					top = (int)y;
				if (t_collisionRectX + t_collisionRectWidth > frameWidth)
					right = (int)(x + frameWidth);
				if (t_collisionRectY + t_collisionRectHeight > frameHeight)
					bottom = (int)(y + frameHeight);
				if (s.t_collisionRectX < 0)
					otherLeft = (int)s.x;
				if (s.t_collisionRectY < 0)
					otherTop = (int)s.y;
				if (s.t_collisionRectX + s.t_collisionRectWidth > s.frameWidth)
					otherRight = (int)(s.x + s.frameWidth);
				if (s.t_collisionRectY + s.t_collisionRectHeight > s.frameHeight)
					otherBottom =(int)( s.y + s.frameHeight);
				if (!intersectRect(otherLeft, otherTop, otherRight,
						otherBottom, left, top, right, bottom)) {
					return false;
				}
				
				else {
					int intersectLeft = left >= otherLeft ? left : otherLeft;
					int intersectTop = top >= otherTop ? top : otherTop;
					int intersectRight = right >= otherRight ? otherRight
							: right;
					int intersectBottom = bottom >= otherBottom ? otherBottom
							: bottom;
					int intersectWidth = Math.abs(intersectRight
							- intersectLeft);
					int intersectHeight = Math.abs(intersectBottom
							- intersectTop);
					return doPixelCollision(s, intersectLeft,
							intersectTop, intersectWidth, intersectHeight);
				}
			}
			else {
				return true;
			}
		}
		else {
			return false;
		}
	}
	private boolean doPixelCollision(Sprite s,int interLeft,int interTop,int interWidth,int interHeight){
		for(int i=interLeft-(int)x,m=interLeft-(int)s.x;i<interLeft-(int)x+interWidth;i++,m++){
			for(int j=interTop-(int)y,n=interTop-(int)s.y;j<interTop-(int)y+interHeight;j++,n++){
				if((rgb[j*frameWidth+i] & s.rgb[n*frameWidth+m])!=0){
					return true;
				}
			}
		}
		return false;
	}
	 private boolean intersectRect(int r1x1, int r1y1, int r1x2, int r1y2, int r2x1, int r2y1, int r2x2, 
	            int r2y2)
	 {
	        return r2x1 < r1x2 && r2y1 < r1y2 && r2x2 > r1x1 && r2y2 > r1y1;
	 }
	public void setRegion(){
		region = new Region((int)(x+left),(int)(y+top),(int)(x+right),(int)(y+bottom));
	}
	public void setRect(float temLeft,int temTop,int temRight,int temBottom){
		left = temLeft;
		top = temTop;
		right = temRight;
		bottom = temBottom;
	}
	public void paint(Canvas canvas){
		if(visible){
			canvas.drawBitmap(img, x, y, paint);
		}
	}
	public void setSpead(double temspeadX,double temSpeadY){
		this.speadX = temspeadX;
		this.speadY = temSpeadY;
	}

}
