package com.D121211035.SearchImage.ui.components

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.D121211035.SearchImage.network.model.Hit

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun MainContent(viewModel: MainViewModel = hiltViewModel()) {
    val query: MutableState<String> = remember { mutableStateOf("") }
    val result = viewModel.list.value

    // Load random images when the composable is first launched
    LaunchedEffect(true) {
        viewModel.getImageList("") // Pass an empty query to fetch random images
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(8.dp)) {

            OutlinedTextField(
                value = query.value,
                onValueChange = {
                    query.value = it
                    viewModel.getImageList(query.value)
                },
                enabled = true,
                singleLine = true,
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null)
                },
                label = { Text(text = "Search here...") },
                modifier = Modifier.fillMaxWidth()

            )

            if (result.isLoading) {
                Log.d("TAG", "MainContent: in the loading")
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }

            if (result.error.isNotBlank()) {
                Log.d("TAG", "MainContent: ${result.error}")
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = viewModel.list.value.error
                    )
                }
            }

            if (result.data.isNotEmpty()) {
                LazyVerticalGrid(cells = GridCells.Fixed(2)) {
                    items(result.data.size) { index ->
                        MainContentItem(result.data[index]) { clickedItem ->
                            // Handle item click here
                        }
                    }
                }
            }


        }
    }


}

@Composable
fun MainContentItem(hit: Hit, onItemClick: (Hit) -> Unit) {
    // State to track whether the item is clicked
    val showDialog = remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(200.dp)
            .clickable { onItemClick(hit) } // Handle item click
    ) {
        Image(
            painter = rememberImagePainter(data = hit.largeImageURL),
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .clickable { showDialog.value = true } // Open full-screen image on click
        )
    }

    // Show full-screen image in a Dialog when showDialog is true
    if (showDialog.value) {
        Dialog(onDismissRequest = { showDialog.value = false }) {
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = rememberImagePainter(data = hit.largeImageURL),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

