package com.example.owntage.es2.game_logic.components;

import com.example.owntage.es2.game_logic.ComponentUpdate;

/**
 * Created by Owntage on 11/20/2015.
 */
public class SentryGunUpdate extends ComponentUpdate {
    public String tripodTexture;
    public float angle;
    public float radius;
    public SentryGunUpdate(String tripodTexture, float angle, float radius) {
        name = "sentry";
        this.tripodTexture = tripodTexture;
        this.angle = angle;
        this.radius = radius;
    }

}
