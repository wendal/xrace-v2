///:~
/**
 * XRace V2
 * <p/>Use for ... ...
 * <p/>ID: $Id$
 * Last Commit:  $Author$
 * @version $Revision: $
 * 
 */
package com.sa.xrace.client;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder mHolder;

    public GameView(Context context) {
        super(context);

        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_HARDWARE);
        mHolder.setFormat(PixelFormat.RGBA_8888);
    }

    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        ;
    }

    public void surfaceCreated(SurfaceHolder arg0) {
        new GLThread_Room(mHolder, this).start();
    }

    public void surfaceDestroyed(SurfaceHolder arg0) {
    	;
    }
}
