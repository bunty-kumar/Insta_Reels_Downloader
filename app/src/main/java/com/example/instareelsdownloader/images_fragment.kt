package com.example.instareelsdownloader

import android.app.DownloadManager
import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_igtv_fragment.*
import kotlinx.android.synthetic.main.fragment_images_fragment.*
import org.apache.commons.lang3.StringUtils

class images_fragment : Fragment() {

    var URL:String = "NULL"
    lateinit var ivImages: ImageView
    lateinit var etImages: EditText
    lateinit var btnGetImages: Button
    lateinit var btnDownload: Button
    var imagesUrl:String = "1"
    private lateinit var uri2: Uri

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var v = inflater.inflate(R.layout.fragment_images_fragment, container, false)
        // Inflate the layout for this fragment

        ivImages = v.findViewById(R.id.ivImages)
        etImages = v.findViewById(R.id.etImages)
        btnGetImages = v.findViewById(R.id.btnImages)
        btnDownload = v.findViewById(R.id.downloadBtnImages)

        btnGetImages.setOnClickListener {

            URL = etImages.text.toString().trim()
            if (etImages.equals("NULL")){
                Toast.makeText(context, "please enter url", Toast.LENGTH_SHORT).show()
            }
            else{

                progressbarImages.visibility = View.VISIBLE
                var result2:String = StringUtils.substringBefore(URL,"/?")
                URL = result2+ "/?__a=1"
                processdata()

            }
        }

        btnDownload.setOnClickListener {

            if (!imagesUrl.equals("1")){

                var manager = context?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                val request = DownloadManager.Request(uri2)
                request.setTitle("Download")
                request.setDescription("..........")
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE)
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                request.allowScanningByMediaScanner()
                manager?.enqueue(request)

                /*val request =
                    DownloadManager.Request(uri2)
                request.setTitle("Voyager")
                    .setDescription("File is downloading...")
                    .setDestinationInExternalFilesDir(
                        context,
                        Environment.DIRECTORY_DOWNLOADS, fileName
                    )
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                downLoadId = downloadManager.enqueue(request)*/

               /* var request= DownloadManager.Request(uri2)
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE)
                request.setTitle("instasaver")
                request.setDescription("File is downloading...")
                request.setDestinationInExternalPublicDir(
                    context.toString(),
                        Environment.DIRECTORY_DOWNLOADS
                )
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                val downloadManager = context?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                downloadManager.enqueue(request)*/

                Toast.makeText(context, "Downloaded", Toast.LENGTH_SHORT).show()

            }else{
                Toast.makeText(context, "no video download", Toast.LENGTH_SHORT).show()
            }

        }

        return v
    }

    private fun processdata() {

        val queue = Volley.newRequestQueue(context)

        val stringRequest = StringRequest( URL, Response.Listener<String> { response ->

                val gsonBuilder = GsonBuilder()
                val gson = gsonBuilder.create()

                val mainUrl = gson.fromJson(response, MainUrl::class.java)
                imagesUrl = mainUrl.graphql.shortcode_media.display_url
                uri2 = Uri.parse(imagesUrl)
                context?.let { Glide.with(it).load(uri2).listener(object :
                    RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressbarImages.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressbarImages.visibility = View.GONE
                        return false
                    }

                }).into(ivImages) }

            },

            Response.ErrorListener {
                Toast.makeText(context, "error in fetching data", Toast.LENGTH_SHORT).show()
            })
// Add the request to the RequestQueue.
        queue.add(stringRequest)

    }

}