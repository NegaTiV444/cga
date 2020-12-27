package com.negativ.render.math;

public class MyColor {

    public static void swap(MyColor v1, MyColor v2) {
        MyColor tmp = v1.copy();
        v1.x = v2.x;
        v1.y = v2.y;
        v1.z = v2.z;
        v2.x = tmp.x;
        v2.y = tmp.y;
        v2.z = tmp.z;
    }

    public int x, y, z;

    public MyColor(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public MyColor() {
        this.x = 1;
        this.y = 1;
        this.z = 1;
    }

//    public MyColor(MyVector3 myVector3) {
//        this.x = (int)(myVector3.x + (myVector3.x > 0 ? 0.5f : -0.5f));
//        this.y = (int)(myVector3.y + (myVector3.y > 0 ? 0.5f : -0.5f));
//        this.z = (int)(myVector3.z + (myVector3.z > 0 ? 0.5f : -0.5f));
//    }

    public MyColor(MyVector3 myVector3) {
        this.x = (int)(myVector3.x + 0.5f);
        this.y = (int)(myVector3.y + 0.5f);
        this.z = (int)myVector3.z;
    }

    public int len() {
        return (int)Math.sqrt(x * x + y * y + z * z);
    }

    public MyColor scale(MyVector3 vector) {
        return scale(vector.x, vector.y, vector.z);
    }

    public MyColor scale(double x, double y, double z) {
        return new MyColor(new MyVector3(this.x * x, this.y * y, this.z * z));
    }

    public MyColor add(MyColor vector) {
        return add(vector.x, vector.y, vector.z);
    }

    public MyColor add(int x, int y, int z) {
        return new MyColor(this.x + x, this.y + y, this.z + z);
    }

    public MyColor scale(double scale) {
        return scale(scale, scale, scale);
    }

    public MyColor scale(MyColor vector) {
        return scale(vector.x, vector.y, vector.z);
    }

    public MyColor sub(MyColor vector) {
        return sub(vector.x, vector.y, vector.z);
    }

    public MyColor sub(int x, int y, int z) {
        return new MyColor(this.x - x, this.y - y, this.z - z);
    }

    public MyColor copy() {
        return new MyColor(x, y, z);
    }

    @Override
    public String toString() {
        return String.format("x: %4d y: %4d z: %4d", x, y, z);
    }

    public int toRGB888() {
        int r, g, b;
        r = Math.min(x, 255);
        g = Math.min(y, 255);
        b = Math.min(z, 255);
        return (r << 24) + (g << 16) + (b << 8);
    }
}
