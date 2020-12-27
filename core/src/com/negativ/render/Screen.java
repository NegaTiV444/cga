package com.negativ.render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.negativ.render.math.MyColor;
import com.negativ.render.math.MyVector2;
import com.negativ.render.math.MyVector3;
import com.negativ.render.math.MyVector3i;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Screen implements Disposable {

    private static long countDraw = 0;
    private static long countSkip = 0;

    private final MyVector3i DEFAULT_COLOR = new MyVector3i(0xAA, 0, 0);
    private final MyVector3i AMBIENT_COLOR = new MyVector3i(0xFF, 0, 0).scale(0.06f);

    private double DEFAULT_Z_BUFFER_VALUE = -10000;

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
        zBuffer = new double[width * height];
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
//        System.out.println("Draw: " + countDraw + " Skip: " + countSkip);
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
        drawPixel(x, y, z, DEFAULT_COLOR.toRGB888());
    }

    public void drawPixel(int x, int y, double z, int color) {
        if ((x > 0) && (x < width) && (y > 0) && (y < height)) {
            int zIndex = x + y * width;
            if (zBuffer[zIndex] <= z) {
                zBuffer[zIndex] = z;
                pixmap.drawPixel(x, y, color);
            }
        }
    }

    void drawPolygon(MyVector3 t0, MyVector3 t1, MyVector3 t2,
                     MyVector3 tex0, MyVector3 tex1, MyVector3 tex2,
                     MyVector3 light, List<MyVector3> normals, List<Double> reflectFromMap,
                     List<MyColor> colorsFromMap, int mapHeight, int mapWidth) {
        MyVector3 n1, n2;
        n1 = t2.sub(t1);
        n2 = t0.sub(t2);
        if (n2.crossProduct(n1).dotProduct(new MyVector3(0, 0, -1)) > 0) {
            MyColor color;
            MyVector3 vn;
            double maxX, maxY, minX, minY;
            maxX = Math.max(t0.x, Math.max(t1.x, t2.x));
            maxY = Math.max(t0.y, Math.max(t1.y, t2.y));
            minX = Math.min(t0.x, Math.min(t1.x, t2.x));
            minY = Math.min(t0.y, Math.min(t1.y, t2.y));
            MyVector2 A, B, C;
            A = new MyVector2(t0.x, t0.y);
            B = new MyVector2(t1.x, t1.y);
            C = new MyVector2(t2.x, t2.y);
            int normalsSize = normals.size();
            int index;
            double reflect;
            for (double x = minX; x <= maxX; x++)
                for (double y = minY; y <= maxY; y++) {
                    MyVector3 barycentric = barycentric(A, B, C, new MyVector2(x, y));
                    if (barycentric.x > -0.06 && barycentric.y > -0.06 && barycentric.z > -0.06) {
                        double z = barycentric.x * t0.z + barycentric.z * t1.z + barycentric.y * t2.z;
                        double texX = (barycentric.x * tex0.x + barycentric.z * tex1.x + barycentric.y * tex2.x) * mapWidth - 1;
                        double texY = mapHeight - ((barycentric.x * tex0.y + barycentric.z * tex1.y + barycentric.y * tex2.y) * mapHeight) - 1;
                        index = (int) (texY) + (int) (texX) * mapHeight;
                        if (index < 0) {
                            index = 0;
                        } else if (index > normalsSize) {
                            index = normalsSize - 1;
                        }
                        vn = normals.get(index);
                        color = colorsFromMap.get(index);
                        reflect = reflectFromMap.get(index);
                        color = getColor(color, light, vn, new MyVector3(0, 0, -1), reflect);

                        drawPixel((int) (x + 0.5), (int) (y + 0.5), z, color.toRGB888());
                    }
                }
        }

    }

    private MyColor getColor(MyColor color, MyVector3 light, MyVector3 normal, MyVector3 eye, double reflect) {

        MyColor result = color.scale(0.2);
        light = light.nor();
        double dot = normal.nor().dotProduct(light);
        if (dot <= 0) {
            return result;
        }
        double scale = light.dotProduct(normal) * 2;
        MyVector3 lightInvert = light.sub(normal.scale(scale));
        scale = Math.pow(eye.dotProduct(lightInvert), 16);
        result = result.add(color.scale((float) dot).scale(0.7)).add(color.scale(scale * reflect));

        return result;
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
        int dy = -Math.abs(y2 - y1);
        int sy = y1 < y2 ? 1 : -1;
        int err = dx + dy;
        for (; ; ) {
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
