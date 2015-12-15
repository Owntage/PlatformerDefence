package com.example.owntage.es2.game_logic.components.physics;

import android.util.Log;
import android.util.Pair;

import java.util.Collection;
import java.util.LinkedList;
import java.util.TreeMap;

/**
 * Created by Owntage on 10/15/2015.
 */
public class World {

    TreeMap<Integer, Body> bodies = new TreeMap<>();
    TreeMap<Integer, Body> activeBodies = new TreeMap<>();
    int bodyCount = 0;
    Vector gravity = new Vector(0.0f, -20.0f);
    float friction = 70.0f;

    void checkActiveBodies() {
        Collection<Body> bodyCollection = activeBodies.values();
        for(Body body : bodyCollection) {
            if(body.velocity.lengthSquared() < 0.00001f) {
                body.active = false;
                activeBodies.remove(body.id);
            }
        }
    }

    boolean isColliding(Body bodyA, Body bodyB) {
        if(bodyA.position.getX() + bodyA.scale.getX() * 0.5f < bodyB.position.getX() - bodyB.scale.getX() * 0.5f) return false;
        if(bodyA.position.getX() - bodyA.scale.getX() * 0.5f > bodyB.position.getX() + bodyB.scale.getX() * 0.5f) return false;

        if(bodyA.position.getY() + bodyA.scale.getY() * 0.5f < bodyB.position.getY() - bodyB.scale.getY() * 0.5f) return false;
        if(bodyA.position.getY() - bodyA.scale.getY() * 0.5f > bodyB.position.getY() + bodyB.scale.getY() * 0.5f) return false;
        if(!bodyA.isDynamic && !bodyB.isDynamic) return false;
        return true;
    }

    void correctPositions(Body bodyA, Body bodyB, Vector normal, float depth) {
        float percent = 0.3f;
        float slop = 0.03f;
        Vector correction = Vector.multiply(normal, Math.max(depth - slop, 0.0f) / (bodyA.invMass + bodyB.invMass) * percent);
        bodyA.setPosition(Vector.subtract(bodyA.getPosition(), Vector.multiply(correction, bodyA.invMass)));
        bodyB.setPosition(Vector.add(bodyB.getPosition(), Vector.multiply(correction, bodyB.invMass)));

    }

    void resolveCollision(Body bodyA, Body bodyB, Vector normal, float depth) {
        Vector relativeVelocity = Vector.subtract(bodyB.getVelocity(), bodyA.getVelocity());
        float velAlongNormal = Vector.dotProduct(relativeVelocity, normal);
        if(velAlongNormal > 0.0f) {
            return;
        }

        float e = Math.min(bodyA.getRestitution(), bodyB.getRestitution());

        float j = -(1 + e) * velAlongNormal;
        j /= bodyA.invMass + bodyB.invMass;

        Vector impulse = Vector.multiply(normal, j);

        float massSum = bodyA.mass + bodyB.mass;
        float ratio = bodyB.mass / massSum;
        bodyA.setVelocity(Vector.subtract(bodyA.getVelocity(), Vector.multiply(impulse, ratio)));
        ratio = bodyA.mass / massSum;
        bodyB.setVelocity(Vector.add(bodyB.getVelocity(), Vector.multiply(impulse, ratio)));
    }

    void detectCollisions() {
        for(Body body : bodies.values()) {
            body.ableToJump = false;
            body.collided = false;
        }

        Collection<Body> activeBodiesCollection = activeBodies.values();
        for(Body bodyA : activeBodiesCollection) {
            for(Body bodyB : bodies.values()) {
                if(isColliding(bodyA, bodyB)) {
                    if(bodyB.mass != 0.0f) {
                        setBodyActive(bodyB);
                    }

                    bodyB.collided = true;
                    bodyA.collided = true;
                    bodyA.collidedBodyName = bodyB.bodyName;
                    bodyB.collidedBodyName = bodyA.bodyName;

                    if(!bodyA.sensor && !bodyB.sensor) {

                        float overlapX = bodyA.position.getX() - bodyB.position.getX();
                        if (overlapX < 0.0f) overlapX = -overlapX;
                        overlapX = (bodyA.scale.getX() + bodyB.scale.getX()) / 2.0f - overlapX;

                        float overlapY = bodyA.position.getY() - bodyB.position.getY();
                        if (overlapY < 0.0f) overlapY = -overlapY;
                        overlapY = (bodyA.scale.getY() + bodyB.scale.getY()) / 2.0f - overlapY;

                        Vector normal = new Vector();
                        float depth;

                        if (overlapX < overlapY) {
                            depth = overlapX;
                            if (bodyA.position.getX() < bodyB.position.getX()) {
                                normal.setX(1.0f);
                            } else {
                                normal.setX(-1.0f);
                            }
                        } else {
                            depth = overlapY;
                            if (bodyA.position.getY() < bodyB.position.getY()) {
                                normal.setY(1.0f);
                                bodyB.ableToJump = true;
                            } else {

                                normal.setY(-1.0f);
                            }
                        }

                        resolveCollision(bodyA, bodyB, normal, depth);
                        correctPositions(bodyA, bodyB, normal, depth);
                    }
                }
            }
        }
    }

    public World() {}

    public Body createBody(boolean isDynamic, boolean sensor, String bodyName) {
        Body result = new Body(isDynamic, sensor, bodyName);
        result.id = bodyCount;
        bodyCount++;
        bodies.put(result.id, result);
        setBodyActive(result);
        return result;
    }

    public void destroyBody(Body body) {
        activeBodies.remove(body.id);
        bodies.remove(body.id);
        body.isDeleted = true;
    }

    public void setBodyActive(Body body) {
        if(!body.active) {
            activeBodies.put(body.id, body);
            body.active = true;
        }
    }

    public void setBodyPosition(Body body, Vector position) {
        body.setPosition(position);
        setBodyActive(body);
    }

    public void setBodyVelocity(Body body, Vector velocity) {
        body.setVelocity(velocity);
        setBodyActive(body);
    }

    public void setBodyAcceleration(Body body, Vector acceleration) {
        body.setAcceleration(acceleration);
        setBodyActive(body);
    }

    public void setBodyScale(Body body, Vector scale) {
        body.setScale(scale);
        setBodyActive(body);
    }

    public void setBodyRestitution(Body body, float restitution) {
        body.setRestitution(restitution);
    }

    public void setBodyMass(Body body, float mass) {
        body.setMass(mass);
    }

    public void step(float dt) {
        //checkActiveBodies();
        detectCollisions();
        for(Body body : bodies.values()) {
            body.getPosition().add(Vector.multiply(body.getVelocity(), dt));
            if(body.isDynamic) {

                body.getVelocity().add(Vector.multiply(gravity, dt));
                if (body.getVelocity().getX() > 0) {
                    Vector frictionVector = new Vector(-friction, 0.0f);
                    body.getVelocity().add(Vector.multiply(frictionVector, dt));
                } else if (body.getVelocity().getX() < 0) {
                    Vector frictionVector = new Vector(friction, 0.0f);
                    body.getVelocity().add(Vector.multiply(frictionVector, dt));
                }
                if(body.getVelocity().lengthSquared() < friction * friction * dt * dt * 0.25f) {
                    body.getVelocity().setX(0.0f);
                }
                body.getVelocity().add(Vector.multiply(body.getAcceleration(), dt));

            }
        }
    }

    public Pair<String, Float> raycast(Body body, float angle, float length) {
        final float step = 0.1f;
        float current = 0;
        float angleCos = (float) Math.cos(angle);
        float angleSin = (float) Math.sin(angle);
        while(current < length) {
            current += step;
            float tempX = body.getPosition().getX() + current * angleCos;
            float tempY = body.getPosition().getY() + current * angleSin;
            for(Body otherBody : bodies.values()) {
                if(otherBody != body) {
                    float otherX = otherBody.getPosition().getX();
                    float otherY = otherBody.getPosition().getY();
                    float otherScaleX = otherBody.getScale().getX();
                    float otherScaleY = otherBody.getScale().getY();
                    if(tempX > otherX - otherScaleX * 0.5f && tempX < otherX + otherScaleX * 0.5f &&
                            tempY > otherY - otherScaleY * 0.5f && tempY < otherY + otherScaleY * 0.5f) {
                        otherBody.raycasted = true;
                        otherBody.raycastedBodyName = body.bodyName;
                        return new Pair<>(otherBody.bodyName, current);
                    }
                }
            }
        }
        return new Pair<>(null, current);
    }

    public Body findTarget(Body body, String type, float length) {
        float minLength = -1.0f;
        Body result = null;
        for(Body otherBody : bodies.values()) {
            if(otherBody.bodyName.equals(type)) {
                float dx = otherBody.getPosition().getX() - body.getPosition().getX();
                float dy = otherBody.getPosition().getY() - body.getPosition().getY();
                String bodyName = body.bodyName;
                body.bodyName = "";
                Pair<String, Float> raycastResult = raycast(body, Utility.getAngle(dx, dy), length);
                body.bodyName = bodyName;
                if(raycastResult.first == null) continue;
                if(!raycastResult.first.equals(type)) continue;
                if(minLength == -1.0f || raycastResult.second < minLength) {
                    result = otherBody;
                    minLength = raycastResult.second;
                }
            }
        }
        return result;
    }

    public float getGravity() {
        return -gravity.getY();
    }

}
