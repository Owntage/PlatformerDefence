package com.example.owntage.es2;

import android.app.Application;
import android.support.annotation.Nullable;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKAccessTokenTracker;
import com.vk.sdk.VKSdk;

/**
 * Created by Алексей on 15.12.2015.
 */
public class MyApplication extends Application {
    VKAccessTokenTracker vkAccessTokenTracker=new VKAccessTokenTracker() {
        @Override
        public void onVKAccessTokenChanged(@Nullable VKAccessToken oldToken, @Nullable VKAccessToken newToken) {
            if(newToken==null)
            {
                //invalid
            }
        }
    };
    @Override
    public void onCreate() {
        super.onCreate();

        // Инициализируем SDK Вконтакте

        VKSdk.initialize(this);
    }
}
