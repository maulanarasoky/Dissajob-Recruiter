package org.d3ifcool.dissajobrecruiter.ui.applicant.media

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.downloadservice.filedownloadservice.manager.FileDownloadManager
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.rajat.pdfviewer.PdfViewerActivity
import org.d3ifcool.dissajobrecruiter.R
import org.d3ifcool.dissajobrecruiter.databinding.ActivityApplicantMediaBinding
import org.d3ifcool.dissajobrecruiter.ui.viewmodel.ViewModelFactory
import org.d3ifcool.dissajobrecruiter.vo.Status
import java.io.File


class ApplicantMediaActivity : AppCompatActivity(), LoadPdfCallback, OnMediaItemClickListener,
    SendMediaDataCallback, View.OnClickListener {

    companion object {
        const val APPLICANT_ID = "applicant_id"

        //Permission code
        private const val PERMISSION_CODE = 1001
    }

    private lateinit var activityApplicantMediaBinding: ActivityApplicantMediaBinding

    private lateinit var mediaViewModel: MediaViewModel

    private lateinit var mediaAdapter: MediaAdapter

    private lateinit var applicantId: String

    private val storageRef = Firebase.storage.reference

    private val mediaUrlList = ArrayList<Uri>()
    private val mediaNameList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityApplicantMediaBinding = ActivityApplicantMediaBinding.inflate(layoutInflater)
        setContentView(activityApplicantMediaBinding.root)

        activityApplicantMediaBinding.toolbar.title = resources.getString(R.string.txt_media)
        setSupportActionBar(activityApplicantMediaBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        if (intent == null) {
            finish()
            return
        }

//        val config = PRDownloaderConfig.newBuilder()
//            .setDatabaseEnabled(true)
//            .build()
//        PRDownloader.initialize(applicationContext, config)

        applicantId = intent.getStringExtra(APPLICANT_ID).toString()
        val factory = ViewModelFactory.getInstance(this)
        mediaViewModel = ViewModelProvider(this, factory)[MediaViewModel::class.java]
        mediaAdapter = MediaAdapter(this, this, this)
        mediaViewModel.getApplicantMedia(applicantId)
            .observe(this) { mediaFiles ->
                if (mediaFiles != null) {
                    when (mediaFiles.status) {
                        Status.LOADING -> showLoading(true)
                        Status.SUCCESS -> {
                            showLoading(false)
                            if (mediaFiles.data?.isNotEmpty() == true) {
                                mediaAdapter.submitList(mediaFiles.data)
                                mediaAdapter.notifyDataSetChanged()
                                activityApplicantMediaBinding.tvNoData.visibility = View.GONE
                            } else {
                                activityApplicantMediaBinding.tvNoData.visibility = View.VISIBLE
                            }
                        }
                        Status.ERROR -> {
                            showLoading(false)
                            showErrorToast()
                        }
                    }
                }
            }
        with(activityApplicantMediaBinding.rvMedia) {
            recycledViewPool.setMaxRecycledViews(0, 0)
            layoutManager = LinearLayoutManager(this@ApplicantMediaActivity)
            setHasFixedSize(true)
            addItemDecoration(
                DividerItemDecoration(
                    this@ApplicantMediaActivity,
                    DividerItemDecoration.VERTICAL
                )
            )
            adapter = mediaAdapter
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            activityApplicantMediaBinding.progressBar.visibility = View.VISIBLE
        } else {
            activityApplicantMediaBinding.progressBar.visibility = View.GONE
        }
    }

    private fun showErrorToast() {
        Toast.makeText(
            this,
            resources.getString(R.string.txt_error_occurred),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onLoadPdfData(mediaId: String, callback: LoadPdfCallback) {
        mediaViewModel.getMediaById(mediaId).observe(this) { file ->
            if (file != null) {
                callback.onPdfDataReceived(file)
            } else {
                showErrorToast()
            }
        }
    }

    override fun onPdfDataReceived(mediaFile: ByteArray) {
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onItemClick(fileId: String, mediaName: String) {
        storageRef.child("applicant/media/$fileId").downloadUrl.addOnSuccessListener {
            startActivity(
                PdfViewerActivity.launchPdfFromUrl(
                    this,
                    it.toString(),
                    mediaName,
                    "/dissajob/media",
                    enableDownload = true
                )
            )
        }.addOnFailureListener {
            showErrorToast()
        }
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
            ) {
                downloadAllApplicantMedia()
            } else {
                //permission denied
                val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                //show popup to request runtime permission
                requestPermissions(permissions, PERMISSION_CODE)
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            downloadAllApplicantMedia()
        }
    }

    private fun downloadAllApplicantMedia() {
        val folder =
            File(Environment.getExternalStorageDirectory().toString() + "/" + "Dissajob Recruiter")
        if (!folder.exists()) {
            folder.mkdirs()
        }
        for (i in 0 until mediaUrlList.size) {
//            PRDownloader.download(
//                mediaUrlList[i].toString(),
//                "/Download/dissajob_recruiter/media",
//                mediaNameList[i]
//            ).build().setOnStartOrResumeListener {
//
//            }.setOnPauseListener {
//
//            }.setOnCancelListener {
//
//            }.setOnProgressListener {
//
//            }.start(object : OnDownloadListener {
//                override fun onDownloadComplete() {
//                }
//
//                override fun onError(error: Error?) {
//                }
//
//            })

            FileDownloadManager.initDownload(
                this,
                mediaUrlList[i].toString(),
                folder.absolutePath,
                "${mediaNameList[i]}.pdf"
            )
        }
    }

    override fun sendMediaData(fileId: String, mediaName: String) {
        storageRef.child("applicant/media/$fileId").downloadUrl.addOnSuccessListener {
            mediaUrlList.add(it)
            mediaNameList.add(mediaName)

            if (mediaUrlList.size == mediaAdapter.itemCount && mediaUrlList.size == mediaNameList.size) {
                activityApplicantMediaBinding.btnDownloadAllApplicantMedia.visibility = View.VISIBLE
                activityApplicantMediaBinding.btnDownloadAllApplicantMedia.setOnClickListener(this)
            } else {
                activityApplicantMediaBinding.btnDownloadAllApplicantMedia.visibility = View.GONE
            }
        }.addOnFailureListener {
            showErrorToast()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnDownloadAllApplicantMedia -> checkPermission()
        }
    }
}