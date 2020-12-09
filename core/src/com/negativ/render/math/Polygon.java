package com.negativ.render.math;

import java.util.List;

public class Polygon {

    private List<Integer> vertexesId;

    public Polygon(List<Integer> vertexesId) {
        this.vertexesId = vertexesId;
    }

    public List<Integer> getVertexesId() {
        return vertexesId;
    }
}
