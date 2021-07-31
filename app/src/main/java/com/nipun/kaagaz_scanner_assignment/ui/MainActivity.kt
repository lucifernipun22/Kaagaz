package com.nipun.kaagaz_scanner_assignment.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.nipun.kaagaz_scanner_assignment.R
import com.nipun.kaagaz_scanner_assignment.db.model.MyDataDatabase
import com.nipun.kaagaz_scanner_assignment.db.model.MyDataEntity
import com.nipun.kaagaz_scanner_assignment.repository.MyRepository
import com.nipun.kaagaz_scanner_assignment.db.viewModel.MyViewModel
import com.nipun.kaagaz_scanner_assignment.db.viewModel.ViewModelFactory
import kotlinx.android.synthetic.main.activity_image_recycler_view.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private val PERMISSIONS_REQUEST_CODE = 10
    private var imageCapture: ImageCapture? = null
    private lateinit var directory: File
    private lateinit var cameraExecuter: ExecutorService
    private lateinit var myViewModel: MyViewModel
    private var myDataEntity: MyDataEntity? = null

    companion object {
        /**
         * This function is used to check camera permissions is granted or not.
         */
        fun isPermissions(context: Context) = arrayOf(Manifest.permission.CAMERA).all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }

        private const val FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        clickListener()

        /**
         * Request camera-related permissions
         */
        if (!isPermissions(this)) {

            requestPermissions(arrayOf(Manifest.permission.CAMERA), PERMISSIONS_REQUEST_CODE)
        }

        /**
         * If permissions have already been granted, it will be proceed to mainActivity
         */
        else {
            openCamera()
        }
    }

    /**
     * This function is for set the click on ImageButton like switch camera, captureImage, Gallery.
     */
    private fun clickListener() {
        camera_capture_button.setOnClickListener {
            takePhoto()
        }
        /**
         * launch imageRecyclerActivity on click of photo_view button.
         */
        photo_view_button.setOnClickListener {
            val intent = Intent(this, ImageRecyclerViewActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * when user all permission then it open the camera.
     */
    @RequiresApi(Build.VERSION_CODES.R)
    private fun openCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener(Runnable {
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()


            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(view_finder.surfaceProvider)
                }
            imageCapture = ImageCapture.Builder().build()
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )
            } catch (e: Exception) {
                Log.d("Nipun", "failed", e)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    /**
     * capture the photos on click of capture button.
     */
    private fun takePhoto() {
        directory = getOutputDirectory()
        val imageCapture = imageCapture ?: return
        val photoFile = File(
            directory,
            SimpleDateFormat(
                FILENAME,
                Locale.US
            ).format(System.currentTimeMillis()) + ".jpg"
        )
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exception: ImageCaptureException) {
                    Log.d("Nipun","Image not Saved")
                }

                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val savedPath = Uri.fromFile(photoFile)
                    findViewById<ImageButton>(R.id.photo_view_button).visibility = View.VISIBLE
                    findViewById<ImageButton>(R.id.photo_view_button).setImageURI(savedPath)
                    Toast.makeText(
                        this@MainActivity,
                        "Saved image at ${savedPath}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )
        val MyDataDao by lazy {
            val roomDatabase = MyDataDatabase.getDatabase(this)
            roomDatabase.getMyDao()
        }
        val repository by lazy {
            MyRepository(MyDataDao)
        }
        val factory = ViewModelFactory(repository)
        myViewModel = ViewModelProvider(this, factory).get(MyViewModel::class.java)

        if (isNetworkConnected()) {
            directory = getOutputDirectory()
            val photoFile = File(
                directory,
                SimpleDateFormat(
                    FILENAME,
                    Locale.US
                ).format(System.currentTimeMillis()) + ".jpg"
            )

            val sdf = SimpleDateFormat("yyyy.MM.dd.HH.mm.ss")
            val timestamp = Timestamp(System.currentTimeMillis())
            val date = Date()
            println(Timestamp(date.time))
            println(timestamp.time)
            val savedPath = Uri.fromFile(photoFile)

            CoroutineScope(Dispatchers.IO).launch {
                myDataEntity?.let { myViewModel.addImage(it) }
                var myEntity = MyDataEntity(savedPath.toString(),sdf.format(timestamp))
                myViewModel.addImage(myEntity)
            }

        } else if (!isNetworkConnected()) {

        }
    }

    /**
     * Initialize internal directory to save images at a internal path
     */
    private fun getOutputDirectory(): File {
        var mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply {
                mkdir()
            }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            /**
             * If permissions is granted, it will be proceed to open camera.
             */
            if (PackageManager.PERMISSION_GRANTED == grantResults.firstOrNull()) {
                openCamera()
            } else {
                Toast.makeText(this, "Required permissions", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun isNetworkConnected(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            connectivityManager.activeNetwork
        } else {
            TODO("VERSION.SDK_INT < M")
        }
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        return networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    /**
     * when user click destroy the activity then shut the camera.
     */
    override fun onDestroy() {
        cameraExecuter = Executors.newSingleThreadExecutor()
        super.onDestroy()
        cameraExecuter.shutdown()
    }

}