package com.github.ineedawesome.graphics;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryStack;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

public class ShaderProgram {

	public int shaderId;

	public ShaderProgram(String filepathVertex, String filepathFragment) {
		shaderId = GL20.glCreateProgram();
		bind();

		compileShader(loadFileContent(filepathVertex), GL20.GL_VERTEX_SHADER);
		compileShader(loadFileContent(filepathFragment), GL20.GL_FRAGMENT_SHADER);

		GL20.glLinkProgram(shaderId);
		unbind();
	}

	public void bind() {
		GL20.glUseProgram(shaderId);
	}

	public void unbind() {
		GL20.glUseProgram(0);
	}

	public void delete() {
		GL20.glDeleteShader(shaderId);
	}

	public void loadMatrix(int location, Matrix4f matrix4f) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer fb = stack.mallocFloat(16);
			matrix4f.get(fb);
			GL20.glUniformMatrix4fv(location, false, fb);
		}
	}

	public void compileShader(String shaderSource, int shaderType) {
		int shaderID = GL20.glCreateShader(shaderType);
		GL20.glShaderSource(shaderID, shaderSource);
		GL20.glCompileShader(shaderID);

		int success = GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS);
		if (success == GL11.GL_FALSE) {
			System.out.println("ERROR::SHADER::FRAGMENT::COMPILATION_FAILED\n" + GL20.glGetShaderInfoLog(shaderID, 1024));
		}
		GL20.glAttachShader(shaderId, shaderID);
		GL20.glDeleteShader(shaderID);
	}

	private String loadFileContent(String file) {
		StringBuilder shaderSource = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while ((line = reader.readLine()) != null) {
				shaderSource.append(line).append("\n");
			}
			reader.close();
			return shaderSource.toString();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
		return "";
	}


}
