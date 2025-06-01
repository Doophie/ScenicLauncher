package ca.doophie.swipelauncher.views

import android.content.Context
import android.graphics.Point
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import ca.doophie.swipelauncher.data.ApplicationFetcher
import ca.doophie.swipelauncher.R
import ca.doophie.swipelauncher.data.DayPeriod
import ca.doophie.swipelauncher.data.getDayPeriod
import ca.doophie.swipelauncher.data.hasNotifications
import ca.doophie.swipelauncher.ui.theme.SkyBlue
import ca.doophie.swipelauncher.ui.theme.SkyEvening
import ca.doophie.swipelauncher.ui.theme.SkyMorning
import ca.doophie.swipelauncher.ui.theme.SkyNight
import ca.doophie.swipelauncher.utils.UniversalTextClearer
import ca.doophie.swipelauncher.utils.keyboardAsState
import ca.doophie.swipelauncher.widgets.BoomBoxWidget
import ca.doophie.swipelauncher.widgets.ClockWidget
import ca.doophie.swipelauncher.widgets.BasicWidget
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.util.Locale

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ScenicView(context: Context,
               fetcher: ApplicationFetcher) {
    val isRunning = true

    // Track time for clock/sun/sky
    val time = LocalTime.now()
    var seconds by remember { mutableIntStateOf(time.second) }
    var minutes by remember { mutableIntStateOf(time.minute) }
    var hours by remember { mutableIntStateOf(time.hour) }

    var dayPeriod by remember { mutableStateOf(getDayPeriod()) }

    // Get all apps and search ready
    val items = fetcher.filteredApplicationsList.collectAsState()
    val searchText = fetcher.searchText.collectAsState()

    // Variables for hiding / showing all apps list
    var showSearchResults by remember { mutableStateOf(false) }
    var hideKeyboard by remember { mutableStateOf(false) }
    val isKeyboardOpen by keyboardAsState()

    // offset used to animate the all app list in / out
    val applicationListIntOffsets = List(items.value.size) { index ->
        animateIntOffsetAsState(
            targetValue = if (showSearchResults || isKeyboardOpen) {
                IntOffset.Zero
            } else {
                IntOffset(1080, 0)
            },
            label = "offset",
            animationSpec = tween(1000 + (index * 100)))
    }

    val hideScenicBackgroundOffset by animateIntOffsetAsState(
        targetValue = if (showSearchResults || isKeyboardOpen) {
            IntOffset(0, 2500)
        } else {
            IntOffset(0, 200)
        },
        label = "offset",
        animationSpec = tween(1000)
    )

    // For updating the time in the background continuously
    LaunchedEffect(context) {
        // set text clear
        UniversalTextClearer.callback = {
            fetcher.clearText()
        }

        while (isRunning) {
            val time = LocalTime.now()

            dayPeriod = getDayPeriod()

            seconds = time.second
            minutes = time.minute
            hours = time.hour

            delay(1000)
        }
    }

    Box(
        Modifier
            .background(
                color =
                when (dayPeriod) {
                    DayPeriod.MORNING -> SkyMorning
                    DayPeriod.AFTERNOON -> SkyBlue
                    DayPeriod.EVENING -> SkyEvening
                    DayPeriod.NIGHT -> SkyNight
                }
            )
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()

                    val (x, y) = dragAmount
                    when {
                        x > 0 -> { /* right */ }
                        x < 0 -> { /* left */ }
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
        Box(Modifier.offset { hideScenicBackgroundOffset }) {
            if (hideKeyboard) {
                LocalSoftwareKeyboardController.current?.hide()
                hideKeyboard = false
            }

            // background
            Image(painter = painterResource(id = R.drawable.scenic_background_grass_pond), contentDescription = "00b1dd")

            // cloud 1
            BasicWidget(context = context,
                imageId = R.drawable.scenic_background_cloud_1,
                location = Point(15, 670),
                appToOpen = fetcher.getApplication("firefox"))

            // cloud 2
            BasicWidget(context = context,
                imageId = R.drawable.scenic_background_cloud_2,
                location = Point(357, -200),
                appToOpen = fetcher.getApplication("weather"))

            // sun / moon
            BasicWidget(context = context,
                imageId = if (dayPeriod == DayPeriod.EVENING || dayPeriod == DayPeriod.NIGHT) R.drawable.scenic_background_moon else R.drawable.scenic_background_sun,
                location = Point(650, 200),
                appToOpen = fetcher.getApplication("reddit"))

            // boombox
            BoomBoxWidget(context = context,
                fetcher = fetcher,
                noMedia = R.drawable.scenic_background_radio_display_empty,
                playMedia = R.drawable.scenic_background_radio_display_playing,
                pauseMedia = R.drawable.scenic_background_radio_display,
                nextMedia = R.drawable.scenic_background_speaker,
                playPauseMedia = R.drawable.scenic_background_speaker,
                offButton = R.drawable.scenic_background_off_button,
                location = Point(10, 1080))

            // red bird - gmail
            val gmailApp = fetcher.getApplication("gmail")
            BasicWidget(context = context,
                imageId = if (gmailApp?.hasNotifications() == true) R.drawable.scenic_background_red_bird_full else R.drawable.scenic_background_red_bird_empty,
                location = Point(130, 400),
                appToOpen = gmailApp)

            // green bird - whatsapp
            val whatsApp = fetcher.getApplication("whatsapp")
            BasicWidget(context = context,
                imageId = if (whatsApp?.hasNotifications() == true) R.drawable.scenic_background_green_bird_full else R.drawable.scenic_background_green_bird_empty,
                location = Point(520, 420),
                appToOpen = whatsApp)

            // red bird - gmail
            val slackApp = fetcher.getApplication("slack")
            BasicWidget(context = context,
                imageId = if (slackApp?.hasNotifications() == true) R.drawable.scenic_background_poop_bird_full else R.drawable.scenic_background_poop_bird_empty,
                location = Point(130, 200),
                appToOpen = slackApp)

            // blue bird - messenger
            val messengerApp = fetcher.getApplication("messenger")
            BasicWidget(context = context,
                imageId = if (messengerApp?.hasNotifications() == true) R.drawable.scenic_background_blue_bird_full else R.drawable.scenic_background_blue_bird_empty,
                location = Point(260, 490),
                appToOpen = fetcher.allApplicationsList.firstOrNull { it.name.lowercase().contains("messenger") })

            // pink bird - insta
            val instaApp = fetcher.getApplication("instagram")
            BasicWidget(context = context,
                imageId = if (instaApp?.hasNotifications() == true) R.drawable.scenic_background_pink_bird_full else R.drawable.scenic_background_pink_bird_empty,
                location = Point(320, 320),
                appToOpen = fetcher.allApplicationsList.firstOrNull { it.name.lowercase().contains("instagram") })

            // chess
            BasicWidget(context = context,
                imageId = R.drawable.scenic_background_chess,
                location = Point(0, 1600),
                appToOpen = fetcher.allApplicationsList.firstOrNull { it.name.lowercase().contains("lichess") })

            // phone
            BasicWidget(context = context,
                imageId = R.drawable.scenic_background_phone_booth,
                location = Point(800, 1000),
                appToOpen = fetcher.allApplicationsList.firstOrNull { it.name.lowercase().contains("phone") })

            // clockPond
            ClockWidget(
                context,
                location = Point(530, 1550),
                size = 135,
                secondsHands = R.drawable.scenic_background_seconds_fly,
                minutesHands = R.drawable.scenic_background_minute_duck,
                hoursHand = R.drawable.scenic_background_hour_duck,
                appToOpen = fetcher.allApplicationsList.firstOrNull { it.name.toLowerCase(Locale.getDefault()).contains("clock") })
        }

        // showing all apps search
        if (showSearchResults || isKeyboardOpen) {
            Column {
                AllApplicationsList(
                    context = context,
                    applications = items.value,
                    intOffsets = applicationListIntOffsets,
                    Modifier
                        .weight(3f)
                        .padding(0.dp, 24.dp, 0.dp, 0.dp)
                )

                TempAutoFocusingText(
                    value = searchText.value,
                    onValueChange = fetcher::onSearchTextChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                )

                Spacer(modifier = Modifier.weight(2f))
            }
        } else {
            UniversalTextClearer.callback.invoke()
        }
    }
}

@Composable
fun TempAutoFocusingText(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusRequester = remember { FocusRequester() }

    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.focusRequester(focusRequester)
    )

    LaunchedEffect(Unit) {
        delay(300)

        focusRequester.requestFocus()
    }
}










