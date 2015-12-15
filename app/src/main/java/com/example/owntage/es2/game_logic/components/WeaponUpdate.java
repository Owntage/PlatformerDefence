package com.example.owntage.es2.game_logic.components;

import com.example.owntage.es2.game_logic.ComponentUpdate;

/**
 * Created by Owntage on 11/17/2015.
 */
public class WeaponUpdate extends ComponentUpdate {
    public float angle;
    public float length;
    public float shootingPeriod;
    public float maxShootingPeriod;
    public float reloadTime;
    public float maxReloadTime;
    public WeaponUpdate(float angle, float length, float shootingPeriod, float maxShootingPeriod, float reloadTime, float maxReloadTime) {
        this.angle = angle;
        this.length = length;
        this.shootingPeriod = shootingPeriod;
        this.maxShootingPeriod = maxShootingPeriod;
        this.reloadTime = reloadTime;
        this.maxReloadTime = maxReloadTime;
        name = "weapon";
    }
    public boolean isEqualTo(WeaponUpdate weaponUpdate) {
        if(angle != weaponUpdate.angle) return false;
        if(length != weaponUpdate.length) return false;
        if(shootingPeriod != weaponUpdate.shootingPeriod) return false;
        if(maxShootingPeriod != weaponUpdate.maxShootingPeriod) return false;
        if(reloadTime != weaponUpdate.reloadTime) return false;
        return maxReloadTime == weaponUpdate.maxReloadTime;
    }
}
