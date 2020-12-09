package com.negativ.render.math;

public class MyVector4 {

    public static void swap(MyVector4 v1, MyVector4 v2) {
        MyVector4 tmp = v1.copy();
        v1.x = v2.x;
        v1.y = v2.y;
        v1.z = v2.z;
        v1.w = v2.w;
        v2.x = tmp.x;
        v2.y = tmp.y;
        v2.z = tmp.z;
        v2.w = tmp.w;
    }

    public double x, y, z, w;

    public MyVector4(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public MyVector4() {
        this.x = 1f;
        this.y = 1f;
        this.z = 1f;
        this.w = 1f;
    }

    public double len4() {
        return Math.sqrt(x * x + y * y + z * z + w * w);
    }

    public double len3() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public double len2() {
        return x * x + y * y + z * z;
    }

    public MyVector4 nor2() {
        final double len2 = this.len2();
        if (len2 == 0f || len2 == 1f) return this;
        return new MyVector4(x * 1f / Math.sqrt(len2), y * 1f / Math.sqrt(len2), z * 1f / Math.sqrt(len2), w * 1f / Math.sqrt(len2));
    }

    public MyVector4 mul(MyMatrix4 matrix) {
        final double val[] = matrix.val;
        return new MyVector4(x * val[0] + y * val[1] + z * val[2] + w * val[3], x
                * val[4] + y * val[5] + z * val[6] + w * val[7], x * val[8] + y
                * val[9] + z * val[10] + w * val[11], x * val[12] + y
                * val[13] + z * val[14] + w * val[15]);
    }

    public MyVector4 copy() {
        return new MyVector4(x, y, z, w);
    }

    public MyVector4 reverse() {
        return new MyVector4(-x, -y, -z, -w);
    }

    public MyVector3 toVector3() {
        return new MyVector3(x / w, y / w, z / w);
    }

    @Override
    public String toString() {
        return String.format("x: %7f y: %7f z: %7f w: %7f", x, y, z, w);
    }
    
}
