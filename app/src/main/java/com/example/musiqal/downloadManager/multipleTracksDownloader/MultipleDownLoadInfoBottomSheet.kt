package com.example.musiqal.downloadManager.multipleTracksDownloader


import android.os.Bundle
import android.util.Log

import com.google.android.material.bottomsheet.BottomSheetDialogFragment

import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.musiqal.R
import com.example.musiqal.databinding.DownloadInfoBottomSheatLayoutBinding
import com.example.musiqal.downloadManager.util.OnMultipleExtractedItemClickListener
import com.example.musiqal.downloadManager.util.OnMultipleSelectedTrackClickedListener
import com.example.musiqal.downloadManager.util.OnSelectedTrackClickedListener
import com.example.musiqal.youtubeAudioVideoExtractor.model.YouTubeDlExtractorResultDataItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class MultipleDownLoadInfoBottomSheet(private val onSelectedTrackClickedListener: OnMultipleSelectedTrackClickedListener) :
    BottomSheetDialogFragment(), OnMultipleExtractedItemClickListener {
    private lateinit var currentSelectedItem: List<YouTubeDlExtractorResultDataItem>
    lateinit var binding: DownloadInfoBottomSheatLayoutBinding
    lateinit var txtDuration: TextView
    lateinit var imgSong: ImageView
    lateinit var txtSongName: TextView

    private val TAG = "DownLoadInfoBottomSheet"
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DownloadInfoBottomSheatLayoutBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getDeviceInfoAndDownloadPath()
        arguments?.let {
            val downLoadItemsInfo = it.getString(ARG_DOWNLOAD_ITEMS_INFO)!!
            val imageUrl = it.getString(IMAGE_URL)!!
            Log.d(TAG, "onViewCreated: " + downLoadItemsInfo)
            val itemsList: MutableList<List<YouTubeDlExtractorResultDataItem>> =
                convertItemsToListOfYouTubeDlExtractorDataItem(downLoadItemsInfo)
            initializeAndSetupSongInfoViewsWithData(itemsList,imageUrl)
            setupDataIntoRecyclerView(itemsList)


        }

        binding.downloadInfoBottomSheetBtnStartDownload.setOnClickListener {
            if (::currentSelectedItem.isInitialized) {
                notifyCurrentSelectedTrack(currentSelectedItem)
            }
            else
            {

            }
        }

    }

    private fun notifyCurrentSelectedTrack(currentSelectedItem: List<YouTubeDlExtractorResultDataItem>) {
        onSelectedTrackClickedListener.onTrackSeelcted(currentSelectedItem)
        this.dismiss()

    }


    private fun getDeviceInfoAndDownloadPath() {


    }

    private fun initializeAndSetupSongInfoViewsWithData(
        itemsList: MutableList<List<YouTubeDlExtractorResultDataItem>>,
        imageUrl: String
    ) {
        if (itemsList.size > 0) {
            val youTubeDlExtractorResultDataItem = itemsList.get(itemsList.size - 1)
            val downloadInfoBottomSheetTrackInfoConstraint =
                binding.downloadInfoBottomSheetTrackInfo
            initializeSongInfoViews(downloadInfoBottomSheetTrackInfoConstraint.rootView!!,imageUrl)
            txtDuration.text =""
//                YouTubeDurationConverter.getTimeInStringFormated(youTubeDlExtractorResultDataItem.videoDuration!!)
            txtSongName.text = "youTubeDlExtractorResultDataItem.videoTitle"

        }

    }

    private fun initializeSongInfoViews(
        downloadInfoBottomSheetTrackInfoConstraint: View,
        imageUrl: String
    ) {
        txtDuration =
            downloadInfoBottomSheetTrackInfoConstraint.findViewById<TextView>(R.id.currentDownloadInfo_txtDuration)
        txtSongName =
            downloadInfoBottomSheetTrackInfoConstraint.findViewById<TextView>(R.id.currentDownloadInfo_txtSongName)
        imgSong =
            downloadInfoBottomSheetTrackInfoConstraint.findViewById<ImageView>(R.id.currentDownloadInfo_ImageView)

        Glide.with(requireContext())
            .load(imageUrl)
            .into(imgSong)
    }

    lateinit var downloadSheetAdapter: MultipleDownLoadSheetAdapter
    private fun setupDataIntoRecyclerView(itemsList: MutableList<List<YouTubeDlExtractorResultDataItem>>) {

        val layoutManager = LinearLayoutManager(requireContext())
        downloadSheetAdapter = MultipleDownLoadSheetAdapter(requireContext(), this)
        downloadSheetAdapter.setItemsList(itemsList)
        binding.downloadInfoBottomSheetRecyclerView.layoutManager = layoutManager
        binding.downloadInfoBottomSheetRecyclerView.adapter = downloadSheetAdapter

    }

    private fun convertItemsToListOfYouTubeDlExtractorDataItem(downLoadItemsInfo: String): MutableList<List<YouTubeDlExtractorResultDataItem>> {

        val type = object : TypeToken<MutableList<List<YouTubeDlExtractorResultDataItem>>>() {}.type!!
        return Gson().fromJson(downLoadItemsInfo, type)

    }

    companion object {
        val ARG_DOWNLOAD_ITEMS_INFO = "items"
        val IMAGE_URL = "image_url"

        @JvmStatic
        fun newInstance(
            param1: String,
            onSelectedTrackClickedListener: OnMultipleSelectedTrackClickedListener,
            imageUrl: String
        ) =
            MultipleDownLoadInfoBottomSheet(onSelectedTrackClickedListener).apply {
                arguments = Bundle().apply {
                    putString(ARG_DOWNLOAD_ITEMS_INFO, param1)
                    putString(IMAGE_URL, imageUrl)
                }
            }
    }



    override fun onClick(
        youTubeDlExtractorResultDataItem: List<YouTubeDlExtractorResultDataItem>,
        adapterPosition: Int
    ) {
        downloadSheetAdapter.notifyDataSetChanged()
        this.currentSelectedItem = youTubeDlExtractorResultDataItem
    }

}