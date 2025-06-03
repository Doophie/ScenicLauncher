package ca.doophie.swipelauncher.widgets

import android.content.Context
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ca.doophie.swipelauncher.data.App
import ca.doophie.swipelauncher.data.ApplicationFetcher
import ca.doophie.swipelauncher.getBitmapFromDrawable
import ca.doophie.swipelauncher.data.launch
import ca.doophie.swipelauncher.utils.UniversalTextClearer
import ca.doophie.swipelauncher.utils.getVibrantColor
import ca.doophie.swipelauncher.views.TempAutoFocusingText

@Composable
fun AllApplicationsSearchWidget(context: Context,
                        fetcher: ApplicationFetcher,
                        listItemBackground: Int,
                        listItemAltBackground: Int,
                        modifier: Modifier = Modifier) {
    // For updating the time in the background continuously
    LaunchedEffect(context) {
        // set text clear
        UniversalTextClearer.callback = {
            fetcher.clearText()
        }
    }

    @Composable
    fun ApplicationListItem(context: Context, application: App, modifier: Modifier = Modifier) {
        var planeColor by remember { mutableStateOf(Red) }

        LaunchedEffect(Unit) {
            application.icon?.getVibrantColor {
                planeColor = it
            }
        }

        Box(modifier = modifier.offset()) {
            Image(
                painter = painterResource(id = listItemBackground),
                contentDescription = "",
                modifier = Modifier
                    .clickable { application.launch(context) })

            Icon(
                painter = painterResource(id = listItemAltBackground),
                contentDescription = "",
                tint = planeColor)

            Row(verticalAlignment = Alignment.Bottom) {
                Spacer(modifier = Modifier.weight(1.5f))

                Text(
                    color = Color.Black,
                    fontSize = 12.sp,
                    text = application.name,
                    modifier = Modifier.weight(2f).offset(0.dp, 26.dp)
                )

                Image(
                    getBitmapFromDrawable(application.icon!!), "Icon",
                    modifier = Modifier
                        .width(24.dp)
                        .height(24.dp)
                        .offset(0.dp, 26.dp)
                        .weight(1.3f)
                )
            }
        }
    }

    val applications = fetcher.filteredApplicationsList.collectAsState()
    val searchText = fetcher.searchText.collectAsState()

    Column(modifier = Modifier
        .padding(0.dp, 42.dp, 0.dp, 0.dp)) {
        LazyColumn(modifier = modifier
            .weight(3F)) {
            items(applications.value.count()) { index ->
                ApplicationListItem(
                    context = context,
                    application = applications.value.sortedBy { it.name }[index]
                )
            }
        }

        TempAutoFocusingText(
            value = searchText.value,
            onValueChange = fetcher::onSearchTextChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        )

        Spacer(modifier = Modifier.weight(2f))
    }
}
