package com.example.instareelsdownloader

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_igtv_fragment.*
import org.apache.commons.lang3.StringUtils

class igtv_fragment : Fragment() {

    var URL:String = "NULL"
    lateinit var igtvVideoView:VideoView
    lateinit var etIgtv: EditText
    lateinit var btnGetIgtv: Button
    lateinit var btnDownload: Button
    private lateinit var mediaController:MediaController
    var reelUrl:String = "1"
    private lateinit var uri2:Uri


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var v = inflater.inflate(R.layout.fragment_igtv_fragment, container, false)


        igtvVideoView = v.findViewById(R.id.videoviewIgtv)
        etIgtv = v.findViewById(R.id.etIgtv)
        btnGetIgtv = v.findViewById(R.id.btnIgtv)
        btnDownload = v.findViewById(R.id.downloadBtnIgtv)
        mediaController = MediaController(context)
        mediaController.setAnchorView(igtvVideoView)


        btnGetIgtv.setOnClickListener {

            URL = etIgtv.text.toString().trim()
            if (etIgtv.equals("NULL")){
                Toast.makeText(context, "please enter url", Toast.LENGTH_SHORT).show()
            }
            else{
                progressbarIgtv.visibility = View.VISIBLE
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

                /*var request= DownloadManager.Request(uri2)
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE)
                request.setTitle("Download")
                request.setDescription("..........")
                request.allowScanningByMediaScanner()
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                request.setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_PICTURES,
                    "insta saver" )
                val downloadManager = context?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                downloadManager.enqueue(request)*/
                Toast.makeText(context, "Downloaded", Toast.LENGTH_SHORT).show()

            }else{
                Toast.makeText(context, "no video download", Toast.LENGTH_SHORT).show()
            }

        }

        // Inflate the layout for this fragment
        return v
    }

    private fun processdata() {

        val queue = Volley.newRequestQueue(context)

        val stringRequest = StringRequest( URL,
            Response.Listener<String> { response ->


                val gsonBuilder = GsonBuilder()
                val gson = gsonBuilder.create()

                val mainUrl = gson.fromJson(response, MainUrl::class.java)
                reelUrl = mainUrl.graphql.shortcode_media.video_url

                uri2 = Uri.parse(reelUrl)
                progressbarIgtv.visibility = View.GONE
                igtvVideoView.setMediaController(mediaController)
                igtvVideoView.setVideoURI(uri2)
                igtvVideoView.requestFocus()
                igtvVideoView.start()

            },

            Response.ErrorListener {
                progressbarIgtv.visibility = View.GONE
                Toast.makeText(context, "error in fetching data", Toast.LENGTH_SHORT).show()
            })
// Add the request to the RequestQueue.
        queue.add(stringRequest)

    }

}