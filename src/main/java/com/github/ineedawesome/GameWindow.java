package com.github.ineedawesome;

import com.github.ineedawesome.input.KeyboardInputCallback;
import com.github.ineedawesome.input.MouseButtonCallback;
import com.github.ineedawesome.input.MousePositionCallback;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

public class GameWindow {

	private static long pointer;

	private static int width;
	private static int height;
	private static String title;

	public static void createWindow(int windowWidth, int windowHeight, String windowTitle) {
		width = windowWidth;
		height = windowHeight;
		title = windowTitle;

		// Configure GLFW
		GLFW.glfwDefaultWindowHints(); // optional, the current window hints are already the default
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE); // the window will stay hidden after creation
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE); // the window will be resizable

		// Create the window
		pointer = GLFW.glfwCreateWindow(getWidth(), getHeight(), getTitle(), 0, 0);
		if (pointer == 0)
			throw new RuntimeException("Failed to create the GLFW window");

		// Set up a key callback. It will be called every time a key is pressed, repeated or released.
		GLFW.glfwSetKeyCallback(pointer, new KeyboardInputCallback());
		GLFW.glfwSetCursorPosCallback(pointer, new MousePositionCallback());
		GLFW.glfwSetMouseButtonCallback(pointer, new MouseButtonCallback());

		GLFW.glfwSetWindowSizeCallback(pointer, (window, width, height) -> {
			GL11.glViewport(0, 0, width, height);
			setWidth(width);
			setHeight(height);
		});

		// Get the thread stack and push a new frame
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			// Get the window size passed to glfwCreateWindow
			GLFW.glfwGetWindowSize(pointer, pWidth, pHeight);

			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());

			// Center the window
			GLFW.glfwSetWindowPos(
					pointer,
					(vidmode.width() - pWidth.get(0)) / 2,
					(vidmode.height() - pHeight.get(0)) / 2
			);
		} // the stack frame is popped automatically

		// Make the OpenGL context current
		GLFW.glfwMakeContextCurrent(pointer);
		// Enable v-sync
		GLFW.glfwSwapInterval(1);
		GL.createCapabilities();
		// Make the window visible

	}

	public static int getWidth() {
		return GameWindow.width;
	}

	public static void setWidth(int width) {
		GameWindow.width = width;
	}

	public static int getHeight() {
		return GameWindow.height;
	}

	public static void setHeight(int height) {
		GameWindow.height = height;
	}

	public static String getTitle() {
		return GameWindow.title;
	}

	public static void setTitle(String title) {
		GameWindow.title = title;
	}

	public static long getPointer() {
		return pointer;
	}
}
