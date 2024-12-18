package com.example.musiqal.datamodels.youtubeApiSearchForPlayList

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.musiqal.database.YoutubePlaylistTypeConverter
import com.google.gson.annotations.SerializedName

@Entity(tableName = "youtubeRandomPlayList")
@TypeConverters(YoutubePlaylistTypeConverter::class)
data class YoutubeApiSearchForPlayListRequest(
    @Ignore
    val etag: String,
    @SerializedName("items")
    var items: List<Item>,
    @Ignore
    val kind: String,
    @Ignore
    val nextPageToken: String,
    @Ignore
    val pageInfo: PageInfo,
    @Ignore
    val regionCode: String,
    @PrimaryKey(autoGenerate = false) var id:Int
)
{
    constructor():this("",
        listOf(Item(
            "0", Id("", "","",""), "", Snippet(
        "", "", "", "", "", "",
        Thumbnails(
            Default(0, "", 0),
            High(0, "", 0), Medium
                (0, "", 0)
        ), "")
    )),"","", PageInfo(0,0),"",0){}
}