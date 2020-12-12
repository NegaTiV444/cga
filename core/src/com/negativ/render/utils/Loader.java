package com.negativ.render.utils;

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

    public ModelMetaInf loadModel(String fileName) throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        List<MyVector4> vertexes = new ArrayList<>();
        List<Polygon> polygons = new ArrayList<>();
        List<MyVector3> normals = new ArrayList<>();
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
                            for (int i = 1; i < components.length; i++) {
                                String[] idArray = components[i].split(POLYGON_VERTEXES_SPLITTER);
                                polygonVertexesId.add(Integer.parseInt(idArray[0]));
                                polygonNormalsId.add(Integer.parseInt(idArray[2]));
                            }
                            polygons.add(new Polygon(polygonVertexesId, polygonNormalsId));
                        } else {
                            System.out.println("Line '" + line + "' was skipped");
                        }
                    } else if (components[0].equals(VERTEX_NORMAL_MARKER)) {
                        if (components.length == VERTEX_NORMALS_IN_LINE_COUNT + 1) {
                            normals.add(new MyVector3(Double.parseDouble(components[1]), Double.parseDouble(components[2]), Double.parseDouble(components[3])));
                        } else {
                            System.out.println("LIne '" + line + "' was skipped");
                        }
                    }
                });
        return new ModelMetaInf(vertexes, polygons, normals);
    }

}
