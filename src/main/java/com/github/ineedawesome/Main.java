package com.github.ineedawesome;

import com.github.ineedawesome.gl.IBO;
import com.github.ineedawesome.gl.VAO;
import com.github.ineedawesome.gl.VBO;
import com.github.ineedawesome.graphics.ShaderProgram;
import com.github.ineedawesome.graphics.Texture;
import org.joml.*;
import org.lwjgl.Version;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Main implements Runnable {

	GameWindow gameWindow;
	Camera camera;

	VAO vao;
	IBO ibo;
	ShaderProgram shaderProgram;
	Texture texture;

	List<Vector3f> vertices = Arrays.asList(
			// front face
			new Vector3f(-0.5f, 0.5f, 0.5f), // topleft vert
			new Vector3f(0.5f, 0.5f, 0.5f), // topright vert
			new Vector3f(0.5f, -0.5f, 0.5f), // bottomright vert
			new Vector3f(-0.5f, -0.5f, 0.5f), // bottomleft vert
			// right face
			new Vector3f(0.5f, 0.5f, 0.5f), // topleft vert
			new Vector3f(0.5f, 0.5f, -0.5f), // topright vert
			new Vector3f(0.5f, -0.5f, -0.5f), // bottomright vert
			new Vector3f(0.5f, -0.5f, 0.5f), // bottomleft vert
			// back face
			new Vector3f(0.5f, 0.5f, -0.5f), // topleft vert
			new Vector3f(-0.5f, 0.5f, -0.5f), // topright vert
			new Vector3f(-0.5f, -0.5f, -0.5f), // bottomright vert
			new Vector3f(0.5f, -0.5f, -0.5f), // bottomleft vert
			// left face
			new Vector3f(-0.5f, 0.5f, -0.5f), // topleft vert
			new Vector3f(-0.5f, 0.5f, 0.5f), // topright vert
			new Vector3f(-0.5f, -0.5f, 0.5f), // bottomright vert
			new Vector3f(-0.5f, -0.5f, -0.5f), // bottomleft vert
			// top face
			new Vector3f(-0.5f, 0.5f, -0.5f), // topleft vert
			new Vector3f(0.5f, 0.5f, -0.5f), // topright vert
			new Vector3f(0.5f, 0.5f, 0.5f), // bottomright vert
			new Vector3f(-0.5f, 0.5f, 0.5f), // bottomleft vert
			// bottom face
			new Vector3f(-0.5f, -0.5f, 0.5f), // topleft vert
			new Vector3f(0.5f, -0.5f, 0.5f), // topright vert
			new Vector3f(0.5f, -0.5f, -0.5f), // bottomright vert
			new Vector3f(-0.5f, -0.5f, -0.5f) // bottomleft vert
	);

	List<Vector3i> indices = Arrays.asList(
			new Vector3i(0, 1, 2), // first face
			new Vector3i(2, 3, 0),

			new Vector3i(4, 5, 6), // second, etc...
			new Vector3i(6, 7, 4),

			new Vector3i(8, 9, 10),
			new Vector3i(10, 11, 8),

			new Vector3i(12, 13, 14),
			new Vector3i(14, 15, 12),

			new Vector3i(16, 17, 18),
			new Vector3i(18, 19, 16),

			new Vector3i(20, 21, 22),
			new Vector3i(22, 23, 20)
	);
	List<Vector2f> textureCoords = Arrays.asList(
			new Vector2f(0f, 1f),
			new Vector2f(1f, 1f),
			new Vector2f(1f, 0f),
			new Vector2f(0f, 0f),

			new Vector2f(0f, 1f),
			new Vector2f(1f, 1f),
			new Vector2f(1f, 0f),
			new Vector2f(0f, 0f),

			new Vector2f(0f, 1f),
			new Vector2f(1f, 1f),
			new Vector2f(1f, 0f),
			new Vector2f(0f, 0f),

			new Vector2f(0f, 1f),
			new Vector2f(1f, 1f),
			new Vector2f(1f, 0f),
			new Vector2f(0f, 0f),

			new Vector2f(0f, 1f),
			new Vector2f(1f, 1f),
			new Vector2f(1f, 0f),
			new Vector2f(0f, 0f),

			new Vector2f(0f, 1f),
			new Vector2f(1f, 1f),
			new Vector2f(1f, 0f),
			new Vector2f(0f, 0f)
	);

	public static void main(String[] args) {
		new Main().run();
	}

	@Override
	public void run() {
		System.out.println("Hello LWJGL " + Version.getVersion() + "!");

		init();
		loop();

		Callbacks.glfwFreeCallbacks(GameWindow.getPointer());
		GLFW.glfwDestroyWindow(GameWindow.getPointer());

		ibo.delete();
		shaderProgram.delete();
		texture.delete();
		vao.delete();

		GLFW.glfwTerminate();
		Objects.requireNonNull(GLFW.glfwSetErrorCallback(null)).free();
	}

	private void init() {
		GLFWErrorCallback.createPrint(System.err).set();
		if (!GLFW.glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");

		GameWindow.createWindow(1280, 720, "Game Title!");

		this.camera = new Camera(new Vector3f(1, -1, 3));

		vao = new VAO();
		VBO verticesVbo = new VBO().VBOVector3(vertices);
		VBO uvsVbo = new VBO().VBOVector2(textureCoords);
		vao.linkToVAO(0, 3, verticesVbo);
		vao.linkToVAO(1, 2, uvsVbo);
		ibo = new IBO(indices);

		shaderProgram = new ShaderProgram("resources/shaders/vertex.glsl", "resources/shaders/fragment.glsl");

		texture = new Texture("resources/textures/dirt.png");

		GLFW.glfwSetInputMode(GameWindow.getPointer(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

	private void loop() {
		while (!GLFW.glfwWindowShouldClose(GameWindow.getPointer())) {
			GL11.glClearColor(0.8f, 0.0f, 0.1f, 1f);
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT); // clear the framebuffer

			camera.Update();

			//prepare draw here
			vao.bind();
			ibo.bind();
			texture.bind();
			shaderProgram.bind();
			vao.enable(0);
			vao.enable(1);

			// create the mvp every frame
			Matrix4f model = new Matrix4f();

			int modelLocation = GL20.glGetUniformLocation(shaderProgram.shaderId, "model");
			int viewLocation = GL20.glGetUniformLocation(shaderProgram.shaderId, "view");
			int projectionLocation = GL20.glGetUniformLocation(shaderProgram.shaderId, "projection");

			loadMatrix(modelLocation, model);
			loadMatrix(viewLocation, camera.getViewMatrix());
			loadMatrix(projectionLocation, camera.getProjectionMatrix());

			// draw
			GL11.glDrawElements(GL11.GL_TRIANGLES, indices.size()*3, GL11.GL_UNSIGNED_INT, 0);

			// end draw
			vao.disable(1);
			vao.disable(0);
			shaderProgram.unbind();
			texture.unbind();
			ibo.unbind();
			vao.unbind();

			// Refresh screen
			GLFW.glfwSwapBuffers(GameWindow.getPointer());
			GLFW.glfwPollEvents();
		}
	}


	public void loadMatrix(int location, Matrix4f matrix4f) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer fb = stack.mallocFloat(16);
			matrix4f.get(fb);
			GL20.glUniformMatrix4fv(location, false, fb);
		}
	}

	public static float[] Vector3fListToFloatArray(List<Vector3f> data)
	{
		float[] floatArray = new float[data.size()*3];
		for (int i = 0; i < data.size(); i++) {
			floatArray[i*3] = data.get(i).x;
			floatArray[i*3+1] = data.get(i).y;
			floatArray[i*3+2] = data.get(i).z;
		}
		return floatArray;
	}
	public static float[] Vector2fListToFloatArray(List<Vector2f> data)
	{
		float[] floatArray = new float[data.size()*2];
		for (int i = 0; i < data.size(); i++) {
			floatArray[i*2] = data.get(i).x;
			floatArray[i*2+1] = data.get(i).y;
		}
		return floatArray;
	}
	public static int[] Vector3iListToIntArray(List<Vector3i> data)
	{
		int[] intArray = new int[data.size()*3];
		for (int i = 0; i < data.size(); i++) {
			intArray[i*3] = data.get(i).x;
			intArray[i*3+1] = data.get(i).y;
			intArray[i*3+2] = data.get(i).z;
		}
		return intArray;
	}
}
