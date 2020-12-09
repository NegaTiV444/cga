package com.negativ.render.math;

public class MyMatrix4 {

    protected double[] val = new double[]{
            1, 0, 0, 0,
            0, 1, 0, 0,
            0, 0, 1, 0,
            0, 0, 0, 1
    };

    public MyMatrix4(double[] val) {
        this.val = val;
    }

    public MyMatrix4() {
    }

    public static MyMatrix4 getTranslationMatrix(MyVector3 translation) {
        return new MyMatrix4(new double[]{
                1f, 0, 0, translation.x,
                0, 1f, 0, translation.y,
                0, 0, 1f, translation.z,
                0, 0, 0, 1f
        });
    }

    public static MyMatrix4 getScaleMatrix(MyVector3 scale) {
        return new MyMatrix4(new double[]{
                scale.x, 0, 0, 0,
                0, scale.y, 0, 0,
                0, 0, scale.z, 0,
                0, 0, 0, 1f
        });
    }

    public static MyMatrix4 getXrotationMatrix(MyVector3 rotation) {
        return new MyMatrix4(new double[]{
                1f, 0, 0, 0,
                0, Math.cos(rotation.x), -1 * Math.sin(rotation.x), 0,
                0, Math.sin(rotation.x), Math.cos(rotation.x), 0,
                0, 0, 0, 1f
        });
    }

    public static MyMatrix4 getYrotationMatrix(MyVector3 rotation) {
        return new MyMatrix4(new double[]{
                Math.cos(rotation.y), 0, Math.sin(rotation.y), 0,
                0, 1f, 0, 0,
                -1 * Math.sin(rotation.y), 0, Math.cos(rotation.y), 0,
                0, 0, 0, 1f
        });
    }

    public static MyMatrix4 getZrotationMatrix(MyVector3 rotation) {
        return new MyMatrix4(new double[]{
                Math.cos(rotation.z), -1 * Math.sin(rotation.z), 0, 0,
                Math.sin(rotation.z), Math.cos(rotation.z), 0, 0,
                0, 0, 1f, 0,
                0, 0, 0, 1f
        });
    }

    public static MyMatrix4 getRelativeProjectionMatrixUsingFov(double aspect, double fov, double zNear, double zFar) {
        double x11 = 1 / (aspect * Math.tan(fov / 2));
        double x22 = 1 / Math.tan(fov / 2);
        double x33 = zFar / (zNear - zFar);
        double x34 = zNear * zFar / (zNear - zFar);
        double data[] = {
                x11, 0, 0, 0,
                0, x22, 0, 0,
                0, 0, x33, x34,
                0, 0, -1, 0
        };
        return new MyMatrix4(data);
    }

//    public static MyMatrix4 getRelativeProjectionMatrix(double zNear, double zFar, double height, double width) {
////        double x11 = 2 * zNear / width;
////        double x22 = 2 * zNear / height;
////        double x33 = zFar / (zNear - zFar);
////        double x34 = zNear * zFar / (zNear - zFar);
////        double data[] = {
////                x11, 0, 0, 0,
////                0, x22, 0, 0,
////                0, 0, x33, x34,
////                0, 0, -1f/100000, 0
////        };
////        return new MyMatrix4(data);
////    }

    public static MyMatrix4 getOrtoProjection(double zNear, double zFar, double height, double width) {
        double x11 = 2 / width;
        double x22 = 2 / height;
        double x33 = 1 / (zNear - zFar);
        double x34 = zNear/ (zNear - zFar);
        double data[] = {
                x11, 0, 0, 0,
                0, x22, 0, 0,
                0, 0, x33, x34,
                0, 0, 0, 1
        };
        return new MyMatrix4(data);
    }


    public static MyMatrix4 getViewportMatrix(double width, double height) {
        double data[] = {
                width / 2, 0, 0, width / 2,
                0, -height / 2, 0, height / 2,
                0, 0, 1, 0,
                0, 0, 0, 1
        };
        return new MyMatrix4(data);
    }

    public static MyMatrix4 getWorldMatrix() {
        double data[] = {
                1000, 0, 0, 0,
                0, 1000, 0, 0,
                0, 0, 1000, 0,
                0, 0, 0, 1
        };
        return new MyMatrix4(data);
    }

    public static MyMatrix4 getViewMatrix(MyVector3 eye, MyVector3 target, MyVector3 up) {
        MyVector3 z = eye.sub(target).nor();
        MyVector3 x = up.crossProduct(z).nor();
        MyVector3 y = z.crossProduct(x).nor();

        return new MyMatrix4(new double[]{
                x.x, x.y, x.z, -1 * x.dotProduct(eye),
                y.x, y.y, y.z, -1 * y.dotProduct(eye),
                z.x, z.y, z.z, -1 * z.dotProduct(eye),
                0, 0, 0, 1f
        });
//        return new MyMatrix4(new double[]{
//                x.x, x.y, x.z, 0,
//                y.x, y.y, y.z, 0,
//                z.x, z.y, z.z, 0,
//                0, 0, 0, 1f
//        });
    }

    public static MyMatrix4 getModelTranslationMatrix(MyVector3 position, MyVector3 scale, MyVector3 rotation) {
        MyMatrix4 translationMatrix = getTranslationMatrix(position);
        MyMatrix4 scaleMatrix = getScaleMatrix(scale);
        MyMatrix4 xRotationMatrix = getXrotationMatrix(rotation);
        MyMatrix4 yRotationMatrix = getYrotationMatrix(rotation);
        MyMatrix4 zRotationMatrix = getZrotationMatrix(rotation);
        return translationMatrix.mul(scaleMatrix).mul(xRotationMatrix).mul(yRotationMatrix).mul(zRotationMatrix);
    }

    @Override
    public String toString() {
        return "|" + val[0] + ", " + val[1] + ", " + val[2] + ", " + val[3] + "|\n" +
                "|" + val[4] + ", " + val[5] + ", " + val[6] + ", " + val[7] + "|\n" +
                "|" + val[8] + ", " + val[9] + ", " + val[10] + ", " + val[11] + "|\n" +
                "|" + val[12] + ", " + val[13] + ", " + val[14] + ", " + val[15] + "|";
    }

    /*
        0, 1, 2, 3,
        4, 5, 6, 7,
        8, 9, 10, 11,
        12, 13, 14, 15
     */

    public MyMatrix4 mul(MyMatrix4 matrix) {
        return mul(matrix.val);
    }

    public MyMatrix4 mul(double[] val) {
        final double[] mata = this.val;
        final double[] matb = val;

        double m0 = mata[0] * matb[0] + mata[1] * matb[4] + mata[2] * matb[8] + mata[3] * matb[12];
        double m1 = mata[0] * matb[1] + mata[1] * matb[5] + mata[2] * matb[9] + mata[3] * matb[13];
        double m2 = mata[0] * matb[2] + mata[1] * matb[6] + mata[2] * matb[10] + mata[3] * matb[14];
        double m3 = mata[0] * matb[3] + mata[1] * matb[7] + mata[2] * matb[11] + mata[3] * matb[15];

        double m4 = mata[4] * matb[0] + mata[5] * matb[4] + mata[6] * matb[8] + mata[7] * matb[12];
        double m5 = mata[4] * matb[1] + mata[5] * matb[5] + mata[6] * matb[9] + mata[7] * matb[13];
        double m6 = mata[4] * matb[2] + mata[5] * matb[6] + mata[6] * matb[10] + mata[7] * matb[14];
        double m7 = mata[4] * matb[3] + mata[5] * matb[7] + mata[6] * matb[11] + mata[7] * matb[15];

        double m8 = mata[8] * matb[0] + mata[9] * matb[4] + mata[10] * matb[8] + mata[11] * matb[12];
        double m9 = mata[8] * matb[1] + mata[9] * matb[5] + mata[10] * matb[9] + mata[11] * matb[13];
        double m10 = mata[8] * matb[2] + mata[9] * matb[6] + mata[10] * matb[10] + mata[11] * matb[14];
        double m11 = mata[8] * matb[3] + mata[9] * matb[7] + mata[10] * matb[11] + mata[11] * matb[15];

        double m12 = mata[12] * matb[0] + mata[13] * matb[4] + mata[14] * matb[8] + mata[15] * matb[12];
        double m13 = mata[12] * matb[1] + mata[13] * matb[5] + mata[14] * matb[9] + mata[15] * matb[13];
        double m14 = mata[12] * matb[2] + mata[13] * matb[6] + mata[14] * matb[10] + mata[15] * matb[14];
        double m15 = mata[12] * matb[3] + mata[13] * matb[7] + mata[14] * matb[11] + mata[15] * matb[15];

        return new MyMatrix4(new double[]{
                m0, m1, m2, m3,
                m4, m5, m6, m7,
                m8, m9, m10, m11,
                m12, m13, m14, m15
        });
    }
}
