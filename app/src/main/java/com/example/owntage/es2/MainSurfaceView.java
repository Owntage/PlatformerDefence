package com.example.owntage.es2;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by Owntage on 10/6/2015.
 */
public class MainSurfaceView extends GLSurfaceView {


    private MainRenderer renderer;

    public MainSurfaceView(Context context, MainRenderer renderer) {
        super(context);
        this.renderer = renderer;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {


        float x;
        float y;
        int touchID;

        class OnClickRunnable implements Runnable {
            float x;
            float y;
            int touchID;
            boolean onDown;
            public OnClickRunnable(float x, float y, int touchID, boolean onDown) {
                this.x = x;
                this.y = y;
                this.touchID = touchID;
                this.onDown = onDown;
            }
            public void run() {
                if(onDown) {
                    renderer.onDown(x, y, touchID);
                } else {
                    renderer.onUp(x, y, touchID);
                }
            }
        }

        switch(event.getActionMasked()) {
            case MotionEvent.ACTION_UP:
                x = event.getX();
                y = event.getY();
                touchID = event.getPointerId(event.getActionIndex());
                queueEvent(new OnClickRunnable(x, y, touchID, false));
                break;
            case MotionEvent.ACTION_POINTER_UP:
                x = event.getX(event.getPointerId(event.getActionIndex()));
                y = event.getY(event.getPointerId(event.getActionIndex()));
                touchID = event.getPointerId(event.getActionIndex());
                queueEvent(new OnClickRunnable(x, y, touchID, false));
                break;
            case MotionEvent.ACTION_MOVE:
                //renderer.onMove(x, y, touchID);
                break;
            case MotionEvent.ACTION_DOWN:

                x = event.getX();
                y = event.getY();
                touchID = event.getPointerId(event.getActionIndex());

                queueEvent(new OnClickRunnable(x, y, touchID, true));
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                x = event.getX(event.getPointerId(event.getActionIndex()));
                y = event.getY(event.getPointerId(event.getActionIndex()));
                touchID = event.getPointerId(event.getActionIndex());

                queueEvent(new OnClickRunnable(x, y, touchID, true));
                break;
        }

        return true;
    }
}
