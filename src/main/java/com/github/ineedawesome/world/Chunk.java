package com.github.ineedawesome.world;

import com.github.ineedawesome.gl.IBO;
import com.github.ineedawesome.gl.VAO;
import com.github.ineedawesome.gl.VBO;
import com.github.ineedawesome.graphics.ShaderProgram;
import com.github.ineedawesome.graphics.Texture;
import jdk.jfr.Unsigned;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class Chunk {

	public Vector3f position;

	private List<Vector3f> chunkVertices;
	private List<Vector2f> chunkUvs;
	private List<Vector3i> chunkIndices;

//	public static int SIZE = 16;
//	public static int HEIGHT = 32;

	@Unsigned
	private int indexCount;

	VAO vao;
	VBO verticesVbo;
	VBO uvVbo;
	IBO ibo;
	Texture texture;

	public Chunk(Vector3f position) {
		this.position = position;
		this.chunkVertices = new ArrayList<>();
		this.chunkUvs = new ArrayList<>();
		this.chunkIndices = new ArrayList<>();

		genBlocks();
		buildChunk();
	}

//	public void genChunk() {
//
//	}

	public void genBlocks() {
		for (int i = 0; i < 3; i++) {
			Block block = new Block(new Vector3f(i, i, i));
			FaceData frontFaceData = block.getFace(BlockData.Faces.FRONT);
			chunkVertices.addAll(frontFaceData.vertices);
			chunkUvs.addAll(frontFaceData.uvs);

			FaceData backFaceData = block.getFace(BlockData.Faces.BACK);
			chunkVertices.addAll(backFaceData.vertices);
			chunkUvs.addAll(backFaceData.uvs);

			FaceData leftFaceData = block.getFace(BlockData.Faces.LEFT);
			chunkVertices.addAll(leftFaceData.vertices);
			chunkUvs.addAll(leftFaceData.uvs);

			FaceData rightFaceData = block.getFace(BlockData.Faces.RIGHT);
			chunkVertices.addAll(rightFaceData.vertices);
			chunkUvs.addAll(rightFaceData.uvs);

			FaceData topFaceData = block.getFace(BlockData.Faces.TOP);
			chunkVertices.addAll(topFaceData.vertices);
			chunkUvs.addAll(topFaceData.uvs);

			FaceData bottomFaceData = block.getFace(BlockData.Faces.BOTTOM);
			chunkVertices.addAll(bottomFaceData.vertices);
			chunkUvs.addAll(bottomFaceData.uvs);

			addIndices(6);
		}
	}

	public void addIndices(int amountOfFaces) {
		for (int i = 0; i<amountOfFaces; i++) {
			chunkIndices.add(new Vector3i(indexCount, 1+indexCount, 2+indexCount)); // pattern for indices is:	0, 1, 2
			chunkIndices.add(new Vector3i(2+indexCount, 3+indexCount, indexCount)); //							2, 3, 0

			indexCount += 4;
		}
	}

	public void buildChunk() {
		vao = new VAO();
		vao.bind();

		verticesVbo = new VBO().VBOVector3f(chunkVertices);
		verticesVbo.bind();
		vao.linkToVAO(0, 3, verticesVbo);

		uvVbo = new VBO().VBOVector2f(chunkUvs);
		uvVbo.bind();
		vao.linkToVAO(1, 2, uvVbo);

		ibo = new IBO(chunkIndices);

		texture = new Texture("resources/textures/dirt.png");
	}

	public void render(ShaderProgram shader) {
		shader.bind();
		vao.bind();
		verticesVbo.bind();
		uvVbo.bind();
		ibo.bind();
		texture.bind();
		vao.enable(0);
		vao.enable(1);

		GL11.glDrawElements(GL11.GL_TRIANGLES, chunkIndices.size()*3, GL11.GL_UNSIGNED_INT, 0);

//		shader.unbind();

		vao.disable(1);
		vao.disable(0);
		texture.unbind();
		ibo.unbind();
		uvVbo.unbind();
		verticesVbo.unbind();
		vao.unbind();

	}

	public void delete() {
		ibo.delete();
		uvVbo.delete();
		verticesVbo.delete();
		vao.delete();
		texture.delete();
	}
}
