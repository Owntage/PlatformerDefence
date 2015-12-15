package com.example.owntage.es2.game_logic.components.physics;

/**
 * Created by Owntage on 10/15/2015.
 */
public class Body {
    Vector position = new Vector(0.0f, 0.0f);
    Vector scale = new Vector(1.0f, 1.0f);
    Vector velocity = new Vector(0.0f, 0.0f);
    Vector acceleration = new Vector(0.0f, 0.0f);
    String bodyName;
    String collidedBodyName;
    String raycastedBodyName;
    boolean collided = false;
    boolean raycasted = false;
    float restitution = 1.0f;
    float mass = 1.0f;
    float invMass = 1.0f;
    boolean active = false;
    boolean isDynamic;
    boolean ableToJump = false;
    boolean isDeleted = false;
    boolean sensor;
    int id;


    Body(boolean isDynamic, boolean sensor, String bodyName) {
        this.isDynamic = isDynamic;
        this.bodyName = bodyName;
        this.sensor = sensor;
    }

    public boolean getJump() {
        return ableToJump;
    }

    public boolean isCollided() {
        return collided;
    }

    public boolean isRaycasted() {return raycasted;}

    public void setRaycasted(boolean raycasted) {
        this.raycasted = raycasted;
    }

    public String collidedWith() {
        return collidedBodyName;
    }

    public String raycastedWith() { return raycastedBodyName; }

    public Vector getPosition() {
        return position;
    }

    void setPosition(Vector position) {
        this.position = position;
    }

    public Vector getScale() {
        return scale;
    }

    void setScale(Vector scale) {
        this.scale = scale;
    }

    public Vector getVelocity() {
        return velocity;
    }

    void setVelocity(Vector velocity) {
        this.velocity = velocity;
    }

    public Vector getAcceleration() {
        return acceleration;
    }

    void setAcceleration(Vector acceleration) {
        this.acceleration = acceleration;
    }

    public float getRestitution() {
        return restitution;
    }

    void setRestitution(float restitution) {
        this.restitution = restitution;
    }

    void setMass(float mass) {
        if(mass != 0.0f) {
            this.mass = mass;
            this.invMass = 1.0f / mass;
        } else {
            this.mass = 0.0f;
            this.invMass = 0.0f;
        }
    }
}
