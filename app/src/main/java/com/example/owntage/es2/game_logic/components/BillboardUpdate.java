package com.example.owntage.es2.game_logic.components;

import com.example.owntage.es2.game_logic.ComponentUpdate;

/**
 * Created by Owntage on 11/23/2015.
 */
public class BillboardUpdate extends ComponentUpdate {
    public float timeSinceCollided;
    public float displayTime;
    public String billboardTexture;
    public BillboardUpdate(float timeSinceCollided, float displayTime, String billboardTexture) {
        this.timeSinceCollided = timeSinceCollided;
        this.displayTime = displayTime;
        this.billboardTexture = billboardTexture;
        name = "billboard";
    }
}
