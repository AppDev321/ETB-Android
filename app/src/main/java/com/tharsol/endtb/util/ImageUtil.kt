package com.tharsol.endtb.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.graphics.*
import android.location.Location
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Base64
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.tharsol.endtb.R
import com.tharsol.endtb.util.App
import com.tharsol.endtb.util.Utilities.uriAuthority
import com.yalantis.ucrop.UCrop
import org.joda.time.DateTime
import java.io.*

object ImageUtil {
    /* Get uri related content real local file path. */
    fun getUriRealPath(ctx: Context, uri: Uri): String? {
        var ret: String? = ""
        ret = if (isAboveKitKat) {
            // Android OS above sdk version 19.
            getUriRealPathAboveKitkat(ctx, uri)
        } else {
            // Android OS below sdk version 19
            getImageRealPath(ctx.contentResolver, uri, null)
        }
        return ret
    }

    private fun getUriRealPathAboveKitkat(ctx: Context?, uri: Uri?): String? {
        var ret: String? = ""
        if (ctx != null && uri != null) {
            if (isContentUri(uri)) {
                ret = if (isGooglePhotoDoc(uri.authority)) {
                    uri.lastPathSegment
                } else {
                    getImageRealPath(ctx.contentResolver, uri, null)
                }
            } else if (isFileUri(uri)) {
                ret = uri.path
            } else if (isDocumentUri(ctx, uri)) {

                // Get uri related document id.
                val documentId = DocumentsContract.getDocumentId(uri)

                // Get uri authority.
                val uriAuthority = uri.authority
                if (isMediaDoc(uriAuthority)) {
                    val idArr = documentId.split(":").toTypedArray()
                    if (idArr.size == 2) {
                        // First item is document type.
                        val docType = idArr[0]

                        // Second item is document real id.
                        val realDocId = idArr[1]

                        // Get content uri by document type.
                        var mediaContentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        if ("image" == docType) {
                            mediaContentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        } else if ("video" == docType) {
                            mediaContentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                        } else if ("audio" == docType) {
                            mediaContentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                        }

                        // Get where clause with real document id.
                        val whereClause = MediaStore.Images.Media._ID + " = " + realDocId
                        ret = getImageRealPath(ctx.contentResolver, mediaContentUri, whereClause)
                    }
                } else if (isDownloadDoc(uriAuthority)) {
                    // Build download uri.
                    val downloadUri = Uri.parse("content://downloads/public_downloads")

                    // Append download document id at uri end.
                    val downloadUriAppendId =
                        ContentUris.withAppendedId(downloadUri, java.lang.Long.valueOf(documentId))
                    ret = getImageRealPath(ctx.contentResolver, downloadUriAppendId, null)
                } else if (isExternalStoreDoc(uriAuthority)) {
                    val idArr = documentId.split(":").toTypedArray()
                    if (idArr.size == 2) {
                        val type = idArr[0]
                        val realDocId = idArr[1]
                        if ("primary".equals(type, ignoreCase = true)) {
                            ret = Environment.getExternalStorageDirectory()
                                .toString() + "/" + realDocId
                        }
                    }
                }
            }
        }
        return ret
    }

    /* Check whether current android os version is bigger than kitkat or not. */
    private val isAboveKitKat: Boolean
        private get() {
            var ret = false
            ret = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
            return ret
        }

    /* Check whether this uri represent a document or not. */
    private fun isDocumentUri(ctx: Context?, uri: Uri?): Boolean {
        var ret = false
        if (ctx != null && uri != null) {
            ret = DocumentsContract.isDocumentUri(ctx, uri)
        }
        return ret
    }

    /* Check whether this uri is a content uri or not.
     *  content uri like content://media/external/images/media/1302716
     *  */
    private fun isContentUri(uri: Uri?): Boolean {
        var ret = false
        if (uri != null) {
            val uriSchema = uri.scheme
            if ("content".equals(uriSchema, ignoreCase = true)) {
                ret = true
            }
        }
        return ret
    }

    /* Check whether this uri is a file uri or not.
     *  file uri like file:///storage/41B7-12F1/DCIM/Camera/IMG_20180211_095139.jpg
     * */
    private fun isFileUri(uri: Uri?): Boolean {
        var ret = false
        if (uri != null) {
            val uriSchema = uri.scheme
            if ("file".equals(uriSchema, ignoreCase = true)) {
                ret = true
            }
        }
        return ret
    }

    /* Check whether this document is provided by ExternalStorageProvider. */
    private fun isExternalStoreDoc(uriAuthority: String?): Boolean {
        var ret = false
        if ("com.android.externalstorage.documents" == uriAuthority) {
            ret = true
        }
        return ret
    }

    /* Check whether this document is provided by DownloadsProvider. */
    private fun isDownloadDoc(uriAuthority: String?): Boolean {
        var ret = false
        if ("com.android.providers.downloads.documents" == uriAuthority) {
            ret = true
        }
        return ret
    }

    /* Check whether this document is provided by MediaProvider. */
    private fun isMediaDoc(uriAuthority: String?): Boolean {
        var ret = false
        if ("com.android.providers.media.documents" == uriAuthority) {
            ret = true
        }
        return ret
    }

    /* Check whether this document is provided by google photos. */
    private fun isGooglePhotoDoc(uriAuthority: String?): Boolean {
        var ret = false
        if ("com.google.android.apps.photos.content" == uriAuthority) {
            ret = true
        }
        return ret
    }

    /* Return uri represented document file real local path.*/
    private fun getImageRealPath(
        contentResolver: ContentResolver,
        uri: Uri,
        whereClause: String?
    ): String {
        var ret = ""

        // Query the uri with condition.
        val cursor = contentResolver.query(uri, null, whereClause, null, null)
        if (cursor != null) {
            val moveToFirst = cursor.moveToFirst()
            if (moveToFirst) {

                // Get columns name by uri type.
                var columnName = MediaStore.Images.Media.DATA
                if (uri === MediaStore.Images.Media.EXTERNAL_CONTENT_URI) {
                    columnName = MediaStore.Images.Media.DATA
                } else if (uri === MediaStore.Audio.Media.EXTERNAL_CONTENT_URI) {
                    columnName = MediaStore.Audio.Media.DATA
                } else if (uri === MediaStore.Video.Media.EXTERNAL_CONTENT_URI) {
                    columnName = MediaStore.Video.Media.DATA
                }

                // Get column index.
                val imageColumnIndex = cursor.getColumnIndex(columnName)

                // Get column value which is the uri related file local path.
                ret = cursor.getString(imageColumnIndex)
            }
        }
        return ret
    }

    @Throws(IllegalArgumentException::class)
    fun convertBitmap(base64Str: String?): Bitmap {
        val decodedBytes = Base64.decode(base64Str, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }

    @Throws(Exception::class)
    fun convertBase64(imagePath: String?): String {

//        Bitmap bitmap = ImageUtil.compressImage(BitmapFactory.decodeFile(imagePath));
        val file = decodeFile(App.context, File(imagePath), 720, 1280)
//        val lastLocation: Location = Home.getLocation()
//        if (lastLocation != null) saveGpsLoc(
//            file.absolutePath,
//            lastLocation.latitude,
//            lastLocation.longitude
//        )
        return Base64.encodeToString(loadFile(file), Base64.DEFAULT)
    }

    @Throws(IOException::class)
    private fun loadFile(file: File): ByteArray {
        val `is` = FileInputStream(file)
        val length = file.length()
        if (length > Int.MAX_VALUE) {
            // File is too large
        }
        val bytes = ByteArray(length.toInt())
        var offset = 0
        var numRead = 0
        while (offset < bytes.size
            && `is`.read(bytes, offset, bytes.size - offset).also { numRead = it } >= 0
        ) {
            offset += numRead
        }
        if (offset < bytes.size) {
            throw IOException("Could not completely read file " + file.name)
        }
        `is`.close()
        return bytes
    }

    fun convertBase64(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, outputStream)
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
    }

    /**
     * This function will reduce the size of image
     * and size equal to 50 to 70Kb just without loosing quality
     *
     * @param unscaledBitmap
     * @return compress bitmap.
     */
    fun compressImage(unscaledBitmap: Bitmap): Bitmap {
        val rect = Rect(0, 0, unscaledBitmap.width, unscaledBitmap.height)
        val scaledBitmap = Bitmap.createBitmap(
            rect.width(), rect.height(),
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(scaledBitmap)
        canvas.drawBitmap(unscaledBitmap, rect, rect, Paint(Paint.FILTER_BITMAP_FLAG))
        return scaledBitmap
    }

    private fun decodeFile(
        context: Context,
        path: File,
        DESIREDWIDTH: Int,
        DESIREDHEIGHT: Int /*, int orientation*/
    ): File {
//        String strMyImagePath = null;
        var scaledBitmap: Bitmap? = null
        try {
            // Part 1: Decode image
            var unscaledBitmap: Bitmap? = ScalingUtilities.decodeFile(
                path.absolutePath,
                DESIREDWIDTH,
                DESIREDHEIGHT,
                ScalingUtilities.ScalingLogic.FIT
            )

//            if (!(unscaledBitmap.getWidth() <= DESIREDWIDTH && unscaledBitmap.getHeight() <= DESIREDHEIGHT))
//            {
            // Part 2: Scale image
            scaledBitmap = ScalingUtilities.createScaledBitmap(
                unscaledBitmap!!,
                DESIREDWIDTH,
                DESIREDHEIGHT,
                ScalingUtilities.ScalingLogic.FIT
            )
            unscaledBitmap!!.recycle()
            //            }
            /*else {
                unscaledBitmap.recycle();
//                return path;
            }*/unscaledBitmap = scaledBitmap
            // rotate the orientation according to device
            val matrix = Matrix()
            matrix.postRotate(
                ScalingUtilities.getCameraPhotoOrientation(
                    context,
                    FileProvider.getUriForFile(context, uriAuthority, path),
                    path
                ).toFloat()
            )
            scaledBitmap = Bitmap.createBitmap(
                unscaledBitmap,
                0,
                0,
                unscaledBitmap.width,
                unscaledBitmap.height,
                matrix,
                true
            )

//            strMyImagePath = f.getAbsolutePath();
            var fos: FileOutputStream? = null
            try {
                fos = FileOutputStream(path)
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos)
                fos.flush()
                fos.close()
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            scaledBitmap.recycle()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return path
    }

    @SuppressLint("UseRequireInsteadOfGet")
    fun startImageCrop(fragment: Fragment, source: Uri?, destPath: Uri?, requestCode: Int) {
        val options = UCrop.Options()
        options.setToolbarColor(ActivityCompat.getColor(App.context, R.color.colorPrimary))
        options.setStatusBarColor(
            ActivityCompat.getColor(
                App.context,
                R.color.colorPrimaryDark
            )
        )
        options.setHideBottomControls(true)
        UCrop.of(source!!, destPath!!).withAspectRatio(3f, 4f).withMaxResultSize(720, 1280)
            .withOptions(options).start(fragment.context!!, fragment, requestCode)
    }

    fun startImageCrop(context: Activity?, source: Uri?, destPath: Uri?) {
        val options = UCrop.Options()
        options.setToolbarColor(ActivityCompat.getColor(App.context, R.color.colorPrimary))
        options.setStatusBarColor(
            ActivityCompat.getColor(
                App.context,
                R.color.colorPrimaryDark
            )
        )
        UCrop.of(source!!, destPath!!).withAspectRatio(16f, 9f).withMaxResultSize(720, 1280)
            .withOptions(options).start(context!!)
    }

    fun loadImage(view: ImageView, url: String?) {
        val drawable = CircularProgressDrawable(view.context)
        drawable.strokeWidth = 5f
        drawable.centerRadius = 30f
        drawable.start()
        Glide.with(view.context).load(url).placeholder(drawable).into(view)
    }

//    fun saveGpsLoc(imagePath: String?, latitude: Double, longtidue: Double) {
//        try {
//            val exif = ExifInterface(imagePath!!)
//            exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, GPS.convert(latitude))
//            exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, GPS.latitudeRef(latitude))
//            exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, GPS.convert(longtidue))
//            exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, GPS.longitudeRef(longtidue))
//            exif.setAttribute(
//                ExifInterface.TAG_DATETIME,
//                DateUtilz.formatDateTimeFormatBackendStandard(DateTime.now())
//            )
//            exif.saveAttributes()
//        } catch (e: Exception) {
//            // ignore
//        }
//    }
}