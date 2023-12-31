package com.tharsol.endtb.util

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.media.ExifInterface
import android.net.Uri
import android.util.Log
import java.io.File

object ScalingUtilities {
    /**
     * Utility function for decoding an image resource. The decoded bitmap will
     * be optimized for further scaling to the requested destination dimensions
     * and scaling logic.
     *
     * @param res          The resources object containing the image data
     * @param resId        The resource id of the image data
     * @param dstWidth     Width of destination area
     * @param dstHeight    Height of destination area
     * @param scalingLogic Logic to use to avoid image stretching
     * @return Decoded bitmap
     */
    fun decodeResource(
        res: Resources?, resId: Int, dstWidth: Int, dstHeight: Int,
        scalingLogic: ScalingLogic
    ): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(res, resId, options)
        options.inJustDecodeBounds = false
        options.inSampleSize = calculateSampleSize(
            options.outWidth, options.outHeight, dstWidth,
            dstHeight, scalingLogic
        )
        return BitmapFactory.decodeResource(res, resId, options)
    }

    fun decodeFile(
        path: String?, dstWidth: Int, dstHeight: Int,
        scalingLogic: ScalingLogic
    ): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(path, options)
        options.inJustDecodeBounds = false
        options.inSampleSize = calculateSampleSize(
            options.outWidth, options.outHeight, dstWidth,
            dstHeight, scalingLogic
        )
        return BitmapFactory.decodeFile(path, options)
    }

    fun decodeByte(
        path: ByteArray, dstWidth: Int, dstHeight: Int,
        scalingLogic: ScalingLogic
    ): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeByteArray(path, 0, path.size, options)
        options.inJustDecodeBounds = false
        options.inSampleSize = calculateSampleSize(
            options.outWidth, options.outHeight, dstWidth,
            dstHeight, scalingLogic
        )
        return BitmapFactory.decodeByteArray(path, 0, path.size, options)
    }

    /**
     * Utility function for creating a scaled version of an existing bitmap
     *
     * @param unscaledBitmap Bitmap to scale
     * @param dstWidth       Wanted width of destination bitmap
     * @param dstHeight      Wanted height of destination bitmap
     * @param scalingLogic   Logic to use to avoid image stretching
     * @return New scaled bitmap object
     */
    fun createScaledBitmap(
        unscaledBitmap: Bitmap, dstWidth: Int, dstHeight: Int,
        scalingLogic: ScalingLogic
    ): Bitmap {
        val srcRect = calculateSrcRect(
            unscaledBitmap.width, unscaledBitmap.height,
            dstWidth, dstHeight, scalingLogic
        )
        val dstRect = calculateDstRect(
            unscaledBitmap.width, unscaledBitmap.height,
            dstWidth, dstHeight, scalingLogic
        )
        val scaledBitmap = Bitmap.createBitmap(
            dstRect.width(), dstRect.height(),
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(scaledBitmap)
        canvas.drawBitmap(unscaledBitmap, srcRect, dstRect, Paint(Paint.FILTER_BITMAP_FLAG))
        return scaledBitmap
    }

    /**
     * Calculate optimal down-sampling factor given the dimensions of a source
     * image, the dimensions of a destination area and a scaling logic.
     *
     * @param srcWidth     Width of source image
     * @param srcHeight    Height of source image
     * @param dstWidth     Width of destination area
     * @param dstHeight    Height of destination area
     * @param scalingLogic Logic to use to avoid image stretching
     * @return Optimal down scaling sample size for decoding
     */
    fun calculateSampleSize(
        srcWidth: Int, srcHeight: Int, dstWidth: Int, dstHeight: Int,
        scalingLogic: ScalingLogic
    ): Int {
        return if (scalingLogic == ScalingLogic.FIT) {
            val srcAspect = srcWidth.toFloat() / srcHeight.toFloat()
            val dstAspect = dstWidth.toFloat() / dstHeight.toFloat()
            if (srcAspect > dstAspect) {
                srcWidth / dstWidth
            } else {
                srcHeight / dstHeight
            }
        } else {
            val srcAspect = srcWidth.toFloat() / srcHeight.toFloat()
            val dstAspect = dstWidth.toFloat() / dstHeight.toFloat()
            if (srcAspect > dstAspect) {
                srcHeight / dstHeight
            } else {
                srcWidth / dstWidth
            }
        }
    }

    /**
     * Calculates source rectangle for scaling bitmap
     *
     * @param srcWidth     Width of source image
     * @param srcHeight    Height of source image
     * @param dstWidth     Width of destination area
     * @param dstHeight    Height of destination area
     * @param scalingLogic Logic to use to avoid image stretching
     * @return Optimal source rectangle
     */
    fun calculateSrcRect(
        srcWidth: Int, srcHeight: Int, dstWidth: Int, dstHeight: Int,
        scalingLogic: ScalingLogic
    ): Rect {
        return if (scalingLogic == ScalingLogic.CROP) {
            val srcAspect = srcWidth.toFloat() / srcHeight.toFloat()
            val dstAspect = dstWidth.toFloat() / dstHeight.toFloat()
            if (srcAspect > dstAspect) {
                val srcRectWidth = (srcHeight * dstAspect).toInt()
                val srcRectLeft = (srcWidth - srcRectWidth) / 2
                Rect(srcRectLeft, 0, srcRectLeft + srcRectWidth, srcHeight)
            } else {
                val srcRectHeight = (srcWidth / dstAspect).toInt()
                val scrRectTop = (srcHeight - srcRectHeight) / 2
                Rect(0, scrRectTop, srcWidth, scrRectTop + srcRectHeight)
            }
        } else {
            Rect(0, 0, srcWidth, srcHeight)
        }
    }

    /**
     * Calculates destination rectangle for scaling bitmap
     *
     * @param srcWidth     Width of source image
     * @param srcHeight    Height of source image
     * @param dstWidth     Width of destination area
     * @param dstHeight    Height of destination area
     * @param scalingLogic Logic to use to avoid image stretching
     * @return Optimal destination rectangle
     */
    fun calculateDstRect(
        srcWidth: Int, srcHeight: Int, dstWidth: Int, dstHeight: Int,
        scalingLogic: ScalingLogic
    ): Rect {
        return if (scalingLogic == ScalingLogic.FIT) {
            val srcAspect = srcWidth.toFloat() / srcHeight.toFloat()
            val dstAspect = dstWidth.toFloat() / dstHeight.toFloat()
            if (srcAspect > dstAspect) {
                Rect(0, 0, dstWidth, (dstWidth / srcAspect).toInt())
            } else {
                Rect(0, 0, (dstHeight * srcAspect).toInt(), dstHeight)
            }
        } else {
            Rect(0, 0, dstWidth, dstHeight)
        }
    }

    fun getCameraPhotoOrientation(context: Context, imageUri: Uri?, imagePath: String?): Int {
        return getCameraPhotoOrientation(context, imageUri, File(imagePath))
    }

    fun getCameraPhotoOrientation(context: Context, imageUri: Uri?, imageFile: File): Int {
        var rotate = 0
        try {
            context.contentResolver.notifyChange(imageUri!!, null)
            //            File imageFile = new File(imagePath);
            val exif = ExifInterface(imageFile.absolutePath)
            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_270 -> rotate = 270
                ExifInterface.ORIENTATION_ROTATE_180 -> rotate = 180
                ExifInterface.ORIENTATION_ROTATE_90 -> rotate = 90
            }
            Log.i("RotateImage", "Exif orientation: $orientation")
            Log.i("RotateImage", "Rotate value: $rotate")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return rotate
    }

    /**
     * ScalingLogic defines how scaling should be carried out if source and
     * destination image has different aspect ratio.
     *
     *
     * CROP: Scales the image the minimum amount while making sure that at least
     * one of the two dimensions fit inside the requested destination area.
     * Parts of the source image will be cropped to realize this.
     *
     *
     * FIT: Scales the image the minimum amount while making sure both
     * dimensions fit inside the requested destination area. The resulting
     * destination dimensions might be adjusted to a smaller size than
     * requested.
     */
    enum class ScalingLogic {
        CROP, FIT
    }
}