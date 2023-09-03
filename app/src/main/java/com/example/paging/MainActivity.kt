package com.example.paging

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Cyan
import androidx.compose.ui.graphics.Color.Companion.Magenta
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.paging.domain.Beer
import com.example.paging.ui.theme.PagingTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PagingTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    BeerScreen(beers = viewModel.beerPagingFlow.collectAsLazyPagingItems())
                }
            }
        }
    }

    @Composable
    private fun BeerScreen(beers: LazyPagingItems<Beer>) {

        val context = LocalContext.current

        LaunchedEffect(key1 = beers.loadState) {
            if (beers.loadState.refresh is LoadState.Error) {
                Toast.makeText(
                    context,
                    "Error: " + (beers.loadState.refresh as LoadState.Error).error.message.orEmpty(),
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            if (beers.loadState.refresh is LoadState.Loading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(items = beers.itemSnapshotList.items) { beer ->
                        BeersItem(beer = beer, modifier = Modifier.fillMaxWidth())
                    }

                    item {
                        if (beers.loadState.append is LoadState.Loading) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun BeersItem(beer: Beer, modifier: Modifier) {

        var descriptionOverFlow by remember { mutableStateOf(false) }

        ElevatedCard {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                AsyncImage(
                    modifier = Modifier
                        .weight(1F)
                        .clip(MaterialTheme.shapes.small)
                        .height(170.dp),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(beer.imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = beer.name,
                    contentScale = ContentScale.FillBounds
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(
                    modifier = Modifier.weight(3F)
                ) {
                    Text(
                        text = beer.name,
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.displaySmall.copy(
                            fontWeight = FontWeight.Bold,
                            brush = Brush.linearGradient(
                                colors = listOf(Cyan, Blue.copy(alpha = 0.6F), Magenta)
                            )
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = beer.tagline,
                        modifier = Modifier
                            .fillMaxWidth()
                            .alpha(0.5F),
                        style = MaterialTheme.typography.bodyLarge.copy(fontStyle = FontStyle.Italic),
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = beer.description,
                        modifier = Modifier
                            .animateContentSize()
                            .fillMaxWidth()
                            .alpha(0.7F)
                            .clip(MaterialTheme.shapes.small)
                            .clickable { descriptionOverFlow = descriptionOverFlow.not() },
                        maxLines = if (descriptionOverFlow) Int.MAX_VALUE else 2,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "First Brewed in ${beer.firstBrewed}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .alpha(0.8F),
                        style = MaterialTheme.typography.labelSmall,
                        textAlign = TextAlign.End
                    )
                }
            }
        }
    }

    @Preview
    @Composable
    private fun BeerItemPreview() {
        PagingTheme {
            BeersItem(
                beer = Beer(
                    id = 1,
                    name = "Beer",
                    tagline = "This is Test :)",
                    firstBrewed = "2023/09/03",
                    description = "Test Description Should Be More Than 2-3 Lines...\nLine 1... \nLine 2... \nLine 3...",
                    imageUrl = null
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}