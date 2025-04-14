package com.example.compose.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose.R
import com.example.compose.ui.ui.theme.DemoTheme

class TestActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                }
            }
        }
    }
}

@Composable
fun GetImage() {
    val image = painterResource(id = R.drawable.ic_launcher_background)
    Box(modifier = Modifier) {
        Image(
            painter = image,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(400.dp)
                .height(100.dp),
            alpha = 0.5f
        )
    }
}

@Composable
fun GetText(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.title),
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )
        Text(
            text = stringResource(id = R.string.first_paragragh),
            textAlign = TextAlign.Justify,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp)
        )
        Text(
            text = stringResource(id = R.string.second_paragragh),
            textAlign = TextAlign.Justify,
            modifier = Modifier.padding(16.dp)
        )
    }


}

@Preview(showBackground = true)
@Composable
fun ShowPreview() {
    DemoTheme {
        Column(
            verticalArrangement = Arrangement.Center,

            ) {
            GetImage()
            GetText()
        }

    }
}