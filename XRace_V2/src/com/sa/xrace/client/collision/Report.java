package com.sa.xrace.client.collision;

import java.util.ArrayList;

import android.util.Log;

import com.sa.xrace.client.math.Point3f;

public class Report
{
	public static final int RCORNER=10;
	public static final int RFACE=11;
	public static final int RHORN=12;
	
	public static final int INVALID_REPORT=0;
	
	public static final int WALL_REPORT = 20;
	public static final int CAR_REPORT = 21;
	public static final int FINISH_REPORT = 22;
	
	public ArrayList<Integer> selfLineID;
	public ArrayList<Integer> targetLineID;
	public ArrayList<Line2f> targetLines;
	
	public boolean valid = false;
	
	public Point3f collisionPoint;
	
	public int whatReport;
	
	public Report()
	{
		refreshReport();
	}
	public int whatReport()
	{
		return whatReport;
	}
	public void validReport(int reportType)
	{
		whatReport = reportType;
		valid = true;
	}
	public void refreshReport()
	{
		valid = false;
		selfLineID = new ArrayList<Integer>();
		targetLineID = new ArrayList<Integer>();
		targetLines = new ArrayList<Line2f>();
	}
	
	public int checkReport()
	{
		switch (whatReport)
		{
		case WALL_REPORT:
		{
			if(valid == true)
			{
				if(selfLineID.size() >=2)
				{
					if((selfLineID.get(0) == selfLineID.get(1)) && (targetLineID.get(0) != targetLineID.get(1)))//means horn collision
					{					
						Log.e("RHORN","selfLineID"+selfLineID.size()+" targetLineID"+targetLineID.size()+" targetLines"+targetLines.size());
						return RHORN;
					}else if(selfLineID.get(1)-selfLineID.get(0)==1 || 
							(selfLineID.get(0) ==Rectangle.UP && selfLineID.get(1) == Rectangle.RIGHT))
					{
						Log.e("RCORNER","selfLineID"+selfLineID.size()+" targetLineID"+targetLineID.size()+" targetLines"+targetLines.size());
						
						Log.e("RCORNER","selfLineID.get(0)="+selfLineID.get(0)+" targetLineID.get(0) = "+targetLineID.get(0)+" k = "+targetLines.get(0).k+ " b = "+targetLines.get(0).b);
						Log.e("RCORNER","selfLineID.get(1)="+selfLineID.get(1)+" targetLineID.get(1) = "+targetLineID.get(1)+" k = "+targetLines.get(1).k+ " b = "+targetLines.get(1).b);
						
						Log.e("RCORNER","get(0).pointS.x ="+targetLines.get(0).pointS.x +"get(0).pointS.z ="+targetLines.get(0).pointS.z);
						Log.e("RCORNER","get(0).pointE.x ="+targetLines.get(0).pointE.x +"get(0).pointE.z ="+targetLines.get(0).pointE.z);
						
						Log.e("RCORNER","get(1).pointS.x ="+targetLines.get(1).pointS.x +"get(1).pointS.z ="+targetLines.get(1).pointS.z);
						Log.e("RCORNER","get(1).pointE.x ="+targetLines.get(1).pointE.x +"get(1).pointE.z ="+targetLines.get(1).pointE.z);
						
						return RCORNER;
					}else
					{
						Log.e("RFACE","selfLineID"+selfLineID.size()+" targetLineID"+targetLineID.size()+" targetLines"+targetLines.size());
						
						Log.e("RFACE","selfLineID.get(0)="+selfLineID.get(0)+" targetLineID.get(0) = "+targetLineID.get(0)+" k = "+targetLines.get(0).k+ " b = "+targetLines.get(0).b);
						Log.e("RFACE","selfLineID.get(1)="+selfLineID.get(1)+" targetLineID.get(1) = "+targetLineID.get(1)+" k = "+targetLines.get(1).k+ " b = "+targetLines.get(1).b);
						
						Log.e("RFACE","get(0).pointS.x ="+targetLines.get(0).pointS.x +"get(0).pointS.z ="+targetLines.get(0).pointS.z);
						Log.e("RFACE","get(0).pointE.x ="+targetLines.get(0).pointE.x +"get(0).pointE.z ="+targetLines.get(0).pointE.z);
						
						Log.e("RFACE","get(1).pointS.x ="+targetLines.get(1).pointS.x +"get(1).pointS.z ="+targetLines.get(1).pointS.z);
						Log.e("RFACE","get(1).pointE.x ="+targetLines.get(1).pointE.x +"get(1).pointE.z ="+targetLines.get(1).pointE.z);
						
						return RFACE;
					}
				}else
				{
					Log.e("selfLineID.size() error","<="+selfLineID.size());
				}
			}else
			{
				return INVALID_REPORT;
			}
		}
		case CAR_REPORT:
		{
			if(valid == true)
			{
				if(selfLineID.size() >=2)
				{
					if((selfLineID.get(0) == selfLineID.get(1)) && (targetLineID.get(0) != targetLineID.get(1)))//means horn collision
					{
//						Log.e("RHORN","RHORN");
						return RHORN;
					}else if(selfLineID.get(1)-selfLineID.get(0)==1 || 
							(selfLineID.get(0) ==Rectangle.UP && selfLineID.get(1) == Rectangle.RIGHT))
					{
//						Log.e("RCORNER","RCORNER");
						return RCORNER;
					}else
					{
						return RFACE;
					}
				}else
				{
					Log.e("selfLineID.size() error","<="+selfLineID.size());
				}
				
			}else
			{
				return INVALID_REPORT;
			}
		}
		case FINISH_REPORT:
		{
			if(valid == true)
			{
				return FINISH_REPORT;
			}else
			{
				return INVALID_REPORT;
			}
		}
		default:
			return INVALID_REPORT;
		}
	}	
}
