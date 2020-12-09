package com.negativ.render;

import com.badlogic.gdx.ApplicationAdapter;
import com.negativ.render.math.MyVector3;
import com.negativ.render.model.CameraImpl1;
import com.negativ.render.model.CameraImpl2;
import com.negativ.render.model.Model;
import com.negativ.render.utils.Loader;

import java.io.FileNotFoundException;

public class Main extends ApplicationAdapter {

	private final String HEAD_OBJ_FILE_NAME = "Head.obj";
	private final String CUBE_OBJ_FILE_NAME = "Cube.obj";
	private final String DIABLO_OBJ_FILE_NAME = "Diablo.obj";
	private final String TREE_OBJ_FILE_NAME = "Tree.obj";

	private final int WIDTH = 1920;
	private final int HEIGHT = 1080;

	private Screen screen;
	private World world;
	private Loader loader;

	@Override
	public void create () {
		loader = Loader.getInstance();
		screen = new Screen(WIDTH, HEIGHT);
		world = new World(WIDTH, HEIGHT, screen);
		world.setActiveCamera(new CameraImpl2());
		try {
			Model model = new Model(loader.loadModel(HEAD_OBJ_FILE_NAME), new MyVector3(1, 0, 0), "Head");
			world.addModel(model);
			world.setActiveModel(model);
			model = new Model(loader.loadModel(CUBE_OBJ_FILE_NAME), new MyVector3(1, 0, 0), "Cube");
			world.addModel(model);
			model = new Model(loader.loadModel(DIABLO_OBJ_FILE_NAME), new MyVector3(-2, 0, 3), "Diablo");
			world.addModel(model);
		} catch (FileNotFoundException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public void render () {
		world.render();
	}
	
	@Override
	public void dispose () {
		screen.dispose();
	}

}
