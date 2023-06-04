package com.github.ineedawesome.input;

import org.lwjgl.glfw.GLFWCursorPosCallback;

public class MousePositionCallback extends GLFWCursorPosCallback {

	private static int mouseX = 0;
	private static int mouseY = 0;
	private static int mouseDX = 0;
	private static int mouseDY = 0;
	private static boolean firstMove = true;

	@Override
	public void invoke(long window, double xpos, double ypos) {
		if (firstMove) {
			mouseX = (int)xpos;
			mouseY = (int)ypos;
			firstMove = false;
		}
		mouseDX += (int) xpos - mouseX;
		mouseDY += (int) ypos - mouseY;
		mouseX  =  (int) xpos;
		mouseY  =  (int) ypos;
	}

	public static int getMouseDX() {
		return mouseDX | (mouseDX = 0);
	}

	public static int getMouseDY() {
		return mouseDY | (mouseDY = 0);
	}

	public static int getRawX() {
		return mouseX;
	}

	public static int getRawY() {
		return mouseY;
	}
}
