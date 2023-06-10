package com.github.ineedawesome;

import org.joml.*;

import java.util.List;

public class ArrayHelper {

	public static float[] Vector3fListToFloatArray(List<Vector3f> data)
	{
		float[] floatArray = new float[data.size()*3];
		for (int i = 0; i < data.size(); i++) {
			floatArray[i*3] = data.get(i).x;
			floatArray[i*3+1] = data.get(i).y;
			floatArray[i*3+2] = data.get(i).z;
		}
		return floatArray;
	}
	public static int[] Vector3iListToIntArray(List<Vector3i> data)
	{
		int[] intArray = new int[data.size()*3];
		for (int i = 0; i < data.size(); i++) {
			intArray[i*3] = data.get(i).x;
			intArray[i*3+1] = data.get(i).y;
			intArray[i*3+2] = data.get(i).z;
		}
		return intArray;
	}

	public static float[] Vector2fListToFloatArray(List<Vector2f> data)
	{
		float[] floatArray = new float[data.size()*2];
		for (int i = 0; i < data.size(); i++) {
			floatArray[i*2] = data.get(i).x;
			floatArray[i*2+1] = data.get(i).y;
		}
		return floatArray;
	}

	public static int[] Vector2iListToFloatArray(List<Vector2i> data)
	{
		int[] intArray = new int[data.size()*2];
		for (int i = 0; i < data.size(); i++) {
			intArray[i*2] = data.get(i).x;
			intArray[i*2+1] = data.get(i).y;
		}
		return intArray;
	}


}
