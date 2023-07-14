package com.github.ineedawesome.world;

import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Block {

	public Vector3f position;

	public BlockData.BlockType blockType;

	private Map<BlockData.Faces, FaceData> faces;

	public List<Vector2f> dirtUv = new ArrayList<>() {{
		add(new Vector2f(0f, 1f));
		add(new Vector2f(1f, 1f));
		add(new Vector2f(1f, 0f));
		add(new Vector2f(0f, 0f));
	}};

	public Block(Vector3f position, BlockData.BlockType blockType) {
		this.position = position;
		this.blockType = blockType;

		FaceData faceDataFront = new FaceData();
		faceDataFront.vertices = addTransformedVertices(BlockData.rawVertexData.get(BlockData.Faces.FRONT));
		faceDataFront.uvs = dirtUv;

		FaceData faceDataBack = new FaceData();
		faceDataBack.vertices = addTransformedVertices(BlockData.rawVertexData.get(BlockData.Faces.BACK));
		faceDataBack.uvs = dirtUv;

		FaceData faceDataLeft = new FaceData();
		faceDataLeft.vertices = addTransformedVertices(BlockData.rawVertexData.get(BlockData.Faces.LEFT));
		faceDataLeft.uvs = dirtUv;

		FaceData faceDataRight = new FaceData();
		faceDataRight.vertices = addTransformedVertices(BlockData.rawVertexData.get(BlockData.Faces.RIGHT));
		faceDataRight.uvs = dirtUv;

		FaceData faceDataTop = new FaceData();
		faceDataTop.vertices = addTransformedVertices(BlockData.rawVertexData.get(BlockData.Faces.TOP));
		faceDataTop.uvs = dirtUv;

		FaceData faceDataBottom = new FaceData();
		faceDataBottom.vertices = addTransformedVertices(BlockData.rawVertexData.get(BlockData.Faces.BOTTOM));
		faceDataBottom.uvs = dirtUv;

		faces = new HashMap<>() {{
			put(BlockData.Faces.FRONT, faceDataFront);
			put(BlockData.Faces.BACK, faceDataBack);
			put(BlockData.Faces.LEFT, faceDataLeft);
			put(BlockData.Faces.RIGHT, faceDataRight);
			put(BlockData.Faces.TOP, faceDataTop);
			put(BlockData.Faces.BOTTOM, faceDataBottom);
		}};
	}

	public List<Vector3f> addTransformedVertices(List<Vector3f> vertices) {
		List<Vector3f> transformedVertices = new ArrayList<>();
		for (Vector3f vertex: vertices) {
			transformedVertices.add(new Vector3f(vertex.x + position.x, vertex.y + position.y,vertex.z + position.z));
		}
		return transformedVertices;
	}

	public FaceData getFace(BlockData.Faces face) {
		return faces.get(face);
	}
}
