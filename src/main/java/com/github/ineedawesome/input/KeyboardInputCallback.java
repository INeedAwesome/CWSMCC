package com.github.ineedawesome.input;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

public class KeyboardInputCallback extends GLFWKeyCallback {

	private static boolean[] keys = new boolean[GLFW.GLFW_KEY_LAST];

	@Override
	public void invoke(long window, int key, int scancode, int action, int mods) {
		try {
			keys[key] = action != GLFW.GLFW_RELEASE;
		} catch (ArrayIndexOutOfBoundsException ignored){    }
	}

	public static boolean isKeyDown(int keycode) {
		return keys[keycode];
	}

	public static boolean isKeyPressed(int keycode) {
		boolean isDown = keys[keycode];
		keys[keycode] = false;
		return isDown;
		//
		// Thanks to illuminator3#0001 on discord for helping me with this,
		// in discord server Together Java in #frameworks_help_0, 2022.01.01

	}
}
