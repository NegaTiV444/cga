package com.negativ.render.math;

import java.util.List;

public class Polygon {

    private List<Integer> vertexesId;
    private List<Integer> texId;
    private List<Integer> normalsId;

    public Polygon(List<Integer> vertexesId, List<Integer> texId, List<Integer> normalsId) {
        this.vertexesId = vertexesId;
        this.texId = texId;
        this.normalsId = normalsId;
    }

    public List<Integer> getVertexesId() {
        return vertexesId;
    }

    public List<Integer> getNormalId() {
        return normalsId;
    }

    public List<Integer> getTexId() {
        return texId;
    }
}
