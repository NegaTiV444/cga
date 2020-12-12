package com.negativ.render.math;

import java.util.List;

public class Polygon {

    private List<Integer> vertexesId;
    private List<Integer> normalsId;

    public Polygon(List<Integer> vertexesId, List<Integer> normalsId) {
        this.vertexesId = vertexesId;
        this.normalsId = normalsId;
    }

    public List<Integer> getVertexesId() {
        return vertexesId;
    }

    public List<Integer> getNormalId() {
        return normalsId;
    }
}
