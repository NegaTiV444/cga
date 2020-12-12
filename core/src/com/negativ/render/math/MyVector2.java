package com.negativ.render.math;

public class MyVector2 {

    public static void swap(MyVector2 v1, MyVector2 v2) {
        MyVector2 tmp = v1.copy();
        v1.x = v2.x;
        v1.y = v2.y;
        v2.x = tmp.x;
        v2.y = tmp.y;
    }

    public double x, y;

    public MyVector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public MyVector2() {
        this.x = 1f;
        this.y = 1f;
    }

    public MyVector2(MyVector3i myVector3i) {
        this.x = myVector3i.x;
        this.y = myVector3i.x;
    }

    public MyVector2 add(MyVector2 vector) {
        return add(vector.x, vector.y);
    }

    public MyVector2 add(double x, double y) {
        return new MyVector2(this.x + x, this.y + y);
    }

    public MyVector2 scale(double scale) {
        return scale(scale, scale);
    }

    public MyVector2 scale(MyVector2 vector) {
        return scale(vector.x, vector.y);
    }

    public MyVector2 scale(double x, double y) {
        return new MyVector2(this.x * x, this.y * y);
    }

    public MyVector2 sub(MyVector2 vector) {
        return sub(vector.x, vector.y);
    }

    public MyVector2 sub(double x, double y) {
        return new MyVector2(this.x - x, this.y - y);
    }

    public MyVector2 copy() {
        return new MyVector2(x, y);
    }

    public MyVector2 reverse() {
        return new MyVector2(-x, -y);
    }

    @Override
    public String toString() {
        return String.format("x: %7f y: %7f", x, y);
    }
    
}
