package com.xs.lightpuzzle.puzzle.piece;

import android.graphics.Matrix;
import android.graphics.RectF;

import static java.lang.Math.round;

/**
 * Created by xs on 2017/12/12.
 * 编辑页布局相关
 */
public class MatrixUtils {

    private static final float[] sMatrixValues = new float[9];
    private static final Matrix sTempMatrix = new Matrix();

    private MatrixUtils() {
        //no instance
    }

    /**
     * This method calculates scale value for given Matrix object.
     */
    public static float getMatrixScale(Matrix matrix) {
        return (float) Math.sqrt(Math.pow(getMatrixValue(matrix, Matrix.MSCALE_X), 2) + Math.pow(
                getMatrixValue(matrix, Matrix.MSKEW_Y), 2));
    }

    public static float getMatrixTransX(Matrix matrix) {
        return getMatrixValue(matrix, Matrix.MTRANS_X);
    }

    public static float getMatrixTransY(Matrix matrix) {
        return getMatrixValue(matrix, Matrix.MTRANS_Y);
    }

    /**
     * This method calculates rotation angle for given Matrix object.
     */
    public static float getMatrixAngle(Matrix matrix) {
        return (float) -(Math.atan2(getMatrixValue(matrix, Matrix.MSKEW_X),
                getMatrixValue(matrix, Matrix.MSCALE_X)) * (180 / Math.PI));
    }

    public static float getMatrixValue(Matrix matrix, int valueIndex) {
        matrix.getValues(sMatrixValues);
        return sMatrixValues[valueIndex];
    }

    //计算包含给出点的最小矩形
    public static RectF trapToRect(float[] array) {
        RectF r = new RectF(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY,
                Float.NEGATIVE_INFINITY);
        for (int i = 1; i < array.length; i += 2) {
            float x = round(array[i - 1] * 10) / 10.f;
            float y = round(array[i] * 10) / 10.f;
            r.left = (x < r.left) ? x : r.left;
            r.top = (y < r.top) ? y : r.top;
            r.right = (x > r.right) ? x : r.right;
            r.bottom = (y > r.bottom) ? y : r.bottom;
        }
        r.sort();
        return r;
    }

    public static float[] getCornersFromRect(RectF r) {
        return new float[]{
                r.left, r.top, r.right, r.top, r.right, r.bottom, r.left, r.bottom
        };
    }
//
//    public static float getMinMatrixScale(BasePiece piece , RectF rectF) {
//        if (piece != null) {
//
//            sTempMatrix.reset();
//            sTempMatrix.setRotate(-piece.getMatrixAngle());
//
//            float[] unrotatedCropBoundsCorners = getCornersFromRect(rectF);
//
//            sTempMatrix.mapPoints(unrotatedCropBoundsCorners);
//
//            RectF unrotatedCropRect = trapToRect(unrotatedCropBoundsCorners);
//
//            return Math.max(unrotatedCropRect.width() / piece.getWidth(),
//                    unrotatedCropRect.height() / piece.getHeight());
//        }
//
//        return 1f;
//    }
//
//    //判断剪裁框是否在图片内
//    public static boolean judgeIsImageContainsBorder(BasePiece piece,RectF rectF, float rotateDegrees) {
//        sTempMatrix.reset();
//        sTempMatrix.setRotate(-rotateDegrees);
//        float[] unrotatedWrapperCorner = new float[8];
//        float[] unrotateBorderCorner = new float[8];
//        sTempMatrix.mapPoints(unrotatedWrapperCorner, piece.getCurrentDrawablePoints());
//        sTempMatrix.mapPoints(unrotateBorderCorner, getCornersFromRect(rectF));
//
//        return trapToRect(unrotatedWrapperCorner).contains(trapToRect(unrotateBorderCorner));
//    }
//
//    public static float[] calculateImageIndents(BasePiece piece , RectF rectF) {
//        if (piece == null) return new float[]{0, 0, 0, 0, 0, 0, 0, 0};
//
//        sTempMatrix.reset();
//        sTempMatrix.setRotate(-piece.getMatrixAngle());
//
//        final float[] currentImageCorners = piece.getCurrentDrawablePoints();
//        final float[] unrotatedImageCorners =
//                Arrays.copyOf(currentImageCorners, currentImageCorners.length);
//        final float[] unrotatedCropBoundsCorners = getCornersFromRect(rectF);
//
//        sTempMatrix.mapPoints(unrotatedImageCorners);
//        sTempMatrix.mapPoints(unrotatedCropBoundsCorners);
//
//        RectF unrotatedImageRect = trapToRect(unrotatedImageCorners);
//        RectF unrotatedCropRect = trapToRect(unrotatedCropBoundsCorners);
//
//        float deltaLeft = unrotatedImageRect.left - unrotatedCropRect.left;
//        float deltaTop = unrotatedImageRect.top - unrotatedCropRect.top;
//        float deltaRight = unrotatedImageRect.right - unrotatedCropRect.right;
//        float deltaBottom = unrotatedImageRect.bottom - unrotatedCropRect.bottom;
//
//        float indents[] = new float[4];
//
//        indents[0] = (deltaLeft > 0) ? deltaLeft : 0;
//        indents[1] = (deltaTop > 0) ? deltaTop : 0;
//        indents[2] = (deltaRight < 0) ? deltaRight : 0;
//        indents[3] = (deltaBottom < 0) ? deltaBottom : 0;
//
//        sTempMatrix.reset();
//        sTempMatrix.setRotate(piece.getMatrixAngle());
//        sTempMatrix.mapPoints(indents);
//
//        return indents;
//    }
//
//    public static Matrix generateMatrix(BasePiece piece , RectF rectF, float extra) {
//        return generateCenterCropMatrix(rectF, piece.getDrawable().getIntrinsicWidth(),
//                piece.getDrawable().getIntrinsicHeight(), extra);
//    }

    public static Matrix generateCenterCropMatrix(RectF rectF, int width, int height, float extraSize) {

        //将图片的中心平移到矩形框的中心位置，而后以矩形框为中心等比例缩放图片，得到matrix

        Matrix matrix = new Matrix();
        float offsetX = rectF.centerX() - width / 2;
        float offsetY = rectF.centerY() - height / 2;
        matrix.postTranslate(offsetX, offsetY);

        float scale;

        if (width * rectF.height() > rectF.width() * height) {
            scale = (rectF.height() + extraSize) / height;
        } else {
            scale = (rectF.width() + extraSize) / width;
        }

        matrix.postScale(scale, scale, rectF.centerX(), rectF.centerY());

        return matrix;
    }
}
