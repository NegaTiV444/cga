package com.negativ.render.model;

import com.negativ.render.math.MyMatrix4;
import com.negativ.render.math.MyVector3;

public class CameraImpl2 implements Camera{

    private final MyVector3 DEFAULT_CAMERA_POSITION = new MyVector3(0, 0, -10);
    private final MyVector3 DEFAULT_CAMERA_ROTATION = new MyVector3(0,  Math.PI, Math.PI);

    private MyVector3 position  = DEFAULT_CAMERA_POSITION;
    private MyVector3 rotation = DEFAULT_CAMERA_ROTATION;

    @Override
    public String getStateString() {
        return "Position: " + position + "\n" +
                "Rotation: " + rotation;
    }

    @Override
    public MyMatrix4 getViewMatrix() {
        MyMatrix4 translationMatrix = MyMatrix4.getTranslationMatrix(position);
        MyMatrix4 xRotationMatrix = MyMatrix4.getXrotationMatrix(rotation);
        MyMatrix4 yRotationMatrix = MyMatrix4.getYrotationMatrix(rotation);
        MyMatrix4 zRotationMatrix = MyMatrix4.getZrotationMatrix(rotation);
        return zRotationMatrix.mul(yRotationMatrix).mul(xRotationMatrix).mul(translationMatrix);
    }

    @Override
    public void move(MyVector3 movement) {

        MyMatrix4 xRotationMatrix = MyMatrix4.getXrotationMatrix(rotation);
        MyMatrix4 yRotationMatrix = MyMatrix4.getYrotationMatrix(rotation);
        MyMatrix4 zRotationMatrix = MyMatrix4.getZrotationMatrix(rotation);
        position.add(movement.mul(xRotationMatrix).mul(yRotationMatrix).mul(zRotationMatrix));

        this.position = this.position.add(movement);
    }

    @Override
    public void restoreDefault() {
        position  = DEFAULT_CAMERA_POSITION;
        rotation = DEFAULT_CAMERA_ROTATION;
    }

    @Override
    public MyVector3 getPosition() {
        return position;
    }

    @Override
    public void rotate(MyVector3 rotation) {
        this.rotation = this.rotation.add(rotation);
    }

    public void rotateCamera(float dxAngle, float dyAngle, float dzAngle) {

    }

    public void translateCamera(float dx, float dy, float dz) {

    }

}
