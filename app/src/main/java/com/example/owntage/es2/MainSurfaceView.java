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
        //setRenderMode(RENDERMODE_CONTINUOUSLY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {


        float x;
        float y;
        int touchID;


        switch(event.getActionMasked()) {
            case MotionEvent.ACTION_UP:
                x = event.getX();
                y = event.getY();
                touchID = event.getPointerId(event.getActionIndex());
                renderer.onUp(x, y, touchID);
                break;
            case MotionEvent.ACTION_POINTER_UP:
                x = event.getX(event.getPointerId(event.getActionIndex()));
                y = event.getY(event.getPointerId(event.getActionIndex()));
                touchID = event.getPointerId(event.getActionIndex());
                renderer.onUp(x, y, touchID);
                break;
            case MotionEvent.ACTION_MOVE:

                //renderer.onMove(x, y, touchID);

                break;
            case MotionEvent.ACTION_DOWN:

                x = event.getX();
                y = event.getY();
                touchID = event.getPointerId(event.getActionIndex());
                renderer.onDown(x, y, touchID);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                x = event.getX(event.getPointerId(event.getActionIndex()));
                y = event.getY(event.getPointerId(event.getActionIndex()));
                touchID = event.getPointerId(event.getActionIndex());
                renderer.onDown(x, y, touchID);
                break;
        }

        return true;
    }
}
