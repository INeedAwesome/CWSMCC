package com.github.ineedawesome.input;

public class Input { // wrapper for callbacks

	public static final int MOUSE_LEFT = 0;
	public static final int MOUSE_RIGHT = 1;
	public static final int MOUSE_MIDDLE = 2;
	public static final int MOUSE_SIDE_BACK = 3;
	public static final int MOUSE_SIDE_FORWARD = 4;

	public static boolean isKeyDown(int keycode) {
		return KeyboardInputCallback.isKeyDown(keycode);
	}

	public static boolean isKeyPressed(int keycode) {
		return KeyboardInputCallback.isKeyPressed(keycode);
	}

	public static boolean isMouseButtonDown(int keycode) {
		return MouseButtonCallback.isButtonDown(keycode);
	}

	public static int getMouseDX() {
		return MousePositionCallback.getMouseDX();
	}

	public static int getMouseDY() {
		return MousePositionCallback.getMouseDY();
	}

	public static int getMouseXPixel() {
		return MousePositionCallback.getRawX();
	}

	public static int getMouseYPixel() {
		return MousePositionCallback.getRawY();
	}

}
