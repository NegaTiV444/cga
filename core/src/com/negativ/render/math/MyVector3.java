package com.negativ.render.math;

public class MyVector3 {

    public static void swap(MyVector3 v1, MyVector3 v2) {
        MyVector3 tmp = v1.copy();
        v1.x = v2.x;
        v1.y = v2.y;
        v1.z = v2.z;
        v2.x = tmp.x;
        v2.y = tmp.y;
        v2.z = tmp.z;
    }

    public double x, y, z;

    public MyVector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public MyVector3() {
        this.x = 1f;
        this.y = 1f;
        this.z = 1f;
    }

    public double len() {
        return (double)Math.sqrt(x * x + y * y + z * z);
    }

    public double len2() {
        return x * x + y * y + z * z;
    }

    public MyVector3 nor() {
        final double len2 = this.len2();
        if (len2 == 0f || len2 == 1f) return this;
        return new MyVector3(x * 1f / (double)Math.sqrt(len2), y * 1f / (double)Math.sqrt(len2), z * 1f / (double)Math.sqrt(len2));
    }

    public MyVector3 crossProduct(MyVector3 vector) {
        return crossProduct(vector.x, vector.y, vector.z);
    }

    public MyVector3 crossProduct(double x, double y, double z) {
        return new MyVector3(this.y * z - this.z * y, this.z * x - this.x * z, this.x * y - this.y * x);
    }

    public double dotProduct (MyVector3 vector) {
        return dotProduct(vector.x, vector.y, vector.z);
    }

    public double dotProduct (double x, double y, double z) {
        return this.x * x + this.y * y + this.z * z;
    }

    public MyVector3 add(MyVector3 vector) {
        return add(vector.x, vector.y, vector.z);
    }

    public MyVector3 add(double x, double y, double z) {
        return new MyVector3(this.x + x, this.y + y, this.z + z);
    }

    public MyVector3 scale(double scale) {
        return scale(scale, scale, scale);
    }

    public MyVector3 scale(MyVector3 vector) {
        return scale(vector.x, vector.y, vector.z);
    }

    public MyVector3 scale(double x, double y, double z) {
        return new MyVector3(this.x * x, this.y * y, this.z * z);
    }

    public MyVector3 sub(MyVector3 vector) {
        return sub(vector.x, vector.y, vector.z);
    }

    public MyVector3 sub(double x, double y, double z) {
        return new MyVector3(this.x - x, this.y - y, this.z - z);
    }

    public MyVector3 mul(MyMatrix4 matrix) {
        final double val[] = matrix.val;
        return new MyVector3(x * val[0] + y * val[1] + z * val[2] + val[3], x
                * val[4] + y * val[5] + z * val[6] + val[7], x * val[8] + y
                * val[9] + z * val[10] + val[11]);
    }

    public MyVector3 copy() {
        return new MyVector3(x, y, z);
    }

    public MyVector3 reverse() {
        return new MyVector3(-x, -y, -z);
    }

    @Override
    public String toString() {
        return String.format("x: %7f y: %7f z: %7f", x, y, z);
    }
}
