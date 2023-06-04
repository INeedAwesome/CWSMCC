package com.github.ineedawesome.graphics;

import org.lwjgl.opengl.*;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class Texture {

	public int id;
	public String filepath;
	public int width = 0;
	public int height = 0;
	public Texture(String filepath) {
		this.filepath = filepath;

		this.id = GL11.glGenTextures(); // generate textures
		GL15.glActiveTexture(GL13.GL_TEXTURE0); // activate texture
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.id); // bind texture 2d
		GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);

		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);

		ByteBuffer buffer = null;
		try (MemoryStack stack = MemoryStack.stackPush()){
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);
			IntBuffer c = stack.mallocInt(1);

			STBImage.stbi_set_flip_vertically_on_load(true);
			buffer = STBImage.stbi_load(this.filepath, w, h, c, 4);
			if (buffer == null)
				throw new Exception("Error retrieving image file: " + "resources/textures/dirt.png" + "\nReason: " + STBImage.stbi_failure_reason());

			this.width = w.get();
			this.height = h.get();

		}
		catch (Exception exception) {
			exception.printStackTrace();
		}

		assert buffer != null;
		GL20.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, this.width, this.height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
		GL20.glBindTexture(GL11.GL_TEXTURE_2D, 0);

		unbind();
	}

	public void bind() {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
	}

	public void unbind() {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}
	public void delete() {
		GL11.glDeleteTextures(id);
	}
}
