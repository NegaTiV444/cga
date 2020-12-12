package com.negativ.render.model;

import com.negativ.render.math.MyMatrix4;
import com.negativ.render.math.MyVector3;

@Deprecated
public class CameraImpl1 implements Camera{

    private final MyVector3 DEFAULT_CAMERA_POSITION = new MyVector3(0, 0, -100);
    private final MyVector3 DEFAULT_CAMERA_TARGET = new MyVector3(0, 0, 0);
    private final MyVector3 WORLD_Y = new MyVector3(0, 1, 0);


    private MyVector3 pos  = DEFAULT_CAMERA_POSITION;
    private MyVector3 target = DEFAULT_CAMERA_TARGET;
    private MyVector3 z;

    private MyVector3 y = new MyVector3();
    private MyVector3 x = new MyVector3();

    private float yaw = (float)Math.PI / -4;
    private float pitch = 0;

    private float movementSpeed = 21f;
    private float rotateSpeed = (float)Math.PI * 2 / 150.0f;

    public String getStateString() {
        return "Eye: " + pos + "\n" +
                "Target: " + target + "\n" +
                "Reverse direction: " + z + "\n" +
                "y: " + y + "\n" +
                "x: " + x + "\n" +
                "Yaw: " + yaw + "\n" +
                "Pitch: " + pitch;
    }

    public CameraImpl1(MyVector3 position) {
        updateCameraVectors();
    }

    public CameraImpl1() {
        updateCameraVectors();
    }

    public MyMatrix4 getViewMatrix() {
        return MyMatrix4.getViewMatrix(pos, target, y);
//        return MyMatrix4.getViewMatrix(pos, DEFAULT_CAMERA_TARGET, WORLD_y);
    }

    @Override
    public void rotate(MyVector3 rotation) {

    }

    @Override
    public void move(MyVector3 movement) {

    }

    @Override
    public void restoreDefault() {

    }

    @Override
    public MyVector3 getPosition() {
        return null;
    }

    //Camera movement

    public void moveZ(float sign) {
        pos = pos.add(z.scale(sign).scale(movementSpeed));
    }

    public void moveX(float sign) {
        pos = pos.add(x.scale(sign).scale(movementSpeed));
    }

    public void changePitch(float sign) {
        float limit = (float)Math.PI / 2 - 0.05f;
        pitch += rotateSpeed * sign;
        pitch = Math.min(pitch, limit);
        pitch = Math.max(pitch, -limit);
        updateCameraVectors();
    }

    public void changeYaw(float sign) {
        yaw += rotateSpeed * sign;
        updateCameraVectors();
    }

    public void rotate(float sign) {
        int radius = 100;
        float speed = 0.05f;
        pitch += speed * sign;
        pos.x = Math.sin(pitch) * radius;
        pos.z = Math.cos(pitch) * radius;
        updateCameraVectors();
    }

    public void moveTarget(float sign) {
        target.z += 10 * sign;
        updateCameraVectors();
    }

    private void updateCameraVectors() {
//        z.x = (Math.cos(yaw) * Math.cos(pitch));
//        z.y = (Math.sin(pitch));
//        z.z = (Math.sin(yaw) * Math.cos(pitch));
//        z = z.nor();
        z  = pos.sub(target).nor();
        x = z.crossProduct(WORLD_Y).nor();
        y = x.crossProduct(z).nor();
    }

}
