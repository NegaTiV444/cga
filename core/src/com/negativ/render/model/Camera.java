package com.negativ.render.model;

import com.negativ.render.math.MyMatrix4;
import com.negativ.render.math.MyVector3;

public interface Camera {

    String getStateString();
    MyMatrix4 getViewMatrix();
    void rotate(MyVector3 rotation);
    void move(MyVector3 movement);
    void restoreDefault();
    MyVector3 getPosition();
}
