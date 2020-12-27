package com.negativ.render.utils;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.negativ.render.math.MyColor;
import com.negativ.render.math.MyVector3;
import com.negativ.render.math.MyVector4;
import com.negativ.render.math.Polygon;
import com.negativ.render.model.ModelMetaInf;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Loader {

    private final String SPLITTER = " ";
    private final String POLYGON_VERTEXES_SPLITTER = "/";
    private final String VERTEX_MARKER = "v";
    private final String POLYGON_MARKER = "f";
    private final String VERTEX_NORMAL_MARKER = "vn";
    private final String TEX_MARKER = "vt";

    private final int VALUES_IN_VERTEX_LINE_COUNT = 3;
    private final int VERTEX_NORMALS_IN_LINE_COUNT = 3;
    private final int MIN_VALUES_IN_POLYGON_LINE_COUNT = 3;

    private Loader() {};

    private static class SingletonHandler {
        public static Loader INSTANCE = new Loader();
    }

    public static Loader getInstance() {
        return Loader.SingletonHandler.INSTANCE;
    }

    public ModelMetaInf loadModel(String fileName, String diffuseMapFileName, String normalMapFileName, String specularMapFileName) throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        List<MyVector4> vertexes = new ArrayList<>();
        List<Polygon> polygons = new ArrayList<>();
        List<MyVector3> normals = new ArrayList<>();
        List<MyVector3> tex = new ArrayList<>();
        br.lines()
                .filter(line -> line.startsWith(VERTEX_MARKER) || line.startsWith(POLYGON_MARKER))
                .map(line -> line.trim().replaceAll(" +", " "))
                .forEach(line -> {
                    String[] components = line.split(SPLITTER);
                    if (components[0].equals(VERTEX_MARKER)) {
                        if (components.length >= VALUES_IN_VERTEX_LINE_COUNT + 1) {
                            double w = components.length == VALUES_IN_VERTEX_LINE_COUNT + 2 ? Double.parseDouble(components[4]) : 1;
                            vertexes.add(new MyVector4(Double.parseDouble(components[1]), Double.parseDouble(components[2]), Double.parseDouble(components[3]), w));
                        } else {
                            System.out.println("Line '" + line + "' was skipped");
                        }
                    } else if (components[0].equals(POLYGON_MARKER)) {                 //Format - v/vt/vn
                        if (components.length > MIN_VALUES_IN_POLYGON_LINE_COUNT) {
                            List<Integer> polygonVertexesId = new ArrayList<>();
                            List<Integer> polygonNormalsId = new ArrayList<>();
                            List<Integer> polygonTexId = new ArrayList<>();
                            for (int i = 1; i < components.length; i++) {
                                String[] idArray = components[i].split(POLYGON_VERTEXES_SPLITTER);
                                polygonVertexesId.add(Integer.parseInt(idArray[0]));
                                polygonTexId.add(Integer.parseInt(idArray[1]));
                                polygonNormalsId.add(Integer.parseInt(idArray[2]));
                            }
                            polygons.add(new Polygon(polygonVertexesId, polygonTexId, polygonNormalsId));
                        } else {
                            System.out.println("Line '" + line + "' was skipped");
                        }
                    } else if (components[0].equals(VERTEX_NORMAL_MARKER)) {
                        if (components.length == VERTEX_NORMALS_IN_LINE_COUNT + 1) {
                            normals.add(new MyVector3(Double.parseDouble(components[1]), Double.parseDouble(components[2]), Double.parseDouble(components[3])));
                        } else {
                            System.out.println("LIne '" + line + "' was skipped");
                        }
                    } else if (components[0].equals(TEX_MARKER)) {
                        tex.add(new MyVector3(Double.parseDouble(components[1]), Double.parseDouble(components[2]), Double.parseDouble(components[3])));
                    }
                });
        Texture diffuseMap = new Texture(diffuseMapFileName);
        if (!diffuseMap.getTextureData().isPrepared()) {
            diffuseMap.getTextureData().prepare();
        }
        Pixmap pixmap = diffuseMap.getTextureData().consumePixmap();
        int w, h;
        int color;

        w = pixmap.getWidth();
        h = pixmap.getHeight();
        List<MyColor> colorsFromMap = new ArrayList<>((int)(w * h * 1.5));
        for (int i = 0; i < w; i++)
            for (int j = 0; j < h; j++) {
                color = pixmap.getPixel(i, j);
                colorsFromMap.add(new MyColor(((color & 0x0FF000000) >>> 24), ((color & 0x00FF0000) >>> 16), ((color & 0x0000FF00) >>> 8)));
            }

        Texture normalMap = new Texture(normalMapFileName);
        if (!normalMap.getTextureData().isPrepared()) {
            normalMap.getTextureData().prepare();
        }

        pixmap = normalMap.getTextureData().consumePixmap();

        List<MyVector3> normalsFromMap = new ArrayList<>((int)(w * h * 1.5));
        for (int i = 0; i < w; i++)
            for (int j = 0; j < h; j++) {
                color = pixmap.getPixel(i, j);
                normalsFromMap.add(new MyVector3(((color & 0xFF000000) >>> 24) / 255f * 2 - 1, ((color & 0x00FF0000) >>> 16) / 255f * 2 - 1, ((color & 0x0000FF00) >>> 8) / 255f * 2 - 1));
            }
        Texture specularMap = new Texture(specularMapFileName);

        if (!specularMap.getTextureData().isPrepared()) {
            specularMap.getTextureData().prepare();
        }

        pixmap = specularMap.getTextureData().consumePixmap();

        List<Double> reflectFromMap = new ArrayList<>((int)(w * h * 1.5));
        for (int i = 0; i < w; i++)
            for (int j = 0; j < h; j++) {
                color = pixmap.getPixel(i, j);
                reflectFromMap.add(((color & 0x0000FF00) >>> 8) / 255.0);
            }
        return new ModelMetaInf(vertexes, polygons, normals, tex, normalsFromMap, colorsFromMap, reflectFromMap, h, w);
    }

}
