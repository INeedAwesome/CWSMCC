package com.github.ineedawesome;

import com.github.ineedawesome.input.Input;
import com.github.ineedawesome.input.MousePositionCallback;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class Camera {

	public Vector3f position;
	public Vector3f rotation;
	public float fovInDegrees = 90;
	public float sensitivity = 0.1f; // min 0, max 1

	private final float speed = 0.01f;

	public Camera(Vector3f position) {
		this.position = position;
		this.rotation = new Vector3f(0, 0, -90);
	}

	public void Update() {

		InputController();
	}

	public void InputController() {
		this.rotation.x += MousePositionCallback.getMouseDX() * sensitivity;
		this.rotation.y += MousePositionCallback.getMouseDY() * sensitivity;
		this.rotation.y = Math.clamp(-89, 89, this.rotation.y);
		if (this.rotation.x >= 360)
			this.rotation.x = 0;
		if (this.rotation.x < 0)
			this.rotation.x = 360;

		if (Input.isKeyDown(GLFW.GLFW_KEY_W)) {
			this.position.x += Math.sin(Math.toRadians(this.rotation.x)) * speed;
			this.position.z -= Math.cos(Math.toRadians(this.rotation.x)) * speed;
		}
		if (Input.isKeyDown(GLFW.GLFW_KEY_S)) {
			this.position.x -= Math.sin(Math.toRadians(this.rotation.x)) * speed;
			this.position.z += Math.cos(Math.toRadians(this.rotation.x)) * speed;
		}
		if (Input.isKeyDown(GLFW.GLFW_KEY_A)) {
			this.position.x -= Math.cos(Math.toRadians(this.rotation.x)) * speed;
			this.position.z -= Math.sin(Math.toRadians(this.rotation.x)) * speed;
		}
		if (Input.isKeyDown(GLFW.GLFW_KEY_E)) {
			this.position.y += speed;
		}
		if (Input.isKeyDown(GLFW.GLFW_KEY_Q)) {
			this.position.y -= speed;
		}
		if (Input.isKeyDown(GLFW.GLFW_KEY_D)) {
			this.position.x += Math.cos(Math.toRadians(this.rotation.x)) * speed;
			this.position.z += Math.sin(Math.toRadians(this.rotation.x)) * speed;
		}
		if (Input.isKeyDown(GLFW.GLFW_KEY_ESCAPE)) {
			GLFW.glfwSetWindowShouldClose(GameWindow.getPointer(), true);
		}
		//this.position.normalize(); // goes faster with W & D instead of going same speed as only W
	}

	public Matrix4f getViewMatrix() {
		Matrix4f viewMatrix = new Matrix4f();
		viewMatrix.rotate(Math.toRadians(this.rotation.y /*pitch/up down*/), new Vector3f(1, 0, 0), viewMatrix);
		viewMatrix.rotate(Math.toRadians(this.rotation.x/*yaw/side to side*/), new Vector3f(0, 1, 0), viewMatrix);
		Vector3f negativeCameraPos = new Vector3f(-this.position.x, -this.position.y, -this.position.z);
		viewMatrix.translate(negativeCameraPos, viewMatrix);
		return viewMatrix;
	}

	public Matrix4f getProjectionMatrix() {
		float aspectRatio = (float) GameWindow.getWidth() / (float) GameWindow.getHeight();
		return new Matrix4f().perspective(Math.toRadians(fovInDegrees), aspectRatio, 0.01f, 100.0f);
	}
}
