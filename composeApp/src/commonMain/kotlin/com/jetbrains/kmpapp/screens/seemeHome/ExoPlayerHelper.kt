package com.jetbrains.kmpapp.screens.seemeHome

import android.content.Context
import android.net.Uri
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import java.io.File

@OptIn(UnstableApi::class)
fun initNormalVideoPlayer(context: Context, videoUrl: String): ExoPlayer {
    val exoPlayer = ExoPlayer.Builder(context,DefaultRenderersFactory(context).setEnableDecoderFallback(true))
        .build().apply {
        repeatMode = Player.REPEAT_MODE_ALL
    }

//    val userAgent = Util.getUserAgent(context, "TAPAPP")
    val dataSourceFactory = DefaultDataSource.Factory(context)

    // Cache setup (consider extracting this to a separate function for better organization)
    val cache = SimpleCacheManager.getInstance(context)
    val cacheDataSourceFactory = CacheDataSource.Factory()
        .setCache(cache)
        .setUpstreamDataSourceFactory(dataSourceFactory)

    val videoUri = Uri.parse(videoUrl)
    val mediaItem = MediaItem.fromUri(videoUri)
    val videoSource: MediaSource = ProgressiveMediaSource.Factory(cacheDataSourceFactory)
        .createMediaSource(mediaItem)

    exoPlayer.setMediaSource(videoSource)
    exoPlayer.prepare()

    return exoPlayer
}
@OptIn(UnstableApi::class)
object SimpleCacheManager {
    private var simpleCache: SimpleCache? = null
    fun getInstance(context: Context): SimpleCache {
        if (simpleCache == null) {
            val cacheSizeInBytes = 100 * 1024 * 1024L // 100 MB
            simpleCache = SimpleCache(File(context.cacheDir, "media"), LeastRecentlyUsedCacheEvictor(cacheSizeInBytes),null,null,false,false)
        }
        return simpleCache!!
    }
}
@OptIn(UnstableApi::class)
fun initRecyclerViewVideoPlayer(context: Context, videoUrl: String) : ExoPlayer{
    val trackSelector = DefaultTrackSelector(context).apply {
        setParameters(
            buildUponParameters()
                .setMaxVideoSize(360, 360)
                .setMaxVideoFrameRate(24)
//                .setExceedVideoConstraintsIfNecessary(false)
                .setForceHighestSupportedBitrate(false) // Don't force the highest bitrate
        )

    }

    val exoPlayer = ExoPlayer.Builder(context,DefaultRenderersFactory(context).setEnableDecoderFallback(true))
        .setTrackSelector(trackSelector)
        .build() .apply {
        volume = 0f
        repeatMode = Player.REPEAT_MODE_ALL
    }
//    val userAgent = Util.getUserAgent(context, "TAPAPP")
    val dataSourceFactory = DefaultDataSource.Factory(context)

    // Cache setup (consider extracting this to a separate function for better organization)
    val cache = SimpleCacheManager.getInstance(context)
    val cacheDataSourceFactory = CacheDataSource.Factory()
        .setCache(cache)
        .setUpstreamDataSourceFactory(dataSourceFactory)

    val videoUri = Uri.parse(videoUrl)
    val mediaItem = MediaItem.fromUri(videoUri)
    val videoSource: MediaSource = ProgressiveMediaSource.Factory(cacheDataSourceFactory)
        .createMediaSource(mediaItem)

    exoPlayer.setMediaSource(videoSource)
    exoPlayer.prepare()

   return exoPlayer
}