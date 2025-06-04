package ca.doophie.swipelauncher.views

import android.content.Context
import android.graphics.Point
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import ca.doophie.swipelauncher.data.ApplicationFetcher
import ca.doophie.swipelauncher.R
import ca.doophie.swipelauncher.ui.theme.PondBlue
import ca.doophie.swipelauncher.ui.theme.SkyBlue
import ca.doophie.swipelauncher.ui.theme.SkyEvening
import ca.doophie.swipelauncher.ui.theme.SkyMorning
import ca.doophie.swipelauncher.ui.theme.SkyNight
import ca.doophie.swipelauncher.widgets.WidgetBuilder
import ca.doophie.swipelauncher.widgets.WidgetLayout


@Composable
fun ScenicLayout(context: Context, fetcher: ApplicationFetcher) {
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
                ),

                WidgetBuilder.basic(
                    imageId = R.drawable.scenic_background_notebook,
                    location = Point(920, 1330),
                    applicationName = "notes"
                ).rotate(-20f),

                WidgetBuilder.container(
                    imageId = R.drawable.scenic_background_briefcase_closed,
                    altImageId = R.drawable.scenic_background_briefcase_open,
                    items = listOf(
                        WidgetBuilder.basic(
                            imageId = R.drawable.scenic_background_sdk_test_page,
                            location = Point(230, 1710),
                            applicationName = "sdk test"
                        ).rotate(-35f),
                        WidgetBuilder.basic(
                            imageId = R.drawable.scenic_background_eid_me_page,
                            location = Point(270, 1810),
                            applicationName = "eid-me"
                        ),
                        WidgetBuilder.basic(
                            imageId = R.drawable.scenic_background_scan_page,
                            location = Point(230, 1890),
                            applicationName = "card scan"
                        ).rotate(32f),
                    ),
                    location = Point(80, 1810)
                ).rotate(-15f),
            ),
            fetcher,
            listItemBackground = R.drawable.scenic_background_plane,
            listItemAltBackground = R.drawable.scenic_background_plane_blank_slate,
            backgroundImageId = R.drawable.scenic_background_grass_pond,
            backgroundColors = listOf(SkyMorning, SkyBlue, SkyEvening, SkyNight)
        )
    }
}










