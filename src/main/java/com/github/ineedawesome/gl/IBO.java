package com.github.ineedawesome.gl;

import com.github.ineedawesome.ArrayHelper;
import com.github.ineedawesome.Main;
import org.joml.Vector3i;
import org.lwjgl.opengl.GL15;

import java.util.List;

public class IBO {
	public int id;

	public IBO(List<Vector3i> data) {
		id = GL15.glGenBuffers();
		bind();
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, ArrayHelper.Vector3iListToIntArray(data), GL15.GL_STATIC_DRAW);
	}

	public void bind() {
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, id);
	}

	public void unbind() {
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
	}

	public void delete() {
		GL15.glDeleteBuffers(id);
	}
}
