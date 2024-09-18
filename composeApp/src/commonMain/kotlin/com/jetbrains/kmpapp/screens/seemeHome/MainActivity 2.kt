package com.jetbrains.kmpapp.screens.seemeHome

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.jetbrains.kmpapp.AppContext
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource

object MainActivity : Tab {

    override val options: TabOptions
        @Composable
        get() {
            val title = "Home List"
            val icon = rememberVectorPainter(Icons.Default.ThumbUp)

            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        val scope =  rememberCoroutineScope()
        val homeList = remember { SnapshotStateList<HomeData>() }

        LaunchedEffect(homeList.size == 0) {
            scope.launch {
                val homeRes = getHomeFromJson(this@MainActivity, R.raw.jsonviewer)
                homeList.clear()
                homeRes?.data?.let { homeList.addAll(it) }
                homeRes?.data?.let { homeList.addAll(it) }
                homeRes?.data?.let { homeList.addAll(it) }
                homeRes?.data?.let { homeList.addAll(it) }
                homeList.addAll(homeList)
                homeList.addAll(homeList)
            }
        }
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            val listOfList = homeList.chunked(3)

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(listOfList.size, key = { it }) { pos ->
                        val data = listOfList[pos]
                        data.map { it.gallery_images.data.shuffled() }
                        HomeItem(data, pos) {
//                                    showBottomSheet = true
                            logMsg { "HomeItem  ${it.id}" }
                        }
                    }
                }
            }



    }

    @Composable
    fun HomeItem(list: List<HomeData>, pos: Int, onClick: (HomeData) -> Unit) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            val transform: (HomeData) -> List<DataXX> = {
                it.gallery_images.data.shuffled()
            }

            list.map(transform)
//        logMsg { "HomeItem  $pos  list ${list.size} " }
            if (pos % 2 != 0) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OneRounderImg(120.dp, list[0], onClick)
                    OneRounderImg(120.dp, list[1], onClick)
                }
                OneRounderImg(220.dp, list[2], onClick)
            } else {
                OneRounderImg(220.dp, list[0], onClick)
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OneRounderImg(120.dp, list[1], onClick)
                    OneRounderImg(120.dp, list[2], onClick)
                }
            }
        }

    }

    @Composable
    fun OneRounderImg(size: Dp, data: HomeData, onClick: (HomeData) -> Unit) {
        val context = currentCompositionLocalContext
        key(data.id) {
            ConstraintLayout(Modifier.wrapContentSize()) {
                val (box, text, icons) = createRefs()
                Box(contentAlignment = Alignment.Center, modifier = Modifier
                    .constrainAs(box) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .size(size)
                    .clip(CircleShape)
                    .border(width = 0.1.dp, color = Color.Gray, shape = CircleShape)
                    .clickable { onClick(data) }) {
                    val mediaData = data.gallery_images.data[0]
                    when (mediaData.file_type) {
                        1 -> {
                            ExoPlayerView(size, mediaData.file_name)
                        }

                        3 -> {
                            KamelImage(
                                resource = asyncPainterResource(data = mediaData.file_name),
                                contentScale = ContentScale.Crop,
                                contentDescription = "",
                                modifier = Modifier.size(size)
                            )
                        }

                        else -> {
                            KamelImage(
                                resource = asyncPainterResource(data = mediaData.thumbnail_image_200),
                                contentScale = ContentScale.Crop,
                                contentDescription = "",
                                modifier = Modifier.size(size)

                            )
                        }
                    }
                }


                Row(
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .constrainAs(icons) {
//                top.linkTo(parent.top)
                            bottom.linkTo(box.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }, horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    val list = remember {
                        listOf(
                            data.mutual_serious_relation == 1,
                            data.mutual_dating == 1,
                            data.mutual_hookup == 1
                        )
                    }
                    val iconSize = 27.dp
                    var offsetLeft by remember { mutableStateOf(6.dp) }
                    var offsetCenter by remember { mutableStateOf(12.dp) }
                    var offsetRight by remember { mutableStateOf(6.dp) }
                    LaunchedEffect(Unit) {
                        list.also { list ->
                            if (list.any { !it }) {
                                offsetLeft = 12.dp
                                offsetCenter = 12.dp
                                offsetRight = 12.dp
                            }
                        }
                    }
                    if (data.mutual_serious_relation == 1) {
                        Box(
                            Modifier
                                .offset(y = (offsetLeft))
                                .size(iconSize)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_icon_serious),
                                contentDescription = null,
                                contentScale = ContentScale.FillBounds,
                                modifier = Modifier
                                    .size(iconSize)
                                    .align(Alignment.Center)
                            )
                            val mPercentageValue = data.serious_relation_percent
                            if (mPercentageValue != 0) {
                                val target = when {
                                    mPercentageValue == 100 -> 1.7f // 100 was not fitting properly
                                    mPercentageValue >= 85 -> 1.8f // Adjust as needed
                                    mPercentageValue in 70..84 -> 1.6f
                                    mPercentageValue >= 55 -> 1f
                                    else -> 1.4f
                                }
//                                val scaleAnimation = rememberInfiniteTransition(label = "scaleAnimation")
//                                val scaleFactor by scaleAnimation.animateFloat(
//                                    initialValue = 1f,
//                                    targetValue = target, // Adjust scale factor as needed
//                                    animationSpec = infiniteRepeatable(
//                                        animation = keyframes {
//                                            durationMillis = 150 // Total animation duration
////                                            0.0f at 50 with FastOutSlowInEasing // Initial scale
//                                            target at 150 with LinearEasing // Scale up
//                                            1.0f at 300 with LinearEasing // Final scale LinearEasing // Scale back down
//                                        }
//                                    ), label = "infiniteRepeatable"
//                                )
                                var scale by remember { mutableFloatStateOf(1f) }
                                var animationFinished by remember { mutableStateOf(false) }

                                // Animation for scaling up
                                val scaleUpAnimation = remember { Animatable(initialValue = 1f) }

                                // Animation for scaling down
                                val scaleDownAnimation = remember {
                                    Animatable(initialValue = targetScale)
                                }

                                // Trigger the scale up animation when the composable enters or targetScale changes
                                LaunchedEffect(key1 = targetScale) {
                                    scaleUpAnimation.animateTo(
                                        targetValue = targetScale,
                                        animationSpec = tween(
                                            durationMillis = 150,
                                            easing = FastOutSlowInEasing
                                        )
                                    )
                                    animationFinished = true // Mark the first animation as finished
                                }

                                // Trigger the scale down animation when the scale up animation finishes
                                LaunchedEffect(key1 = animationFinished) {
                                    if (animationFinished) {
                                        scaleDownAnimation.animateTo(
                                            targetValue = initialScale,
                                            animationSpec = tween(
                                                durationMillis = 150
                                            )
                                        )
                                        // After scaling down, trigger your percentage animation
                                        animationPercentage(targetPercentage)
                                    }
                                }

                                // Use the appropriate scale value based on the animation progress
                                scale =
                                    if (!animationFinished) scaleUpAnimation.value else scaleDownAnimation.value
                                DrawTextWithBackground(
                                    modifier = Modifier
                                        .align(Alignment.BottomStart)
                                        .padding(bottom = 8.dp),
                                    "${data.serious_relation_percent}%",
                                    9.sp,
                                    Color.White,
                                    Color.Red,
                                    scaleFactor
                                )
                            }
                        }
                    }
                    if (data.mutual_dating == 1) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_icon_dating),
                            contentDescription = null,
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier
                                .offset(y = offsetCenter)
                                .size(iconSize)
                        )
                    }
                    if (data.mutual_hookup == 1) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_icon_hookup),
                            contentScale = ContentScale.FillBounds,
                            contentDescription = null,
                            modifier = Modifier
                                .offset(y = offsetRight)
                                .size(iconSize)
                        )
                    }
                }

                Text(text = data.personal_informations.data.display_name + "-" + data.personal_informations.data.age,
                    color = Color.Black,
                    fontSize = 10.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .constrainAs(text) {
                            top.linkTo(box.bottom)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .width(size)
                        .wrapContentHeight())
            }
        }
    }

    @Composable
    fun DrawTextWithBackground(
        modifier: Modifier,
        text: String,
        fontSize: TextUnit,
        backgroundColor: Color,
        textColor: Color,
        scale: Float = 1f
    ) {
        Canvas(
            modifier = modifier
                .wrapContentSize()
                .scale(scale)
        ) {
            drawTextWithBackground(text, fontSize, backgroundColor, textColor)
        }
    }

    private fun DrawScope.drawTextWithBackground(
        text: String,
        fontSize: TextUnit,
        backgroundColor: Color,
        textColor: Color
    ) {
        val textPaint = Paint().apply {
            color = textColor.toArgb()
            textSize = fontSize.toPx()
            typeface = android.graphics.Typeface.DEFAULT_BOLD
        }
        val borderPaint = android.graphics.Paint().apply {
            color = backgroundColor.toArgb()
            style = android.graphics.Paint.Style.STROKE
            textSize = fontSize.toPx()
            strokeWidth = 2.dp.toPx()
            strokeCap = android.graphics.Paint.Cap.ROUND
        }

        var x =
            size.width / 4 //+ (textPaint.descent() - textPaint.ascent()) / 2 - textPaint.descent()
        val y =
            size.height / 2 + (textPaint.descent() - textPaint.ascent()) / 2 - textPaint.descent()

        text.forEach { char ->
            val charWidth = textPaint.measureText(char.toString())
            drawContext.canvas.nativeCanvas.drawText(char.toString(), x, y, borderPaint)
            drawContext.canvas.nativeCanvas.drawText(char.toString(), x, y, textPaint)
            x += charWidth
        }
    }

    @Composable
    fun ExoPlayerView(size: Dp, url: String) {/*
        logMsg { "ExoPlayerView  $url" }
        // Get the current context
        val context = AppContext.get()

        // Initialize ExoPlayer
        val exoPlayer by remember {
            mutableStateOf<ExoPlayer?>(
                initRecyclerViewVideoPlayer(
                    context, url
                )
            )
        }
        // Manage lifecycle events
        DisposableEffect(Unit) {
            onDispose {
                logMsg { "ExoPlayerView onDispose $url" }
                exoPlayer?.release()
            }
        }

        // Use AndroidView to embed an Android View (PlayerView) into Compose
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    useController = false
                    player = exoPlayer
                    player?.playWhenReady = true
                    player?.volume = 0f
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                }
            }, modifier = Modifier
                .size(size)
                .semantics { }// Set your desired height
        )*/
    }

}

