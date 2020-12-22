package com.negativ.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.negativ.render.math.MyMatrix4;
import com.negativ.render.math.MyVector3;
import com.negativ.render.math.MyVector3i;
import com.negativ.render.model.Camera;
import com.negativ.render.model.Model;

import java.util.ArrayList;
import java.util.List;

public class World {

    private final MyVector3 light = new MyVector3(0, 0, 10);
    private final MyVector3 lightVector = new MyVector3(0, 0, 1000);


    private static final double FOV = (Math.PI / 4);

    private final double REL_SCALE = 0.01;
    private final double REL_ROTATION_SCALE = 0.01;
    private final double ORTO_SCALE = -1;
    private final double ORTO_ROTATION_SCALE = -0.01;

    private double scale = REL_SCALE;
    private double rotationScale = REL_ROTATION_SCALE;

    private double MODEL_SPEED = 5 * scale;
    private double CAMERA_SPEED = 5 * scale;
    private double CAMERA_ROTATION_SPEED = 0.5 * scale * 2;
    private double MODEL_ROTATION_SPEED = 5 * scale;
    private double MODEL_SCALE_COEFFICIENT = 1.05;

    private static final double Z_NEAR = 0.4f;
    private static final double Z_FAR = 200f;

    private int activeModelId = 0;

    private int width;
    private int height;
    private SpriteBatch batch;
    private BitmapFont font;
    private Screen screen;

    private Model activeModel;
    private Camera activeCamera;

    private int projectionMode = 0; //0 - perspective, 2 - orto

    private List<Model> models;

    public World(int width, int height, Screen screen) {
        this.width = width;
        this.height = height;
        this.batch = new SpriteBatch();
        this.font = new BitmapFont();
        this.screen = screen;
        this.models = new ArrayList<>();
    }

    private void updateScale(double scale, double rotationScale) {
        this.scale = scale;
        this.rotationScale = rotationScale;
        MODEL_SPEED = 5 * scale;
        CAMERA_SPEED = 5 * scale;
        CAMERA_ROTATION_SPEED = 0.5 * rotationScale;
        MODEL_ROTATION_SPEED = 5 * rotationScale;
        MODEL_SCALE_COEFFICIENT = 1.05;
    }

    public void setActiveModel(Model activeModel) {
        this.activeModel = activeModel;
    }

    public void addModel(Model model) {
        models.add(model);
        if (activeModel == null) {
            activeModel = model;
        }
    }

    public void setActiveCamera(Camera activeCamera) {
        this.activeCamera = activeCamera;
    }

    //Graphics methods

    protected void render() {
        handleInput();
        models.forEach(this::renderModel);
        screen.render();
        printState();
        printMeasures();
    }

    private void renderModel(Model model) {
        if (model.isVisible()) {
            MyMatrix4 worldMatrix = MyMatrix4.getModelTranslationMatrix(model.getPosition(), model.getScale(), model.getRotation());
            MyMatrix4 viewMatrix = activeCamera.getViewMatrix();
            List<MyVector3> vertexes = calculateModelVertexesDouble(model, worldMatrix, viewMatrix);
            List<MyVector3> normals = new ArrayList<>();
            MyMatrix4 worldMatrixRotation = worldMatrix.tuncTranslation();
            MyMatrix4 viewMatrixRotation  = viewMatrix.tuncTranslation();
            MyVector3 light1 = lightVector.mul(viewMatrixRotation);
            model.getMetaInf().getNormals().forEach(n -> normals.add(n.mul(worldMatrixRotation).mul(viewMatrixRotation).nor()));
            model.getMetaInf().getPolygons().forEach(p -> {
                if (p.getVertexesId().size() == 3) {
                    screen.drawPolygon(
                            vertexes.get(p.getVertexesId().get(0) - 1),
                            vertexes.get(p.getVertexesId().get(1) - 1),
                            vertexes.get(p.getVertexesId().get(2) - 1),
                            normals.get(p.getNormalId().get(0) - 1),
                            normals.get(p.getNormalId().get(1) - 1),
                            normals.get(p.getNormalId().get(2) - 1),
                            light1
                    );
                }
            });
            vertexes.clear();
        }
    }

    private List<MyVector3i> calculateModelVertexesInt(Model model) {
        MyMatrix4 worldMatrix = MyMatrix4.getModelTranslationMatrix(model.getPosition(), model.getScale(), model.getRotation());
        MyMatrix4 viewMatrix = activeCamera.getViewMatrix();
        MyMatrix4 projectionMatrix;
        if (projectionMode == 0) {
            projectionMatrix = MyMatrix4.getRelativeProjectionMatrixUsingFov((float)width/height, FOV, Z_NEAR, Z_FAR);
        } else {
            projectionMatrix = MyMatrix4.getOrtoProjection(Z_NEAR, Z_FAR, height, width);
        }
        MyMatrix4 viewportMatrix = MyMatrix4.getViewportMatrix(width, height);
        List<MyVector3i> vertexes = new ArrayList<>((int) (model.getMetaInf().getVertexes().size() * 1.5 + 1));


        model.getMetaInf().getVertexes().forEach(v -> vertexes.add(new MyVector3i(v.mul(worldMatrix).mul(viewMatrix).mul(projectionMatrix).mul(viewportMatrix).toVector3())));

//        System.out.println("ViewPort matrix\n" + viewportMatrix + "\n");
//        System.out.println("Projection matrix\n" + projectionMatrix + "\n");
//        System.out.println("View matrix\n" + viewMatrix + "\n");
//        System.out.println("World matrix\n" + worldMatrix + "\n");
        return vertexes;
    }

    private List<MyVector3> calculateModelVertexesDouble(Model model, MyMatrix4 worldMatrix, MyMatrix4 viewMatrix) {
        MyMatrix4 projectionMatrix;
        if (projectionMode == 0) {
            projectionMatrix = MyMatrix4.getRelativeProjectionMatrixUsingFov((float)width/height, FOV, Z_NEAR, Z_FAR);
        } else {
            projectionMatrix = MyMatrix4.getOrtoProjection(Z_NEAR, Z_FAR, height, width);
        }
        MyMatrix4 viewportMatrix = MyMatrix4.getViewportMatrix(width, height);
        List<MyVector3> vertexes = new ArrayList<>((int) (model.getMetaInf().getVertexes().size() * 1.5 + 1));


        model.getMetaInf().getVertexes().forEach(v -> vertexes.add(v.mul(worldMatrix).mul(viewMatrix).mul(projectionMatrix).mul(viewportMatrix).toVector3()));

//        System.out.println("ViewPort matrix\n" + viewportMatrix + "\n");
//        System.out.println("Projection matrix\n" + projectionMatrix + "\n");
//        System.out.println("View matrix\n" + viewMatrix + "\n");
//        System.out.println("World matrix\n" + worldMatrix + "\n");
        return vertexes;
    }

    private void showModel() {
        activeModel.setVisible(true);
    }

    private void hideModel() {
        activeModel.setVisible(false);
    }

    private void nextModel() {
        if (activeModelId == models.size() - 1) {
            activeModelId = 0;
        } else {
            activeModelId++;
        }
        activeModel = models.get(activeModelId);
    }

    private void setActiveModel(int id) {
        if (models.size() > id) {
            activeModelId = id;
            activeModel = models.get(activeModelId);
        }
    }

    private void handleInput() {

        //Move model

        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            activeModel.move(new MyVector3(0, -MODEL_SPEED, 0));
        } else if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            activeModel.move(new MyVector3(0, MODEL_SPEED, 0));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            activeModel.move(new MyVector3(MODEL_SPEED, 0, 0));
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            activeModel.move(new MyVector3(-MODEL_SPEED, 0, 0));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Z)) {
            activeModel.move(new MyVector3(0, 0, MODEL_SPEED));
        } else if (Gdx.input.isKeyPressed(Input.Keys.X)) {
            activeModel.move(new MyVector3(0, 0, -MODEL_SPEED));
        }

        //Model rotation

        if (Gdx.input.isKeyPressed(Input.Keys.NUM_1)) {
            activeModel.rotate(new MyVector3(0, MODEL_ROTATION_SPEED, 0));
        } else if (Gdx.input.isKeyPressed(Input.Keys.NUM_2)) {
            activeModel.rotate(new MyVector3(0, -MODEL_ROTATION_SPEED, 0));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_3)) {
            activeModel.rotate(new MyVector3(MODEL_ROTATION_SPEED, 0, 0));
        } else if (Gdx.input.isKeyPressed(Input.Keys.NUM_4)) {
            activeModel.rotate(new MyVector3(-MODEL_ROTATION_SPEED, 0, 0));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_5)) {
            activeModel.rotate(new MyVector3(0, 0, MODEL_ROTATION_SPEED));
        } else if (Gdx.input.isKeyPressed(Input.Keys.NUM_6)) {
            activeModel.rotate(new MyVector3(0, 0, -MODEL_ROTATION_SPEED));
        }

        //Model scale

        if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            activeModel.scale(new MyVector3(MODEL_SCALE_COEFFICIENT, MODEL_SCALE_COEFFICIENT, MODEL_SCALE_COEFFICIENT));
        } else if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            activeModel.scale(new MyVector3(1 / MODEL_SCALE_COEFFICIENT, 1 / MODEL_SCALE_COEFFICIENT, 1 / MODEL_SCALE_COEFFICIENT));
        }

        //Model camera position

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            activeCamera.move(new MyVector3(CAMERA_SPEED, 0, 0));
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            activeCamera.move(new MyVector3(-CAMERA_SPEED, 0, 0));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            activeCamera.move(new MyVector3(0, CAMERA_SPEED, 0));
        } else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            activeCamera.move(new MyVector3(0, -CAMERA_SPEED, 0));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_8)) {
            activeCamera.move(new MyVector3(0, 0, CAMERA_SPEED));
        } else if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_2)) {
            activeCamera.move(new MyVector3(0, 0, -CAMERA_SPEED));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_1)) {
            activeCamera.rotate(new MyVector3(CAMERA_ROTATION_SPEED, 0, 0));
        } else if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_3)) {
            activeCamera.rotate(new MyVector3(-CAMERA_ROTATION_SPEED, 0, 0));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_4)) {
            activeCamera.rotate(new MyVector3(0, CAMERA_ROTATION_SPEED, 0));
        } else if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_6)) {
            activeCamera.rotate(new MyVector3(0, -CAMERA_ROTATION_SPEED, 0));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_7)) {
            activeCamera.rotate(new MyVector3(0, 0, CAMERA_ROTATION_SPEED));
        } else if (Gdx.input.isKeyPressed(Input.Keys.NUMPAD_9)) {
            activeCamera.rotate(new MyVector3(0, 0, -CAMERA_ROTATION_SPEED));
        }

        //Other
        if (Gdx.input.isKeyPressed(Input.Keys.TAB)) {
            nextModel();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.MINUS)) {
            hideModel();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.EQUALS)) {
            showModel();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.F1)) {
            setActiveModel(0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.F2)) {
            setActiveModel(1);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.F3)) {
            setActiveModel(2);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.F4)) {
            setActiveModel(3);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.NUM_9)) {
            projectionMode = 1;
            updateScale(ORTO_SCALE, ORTO_ROTATION_SCALE);
            activeCamera.restoreDefault();
            models.stream().forEach(m -> {
                m.restoreToDefaultWithScale(new MyVector3(1 * 200, 1 * 200, 1 * 200));
                m.setPosition(m.getPosition().scale(new MyVector3(-1, -1, -1)));
                m.setRotation(new MyVector3(0, 0, Math.PI));


            });
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_0)) {
            projectionMode = 0;
            updateScale(REL_SCALE, REL_ROTATION_SCALE);
            activeCamera.restoreDefault();
            models.stream().forEach(m -> {
                m.restoreToDefault();
                m.setScale(new MyVector3(1, 1, 1));
            });
        }

    }

    private void printState() {
        double frameRate = Gdx.graphics.getFramesPerSecond();
        batch.begin();
        font.draw(batch, "ACTIVE CAMERA:\n" + activeCamera.getStateString(), 0, 930);
        font.draw(batch, String.format("FPS: %7f", frameRate), 0, 960);
        font.draw(batch, "ACTIVE MODEL:\n" + activeModel.getStateString(), 0, 785);
        batch.end();
    }

    private void printMeasures() {
        batch.begin();
        font.setColor(Color.RED);
        font.draw(batch, "- 0", 0, 20);
        font.draw(batch, "- 100", 0, 100);
        font.draw(batch, "- 200", 0, 200);
        font.draw(batch, "- 300", 0, 300);
        font.draw(batch, "- 400", 0, 400);
        font.draw(batch, "- 500", 0, 500);
        font.draw(batch, "- 1000", 0, 1000);
        font.draw(batch, "\\\n " + height + " \\ 0", 0, height);
        font.draw(batch, "|\n100", 100, height);
        font.draw(batch, "|\n200", 200, height);
        font.draw(batch, "|\n300", 300, height);
        font.draw(batch, "|\n400", 400, height);
        font.draw(batch, "|\n500", 500, height);
        font.draw(batch, "|\n1000", 1000, height);
        font.draw(batch, "|\n1500", 1500, height);
        batch.end();
    }
}
