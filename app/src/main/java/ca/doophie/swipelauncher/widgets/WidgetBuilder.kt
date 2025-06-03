package ca.doophie.swipelauncher.widgets

import android.content.Context
import android.graphics.Point
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ca.doophie.swipelauncher.R
import ca.doophie.swipelauncher.data.App
import ca.doophie.swipelauncher.data.ApplicationFetcher
import ca.doophie.swipelauncher.data.DayPeriod
import ca.doophie.swipelauncher.data.getDayPeriod
import ca.doophie.swipelauncher.data.hasNotifications
import kotlinx.coroutines.delay
import java.util.Locale

class WidgetBuilder {
    enum class Type {
        BASIC,
        CONTAINER,
        BOOM_BOX,
        CLOCK;
    }

    enum class AltImageType {
        CONTAINER,
        NOTIFICATION,
        DAY_PERIOD;
    }

    private var type: Type = Type.BASIC
    private var altImageType: AltImageType = AltImageType.NOTIFICATION

    private var rotation: Float = 0f

    private var imageId: Int = 0
    private var altImageId: Int? = null

    private var location: Point = Point(0, 0)
    private var onClick: (()->Unit) = {}
    private var applicationName: String = ""

    private var noMedia: Int = 0
    private var playMedia: Int = 0
    private var pauseMedia: Int = 0
    private var offButton: Int = 0
    private var nextMedia: Int = 0
    private var playPauseMedia: Int = 0

    private var items: List<WidgetBuilder> = emptyList()

    private var secondsHands = R.drawable.scenic_background_seconds_fly
    private var minutesHands = R.drawable.scenic_background_minute_duck
    private var hoursHand = R.drawable.scenic_background_hour_duck
    private var backgroundColor = Color.Blue
    private var size: Int = 100

    companion object {
        fun clock(
            location: Point,
            size: Int,
            secondsHands: Int,
            minutesHands: Int,
            hoursHand: Int,
            background: Color
        ): WidgetBuilder {
            val widgetBuilder = WidgetBuilder()

            widgetBuilder.type = Type.CLOCK

            widgetBuilder.location = location
            widgetBuilder.size = size
            widgetBuilder.secondsHands = secondsHands
            widgetBuilder.minutesHands = minutesHands
            widgetBuilder.hoursHand = hoursHand
            widgetBuilder.backgroundColor = background

            return widgetBuilder
        }

        fun basic(
            imageId: Int,
            altImageId: Int? = null,
            altImageType: AltImageType = AltImageType.NOTIFICATION,
            location: Point,
            applicationName: String,
            onClick: (() -> Unit) = {}
        ): WidgetBuilder {
            val widgetBuilder = WidgetBuilder()

            widgetBuilder.imageId = imageId
            widgetBuilder.altImageId = altImageId
            widgetBuilder.location = location
            widgetBuilder.onClick = onClick
            widgetBuilder.applicationName = applicationName
            widgetBuilder.altImageType = altImageType

            return widgetBuilder
        }

        fun container(
            imageId: Int,
            altImageId: Int? = null,
            items: List<WidgetBuilder>,
            location: Point
        ): WidgetBuilder {
            val widgetBuilder = WidgetBuilder()

            widgetBuilder.items = items
            widgetBuilder.altImageType = AltImageType.CONTAINER
            widgetBuilder.type = Type.CONTAINER
            widgetBuilder.imageId = imageId
            widgetBuilder.altImageId = altImageId
            widgetBuilder.location = location

            return widgetBuilder
        }

        fun boomBox(
            noMedia: Int,
            playMedia: Int,
            pauseMedia: Int,
            offButton: Int,
            nextMedia: Int,
            playPauseMedia: Int,
            location: Point
        ): WidgetBuilder {
            val widgetBuilder = WidgetBuilder()

            widgetBuilder.type = Type.BOOM_BOX

            widgetBuilder.noMedia = noMedia
            widgetBuilder.playMedia = playMedia
            widgetBuilder.pauseMedia = pauseMedia
            widgetBuilder.offButton = offButton
            widgetBuilder.nextMedia = nextMedia
            widgetBuilder.playPauseMedia = playPauseMedia
            widgetBuilder.location = location

            return widgetBuilder
        }
    }

    fun rotate(rotation: Float): WidgetBuilder {
        this.rotation = rotation

        return this
    }

    @Composable
    fun Build(context: Context, fetcher: ApplicationFetcher) {
        when (type) {
            Type.BASIC -> BuildBasic(context, fetcher)
            Type.BOOM_BOX -> BuildBoomBox(context, fetcher)
            Type.CLOCK -> BuildClock(context, fetcher)
            Type.CONTAINER -> BuildContainer(context, fetcher)
        }
    }

    @Composable
    private fun BuildBasic(context: Context, fetcher: ApplicationFetcher) {
        val application = fetcher.getApplication(applicationName)

        val altImageType = altImageType

        if (application != null) {
            if (altImageType == AltImageType.NOTIFICATION) {
                var hasNotifications by remember { mutableStateOf(application.hasNotifications()) }

                BasicWidget(context = context,
                    imageId = if (hasNotifications) altImageId ?: imageId else imageId,
                    location = location,
                    rotation = rotation,
                    appToOpen = application,
                    onClick = onClick)

                LaunchedEffect(this) {
                    fetcher.watchNotifications(application) {
                        hasNotifications = it
                    }
                }
            } else {
                // refresh every minute
                var dayPeriod by remember { mutableStateOf(getDayPeriod()) }

                BasicWidget(context = context,
                    imageId = if ((dayPeriod == DayPeriod.AFTERNOON || dayPeriod == DayPeriod.MORNING)) altImageId ?: imageId else imageId,
                    location = location,
                    rotation = rotation,
                    appToOpen = application,
                    onClick = onClick)

                LaunchedEffect(this) {
                    while (true) {
                        dayPeriod = getDayPeriod()

                        delay(60000)
                    }
                }
            }
        } else {
            BasicWidget(context = context,
                imageId = imageId,
                location = location,
                rotation = rotation,
                onClick = onClick)
        }
    }

    @Composable
    private fun BuildBoomBox(context: Context, fetcher: ApplicationFetcher) {
        BoomBoxWidget(context = context,
            fetcher = fetcher,
            noMedia = noMedia,
            playMedia = playMedia,
            pauseMedia = pauseMedia,
            offButton = offButton,
            nextMedia = nextMedia,
            playPauseMedia = playPauseMedia,
            location = location)
    }

    @Composable fun BuildClock(context: Context, fetcher: ApplicationFetcher) {
        ClockWidget(
            context,
            location = location,
            size = size,
            secondsHands = secondsHands,
            minutesHands = minutesHands,
            hoursHand = hoursHand,
            appToOpen = fetcher.allApplicationsList.firstOrNull { it.name.toLowerCase(Locale.getDefault()).contains("clock") })
    }

    @Composable
    private fun BuildContainer(context: Context, fetcher: ApplicationFetcher) {
        var isOpen by remember { mutableStateOf(false) }

        Box {
            BasicWidget(context = context,
                imageId = if (isOpen) altImageId ?: imageId else imageId,
                location = location,
                rotation = rotation,
                onClick = {
                    isOpen = !isOpen
                })

            if (isOpen) {
                for (item in items) {
                    val oldOnClick = item.onClick
                    item.onClick = { isOpen = false; oldOnClick.invoke() }
                    item.Build(context, fetcher)
                }
            }
        }
    }


}