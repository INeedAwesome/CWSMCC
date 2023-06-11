package com.github.ineedawesome;

import com.github.ineedawesome.gl.IBO;
import com.github.ineedawesome.gl.VAO;
import com.github.ineedawesome.gl.VBO;
import com.github.ineedawesome.graphics.ShaderProgram;
import com.github.ineedawesome.graphics.Texture;
import com.github.ineedawesome.input.Input;
import com.github.ineedawesome.input.MousePositionCallback;
import com.github.ineedawesome.world.Chunk;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import org.joml.*;
import org.lwjgl.Version;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

import org.lwjgl.opengl.*;

import java.util.Objects;

public class Main implements Runnable {

	ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
	ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();
	Camera camera;
	ShaderProgram shaderProgram;
	Chunk chunk;

	public static void main(String[] args) {
		new Main().run();
	}

	@Override
	public void run() {
		System.out.println("Hello LWJGL " + Version.getVersion() + "!");
		init();
		loop();
		delete();
	}

	private void delete() {
		chunk.delete();

		Callbacks.glfwFreeCallbacks(GameWindow.getPointer());
		GLFW.glfwDestroyWindow(GameWindow.getPointer());

		GLFW.glfwTerminate();
		Objects.requireNonNull(GLFW.glfwSetErrorCallback(null)).free();
	}

	private void init() {
		GLFWErrorCallback.createPrint(System.err).set();
		if (!GLFW.glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");

		GameWindow.createWindow(1280, 720, "Game Title!");

		ImGui.createContext();
		ImGuiIO io = ImGui.getIO();
		io.addConfigFlags(ImGuiConfigFlags.ViewportsEnable);
		io.addConfigFlags(ImGuiConfigFlags.DockingEnable);
		imGuiGlfw.init(GameWindow.getPointer(), true);
		imGuiGl3.init("#version 330 core");

		chunk = new Chunk(new Vector3f(0, 0, 0));
		shaderProgram = new ShaderProgram("resources/shaders/vertex.glsl", "resources/shaders/fragment.glsl");

		this.camera = new Camera(new Vector3f(0, 1, 0));

		GLFW.glfwSetInputMode(GameWindow.getPointer(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GameWindow.showWindow();
	}

	private void loop() {

		while (!GLFW.glfwWindowShouldClose(GameWindow.getPointer())) {
			GL11.glClearColor(1.0f, 0.0f, 1.0f, 1f);
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT); // clear the framebuffer
			update(0.0061f);
			render();

			imGuiGlfw.newFrame();
			ImGui.newFrame();
			imGuiRender();
			ImGui.render();
			imGuiGl3.renderDrawData(ImGui.getDrawData());
			if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
				final long backupWindowPtr = GLFW.glfwGetCurrentContext();
				ImGui.updatePlatformWindows();
				ImGui.renderPlatformWindowsDefault();
				GLFW.glfwMakeContextCurrent(backupWindowPtr);
			}
			GLFW.glfwSwapBuffers(GameWindow.getPointer());
			GLFW.glfwPollEvents();
		}
	}

	public static boolean renderWireframe = false;

	public void imGuiRender() {
		ImGui.begin("Properties");

		ImGui.text("Camera: ");
		ImGui.text(" - Position X: " + camera.position.x);
		ImGui.text(" - Position Y: " + camera.position.y);
		ImGui.text(" - Position Z: " + camera.position.z);
		ImGui.spacing();
		ImGui.text(" - Yaw: " + camera.rotation.x);
		ImGui.text(" - Pitch: " + camera.rotation.y);

		ImGui.spacing();
		ImGui.spacing();
		if (ImGui.checkbox("Render Wireframe", renderWireframe)) {
			renderWireframe = !renderWireframe;
			useWireframe();
		}

		ImGui.end();
	}

	public void update(float deltaTime) {
		camera.Update(deltaTime);
		if (Input.isKeyPressed(GLFW.GLFW_KEY_LEFT_CONTROL))
			GLFW.glfwSetInputMode(GameWindow.getPointer(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
		if (Input.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT)) {
			GLFW.glfwSetInputMode(GameWindow.getPointer(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
			MousePositionCallback.firstMove = true;
		}
	}
	public void render() {
		//prepare draw here

		int modelLocation = GL20.glGetUniformLocation(shaderProgram.shaderId, "model");
		int viewLocation = GL20.glGetUniformLocation(shaderProgram.shaderId, "view");
		int projectionLocation = GL20.glGetUniformLocation(shaderProgram.shaderId, "projection");

		shaderProgram.loadMatrix(modelLocation, new Matrix4f());
		shaderProgram.loadMatrix(viewLocation, camera.getViewMatrix());
		shaderProgram.loadMatrix(projectionLocation, camera.getProjectionMatrix());

		// draw
		chunk.render(shaderProgram);

		// end draw

	}

	public void useWireframe() {
		if (renderWireframe) {
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		} else {
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		}
	}


}
