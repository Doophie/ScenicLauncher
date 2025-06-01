package ca.doophie.swipelauncher.widgets

import android.content.Context
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import ca.doophie.swipelauncher.data.ApplicationFetcher
import ca.doophie.swipelauncher.data.DayPeriod
import ca.doophie.swipelauncher.data.getDayPeriod
import ca.doophie.swipelauncher.utils.keyboardAsState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun WidgetLayout(context: Context, widgets: List<WidgetBuilder>,
                 fetcher: ApplicationFetcher,
                 listItemBackground: Int,
                 backgroundImageId: Int,
                 backgroundColors: List<Color>
) {


    var dayPeriod by remember { mutableStateOf(getDayPeriod()) }

    // Variables for hiding / showing all apps list
    var showSearchResults by remember { mutableStateOf(false) }
    var hideKeyboard by remember { mutableStateOf(false) }
    val isKeyboardOpen by keyboardAsState()

    val hideBackgroundOffset by animateIntOffsetAsState(
        targetValue = if (isKeyboardOpen || showSearchResults) {
            IntOffset(0, 2500)
        } else {
            IntOffset(0, 200)
        },
        label = "offset",
        animationSpec = tween(1000)
    )

    val showSearchOffset by animateIntOffsetAsState(
        targetValue = if (isKeyboardOpen || showSearchResults) {
            IntOffset(0, 0)
        } else {
            IntOffset(1080, -2500)
        },
        label = "offset",
        animationSpec = tween(1000)
    )

    LaunchedEffect(context) {
        while (true) {
            dayPeriod = getDayPeriod()

            delay(1000 * 60 * 60)
        }
    }

    Box(
        Modifier
        .background(
            color =
                when (dayPeriod) {
                    DayPeriod.MORNING -> backgroundColors[0]//SkyMorning
                    DayPeriod.AFTERNOON -> backgroundColors[1]//SkyBlue
                    DayPeriod.EVENING -> backgroundColors[2]//SkyEvening
                    DayPeriod.NIGHT -> backgroundColors[3]//SkyNight
                }
        )
        .pointerInput(Unit) {
            detectDragGestures { change, dragAmount ->
                change.consume()

                val (x, y) = dragAmount
                when {
                    x > 0 -> { /* right */
                    }

                    x < 0 -> { /* left */
                    }
                }
                when {
                    y < 0 -> { /* down */
                        showSearchResults = false
                        hideKeyboard = true
                    }

                    y > 0 -> {
                        if (change.position.y > 1200) {
                            showSearchResults = true

                            // we set this to false after keyboard is open so that dismissing keyboard
                            // will also dismiss the search view
                            CoroutineScope(IO).launch {
                                delay(2000)
                                showSearchResults = false
                            }
                        }
                    }
                }
            }
        }) {

        // showing the main layout filled with widgets
        Box(Modifier.offset { hideBackgroundOffset }) {
            // background
            Image(painter = painterResource(id = backgroundImageId), contentDescription = "00b1dd")

            widgets.forEach {
                it.Build(context, fetcher)
            }
        }

        // showing the search layout
        Box(Modifier.offset { showSearchOffset }) {
            if (isKeyboardOpen || showSearchResults) {
                AllApplicationsSearchWidget(
                    context,
                    fetcher = fetcher,
                    listItemBackground = listItemBackground
                )
            }
        }

    }
}