package com.jetbrains.kmpapp.screens.list

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.jetbrains.kmpapp.domain.model.MuseumObject
import com.jetbrains.kmpapp.screens.EmptyScreenContent
import com.jetbrains.kmpapp.screens.detail.DetailScreen
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

object ListScreen : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val title = "Home"
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
        val navigator = LocalNavigator.currentOrThrow
        val screenModel: ListScreenModel = getScreenModel()

        val objects by screenModel.objects.collectAsState(initial = null)

        var showLoader by remember { mutableStateOf(true) }

        objects?.let { objects ->
            AnimatedContent(objects.isNotEmpty()) { objectsAvailable ->
                if (objectsAvailable) {
                    showLoader = false
                    ObjectGrid(
                        objects = objects,
                        onObjectClick = { objectId ->
                            navigator.parent?.push(DetailScreen(objectId))
                        }
                    )
                } else {
                    if (showLoader) {
                        EmptyScreenContent(Modifier.fillMaxSize())
                    }
                }
            }
        }
    }
}

@Composable
private fun ObjectGrid(
    objects: List<MuseumObject>,
    onObjectClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow() { }
    LazyVerticalGrid(
        columns = GridCells.Adaptive(180.dp),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(objects, key = { it.objectID }) { obj ->
            Box(modifier = Modifier.padding(8.dp)){
                ObjectFrame(
                    obj = obj,
                    onClick = { onObjectClick(obj.objectID) },
                )
            }
        }
    }
}

@Composable
private fun ObjectFrame(
    obj: MuseumObject,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(Modifier, shape = RoundedCornerShape(10.dp), elevation = 4.dp, backgroundColor = Color.White) {
        Column(
            Modifier
                .clickable { onClick() }
        ) {
            KamelImage(
                resource = asyncPainterResource(data = obj.primaryImageSmall),
                contentDescription = obj.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .background(Color.LightGray),
            )

            Spacer(Modifier.height(2.dp))
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    obj.title,
                    style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    obj.artistDisplayName, style = MaterialTheme.typography.body2,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    obj.objectDate, style = MaterialTheme.typography.caption,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }

}
