package com.negativ.render.model;

import com.negativ.render.math.MyVector3;
import com.negativ.render.math.MyVector4;
import com.negativ.render.math.Polygon;

import java.util.List;

public class ModelMetaInf {

    private List<MyVector4> vertexes;
    private List<Polygon> polygons;
    private List<MyVector3> normals;

    public ModelMetaInf(List<MyVector4> vertexes, List<Polygon> polygons, List<MyVector3> normals) {
        this.vertexes = vertexes;
        this.polygons = polygons;
        this.normals = normals;
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
}
