package com.example.magdiel.taxiseguro;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by magdiel on 18/11/15.
 */
public class FloatingWindow extends Service {

    private WindowManager w;
    private LinearLayout ll;
    private Button stoped;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();

        w = (WindowManager) getSystemService(WINDOW_SERVICE);
        ll = new LinearLayout(this);
        stoped = new Button(this);

        ViewGroup.LayoutParams btnParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        stoped.setText("Dirección");
        stoped.setLayoutParams(btnParams);

        LinearLayout.LayoutParams llParameters = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        ll.setBackgroundColor(Color.argb(255, 255, 255, 255));
        ll.setLayoutParams(llParameters);

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(400, 150, WindowManager.LayoutParams.TYPE_PHONE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
        params.x = 0;
        params.y = 0;
        params.gravity = Gravity.CENTER | Gravity.CENTER;

        ll.addView(stoped);

        w.addView(ll, params);

        ll.setOnTouchListener(new View.OnTouchListener() {

            private WindowManager.LayoutParams updateParams = params;
            int x, y;

            float touchX, touchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        x = updateParams.x;
                        y = updateParams.y;

                        touchX = event.getRawX();
                        touchY = event.getRawY();

                        break;

                    case MotionEvent.ACTION_MOVE:

                        updateParams.x = (int) (x + (event.getRawX() - touchX));
                        updateParams.y = (int) (y + (event.getRawY() - touchY));

                        w.updateViewLayout(ll, updateParams);

                    default:
                        break;
                }
                return false;

            }
        });

        stoped.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                w.removeView(ll);
                stopSelf();
            }
        });

    }
}
