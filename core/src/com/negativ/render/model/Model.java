package com.negativ.render.model;


import com.negativ.render.math.MyVector3;

public class Model {

    private MyVector3 defaultPosition = new MyVector3(0, 0, 0);
    private MyVector3 defaultScale = new MyVector3(1, 1, 1);
    private MyVector3 defaultRotation = new MyVector3(0, 0, 0);

    private MyVector3 position;
    private MyVector3 scale;
    private MyVector3 rotation;

    private ModelMetaInf metaInf;

    private String name;
    private boolean isVisible = true;

    public Model(MyVector3 position, MyVector3 scale, MyVector3 rotation, ModelMetaInf metaInf, String name) {
        this.defaultPosition = position;
        this.defaultScale = scale;
        this.defaultRotation = rotation;
        this.position = defaultPosition;
        this.scale = defaultScale;
        this.rotation = defaultRotation;
        this.metaInf = metaInf;
        this.name = name;
    }

    public Model(ModelMetaInf metaInf, MyVector3 position, String name) {
        this.defaultPosition = position;
        this.position = defaultPosition;
        this.scale = defaultScale;
        this.rotation = defaultRotation;
        this.metaInf = metaInf;
        this.name = name;
    }

    public Model(ModelMetaInf metaInf, String name) {
        this.position = defaultPosition;
        this.scale = defaultScale;
        this.rotation = defaultRotation;
        this.metaInf = metaInf;
        this.name = name;
    }

    public void restoreToDefault() {
        this.position = defaultPosition;
        this.scale = defaultScale;
        this.rotation = defaultRotation;
    }

    public void restoreToDefaultWithScale(MyVector3 scale) {
        this.position = defaultPosition.scale(scale);
        this.scale = scale;
        this.rotation = defaultRotation;
    }

    public String getName() {
        return name;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public MyVector3 getPosition() {
        return position;
    }

    public void setPosition(MyVector3 position) {
        this.position = position;
    }

    public MyVector3 getScale() {
        return scale;
    }

    public void setScale(MyVector3 scale) {
        this.scale = scale;
    }

    public MyVector3 getRotation() {
        return rotation;
    }

    public void setRotation(MyVector3 rotation) {
        this.rotation = rotation;
    }

    public ModelMetaInf getMetaInf() {
        return metaInf;
    }

    public void setMetaInf(ModelMetaInf metaInf) {
        this.metaInf = metaInf;
    }

    public String getStateString() {
        return  "Name: " + name + "\n" +
                "Visible: " + isVisible + "\n" +
                "Position: " + position + "\n" +
                "Scale: " + scale + "\n" +
                "Rotation:" + rotation;
    }

    public void move(MyVector3 movement) {
        this.position = this.position.add(movement);
    }

    public void scale(MyVector3 scale) {
        this.scale = this.scale.scale(scale);
    }

    public void rotate(MyVector3 rotation) {
        this.rotation = this.rotation.add(rotation);
    }
}
