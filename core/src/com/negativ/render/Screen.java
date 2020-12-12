package com.negativ.render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.negativ.render.math.MyVector2;
import com.negativ.render.math.MyVector3;
import com.negativ.render.math.MyVector3i;

import java.util.Arrays;

public class Screen implements Disposable {

    private static long countDraw = 0;
    private static long countSkip = 0;

    private int DEFAULT_COLOR = 0xFF0000FF;

    private double DEFAULT_Z_BUFFER_VALUE = Double.MIN_VALUE;

    private SpriteBatch batch;
    private Texture nextFrame;
    private Pixmap pixmap;

    private int width;
    private int height;

    private double[] zBuffer;

    public Screen(int width, int height) {
        this.width = width;
        this.height = height;
        this.batch = new SpriteBatch();
        zBuffer = new double[height * width];
        pixmap = new Pixmap(width, height, Pixmap.Format.RGB888);
        nextFrame = new Texture(pixmap);
    }

    public void render() {
        batch.begin();
        batch.draw(nextFrame, 0, 0);
        batch.end();
        nextFrame.dispose();
        nextFrame = new Texture(pixmap);
        pixmap.dispose();
        pixmap = new Pixmap(width, height, Pixmap.Format.RGB888);
        pixmap.setBlending(Pixmap.Blending.None);
        System.out.println("Draw: " + countDraw + " Skip: " + countSkip);
        countDraw = 0;
        countSkip = 0;
        clearZBuffer();
    }

    @Override
    public void dispose() {
        batch.dispose();
        nextFrame.dispose();
        pixmap.dispose();
    }

    private void clearZBuffer() {
        Arrays.fill(zBuffer, DEFAULT_Z_BUFFER_VALUE);
    }

    public void drawPixel(int x, int y, double z) {
        drawPixel(x, y, z, DEFAULT_COLOR);
    }

    public void drawPixel(int x, int y, double z, int color) {
        if (x > 0 && x < width && y > 0 && y < height) {
            int zIndex = x + y * width;
            if (zBuffer[zIndex] <= z) {
                zBuffer[zIndex] = z;
                pixmap.drawPixel(x, y, color);
                countDraw++;
            } else {
                countSkip++;
            }
        }
    }

    void drawPolygon(MyVector3 t0, MyVector3 t1, MyVector3 t2, MyVector3 vn0, MyVector3 vn1, MyVector3 vn2, MyVector3 light, MyVector3 cameraPos) {
        drawPolygon(t0, t1, t2, vn0, vn1, vn2, cameraPos, light, DEFAULT_COLOR);
    }

    void drawPolygon(MyVector3 t0, MyVector3 t1, MyVector3 t2, MyVector3 vn0, MyVector3 vn1, MyVector3 vn2, MyVector3 cameraPos, MyVector3 light, int color) {
        MyVector3 n1, n2, front, polygonCentre;

        n1 = t2.sub(t1);
        n2 = t0.sub(t2);
        if (n2.crossProduct(n1).dotProduct(new MyVector3(0, 0, -1)) > 0) {
            int color1;
            double dot0 =  vn0.dotProduct(light.nor());
            color1 = getColor(color, dot0 < 0 ? 0 : dot0);

            double maxX, maxY, minX, minY;
            maxX = Math.max(t0.x, Math.max(t1.x, t2.x));
            maxY = Math.max(t0.y, Math.max(t1.y, t2.y));
            minX = Math.min(t0.x, Math.min(t1.x, t2.x));
            minY = Math.min(t0.y, Math.min(t1.y, t2.y));
            MyVector2 A, B, C;
            A = new MyVector2 (t0.x, t0.y);
            B = new MyVector2 (t1.x, t1.y);
            C = new MyVector2 (t2.x, t2.y);
            for (double x = minX; x <= maxX; x++)
                for (double y = minY; y <= maxY; y++) {
                    MyVector3 barycentric = barycentric(A, B, C, new MyVector2 (x, y));
                    if (barycentric.x > -0.06 && barycentric.y > -0.06 && barycentric.z > -0.06) {
                        double z = barycentric.x * t0.z + barycentric.y * t1.z + barycentric.z * t2.z;
                        drawPixel((int)(x + 0.5), (int)(y + 0.5), z, color1);
                    }
                }
        }

    }

    private int getColor(int color, double coefficient) {
//        int r = color & 0xFF000000;
//        int g = color & 0x00FF0000;
//        int b = color & 0x0000FF00;
//        r = (int)(r * (float)coefficient);
//        r = r & 0xFF000000;
//        g = (int)(g * (float)coefficient) & 0x00FF0000;
//        b = (int)(b * (float)coefficient) & 0x0000FF00;
//        return r + g + b + 0xFF;
        float r = 0xFF * (float)coefficient;
        return (int)r << 8;
    }

    private MyVector3 barycentric(MyVector2 A, MyVector2 B, MyVector2 C, MyVector2 P) {
        MyVector2 AB, AC, PA;
        AB = B.sub(A);
        AC = C.sub(A);
        PA = A.sub(P);
        MyVector3 SX, SY;
        SX = new MyVector3(AB.x, AC.x, PA.x);
        SY = new MyVector3(AB.y, AC.y, PA.y);
        MyVector3 u = SX.crossProduct(SY);
        return new MyVector3(1 - (u.x + u.y) / u.z, u.y / u.z, u.x / u.z);
    }

    public void drawLine(int x1, int y1, int x2, int y2, int color) {
        int dx = Math.abs(x2 - x1);
        int sx = x1 < x2 ? 1 : -1;
        int dy = -Math.abs(y2-y1);
        int sy = y1 < y2 ? 1 : -1;
        int err = dx + dy;
        for (;;) {
            drawPixel(x1, y1, 1000, color);
            if (x1 == x2 && y1 == y2) {
                return;
            }
            int err2 = err * 2;
            if (err2 > dy) {
                err += dy;
                x1 += sx;
            }
            if (err2 <= dx) {
                err += dx;
                y1 += sy;
            }
        }
    }

}
