package com.negativ.render.math;

public class MyVector3i {

    public static void swap(MyVector3i v1, MyVector3i v2) {
        MyVector3i tmp = v1.copy();
        v1.x = v2.x;
        v1.y = v2.y;
        v1.z = v2.z;
        v2.x = tmp.x;
        v2.y = tmp.y;
        v2.z = tmp.z;
    }

    public int x, y;
    public double z;

    public MyVector3i(int x, int y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public MyVector3i() {
        this.x = 1;
        this.y = 1;
        this.z = 1;
    }

//    public MyVector3i(MyVector3 myVector3) {
//        this.x = (int)(myVector3.x + (myVector3.x > 0 ? 0.5f : -0.5f));
//        this.y = (int)(myVector3.y + (myVector3.y > 0 ? 0.5f : -0.5f));
//        this.z = (int)(myVector3.z + (myVector3.z > 0 ? 0.5f : -0.5f));
//    }

    public MyVector3i(MyVector3 myVector3) {
        this.x = (int)(myVector3.x + 0.5f);
        this.y = (int)(myVector3.y + 0.5f);
        this.z = myVector3.z;
    }

    public int len() {
        return (int)Math.sqrt(x * x + y * y + z * z);
    }

    public MyVector3i scale(MyVector3 vector) {
        return scale(vector.x, vector.y, vector.z);
    }

    public MyVector3i scale(double x, double y, double z) {
        return new MyVector3i(new MyVector3(this.x * x, this.y * y, this.z * z));
    }

    public MyVector3i add(MyVector3i vector) {
        return add(vector.x, vector.y, vector.z);
    }

    public MyVector3i add(int x, int y, double z) {
        return new MyVector3i(this.x + x, this.y + y, this.z + z);
    }

    public MyVector3i scale(double scale) {
        return scale(scale, scale, scale);
    }

    public MyVector3i scale(MyVector3i vector) {
        return scale(vector.x, vector.y, vector.z);
    }

    public MyVector3i sub(MyVector3i vector) {
        return sub(vector.x, vector.y, vector.z);
    }

    public MyVector3i sub(int x, int y, double z) {
        return new MyVector3i(this.x - x, this.y - y, this.z - z);
    }

    public MyVector3i copy() {
        return new MyVector3i(x, y, z);
    }

    @Override
    public String toString() {
        return String.format("x: %4d y: %4d z: %fd", x, y, z);
    }

    public int toRGB888() {
        int r, g, b;
        r = Math.min(x, 255);
        g = Math.min(y, 255);
        b = Math.min((int)z, 255);
        return (r << 24) + (g << 16) + (b << 8);
    }
}
