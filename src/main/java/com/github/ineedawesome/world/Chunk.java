package com.github.ineedawesome.world;

import com.github.ineedawesome.gl.IBO;
import com.github.ineedawesome.gl.VAO;
import com.github.ineedawesome.gl.VBO;
import com.github.ineedawesome.graphics.ShaderProgram;
import com.github.ineedawesome.graphics.Texture;
import com.github.ineedawesome.noise.Noise;
import jdk.jfr.Unsigned;
import org.joml.*;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class Chunk {

	public Vector3f position;

	private List<Vector3f> chunkVertices;
	private List<Vector2f> chunkUvs;
	private List<Vector3i> chunkIndices;

	Block[][][] chunkBlocks;

	public static int SIZE = 16;
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

		chunkBlocks = new Block[SIZE][SIZE][SIZE];

		float[][] heightMap = genChunk();
		genBlocks(heightMap);
		genFaces(heightMap);
		buildChunk();
	}

	public float[][] genChunk() {
		float[][] heightMap = new float[SIZE][SIZE];
		Noise.setSeed(123456);
		for (int x = 0; x < SIZE; x++) {
			for (int z = 0; z < SIZE; z++) {
				heightMap[x][z] = Noise.CalcPixel2D(x, z, 0.01f);
			}
		}
		return heightMap;
	}

	public void genBlocks(float[][] heightMap) {
		for (int x = 0; x < SIZE; x++) {
			for (int z = 0; z < SIZE; z++) {
				int columnHeight = (int)(heightMap[x][z] /10);
				for (int y = 0; y < SIZE; y++) {
					if (y < columnHeight) {
						chunkBlocks[x][y][z] = new Block(new Vector3f(x, y, z), BlockData.BlockType.DIRT);
					}
					else {
						chunkBlocks[x][y][z] = new Block(new Vector3f(x, y, z), BlockData.BlockType.EMPTY);
					}
				}
			}
		}
	}

	public void genFaces(float[][] heightMap) {
		for (int x = 0; x < SIZE; x++) {
			for (int z = 0; z < SIZE; z++) {
				int columnHeight = (int)(heightMap[x][z] /10);
				for (int y = 0; y < columnHeight; y++) {
					int numberOfFaces = 0;

					// qualifications: block to left is empty, is the farthest left in chunk
					if (x > 0) {
						if (chunkBlocks[x-1][y][z].blockType == BlockData.BlockType.EMPTY) {
							integrateFace(chunkBlocks[x][y][z], BlockData.Faces.LEFT);
						}
					}
					else {
						integrateFace(chunkBlocks[x][y][z], BlockData.Faces.LEFT);
					}
					numberOfFaces++;

					// qualifications: block to right is empty, is the farthest right in chunk
					if (x < SIZE-1) {
						if (chunkBlocks[x+1][y][z].blockType == BlockData.BlockType.EMPTY) {
							integrateFace(chunkBlocks[x][y][z], BlockData.Faces.RIGHT);
						}
					}
					else {
						integrateFace(chunkBlocks[x][y][z], BlockData.Faces.RIGHT);
					}
					numberOfFaces++;

					// qualifications: block above is empty, is the highest up in chunk
					if (y < columnHeight-1) {
						if (chunkBlocks[x][y][z].blockType == BlockData.BlockType.EMPTY) {
							integrateFace(chunkBlocks[x][y][z], BlockData.Faces.TOP);
						}
					}
					else {
						integrateFace(chunkBlocks[x][y][z], BlockData.Faces.TOP);
					}
					numberOfFaces++;

					// qualifications: block below is empty, is the lowest in chunk
					if (y > 0) {
						if (chunkBlocks[x][y-1][z].blockType == BlockData.BlockType.EMPTY) {
							integrateFace(chunkBlocks[x][y][z], BlockData.Faces.BOTTOM);
						}
					}
					else {
						integrateFace(chunkBlocks[x][y][z], BlockData.Faces.BOTTOM);
					}
					numberOfFaces++;

					// qualifications: block front is empty, is the front in chunk
					if (z < SIZE-1) {
						if (chunkBlocks[x][y][z+1].blockType == BlockData.BlockType.EMPTY) {
							integrateFace(chunkBlocks[x][y][z], BlockData.Faces.FRONT);
						}
					}
					else {
						integrateFace(chunkBlocks[x][y][z], BlockData.Faces.FRONT);
					}
					numberOfFaces++;

					// qualifications: block front is empty, is the back in chunk
					if (z > 0) {
						if (chunkBlocks[x][y][z-1].blockType == BlockData.BlockType.EMPTY) {
							integrateFace(chunkBlocks[x][y][z], BlockData.Faces.BACK);
						}
					}
					else {
						integrateFace(chunkBlocks[x][y][z], BlockData.Faces.BACK);
					}
					numberOfFaces++;

					addIndices(numberOfFaces);

				}
			}
		}
	}

	public void integrateFace(Block block, BlockData.Faces face){
		FaceData faceData = block.getFace(face);
		chunkVertices.addAll(faceData.vertices);
		chunkUvs.addAll(faceData.uvs);
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
