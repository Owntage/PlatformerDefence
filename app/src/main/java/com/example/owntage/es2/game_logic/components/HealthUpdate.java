package com.example.owntage.es2.game_logic.components;

import com.example.owntage.es2.game_logic.ComponentUpdate;

/**
 * Created by Owntage on 11/23/2015.
 */
public class HealthUpdate extends ComponentUpdate {
    public float health;
    public float currentHealth;
    public HealthUpdate(float health, float currentHealth) {
        this.health = health;
        this.currentHealth = currentHealth;
        name = "health";
    }
}
