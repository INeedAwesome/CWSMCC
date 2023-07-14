package com.github.ineedawesome.gl;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class VAO {

	public int id;

	public VAO() {
		id = GL30.glGenVertexArrays();
		bind();
	}

	public void linkToVAO(int location, int size, VBO vbo) {
		bind();
		vbo.bind();
		GL30.glVertexAttribPointer(location, size, GL11.GL_FLOAT, false, 0, 0);
		unbind();
		System.out.println( GL20.glGetInteger(GL20.GL_MAX_VERTEX_ATTRIBS));
	}

	public void enable(int location) {
		GL30.glEnableVertexAttribArray(location);
	}
	public void disable(int location) {
		GL30.glDisableVertexAttribArray(location);
	}

	public void bind() {
		GL30.glBindVertexArray(id);
	}
	public void unbind() {
		GL30.glBindVertexArray(0);
	}
	public void delete() {
		GL30.glDeleteVertexArrays(id);
	}


}
