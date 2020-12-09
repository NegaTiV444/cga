package com.negativ.render;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.negativ.render.math.MyVector3i;

public class Screen implements Disposable {

    private int DEFAULT_COLOR = 0xFFFFFFFF;

    private SpriteBatch batch;
    private Texture nextFrame;
    private Pixmap pixmap;

    private int width;
    private int height;

    public Screen(int width, int height) {
        this.width = width;
        this.height = height;
        this.batch = new SpriteBatch();
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
    }

    @Override
    public void dispose() {
        batch.dispose();
        nextFrame.dispose();
        pixmap.dispose();
    }

    public void drawPixel(int x, int y) {
        drawPixel(x, y, DEFAULT_COLOR);
    }

    public void drawPixel(int x, int y, int color) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            pixmap.drawPixel(x, y, color);
        }
    }

    public void drawLine(int x0, int y0, int x1, int y1) {
        drawLine(x0, y0, x1, y1, DEFAULT_COLOR);
    }

    public void drawLine(int x1, int y1, int x2, int y2, int color) {
        int dx = Math.abs(x2 - x1);
        int sx = x1 < x2 ? 1 : -1;
        int dy = -Math.abs(y2-y1);
        int sy = y1 < y2 ? 1 : -1;
        int err = dx + dy;
        for (;;) {
            drawPixel(x1, y1, color);
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

    public void drawPolygon(MyVector3i v0, MyVector3i v1, MyVector3i v2) {
        drawPolygon(v0, v1, v2, DEFAULT_COLOR);
    }

    public void drawPolygon(MyVector3i v0, MyVector3i v1, MyVector3i v2, int color) {
        drawLine(v0.x, v0.y, v1.x, v1.y, color);
        drawLine(v0.x, v0.y, v2.x, v2.y, color);
        drawLine(v2.x, v2.y, v1.x, v1.y, color);
    }

    public void drawPolygon(MyVector3i v0, MyVector3i v1, MyVector3i v2, MyVector3i v3, int color) {
        drawLine(v0.x, v0.y, v1.x, v1.y, color);
        drawLine(v1.x, v1.y, v2.x, v2.y, color);
        drawLine(v2.x, v2.y, v3.x, v3.y, color);
        drawLine(v3.x, v3.y, v0.x, v0.y, color);
    }

    public void drawPolygon(MyVector3i v0, MyVector3i v1, MyVector3i v2, MyVector3i v3) {
        drawPolygon(v0, v1, v2, v3, DEFAULT_COLOR);
    }
}
