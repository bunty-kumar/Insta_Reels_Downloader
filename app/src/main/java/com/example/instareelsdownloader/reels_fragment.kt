package com.example.instareelsdownloader

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.net.Uri.parse
import android.os.Environment
import android.widget.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.apache.commons.lang3.StringUtils
import com.android.volley.VolleyError
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.fragment_igtv_fragment.*
import kotlinx.android.synthetic.main.fragment_reels_fragment.*


class reels_fragment : Fragment() {

    var URL:String = "NULL"
    lateinit var reelsVideoView:VideoView
    lateinit var etReels:EditText
    lateinit var btnGetReel:Button
    lateinit var btnDownload:Button
    private lateinit var mediaController:MediaController
    var reelUrl:String = "1"
    private lateinit var uri2:Uri


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var v = inflater.inflate(R.layout.fragment_reels_fragment, container, false)
        // Inflate the layout for this fragment

        reelsVideoView = v.findViewById(R.id.videoviewReels)
        etReels = v.findViewById(R.id.etReels)
        btnGetReel = v.findViewById(R.id.btnReels)
        btnDownload = v.findViewById(R.id.downloadBtnReels)
        mediaController = MediaController(context)
        mediaController.setAnchorView(reelsVideoView)


        btnGetReel.setOnClickListener {

            URL = etReels.text.toString().trim()
            if (etReels.equals("NULL")){
                Toast.makeText(context, "please enter url", Toast.LENGTH_SHORT).show()
            }
            else{
                progressbarReels.visibility = View.VISIBLE
                var result2:String = StringUtils.substringBefore(URL,"/?")
                URL = result2+ "/?__a=1"
                processdata()

            }
        }

        btnDownload.setOnClickListener {

            if (!reelUrl.equals("1")){

                var manager = context?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                val request = DownloadManager.Request(uri2)
                request.setTitle("Download")
                request.setDescription("..........")
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE)
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                request.allowScanningByMediaScanner()
                manager?.enqueue(request)
/*
                var request= DownloadManager.Request(uri2)
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE)
                request.setTitle("Download")
                request.setDescription("..........")
                request.allowScanningByMediaScanner()
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES,
                       "insta saver" )
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

        val stringRequest = StringRequest( URL,
            Response.Listener<String> { response ->

                progressbarReels.visibility = View.GONE
                val gsonBuilder = GsonBuilder()
                val gson = gsonBuilder.create()

                val mainUrl = gson.fromJson(response, MainUrl::class.java)
                reelUrl = mainUrl.graphql.shortcode_media.video_url

                uri2 = parse(reelUrl)
                reelsVideoView.setMediaController(mediaController)
                reelsVideoView.setVideoURI(uri2)
                reelsVideoView.requestFocus()
                reelsVideoView.start()

            },

            Response.ErrorListener {
                progressbarReels.visibility = View.GONE
                Toast.makeText(context, "error in fetching data", Toast.LENGTH_SHORT).show()
            })
// Add the request to the RequestQueue.
        queue.add(stringRequest)

    }


}


