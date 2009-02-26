package com.sa.xrace.client.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public final class CloneableCanvas extends Canvas {

    public CloneableCanvas(Bitmap bm){
        super(bm);
    }
    
    @Override
    public Canvas clone() throws CloneNotSupportedException {
        // TODO Auto-generated method stub
        return (Canvas)super.clone();
    }
    
    

}
