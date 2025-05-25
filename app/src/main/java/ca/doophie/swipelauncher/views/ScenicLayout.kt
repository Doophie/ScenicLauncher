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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import ca.doophie.swipelauncher.ui.theme.PondBlue
import ca.doophie.swipelauncher.ui.theme.SkyBlue
import ca.doophie.swipelauncher.ui.theme.SkyEvening
import ca.doophie.swipelauncher.ui.theme.SkyMorning
import ca.doophie.swipelauncher.ui.theme.SkyNight
import ca.doophie.swipelauncher.utils.UniversalTextClearer
import ca.doophie.swipelauncher.utils.keyboardAsState
import ca.doophie.swipelauncher.widgets.BoomBoxWidget
import ca.doophie.swipelauncher.widgets.ClockWidget
import ca.doophie.swipelauncher.widgets.BasicWidget
import ca.doophie.swipelauncher.widgets.WidgetBuilder
import ca.doophie.swipelauncher.widgets.WidgetLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.util.Locale


@Composable
fun ScenicLayout(context: Context,
               fetcher: ApplicationFetcher) {
    Box {
        WidgetLayout(
            context,
            listOf(
                // cloud 1
                WidgetBuilder.basic(
                    imageId = R.drawable.scenic_background_cloud_1,
                    location = Point(15, 670),
                    applicationName = "firefox"
                ),

                // cloud 2
                WidgetBuilder.basic(
                    imageId = R.drawable.scenic_background_cloud_2,
                    location = Point(357, -200),
                    applicationName = "weather"
                ),

                // sun / moon
                WidgetBuilder.basic(
                    imageId = R.drawable.scenic_background_moon,
                    altImageId = R.drawable.scenic_background_sun,
                    altImageType = WidgetBuilder.AltImageType.DAY_PERIOD,
                    location = Point(650, 200),
                    applicationName = "reddit"
                ),

                // boombox
                WidgetBuilder.boomBox(
                    noMedia = R.drawable.scenic_background_radio_display_empty,
                    playMedia = R.drawable.scenic_background_radio_display_playing,
                    pauseMedia = R.drawable.scenic_background_radio_display,
                    nextMedia = R.drawable.scenic_background_speaker,
                    playPauseMedia = R.drawable.scenic_background_speaker,
                    offButton = R.drawable.scenic_background_off_button,
                    location = Point(10, 1080)
                ),

                // red bird - gmail
                WidgetBuilder.basic(
                    imageId = R.drawable.scenic_background_red_bird_empty,
                    altImageId = R.drawable.scenic_background_red_bird_full,
                    location = Point(130, 400),
                    applicationName = "gmail"
                ),

                // green bird - whatsapp
                WidgetBuilder.basic(
                    imageId = R.drawable.scenic_background_green_bird_empty,
                    altImageId = R.drawable.scenic_background_green_bird_full,
                    location = Point(520, 420),
                    applicationName = "whatsapp"
                ),

                // yellow bird - gmail
                WidgetBuilder.basic(
                    imageId = R.drawable.scenic_background_poop_bird_empty,
                    altImageId = R.drawable.scenic_background_poop_bird_full,
                    location = Point(130, 200),
                    applicationName = "slack"
                ),

                // blue bird - messenger
                WidgetBuilder.basic(
                    imageId = R.drawable.scenic_background_blue_bird_empty,
                    altImageId = R.drawable.scenic_background_blue_bird_full,
                    location = Point(260, 490),
                    applicationName = "messenger"
                ),

                // pink bird - insta
                WidgetBuilder.basic(
                    imageId = R.drawable.scenic_background_pink_bird_empty,
                    altImageId = R.drawable.scenic_background_pink_bird_full,
                    location = Point(320, 320),
                    applicationName = "instagram"
                ),

                // chess
                WidgetBuilder.basic(
                    imageId = R.drawable.scenic_background_chess,
                    location = Point(0, 1600),
                    applicationName = "lichess"
                ),

                // phone
                WidgetBuilder.basic(
                    imageId = R.drawable.scenic_background_phone_booth,
                    location = Point(800, 1000),
                    applicationName = "phone"
                ),

                // clockPond
                WidgetBuilder.clock(
                    location = Point(530, 1550),
                    size = 135,
                    secondsHands = R.drawable.scenic_background_seconds_fly,
                    minutesHands = R.drawable.scenic_background_minute_duck,
                    hoursHand = R.drawable.scenic_background_hour_duck,
                    background = PondBlue
                )
            ),
            fetcher,
            listItemBackground = R.drawable.scenic_background_plane,
            backgroundImageId = R.drawable.scenic_background_grass_pond,
            backgroundColors = listOf(SkyMorning, SkyBlue, SkyEvening, SkyNight)
        )
    }
}










