package com.sa.xrace.client.collision;

import android.util.Log;

import com.sa.xrace.client.math.Point3f;

public class Line2f
{
	// k*x + b =  y;
	public static final int VERTICAL = 1;
	public static final int ORDINARY = 2;
	public static final int HORIZONTAL =3;
	
	public static final float ZERO = 0.001f;
	public int lineType;
	public float k ,b ;
	public Point3f pointS,pointE;
	public Point3f collisionPoint;
	
	public Line2f()
	{
		pointS = new Point3f();
		pointE = new Point3f();
		collisionPoint = new Point3f();
	}
	
	public Line2f(Point3f firstP,Point3f secondP)
	{
		generateLine( firstP, secondP);
		collisionPoint = new Point3f();
	}
	
	public void generateLine(Point3f firstP,Point3f secondP)
	{
		pointS=firstP;
		pointE=secondP;
		
		// k = (y2 - y1)/(x2 -x1)
//		if(Math.abs(secondP.x - firstP.x) >= ZERO)
//		{
//			lineType = ORDINARY;
//			k = (secondP.z - firstP.z)/(secondP.x - firstP.x);
//			b = firstP.z - k* firstP.x;
//		}else if(Math.abs(secondP.z - firstP.z) )
//		{// means this line is vertical in xoz
//			lineType = VERTICAL;
//			b = firstP.x;
//		}
		
		// k = (z2 - z1)/(x2 -x1)
		if(Math.abs(secondP.x - firstP.x) <= ZERO)
		{// means this line is vertical in xoz
			lineType = VERTICAL;
			b = firstP.x;
		}else if(Math.abs(secondP.z - firstP.z) <= ZERO)
		{// means this line is horizontal in xoz
			lineType = HORIZONTAL;
			k=0;
			b=firstP.z;
		}else
		{//means this is an ordinary line
			lineType = ORDINARY;
			k = (secondP.z - firstP.z)/(secondP.x - firstP.x);
			b = firstP.z - k* firstP.x;
		}
	}
	
	public float distanceBetweenP2fXZ(Point3f point)
	{
		// kx -y + b =0
		//	 |a*x +b*y +c|
		//d =--------------
		//	  ---------
		//   /a*a + b*b
		return (float)(Math.abs(k * point.x - point.z + b)/ Math.sqrt(k*k +1));
	}
	
	public boolean isCross(Line2f lineInput)
	{
		if(this.lineType == ORDINARY)
		{// means I am a ordinary line
			switch(lineInput.lineType)
			{
			case ORDINARY:
				return o_ordinaryLine(lineInput);
			case VERTICAL:
				return o_verticalLine(lineInput);
			case HORIZONTAL:
				return o_horizontal(lineInput);
			}
		}else if(this.lineType == HORIZONTAL)
		{// means I am a horizontal line
			switch(lineInput.lineType)
			{
			case ORDINARY:
				return h_ordinary(lineInput);
			case VERTICAL:
				return h_vertical(lineInput);
			case HORIZONTAL:				
				return h_horizontal(lineInput);
			}
		}else
		{//means I am a vertical line
			switch(lineInput.lineType)
			{
			case ORDINARY:
				return v_ordinaryLine(lineInput);
			case VERTICAL:
				return v_verticalLine(lineInput);
			case HORIZONTAL:
				return v_horizontal(lineInput);
			}
		}
		return false;
	}
	
	private boolean o_ordinaryLine(Line2f lineInput)
	{
		if(this.k != lineInput.k)
		{
			collisionPoint.x = (this.b-lineInput.b)/(lineInput.k-this.k);
			collisionPoint.z = this.k*collisionPoint.x +this.b;
		
			if(Math.min(lineInput.pointS.x, lineInput.pointE.x)<= collisionPoint.x 
					&& 
					collisionPoint.x <= Math.max(lineInput.pointS.x, lineInput.pointE.x)
					&&
			   Math.min(pointS.x, pointE.x)<= collisionPoint.x 
					&& 
					collisionPoint.x <= Math.max(pointS.x, pointE.x)	
			)
			{
				Log.i("o_o","o_o");
				return true;
			}else
			{
				return false;
			}
				
		}else if(this.b == lineInput.b)
		{
			Log.i("o_o1","o_o1");
				return true;
		}
		else 
		{
				return false;			
		}
	}
	private boolean o_verticalLine(Line2f lineInput)
	{
		collisionPoint.x =lineInput.pointS.x;
		collisionPoint.z = k*lineInput.pointS.x+b;
		
		if(Math.min(pointS.x,pointE.x)<= collisionPoint.x 
				&& 
				collisionPoint.x <= Math.max(pointS.x,pointE.x)
				&&
		   Math.min(pointS.z,pointE.z)<= collisionPoint.z 
		   		&& 
		   		collisionPoint.z <= Math.max(pointS.z,pointE.z)
		   		&&
		   Math.min(lineInput.pointS.z,lineInput.pointE.z)<= collisionPoint.z 
		   		&& 
		   		collisionPoint.z <= Math.max(lineInput.pointS.z,lineInput.pointE.z)	
		)
		{
			Log.i("o_v","o_v");
			return true;
		}
		else
		{
			return false;
		}	
	}
	private boolean v_ordinaryLine(Line2f lineInput)
	{
		collisionPoint.x =pointS.x;
		collisionPoint.z = lineInput.k*pointS.x+lineInput.b;
		
		if(Math.min(lineInput.pointS.x,lineInput.pointE.x)<= collisionPoint.x 
				&& 
				collisionPoint.x <= Math.max(lineInput.pointS.x,lineInput.pointE.x)
				&&
//		   Math.min(lineInput.pointS.z,lineInput.pointE.z)<= collisionPoint.z 
//		   		&& 
//		   		collisionPoint.z <= Math.max(lineInput.pointS.z,lineInput.pointE.z)
//		   		&&
		   Math.min(pointS.z,pointE.z)<= collisionPoint.z 
		   		&& 
		   		collisionPoint.z <= Math.max(pointS.z,pointE.z)		
		)
		{
			Log.i("v_o","v_o");
			Log.e("k "+lineInput.k+" b "+lineInput.b ,"");
			return true;
		}
		else
		{
			return false;
		}
	}
	private boolean v_verticalLine(Line2f lineInput)
	{
		if(this.b == lineInput.b)
		{
			Log.i("v_v","v_v");
			return true;
		}else
		{
			return false;
		}
	}
	private boolean o_horizontal(Line2f lineInput)
	{
		collisionPoint.x = (lineInput.b - this.b)/this.k;	
		collisionPoint.z = lineInput.b;
		if(Math.min(lineInput.pointS.x, lineInput.pointE.x)<= collisionPoint.x 
				&&
				collisionPoint.x <= Math.max(lineInput.pointS.x,lineInput.pointE.x)
				&&
		   Math.min(pointS.z, pointE.z)<= collisionPoint.z 
				&&
				collisionPoint.z <= Math.max(pointS.z,pointE.z)
				)
		{
			Log.i("o_h","o_h");
			return true;
		}else
		{
			return false;
		}
	}
	private boolean v_horizontal(Line2f lineInput)
	{
		collisionPoint.x = this.b;	
		collisionPoint.z = lineInput.b;		
		if(Math.min(lineInput.pointS.x, lineInput.pointE.x)<= collisionPoint.x 
				&&
				collisionPoint.x <= Math.max(lineInput.pointS.x,lineInput.pointE.x)
				&&
		   Math.min(pointS.z, pointE.z)<= collisionPoint.z 
				&&
				collisionPoint.z <= Math.max(pointS.z,pointE.z)
			)
		{
			Log.i("v_h","v_h");
			return true;
		}else
		{
			return false;
		}
	}
	private boolean h_horizontal(Line2f lineInput)
	{
//		if(lineInput.b == this.b)
//		{
//			Log.i("h_h","h_h");
//			return true;
//		}else
//		{
			return false;
//		}
	}
	private boolean h_vertical(Line2f lineInput)
	{
		collisionPoint.z = this.b;	
		collisionPoint.x = lineInput.b;		
		if(Math.min(pointS.x, pointE.x)<= collisionPoint.x 
				&&
				collisionPoint.x <= Math.max(pointS.x,pointE.x)
				&&
		   Math.min(lineInput.pointS.z, lineInput.pointE.z)<= collisionPoint.z 
				&&
				collisionPoint.z <= Math.max(lineInput.pointS.z,lineInput.pointE.z)
			)
		{
			Log.i("h_v","h_v");
			return true;
		}else
		{
			return false;
		}
	}
	private boolean h_ordinary(Line2f lineInput)
	{
		collisionPoint.x = (this.b - lineInput.b)/lineInput.k;	
		collisionPoint.z = this.b;
		if(Math.min(pointS.x, pointE.x)<= collisionPoint.x 
				&&
				collisionPoint.x <= Math.max(pointS.x,pointE.x)
				&&
		   Math.min(lineInput.pointS.x, lineInput.pointE.x)<= collisionPoint.x 
				&&
				collisionPoint.x <= Math.max(lineInput.pointS.x,lineInput.pointE.x)
				)
		{
			Log.i("h_o","h_o");
			return true;
		}else
		{
			return false;
		}
	}
}
