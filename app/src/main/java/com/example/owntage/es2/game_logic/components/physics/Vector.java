package com.example.owntage.es2.game_logic.components.physics;

/**
 * Created by Owntage on 10/15/2015.
 */
public class Vector {
    private float x = 0.0f;
    private float y = 0.0f;

    public Vector() {}

    public Vector(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float length() {
        return (float) Math.sqrt(x * x + y * y);
    }

    public float lengthSquared() {
        return x * x + y * y;
    }

    public Vector add(Vector other) {
        x += other.x;
        y += other.y;
        return this;
    }

    public Vector subtract(Vector other) {
        x -= other.x;
        y -= other.y;
        return this;
    }

    public static Vector add(Vector a, Vector b) {
        return new Vector(a.x + b.x, a.y + b.y);
    }

    public static Vector subtract(Vector a, Vector b) {
        return new Vector(a.x - b.x, a.y - b.y);
    }


    public static float dotProduct(Vector a, Vector b) {
        return a.x * b.x + a.y * b.y;
    }



    public static Vector divide(Vector a, float b) {
        return new Vector(a.x / b, a.y / b);
    }

    public static Vector multiply(Vector a, float b) {
        return new Vector(a.x * b, a.y * b);
    }

    public static Vector getNormalVector(Vector a) {
        return divide(a, a.length());
    }
}
