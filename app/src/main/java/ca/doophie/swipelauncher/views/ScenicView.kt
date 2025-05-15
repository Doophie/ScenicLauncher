package ca.doophie.swipelauncher.views

import android.content.BroadcastReceiver
import android.content.Context
import android.graphics.Point
import android.graphics.PointF
import android.util.Log
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ca.doophie.swipelauncher.data.ApplicationFetcher
import ca.doophie.swipelauncher.R
import ca.doophie.swipelauncher.data.App
import ca.doophie.swipelauncher.data.DayPeriod
import ca.doophie.swipelauncher.data.getDayPeriod
import ca.doophie.swipelauncher.data.hasNotifications
import ca.doophie.swipelauncher.data.launch
import ca.doophie.swipelauncher.ui.theme.PondBlue
import ca.doophie.swipelauncher.ui.theme.SkyBlue
import ca.doophie.swipelauncher.ui.theme.SkyEvening
import ca.doophie.swipelauncher.ui.theme.SkyMorning
import ca.doophie.swipelauncher.ui.theme.SkyNight
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.types.PlayerState
import com.spotify.protocol.types.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.util.Locale
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun Dp.dpToPx() = with(LocalDensity.current) { this@dpToPx.toPx() }

@Composable
fun Int.pxToDp() = with(LocalDensity.current) { this@pxToDp.toDp() }

@Composable
fun ScenicView(context: Context,
               fetcher: ApplicationFetcher) {
    val isRunning = true

    // Track time for clock/sun/sky
    val time = LocalTime.now()
    var seconds by remember { mutableIntStateOf(time.second) }
    var minutes by remember { mutableIntStateOf(time.minute) }
    var hours by remember { mutableIntStateOf(time.hour) }

    // Get all apps and search ready
    val items = fetcher.filteredApplicationsList.collectAsState()
    val searchText = fetcher.searchText.collectAsState()

    // Variables for hiding / showing all apps list
    var showSearchResults by remember { mutableStateOf(false) }
    val isKeyboardOpen by keyboardAsState()

    // offset used to animate the all app list in / out
    val hideAllAppsOffset by animateIntOffsetAsState(
        targetValue = if (showSearchResults || isKeyboardOpen) {
            IntOffset.Zero
        } else {
            IntOffset(1080, 0)
        },
        label = "offset",
        animationSpec = tween(1000)
    )
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
        while (isRunning) {
            val time = LocalTime.now()

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
                when (getDayPeriod()) {
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
                        x > 0 -> { /* right */
                        }

                        x < 0 -> { /* left */
                        }
                    }
                    when {
                        y < 0 -> { /* down */
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
            // background
            Image(painter = painterResource(id = R.drawable.scenic_background_grass_pond), contentDescription = "00b1dd")

            // cloud 1
            ScenicWidget(context = context,
                imageId = R.drawable.scenic_background_cloud_1,
                location = Point(15, 670),
                appToOpen = fetcher.getApplication("firefox"))

            // cloud 2
            ScenicWidget(context = context,
                imageId = R.drawable.scenic_background_cloud_2,
                location = Point(357, -200),
                appToOpen = fetcher.getApplication("weather"))

            // sun / moon
            ScenicWidget(context = context,
                imageId = if (getDayPeriod() == DayPeriod.EVENING || getDayPeriod() == DayPeriod.NIGHT) R.drawable.scenic_background_moon else R.drawable.scenic_background_sun,
                location = Point(600, 470),
                appToOpen = fetcher.getApplication("reddit"))

            // boombox
            BoomBoxWidget(context = context, fetcher = fetcher, location = Point(10, 1080))

            // red bird - gmail
            val gmailApp = fetcher.getApplication("gmail")
            ScenicWidget(context = context,
                imageId = if (gmailApp?.hasNotifications() == true) R.drawable.scenic_background_red_bird_full else R.drawable.scenic_background_red_bird_empty,
                location = Point(130, 400),
                appToOpen = gmailApp)

            // green bird - whatsapp
            val whatsApp = fetcher.getApplication("whatsapp")
            ScenicWidget(context = context,
                imageId = if (whatsApp?.hasNotifications() == true) R.drawable.scenic_background_green_bird_full else R.drawable.scenic_background_green_bird_empty,
                location = Point(520, 420),
                appToOpen = whatsApp)

            // red bird - gmail
            val slackApp = fetcher.getApplication("slack")
            ScenicWidget(context = context,
                imageId = if (slackApp?.hasNotifications() == true) R.drawable.scenic_background_poop_bird_full else R.drawable.scenic_background_poop_bird_empty,
                location = Point(130, 200),
                appToOpen = slackApp)

            // blue bird - messenger
            val messengerApp = fetcher.getApplication("messenger")
            ScenicWidget(context = context,
                imageId = if (messengerApp?.hasNotifications() == true) R.drawable.scenic_background_blue_bird_full else R.drawable.scenic_background_blue_bird_empty,
                location = Point(260, 490),
                appToOpen = fetcher.allApplicationsList.firstOrNull { it.name.lowercase().contains("messenger") })

            // pink bird - insta
            val instaApp = fetcher.getApplication("instagram")
            ScenicWidget(context = context,
                imageId = if (instaApp?.hasNotifications() == true) R.drawable.scenic_background_pink_bird_full else R.drawable.scenic_background_pink_bird_empty,
                location = Point(320, 320),
                appToOpen = fetcher.allApplicationsList.firstOrNull { it.name.lowercase().contains("instagram") })

            // chess
            ScenicWidget(context = context,
                imageId = R.drawable.scenic_background_chess,
                location = Point(0, 1600),
                appToOpen = fetcher.allApplicationsList.firstOrNull { it.name.lowercase().contains("lichess") })

            // phone
            ScenicWidget(context = context,
                imageId = R.drawable.scenic_background_phone_booth,
                location = Point(800, 1000),
                appToOpen = fetcher.allApplicationsList.firstOrNull { it.name.lowercase().contains("phone") })

            // clockPond
            ClockWidget(
                context,
                location = Point(530, 1550),
                size = 135,
                seconds = seconds,
                minutes = minutes,
                hours = hours,
                appToOpen = fetcher.allApplicationsList.firstOrNull { it.name.toLowerCase(Locale.getDefault()).contains("clock") })
        }

        // showing all apps search
        if (showSearchResults || isKeyboardOpen) {
            Column(modifier = Modifier.offset { hideAllAppsOffset }) {
                AllApplicationsList(
                    context = context,
                    applications = items.value,
                    Modifier
                        .weight(3f)
                        .padding(0.dp, 24.dp, 0.dp, 0.dp)
                )

                AutoFocusingText(
                    value = searchText.value,
                    onValueChange = fetcher::onSearchTextChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                )

                Spacer(modifier = Modifier.weight(2f))
            }
        }
    }
}

@Composable
fun AutoFocusingText(
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

@Composable
fun keyboardAsState(): State<Boolean> {
    val isImeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
    return rememberUpdatedState(isImeVisible)
}

@Composable
fun ClockWidget(context: Context,
                location: Point, size: Int,
                appToOpen: App?,
                seconds: Int,
                minutes: Int,
                hours: Int) {

    val center        = size/2f
    val radius        = size/2.75f
    val hoursRadius   = size/3.5f
    val secondsRadius = size/2f

    val angleDegreeMinutesSeconds = (360f / 60f)
    val angleDegreeHours = (360f / 12f)

    val secondsAngle = (((angleDegreeMinutesSeconds * seconds) - 90f) * (Math.PI / 180f)).toFloat()
    val minutesAngle = (((angleDegreeMinutesSeconds * minutes) - 90f) * (Math.PI / 180f)).toFloat()
    val hoursAngle   = (((angleDegreeHours * hours) - 90f) * (Math.PI / 180f)).toFloat()

    val secondsOffset = PointF(
        (secondsRadius - ((secondsRadius * .05f) / 2) ) * cos(secondsAngle) + center,
        (secondsRadius - ((secondsRadius * .05f) / 2) ) * sin(secondsAngle) + center
    )

    val minuteOffset = PointF(
        (radius - ((radius * .05f) / 2) ) * cos(minutesAngle) + center,
        (radius - ((radius * .05f) / 2) ) * sin(minutesAngle) + center
    )

    val hoursOffset = PointF(
        (hoursRadius - ((hoursRadius * .05f) / 2) ) * cos(hoursAngle) + center,
        (hoursRadius - ((hoursRadius * .05f) / 2) ) * sin(hoursAngle) + center
    )

    Box {
        Box(
            Modifier
                .offset(location.x.pxToDp(), location.y.pxToDp())
                .width(size.dp)
                .height(size.dp)
        ) {
            Button(
                modifier = Modifier.size(size.dp),
                colors = ButtonDefaults.buttonColors(PondBlue),
                shape = CircleShape,
                onClick = { appToOpen?.launch(context) }) {
                Text("")
            }

            Image(painter = painterResource(id = R.drawable.scenic_background_hour_duck),
                contentDescription = "",
                modifier = Modifier
                    .offset(hoursOffset.x.dp, hoursOffset.y.dp)
                    .rotate(hours * angleDegreeHours))

            Image(painter = painterResource(id = R.drawable.scenic_background_minute_duck),
                contentDescription = "",
                modifier = Modifier
                    .offset(minuteOffset.x.dp, minuteOffset.y.dp)
                    .rotate(minutes * angleDegreeMinutesSeconds))

            Image(painter = painterResource(id = R.drawable.scenic_background_seconds_fly),
                contentDescription = "",
                modifier = Modifier
                    .offset(secondsOffset.x.dp, secondsOffset.y.dp)
                    .rotate(seconds * angleDegreeMinutesSeconds))
        }
    }
}


@Composable
fun ScenicWidget(context: Context,
                 imageId: Int,
                 location: Point,
                 appToOpen: App? = null,
                 onClick: (()->Unit) = {}) {
    Box {
        Image(painter = painterResource(id = imageId),
            contentDescription = "",
            modifier = Modifier
                .offset(location.x.pxToDp(), location.y.pxToDp())
                .clickable { appToOpen?.launch(context) ?: onClick.invoke() })
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BoomBoxWidget(context: Context,
                  location: Point,
                  fetcher: ApplicationFetcher,
                  modifier: Modifier = Modifier) {
    var showTheBand by remember { mutableStateOf(false) }

    var isPaused by remember { mutableStateOf(true) }
    var currentSong by remember { mutableStateOf("") }
    var remote by remember { mutableStateOf<SpotifyAppRemote?>(null) }

    val clientId = "be41603fadcf4318976522a379adc3ec"
    val redirectUri = "https://doophie.ca/callback"

    val connectionParams = ConnectionParams.Builder(clientId)
        .setRedirectUri(redirectUri)
        .showAuthView(true)
        .build()

    SpotifyAppRemote.connect(context, connectionParams, object : Connector.ConnectionListener {
        override fun onConnected(appRemote: SpotifyAppRemote) {
            remote = appRemote
            Log.d("MainActivity", "Connected! Yay!")

            // Subscribe to PlayerState
            appRemote.playerApi.subscribeToPlayerState().setEventCallback {
                isPaused = it.isPaused

                val track: Track = it.track
                currentSong = (track.name + " : " + track.artist.name).uppercase()

                if (isPaused != true) {
                    showTheBand = true
                }
            }
        }

        override fun onFailure(throwable: Throwable) {
            Log.e("MainActivity", throwable.message, throwable)
            // Something went wrong when attempting to connect! Handle errors here
        }
    })

    Box(modifier = modifier) {

        ScenicWidget(context = context,
            imageId = if (showTheBand) {
                if (isPaused) {
                    R.drawable.scenic_background_radio_display
                } else {
                    R.drawable.scenic_background_radio_display_playing
                }
            } else
                R.drawable.scenic_background_radio_display_empty,
            location = Point(location.x + 130, location.y),
            appToOpen = fetcher.getApplication("spotify"))

        if (showTheBand) {
            ScenicWidget(context = context,
                imageId = R.drawable.scenic_background_speaker,
                location = Point(location.x + 60, location.y + 290),
                onClick = {  if (isPaused) remote?.playerApi?.resume() else remote?.playerApi?.pause() })

            ScenicWidget(context = context,
                imageId = R.drawable.scenic_background_speaker,
                location = Point(location.x + 460, location.y + 290),
                onClick = { remote?.playerApi?.skipNext() })

            ScenicWidget(context = context,
                imageId = R.drawable.scenic_background_off_button,
                location = Point(location.x + 210, location.y + 360),
                onClick = {
                    remote?.playerApi?.pause()
                    showTheBand = false
                })

            Text(text = currentSong,
                fontSize = 10.sp,
                color = Color.Green,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier
                    .offset((location.x + 200).pxToDp(), (location.y + 40).pxToDp())
                    .width(300.pxToDp())
                    .basicMarquee()
            )
        }


    }
}





