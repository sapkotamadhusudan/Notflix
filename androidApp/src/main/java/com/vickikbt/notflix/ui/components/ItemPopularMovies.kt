package com.vickikbt.notflix.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.fade
import com.google.accompanist.placeholder.material.placeholder
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarStyle
import com.gowtham.ratingbar.StepSize
import com.vickikbt.notflix.ui.theme.Golden
import com.vickikbt.notflix.ui.theme.Gray
import com.vickikbt.notflix.util.PaletteGenerator
import com.vickikbt.notflix.util.loadImage
import com.vickikbt.shared.domain.models.Movie
import com.vickikbt.shared.utils.capitalizeEachWord
import com.vickikbt.shared.utils.getRating
import com.vickikbt.shared.utils.getReleaseDate

@ExperimentalCoilApi
@ExperimentalMaterialApi
@Composable
fun ItemPopularMovies(
    modifier: Modifier = Modifier,
    movie: Movie,
    onClickItem: (Movie) -> Unit
) {
    val defaultDominantTextColor = MaterialTheme.colors.onSurface
    var dominantColor by remember { mutableStateOf(Color.Transparent) }
    var dominantTextColor by remember { mutableStateOf(defaultDominantTextColor) }
    var dominantSubTextColor by remember { mutableStateOf(defaultDominantTextColor) }

    val painter = rememberImagePainter(
        data = movie.backdropPath?.loadImage(),
        builder = { crossfade(true) }
    )

    if (painter.state is ImagePainter.State.Success) {
        LaunchedEffect(key1 = painter) {
            val imageDrawable = painter.imageLoader.execute(painter.request).drawable
            imageDrawable?.let {
                PaletteGenerator.generateImagePalette(imageDrawable = it) { color ->
                    dominantColor = Color(color.rgb)
                    dominantTextColor = Color(color.titleTextColor)
                    dominantSubTextColor = Color(color.bodyTextColor)
                }
            }
        }
    }

    Card(
        modifier = modifier
            .clickable { onClickItem(movie) }
            .placeholder(
                visible = false,
                color = Color.Black,
                highlight = PlaceholderHighlight.fade()
            ),
        elevation = 8.dp,
        shape = RoundedCornerShape(4.dp)
    ) {
        Box(modifier = modifier) {

            //region Movie Cover
            Image(
                modifier = Modifier
                    .fillMaxSize()
                    .placeholder(
                        visible = false,
                        color = Color.Black,
                        highlight = PlaceholderHighlight.fade()
                    )
                    .align(Alignment.Center),
                alignment = Alignment.Center,
                contentScale = ContentScale.Crop,
                painter = painter,
                contentDescription = null
            )
            //endregion

            //region Fading Edge
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color.Transparent,
                                dominantColor
                            )
                        )
                    )
                    .align(Alignment.BottomCenter)
            )
            //endregion

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .align(Alignment.BottomCenter)
            ) {
                //region Movie Title
                Text(
                    text = movie.title ?: "Unknown movie",
                    fontSize = 18.sp,
                    maxLines = 2,
                    style = MaterialTheme.typography.h6,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Start,
                    color = dominantTextColor
                )
                //endregion

                //region Movie Rating
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    RatingBar(
                        modifier = Modifier,
                        value = movie.voteAverage?.getRating()?.toFloat() ?: 0f,
                        numStars = 5,
                        size = 15.dp,
                        stepSize = StepSize.HALF,
                        isIndicator = true,
                        ratingBarStyle = RatingBarStyle.Normal,
                        activeColor = Golden,
                        inactiveColor = Gray,
                        onValueChange = {},
                        onRatingChanged = {}
                    )

                    movie.releaseDate?.let {
                        Divider(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .width(1.dp)
                                .height(13.dp),
                            color = dominantSubTextColor,
                        )

                        Text(
                            modifier = Modifier,
                            text = movie.releaseDate.getReleaseDate()?.capitalizeEachWord()!!,
                            fontSize = 14.sp,
                            maxLines = 1,
                            style = MaterialTheme.typography.h4,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Start,
                            color = dominantSubTextColor
                        )
                    }
                }
                //endregion
            }
        }
    }
}
