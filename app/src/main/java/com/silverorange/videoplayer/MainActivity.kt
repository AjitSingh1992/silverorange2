package com.silverorange.videoplayer

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {

    lateinit var btnPrevious: RelativeLayout
    lateinit var btnNext: RelativeLayout
    lateinit var btnPlayPause: RelativeLayout
    lateinit var imagePlayPause: ImageView
    lateinit var loadingPB: ProgressBar
    lateinit var videoView: VideoView
    lateinit var txtName: TextView
    lateinit var txtTitle: TextView
    lateinit var txtDescription: TextView
    lateinit var videoList: ArrayList<VideoDataModel>
    var currentPosition: Int = 0
    var videoPaused: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // supportActionBar?.hide();

        // on below line we are initializing our variable with their ids.
        loadingPB = findViewById(R.id.idLoadingPB)
        txtName = findViewById(R.id.txtName)
        txtTitle = findViewById(R.id.txtTitle)
        txtDescription = findViewById(R.id.txtDescription)
        videoView = findViewById(R.id.videoView)
        imagePlayPause = findViewById(R.id.imagePlayPause)
        btnPrevious = findViewById(R.id.btnPrevious)
        btnNext = findViewById(R.id.btnNext)
        btnPlayPause = findViewById(R.id.btnPlayPause)

        // on below line we are creating a method
        // to get data from api using retrofit.

        btnNext.setOnClickListener {
            if (currentPosition < videoList.size && currentPosition!=videoList.size-1) {
                currentPosition++;
                setTitleOfVideo(currentPosition)
            }else {
                Toast.makeText(this@MainActivity, "List ended..", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        btnPrevious.setOnClickListener {
            if (currentPosition > 0) {
                currentPosition--;
                setTitleOfVideo(currentPosition)
            }else{
                Toast.makeText(this@MainActivity, "This is first video..", Toast.LENGTH_SHORT)
                        .show()
            }

        }
        btnPlayPause.setOnClickListener {
            if (videoPaused) {
                videoView.start()
                imagePlayPause.setImageResource((R.drawable.pause))
                videoPaused = false;
            } else {
                videoView.pause()
                imagePlayPause.setImageResource(R.drawable.play)
                videoPaused = true;
            }

        }
        videoView.setOnCompletionListener { // not playVideo
            setTitleOfVideo(currentPosition)
        }
        getVideos()

    }

    fun setTitleOfVideo(currentPosition:Int){

        videoView.setVideoPath(videoList.get(currentPosition).fullURL)
        videoView.start()

        txtTitle.setText(videoList.get(currentPosition).title)
        txtDescription.setText(videoList.get(currentPosition).description)
        txtName.setText(videoList.get(currentPosition).author.name)
        videoPaused = false;



    }
    private fun getVideos() {
        // on below line we are creating a retrofit
        // builder and passing our base url
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:4000/")
            // on below line we are calling add
            // Converter factory as Gson converter factory.
            // at last we are building our retrofit builder.
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // below line is to create an instance for our retrofit api class.
        val retrofitAPI = retrofit.create(RetrofitAPI::class.java)

        // on below line we are calling a method to get all the courses from API.
        val call: Call<ArrayList<VideoDataModel>?>? = retrofitAPI.getVideos()

        // on below line we are calling method to enqueue and calling
        // all the data from array list.
        call!!.enqueue(object : Callback<ArrayList<VideoDataModel>?> {
            override fun onResponse(
                call: Call<ArrayList<VideoDataModel>?>,
                response: Response<ArrayList<VideoDataModel>?>
            ) {
                if (response.isSuccessful) {
                    loadingPB.visibility = View.GONE
                    videoList = response.body()!!

                    if (videoList.size > 0) {
                        txtName.setText(videoList.get(0).author.name)
                        txtTitle.setText(videoList.get(0).title)
                        txtDescription.setText(videoList.get(0).description)
                        videoView.setVideoPath(videoList.get(0).fullURL)

                        currentPosition = 0;
                        videoPaused = true


                    }
                }

            }

            override fun onFailure(call: Call<ArrayList<VideoDataModel>?>, t: Throwable) {
                // displaying an error message in toast
                Toast.makeText(this@MainActivity, "Fail to get the data..", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }
}