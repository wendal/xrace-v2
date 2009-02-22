package com.sa.xrace.client.pool;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

public class RoomInforPool {
	
///////////////////////////////////////UP_Left/////////////////////////////////////
	int upLeft_position[] = {
			-toFixed(0.8f),toFixed(0.9f),-toFixed(6.7f),       //up_left
			-toFixed(0.8f),toFixed(0.35f),-toFixed(6.7f),
			-toFixed(0.1f), toFixed(0.35f),-toFixed(6.7f),
			
			-toFixed(0.8f),toFixed(0.9f),-toFixed(6.7f),    
			-toFixed(0.1f), toFixed(0.35f),-toFixed(6.7f),
			-toFixed(0.1f),toFixed(0.9f),-toFixed(6.7f), 
	};
	
	float upLeft_color[] = {
			1.0f,1.0f,1.0f,1.0f,
			1.0f,1.0f,1.0f,1.0f,
			1.0f,1.0f,1.0f,1.0f,
			
			1.0f,1.0f,1.0f,1.0f,
			1.0f,1.0f,1.0f,1.0f,
			1.0f,1.0f,1.0f,1.0f,
	};
	short upLeft_index[] = {
			0,1,2,
			3,4,5
	};
	
	float upLeft_UT[] = {
			0,0,	0,1,	1,1,
			0,0,	1,1,	1,0
	};
	
	
///////////////UP_Right//////////////////////////////////////////
	int upRight_position[] = {
			toFixed(0.1f),toFixed(0.9f),-toFixed(6.7f),       //up_left
			toFixed(0.1f),toFixed(0.35f),-toFixed(6.7f),
			toFixed(0.8f), toFixed(0.35f),-toFixed(6.7f),
			
			toFixed(0.1f),toFixed(0.9f),-toFixed(6.7f),    
			toFixed(0.8f), toFixed(0.35f),-toFixed(6.7f),
			toFixed(0.8f),toFixed(0.9f),-toFixed(6.7f), 
	};
	float upRight_color[] = {
			1.0f,1.0f,1.0f,1.0f,
			1.0f,1.0f,1.0f,1.0f,
			1.0f,1.0f,1.0f,1.0f,
			
			1.0f,1.0f,1.0f,1.0f,
			1.0f,1.0f,1.0f,1.0f,
			1.0f,1.0f,1.0f,1.0f,
	};
	short upRight_index[] = {
			0,1,2,
			3,4,5
	};
	float upRight_UT[] = {
			0,0,	0,1,	1,1,
			0,0,	1,1,	1,0
		};

		
///////////////Down_left///////////////////	///////////////////
	
	int downLeft_position[]={
			-toFixed(0.8f),toFixed(0.25f),-toFixed(6.7f),       //up_left
			-toFixed(0.8f),-toFixed(0.3f),-toFixed(6.7f),
			-toFixed(0.1f), -toFixed(0.3f),-toFixed(6.7f),
			
			-toFixed(0.8f),toFixed(0.25f),-toFixed(6.7f),    
			-toFixed(0.1f), -toFixed(0.3f),-toFixed(6.7f),
			-toFixed(0.1f),toFixed(0.25f),-toFixed(6.7f), 
	};
	

	
	float downLeft_color[]={
			1.0f,1.0f,1.0f,1.0f,
			1.0f,1.0f,1.0f,1.0f,
			1.0f,1.0f,1.0f,1.0f,
			
			1.0f,1.0f,1.0f,1.0f,
			1.0f,1.0f,1.0f,1.0f,
			1.0f,1.0f,1.0f,1.0f,
	};
	short downLeft_index[] = {
			0,1,2,
			3,4,5
	};
	float downLeft_UT[] = {
			0,0,	0,1,	1,1,
			0,0,	1,1,	1,0
	};
	
/////////////////////////////////down_Right/////////////////////////////////////
	int downRight_position[]={
			toFixed(0.1f),toFixed(0.25f),-toFixed(6.7f),       //up_left
			toFixed(0.1f),-toFixed(0.3f),-toFixed(6.7f),
			toFixed(0.8f), -toFixed(0.3f),-toFixed(6.7f),
			
			toFixed(0.1f),toFixed(0.25f),-toFixed(6.7f),    
			toFixed(0.8f),-toFixed(0.3f),-toFixed(6.7f),
			toFixed(0.8f),toFixed(0.25f),-toFixed(6.7f), 
	};
	float downRight_color[]={
			1.0f,1.0f,1.0f,1.0f,
			1.0f,1.0f,1.0f,1.0f,
			1.0f,1.0f,1.0f,1.0f,
			
			1.0f,1.0f,1.0f,1.0f,
			1.0f,1.0f,1.0f,1.0f,
			1.0f,1.0f,1.0f,1.0f,
	};
	short downRight_index[] = {
			0,1,2,
			3,4,5
	};
	float downRight_UT[] = {
			0,0,	0,1,	1,1,
			0,0,	1,1,	1,0
	};
	
////////////////////////////////////DOWN///////////////////////////
	int down_position[] ={ 							
			-toFixed(1f),-toFixed(0.4f),-toFixed(6.7f),       //up_left
			-toFixed(1f),-toFixed(1.0f),-toFixed(6.7f),
			toFixed(1f), -toFixed(1.0f),-toFixed(6.7f),
			
			-toFixed(1f),-toFixed(0.4f),-toFixed(6.7f),    
			toFixed(1f), -toFixed(1.0f),-toFixed(6.7f),
			toFixed(1f),-toFixed(0.4f),-toFixed(6.7f), 
	};
	float down_color[] = {                   
			1.0f,1.0f,1.0f,1.0f,
			1.0f,1.0f,1.0f,1.0f,
			1.0f,1.0f,1.0f,1.0f,
			
			1.0f,1.0f,1.0f,1.0f,
			1.0f,1.0f,1.0f,1.0f,
			1.0f,1.0f,1.0f,1.0f,
			
	};
	short down_index[] = {
			0,1,2,
			3,4,5
	};
	float down_UT[] = {
			0,0,	0,1,	1,1,
			0,0,	1,1,	1,0
	};
	
////////////////////////////MAP///////////////////////////////////////
	
	int mapBtn_position[] = {
			-toFixed(0.50f),-toFixed(0.69f),-toFixed(6.5f),       //up_left
			-toFixed(0.50f),-toFixed(0.95f),-toFixed(6.5f),
			-toFixed(0.0f), -toFixed(0.95f),-toFixed(6.5f),
			
			-toFixed(0.50f),-toFixed(0.69f),-toFixed(6.5f),      
			-toFixed(0.05f), -toFixed(0.95f),-toFixed(6.5f),
			-toFixed(0.05f), -toFixed(0.69f),-toFixed(6.5f),
	};
	float mapBtn_color[]={
			1.0f,1.0f,1.0f,1.0f,
			1.0f,1.0f,1.0f,1.0f,
			1.0f,1.0f,1.0f,1.0f,
			
			1.0f,1.0f,1.0f,1.0f,
			1.0f,1.0f,1.0f,1.0f,
			1.0f,1.0f,1.0f,1.0f,
	};
	short mapBtn_index[]={
			0,1,2,
			3,4,5
	};
	float mapBtn_UT[] = {
			0,0,	0,1,	1,1,
			0,0,	1,1,	1,0
		};
	
/////////////////////////////////START////////////////////////////////////////
	
	int startBtn_position[] = {
			toFixed(0.15f),-toFixed(0.69f),-toFixed(6.5f),       //up_left
			toFixed(0.15f),-toFixed(0.95f),-toFixed(6.5f),
			toFixed(0.6f), -toFixed(0.95f),-toFixed(6.5f),
			
			toFixed(0.15f),-toFixed(0.69f),-toFixed(6.5f),      
			toFixed(0.6f), -toFixed(0.95f),-toFixed(6.5f),
			toFixed(0.6f), -toFixed(0.69f),-toFixed(6.5f),
	};
	float startBtn_color[]={
			1.0f,1.0f,1.0f,1.0f,
			1.0f,1.0f,1.0f,1.0f,
			1.0f,1.0f,1.0f,1.0f,
			
			1.0f,1.0f,1.0f,1.0f,
			1.0f,1.0f,1.0f,1.0f,
			1.0f,1.0f,1.0f,1.0f,
	};
	short startBtn_index[]={
			0,1,2,
			3,4,5
	};
	float startBtn_UT[] = {
			0,0,	0,1,	1,1,
			0,0,	1,1,	1,0
		};
	
	
	
//	private IntBuffer texturesB;
	private ByteBuffer tempBuffer;

	public static IntBuffer upLeft_FaceVB, upRight_FaceVB,downLeft_FaceVB,downRight_FaceVB,down_FaceVB,mapBtn_FaceVB,startBtn_FaceVB;
	public static FloatBuffer upLeft_FaceCB, upRight_FaceCB,downLeft_FaceCB,downRight_FaceCB,down_FaceCB,mapBtn_FaceCB,startBtn_FaceCB;
	public static ShortBuffer upLeft_FaceIB, upRight_FaceIB, downLeft_FaceIB,downRight_FaceIB,down_FaceIB,mapBtn_FaceIB,startBtn_FaceIB;
	public static FloatBuffer upLeft_FaceUTB,upRight_FaceUTB,down_FaceUTB,mapBtn_FaceUTB,startBtn_FaceUTB,downLeft_FaceUTB,downRight_FaceUTB;




	private GL10 gl;
//	private RoomPicPool picPool;
	public RoomInforPool(GL10 gl,RoomPicPool picPool,IntBuffer texturesB){
		prepareupLeft_Face();
		prepareupRight_Face();
		preparedownLeft_Face();
		preparedownRight_Face();
		prepareDown_Face();
//		preparemapBtn();
//		preparestartBtn();
		this.gl = gl;
//		this.picPool = picPool;
//		this.texturesB =texturesB;
	}
	
	public int toFixed(float x) {
		return (int) (x * 65536.0f);
	}
	
	public void prepareupLeft_Face(){
		tempBuffer = ByteBuffer.allocateDirect(upLeft_position.length*4);
		tempBuffer.order(ByteOrder.nativeOrder());
		upLeft_FaceVB = tempBuffer.asIntBuffer();
		for(int i=0;i<upLeft_position.length;i++){
			upLeft_FaceVB.put(upLeft_position[i]);
		}
		upLeft_FaceVB.position(0);
		
		
		tempBuffer = ByteBuffer.allocateDirect(upLeft_color.length*4);
		tempBuffer.order(ByteOrder.nativeOrder());
		upLeft_FaceCB = tempBuffer.asFloatBuffer();
		for(int i=0;i<upLeft_color.length;i++){
			upLeft_FaceCB.put(upLeft_color[i]);
		}
		upLeft_FaceCB.position(0);
		
		
		tempBuffer = ByteBuffer.allocateDirect(upLeft_index.length*2);
		tempBuffer.order(ByteOrder.nativeOrder());
		upLeft_FaceIB = tempBuffer.asShortBuffer();
		for(int i=0;i<upLeft_index.length;i++){
			upLeft_FaceIB.put(upLeft_index[i]);
		}
		upLeft_FaceIB.position(0);
		
		tempBuffer = ByteBuffer.allocateDirect(upLeft_UT.length*4);
		tempBuffer.order(ByteOrder.nativeOrder());
		upLeft_FaceUTB = tempBuffer.asFloatBuffer();
		for(int i=0;i<upLeft_UT.length;i++){
			upLeft_FaceUTB.put(upLeft_UT[i]);
		}
		upLeft_FaceUTB.position(0);
				
	}
	public void prepareupRight_Face(){
		tempBuffer = ByteBuffer.allocateDirect(upRight_position.length*4);
		tempBuffer.order(ByteOrder.nativeOrder()); 
		upRight_FaceVB = tempBuffer.asIntBuffer();
		for(int i=0;i<upRight_position.length;i++){
			upRight_FaceVB.put(upRight_position[i]);
		}
		upRight_FaceVB.position(0);
		
		
		tempBuffer = ByteBuffer.allocateDirect(upRight_color.length*4);
		tempBuffer.order(ByteOrder.nativeOrder());
		upRight_FaceCB = tempBuffer.asFloatBuffer();
		for(int i= 0;i<upRight_color.length;i++){
			upRight_FaceCB.put(upRight_color[i]);
		}
		upRight_FaceCB.position(0);
		
		
		tempBuffer = ByteBuffer.allocateDirect(upRight_index.length*2);
		tempBuffer.order(ByteOrder.nativeOrder());
		upRight_FaceIB = tempBuffer.asShortBuffer();
		for(int i=0;i<upRight_index.length;i++){
			upRight_FaceIB.put(upRight_index[i]);
		}
		upRight_FaceIB.position(0);
		
		tempBuffer = ByteBuffer.allocateDirect(upRight_UT.length*4);
		tempBuffer.order(ByteOrder.nativeOrder());
		upRight_FaceUTB = tempBuffer.asFloatBuffer();
		for(int i=0;i<upRight_UT.length;i++){
			upRight_FaceUTB.put(upRight_UT[i]);
		}
		upRight_FaceUTB.position(0);
	}
	
	public void preparedownLeft_Face(){
		tempBuffer = ByteBuffer.allocateDirect(downLeft_position.length*4);
		tempBuffer.order(ByteOrder.nativeOrder());
		downLeft_FaceVB = tempBuffer.asIntBuffer();
		for(int i=0;i<downLeft_position.length;i++){
			downLeft_FaceVB.put(downLeft_position[i]);
		}
		downLeft_FaceVB.position(0);
		
		tempBuffer = ByteBuffer.allocateDirect(downLeft_color.length*4);
		tempBuffer.order(ByteOrder.nativeOrder());
		downLeft_FaceCB = tempBuffer.asFloatBuffer();
		for(int i= 0;i<downLeft_color.length;i++){
			downLeft_FaceCB.put(downLeft_color[i]);
		}
		downLeft_FaceCB.position(0);
		
		tempBuffer = ByteBuffer.allocateDirect(downLeft_index.length*2);
		tempBuffer.order(ByteOrder.nativeOrder());
		downLeft_FaceIB = tempBuffer.asShortBuffer();
		for(int i=0;i<downLeft_index.length;i++){
			downLeft_FaceIB.put(downLeft_index[i]);
		}
		downLeft_FaceIB.position(0);
		
		tempBuffer = ByteBuffer.allocateDirect(downLeft_UT.length*4);
		tempBuffer.order(ByteOrder.nativeOrder());
		downLeft_FaceUTB = tempBuffer.asFloatBuffer();
		for(int i=0;i<downLeft_UT.length;i++){
			downLeft_FaceUTB.put(downLeft_UT[i]);
		}
		downLeft_FaceUTB.position(0);
	}
	
	public void preparedownRight_Face(){
		tempBuffer = ByteBuffer.allocateDirect(downRight_position.length*4);
		tempBuffer.order(ByteOrder.nativeOrder());
		downRight_FaceVB = tempBuffer.asIntBuffer();
		for(int i=0;i<downRight_position.length;i++){
			downRight_FaceVB.put(downRight_position[i]);
		}
		downRight_FaceVB.position(0);
		
		tempBuffer = ByteBuffer.allocateDirect(downRight_color.length*4);
		tempBuffer.order(ByteOrder.nativeOrder());
		downRight_FaceCB = tempBuffer.asFloatBuffer();
		for(int i= 0;i<downRight_color.length;i++){
			downRight_FaceCB.put(downRight_color[i]);
		}
		downRight_FaceCB.position(0);
		
		tempBuffer = ByteBuffer.allocateDirect(downRight_index.length*2);
		tempBuffer.order(ByteOrder.nativeOrder());
		downRight_FaceIB = tempBuffer.asShortBuffer();
		for(int i=0;i<downRight_index.length;i++){
			downRight_FaceIB.put(downRight_index[i]);
		}
		downRight_FaceIB.position(0);
		
		tempBuffer = ByteBuffer.allocateDirect(downRight_UT.length*4);
		tempBuffer.order(ByteOrder.nativeOrder());
		downRight_FaceUTB = tempBuffer.asFloatBuffer();
		for(int i=0;i<downRight_UT.length;i++){
			downRight_FaceUTB.put(downRight_UT[i]);
		}
		downRight_FaceUTB.position(0);
	}
	
	public void prepareDown_Face(){
		tempBuffer = ByteBuffer.allocateDirect(down_position.length*4);
		tempBuffer.order(ByteOrder.nativeOrder());
		down_FaceVB = tempBuffer.asIntBuffer();
		for(int i=0;i<down_position.length;i++){
			down_FaceVB.put(down_position[i]);
		}
		down_FaceVB.position(0);
		
		tempBuffer = ByteBuffer.allocateDirect(down_color.length*4);
		tempBuffer.order(ByteOrder.nativeOrder());
		down_FaceCB = tempBuffer.asFloatBuffer();
		for(int i= 0;i<down_color.length;i++){
			down_FaceCB.put(down_color[i]);
		}
		down_FaceCB.position(0);
		
		tempBuffer = ByteBuffer.allocateDirect(down_index.length*2);
		tempBuffer.order(ByteOrder.nativeOrder());
		down_FaceIB = tempBuffer.asShortBuffer();
		for(int i=0;i<down_index.length;i++){
			down_FaceIB.put(down_index[i]);
		}
		down_FaceIB.position(0);
		tempBuffer = ByteBuffer.allocateDirect(down_UT.length*4);
		tempBuffer.order(ByteOrder.nativeOrder());
		down_FaceUTB = tempBuffer.asFloatBuffer();
		for(int i=0;i<down_UT.length;i++){
			down_FaceUTB.put(down_UT[i]);
		}
		down_FaceUTB.position(0);
	}
	public void preparemapBtn(){
		tempBuffer = ByteBuffer.allocateDirect(mapBtn_position.length*4);
		tempBuffer.order(ByteOrder.nativeOrder());
		mapBtn_FaceVB = tempBuffer.asIntBuffer();
		for(int i=0;i<mapBtn_position.length;i++){
			mapBtn_FaceVB.put(mapBtn_position[i]);
		}
		mapBtn_FaceVB.position(0);
		
		tempBuffer = ByteBuffer.allocateDirect(mapBtn_color.length*4);
		tempBuffer.order(ByteOrder.nativeOrder());
		mapBtn_FaceCB = tempBuffer.asFloatBuffer();
		for(int i=0;i<mapBtn_color.length;i++){
			mapBtn_FaceCB.put(mapBtn_color[i]);
		}
		mapBtn_FaceCB.position(0);
		
		tempBuffer = ByteBuffer.allocateDirect(mapBtn_index.length*2);
		tempBuffer.order(ByteOrder.nativeOrder());
		mapBtn_FaceIB = tempBuffer.asShortBuffer();
		for(int i=0;i<mapBtn_index.length;i++){
		mapBtn_FaceIB.put(mapBtn_index[i]);
		}
		mapBtn_FaceIB.position(0);
		
		tempBuffer = ByteBuffer.allocateDirect(mapBtn_UT.length*4);
		tempBuffer.order(ByteOrder.nativeOrder());
		mapBtn_FaceUTB = tempBuffer.asFloatBuffer();
		for(int i=0;i<mapBtn_UT.length;i++){
			mapBtn_FaceUTB.put(mapBtn_UT[i]);
		}
		mapBtn_FaceUTB.position(0);
	}
	
	public void preparestartBtn(){
		tempBuffer = ByteBuffer.allocateDirect(startBtn_position.length*4);
		tempBuffer.order(ByteOrder.nativeOrder());
		startBtn_FaceVB = tempBuffer.asIntBuffer();
		for(int i=0;i<startBtn_position.length;i++){
			startBtn_FaceVB.put(startBtn_position[i]);
		}
		startBtn_FaceVB.position(0);
		
		tempBuffer = ByteBuffer.allocateDirect(startBtn_color.length*4);
		tempBuffer.order(ByteOrder.nativeOrder());
		startBtn_FaceCB = tempBuffer.asFloatBuffer();
		for(int i=0;i<startBtn_color.length;i++){
			startBtn_FaceCB.put(startBtn_color[i]);
		}
		startBtn_FaceCB.position(0);
		
		tempBuffer = ByteBuffer.allocateDirect(startBtn_index.length*2);
		tempBuffer.order(ByteOrder.nativeOrder());
		startBtn_FaceIB = tempBuffer.asShortBuffer();
		for(int i=0;i<startBtn_index.length;i++){
			startBtn_FaceIB.put(startBtn_index[i]);
		}
		startBtn_FaceIB.position(0);
		
		tempBuffer = ByteBuffer.allocateDirect(startBtn_UT.length*4);
		tempBuffer.order(ByteOrder.nativeOrder());
		startBtn_FaceUTB = tempBuffer.asFloatBuffer();
		for(int i=0;i<startBtn_UT.length;i++){
			startBtn_FaceUTB.put(startBtn_UT[i]);
		}
		startBtn_FaceUTB.position(0);
	}
	
	
	
	public void drawMachine(){
		gl.glPushMatrix();
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);   
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);		
		gl.glFrontFace(GL10.GL_CCW);
//		gl.glEnable(GL10.GL_TEXTURE_2D);
		
		
		
//		gl.glBindTexture(GL10.GL_TEXTURE_2D, texturesB.get(0));				
		upLeft_FaceVB.position(0);
		upLeft_FaceCB.position(0);
		upLeft_FaceIB.position(0);
//		upLeft_FaceUTB.position(0);
		gl.glVertexPointer(3, GL10.GL_FIXED, 0, upLeft_FaceVB);
		gl.glColorPointer(4, GL10.GL_FLOAT, 0, upLeft_FaceCB);
//		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, upLeft_FaceUTB);
		gl.glDrawElements(GL10.GL_TRIANGLES, upLeft_index.length,GL10.GL_UNSIGNED_SHORT, upLeft_FaceIB);		
		
//		gl.glBindTexture(GL10.GL_TEXTURE_2D, texturesB.get(1));
		upRight_FaceVB.position(0);
		upRight_FaceCB.position(0);
		upRight_FaceIB.position(0);
//		upRight_FaceUTB.position(0);
		gl.glVertexPointer(3,  GL10.GL_FIXED, 0, upRight_FaceVB);
		gl.glColorPointer(4, GL10.GL_FLOAT, 0, upRight_FaceCB);
//		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, upRight_FaceUTB);
		gl.glDrawElements(GL10.GL_TRIANGLES, upRight_index.length, GL10.GL_UNSIGNED_SHORT, upRight_FaceIB);
		
//		gl.glBindTexture(GL10.GL_TEXTURE_2D, texturesB.get(2));
		downLeft_FaceVB.position(0);
		downLeft_FaceCB.position(0);
		downLeft_FaceIB.position(0);
//		downLeft_FaceUTB.position(0);
		gl.glVertexPointer(3, GL10.GL_FIXED, 0, downLeft_FaceVB);
		gl.glColorPointer(4, GL10.GL_FLOAT, 0, downLeft_FaceCB);
//		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, downLeft_FaceUTB);
		gl.glDrawElements(GL10.GL_TRIANGLES, downLeft_index.length, GL10.GL_UNSIGNED_SHORT, downLeft_FaceIB);

		
			
//		gl.glBindTexture(GL10.GL_TEXTURE_2D, texturesB.get(3));
		downRight_FaceVB.position(0);
		downRight_FaceCB.position(0);
		downRight_FaceIB.position(0);
//		downRight_FaceUTB.position(0);
		gl.glVertexPointer(3, GL10.GL_FIXED, 0, downRight_FaceVB);
		gl.glColorPointer(4, GL10.GL_FLOAT, 0, downRight_FaceCB);
//		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, downRight_FaceUTB);
		gl.glDrawElements(GL10.GL_TRIANGLES, downRight_index.length, GL10.GL_UNSIGNED_SHORT, downRight_FaceIB);
					
//		gl.glBindTexture(GL10.GL_TEXTURE_2D, texturesB.get(4));
		down_FaceVB.position(0);
		down_FaceCB.position(0);
		down_FaceIB.position(0);
//		down_FaceUTB.position(0);
		gl.glVertexPointer(3, GL10.GL_FIXED, 0, down_FaceVB);
		gl.glColorPointer(4, GL10.GL_FLOAT, 0, down_FaceCB);
//		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, down_FaceUTB);
		gl.glDrawElements(GL10.GL_TRIANGLES, down_index.length, GL10.GL_UNSIGNED_SHORT, down_FaceIB);	
		
		
		gl.glPopMatrix();
		
//		gl.glDisable(GL10.GL_TEXTURE_2D);
	}
	
	
}
