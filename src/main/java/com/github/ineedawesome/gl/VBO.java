package com.github.ineedawesome.gl;

import com.github.ineedawesome.Main;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL15;

import java.util.List;

public class VBO {
	public int id;
	public VBO VBOVector3(List<Vector3f> data) {
		id = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, id);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, Main.Vector3fListToFloatArray(data), GL15.GL_STATIC_DRAW );
		return this;
	}
	public VBO VBOVector2(List<Vector2f> data) {
		id = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, id);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, Main.Vector2fListToFloatArray(data), GL15.GL_STATIC_DRAW );
		return this;
	}

	public void bind() {
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, id);
	}
	public void unbind() {
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	public void delete() {
		GL15.glDeleteBuffers(id);
	}
}
