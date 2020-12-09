package com.negativ.render.math;

public class MyVector3i {

    public static void swap(MyVector3i v1, MyVector3i v2) {
        MyVector3i tmp = v1.copy();
        v1.x = v2.x;
        v1.y = v2.y;
        v2.x = tmp.x;
        v2.y = tmp.y;
    }

    public int x, y, z;

    public MyVector3i(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public MyVector3i() {
        this.x = 1;
        this.y = 1;
        this.z = 1;
    }

    public MyVector3i(MyVector3 myVector3) {
        this.x = (int)(myVector3.x + (myVector3.x > 0 ? 0.5f : -0.5f));
        this.y = (int)(myVector3.y + (myVector3.y > 0 ? 0.5f : -0.5f));
        this.z = (int)(myVector3.z + (myVector3.z > 0 ? 0.5f : -0.5f));
    }

    public int len() {
        return (int)Math.sqrt(x * x + y * y + z * z);
    }

    public int len2() {
        return x * x + y * y + z * z;
    }

    public MyVector3i nor() {
        final int len2 = this.len2();
        if (len2 == 0f || len2 == 1f) return this;
        return new MyVector3i(x / (int)Math.sqrt(len2), y / (int)Math.sqrt(len2), z / (int)Math.sqrt(len2));
    }

    public MyVector3i crossProduct(MyVector3i vector) {
        return crossProduct(vector.x, vector.y, vector.z);
    }

    public MyVector3i crossProduct(int x, int y, int z) {
        return new MyVector3i(this.y * z - this.z * y, this.z * x - this.x * z, this.x * y - this.y * x);
    }

    public int dotProduct (MyVector3i vector) {
        return dotProduct(vector.x, vector.y, vector.z);
    }

    public int dotProduct (int x, int y, int z) {
        return this.x * x + this.y * y + this.z * z;
    }

    public MyVector3i add(MyVector3i vector) {
        return add(vector.x, vector.y, vector.z);
    }

    public MyVector3i add(int x, int y, int z) {
        return new MyVector3i(this.x + x, this.y + y, this.z + z);
    }

    public MyVector3i scale(double scale) {
        return scale(scale, scale, scale);
    }

    public MyVector3i scale(MyVector3i vector) {
        return scale(vector.x, vector.y, vector.z);
    }

    public MyVector3i scale(double x, double y, double z) {
        return new MyVector3i((int)(this.x * x), (int)(this.y * y), (int)(this.z * z));
    }

    public MyVector3i sub(MyVector3i vector) {
        return sub(vector.x, vector.y, vector.z);
    }

    public MyVector3i sub(int x, int y, int z) {
        return new MyVector3i(this.x - x, this.y - y, this.z - z);
    }

    public MyVector3i copy() {
        return new MyVector3i(x, y, z);
    }

    @Override
    public String toString() {
        return String.format("x: %4d y: %4d z: %4d", x, y, z);
    }
}
