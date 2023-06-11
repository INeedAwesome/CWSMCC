package com.github.ineedawesome.world;

import org.joml.Vector3f;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlockData {

	enum Faces {
		FRONT, BACK, LEFT, RIGHT, TOP, BOTTOM
	};

	private static final List<Vector3f> frontVertices = Arrays.asList(
			new Vector3f(-0.5f, 0.5f, 0.5f),
			new Vector3f(0.5f, 0.5f, 0.5f),
			new Vector3f(0.5f, -0.5f, 0.5f),
			new Vector3f(-0.5f, -0.5f, 0.5f)
	);
	private static final List<Vector3f> backVertices = Arrays.asList(
			new Vector3f(0.5f, 0.5f, -0.5f),
			new Vector3f(-0.5f, 0.5f, -0.5f),
			new Vector3f(-0.5f, -0.5f, -0.5f),
			new Vector3f(0.5f, -0.5f, -0.5f)
	);
	private static final List<Vector3f> leftVertices = Arrays.asList(
			new Vector3f(-0.5f, 0.5f, -0.5f),
			new Vector3f(-0.5f, 0.5f, 0.5f),
			new Vector3f(-0.5f, -0.5f, 0.5f),
			new Vector3f(-0.5f, -0.5f, -0.5f)
	);
	private static final List<Vector3f> rightVertices = Arrays.asList(
			new Vector3f(0.5f, 0.5f, 0.5f),
			new Vector3f(0.5f, 0.5f, -0.5f),
			new Vector3f(0.5f, -0.5f, -0.5f),
			new Vector3f(0.5f, -0.5f, 0.5f)
	);
	private static final List<Vector3f> topVertices = Arrays.asList(
			new Vector3f(-0.5f, 0.5f, -0.5f),
			new Vector3f(0.5f, 0.5f, -0.5f),
			new Vector3f(0.5f, 0.5f, 0.5f),
			new Vector3f(-0.5f, 0.5f, 0.5f)
	);
	private static final List<Vector3f> bottomVertices = Arrays.asList(
			new Vector3f(-0.5f, -0.5f, 0.5f),
			new Vector3f(0.5f, -0.5f, 0.5f),
			new Vector3f(0.5f, -0.5f, -0.5f),
			new Vector3f(-0.5f, -0.5f, -0.5f)
	);

	public static Map<BlockData.Faces, List<Vector3f>> rawVertexData = new HashMap<>() {
		{
			put(BlockData.Faces.FRONT, frontVertices);
			put(BlockData.Faces.BACK, backVertices);
			put(BlockData.Faces.LEFT, leftVertices);
			put(BlockData.Faces.RIGHT, rightVertices);
			put(BlockData.Faces.TOP, topVertices);
			put(BlockData.Faces.BOTTOM, bottomVertices);
		}
	};

}
