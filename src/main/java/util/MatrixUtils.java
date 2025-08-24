package util;

import org.joml.*;

public class MatrixUtils {

    // Convert a Matrix4d to Matrix4f for OpenGL
    public static Matrix4f toFloat(Matrix4d matD) {
        Matrix4f matF = new Matrix4f();
        matF.set(matD); // JOML handles the casting automatically
        return matF;
    }

    // Convert a Matrix3d to Matrix3f for OpenGL
    public static Matrix3f toFloat(Matrix3d matD) {
        return new Matrix3f((Matrix3fc) matD);
    }
}
