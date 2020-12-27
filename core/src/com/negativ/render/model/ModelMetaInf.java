package com.negativ.render.model;

import com.badlogic.gdx.graphics.Pixmap;
import com.negativ.render.math.MyColor;
import com.negativ.render.math.MyVector3;
import com.negativ.render.math.MyVector4;
import com.negativ.render.math.Polygon;

import java.util.List;

public class ModelMetaInf {

    private List<MyVector4> vertexes;
    private List<Polygon> polygons;
    private List<MyVector3> normals;
    private List<MyVector3> normalsFromMap;
    private List<MyColor> colorsFromMap;
    private List<Double> reflectFromMap;
    private List<MyVector3> tex;
    private int mapHeight;
    private int mapWidth;

    public ModelMetaInf(List<MyVector4> vertexes, List<Polygon> polygons, List<MyVector3> normals, List<MyVector3> tex,
                        List<MyVector3> normalsFromMap, List<MyColor> colorsFromMap, List<Double> reflectFromMap, int mapHeight, int mapWidth) {
        this.vertexes = vertexes;
        this.polygons = polygons;
        this.normals = normals;
        this.colorsFromMap = colorsFromMap;
        this.tex = tex;
        this.normalsFromMap = normalsFromMap;
        this.mapHeight = mapHeight;
        this.mapWidth = mapWidth;
        this. reflectFromMap = reflectFromMap;
    }

    public List<MyVector4> getVertexes() {
        return vertexes;
    }

    public List<Polygon> getPolygons() {
        return polygons;
    }

    public List<MyVector3> getNormals() {
        return normals;
    }

    public List<MyVector3> getTex() {
        return tex;
    }

    public List<MyVector3> getNormalsFromMap() {
        return normalsFromMap;
    }

    public List<MyColor> getColorsFromMap() {
        return colorsFromMap;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public List<Double> getReflectFromMap() {
        return reflectFromMap;
    }
}
