package ca.doophie.swipelauncher.widgets

import android.content.Context
import android.graphics.Point
import android.graphics.PointF
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import ca.doophie.swipelauncher.data.App
import ca.doophie.swipelauncher.data.launch
import ca.doophie.swipelauncher.ui.theme.PondBlue
import ca.doophie.swipelauncher.utils.dpToPx
import ca.doophie.swipelauncher.utils.pxToDp
import kotlinx.coroutines.delay
import java.time.LocalTime
import kotlin.math.cos
import kotlin.math.sin


@Composable
fun ClockWidget(context: Context,
                location: Point,
                size: Int,
                appToOpen: App?,
                hoursHand: Int,
                minutesHands: Int,
                secondsHands: Int) {

    // Track time for clock/sun/sky
    var time = LocalTime.now()
    var seconds by remember { mutableIntStateOf(time.second) }
    var minutes by remember { mutableIntStateOf(time.minute) }
    var hours by remember { mutableIntStateOf(time.hour) }

    val center        = size/2f
    val radius        = size/2.75f
    val hoursRadius   = size/3.5f
    val secondsRadius = size/2f

    val angleDegreeMinutesSeconds = (360f / 60f)
    val angleDegreeHours = (360f / 12f)

    fun secondsAngle(seconds: Int) = (((angleDegreeMinutesSeconds * seconds) - 90f) * (Math.PI / 180f)).toFloat()
    fun minutesAngle(minutes: Int) = (((angleDegreeMinutesSeconds * minutes) - 90f) * (Math.PI / 180f)).toFloat()
    fun hoursAngle(hours: Int)     = (((angleDegreeHours * hours) - 90f) * (Math.PI / 180f)).toFloat()

    fun secondsOffset(secondsAngle: Float): Offset = Offset(
        (secondsRadius - ((secondsRadius * .05f) / 2) ) * cos(secondsAngle) + center,
        (secondsRadius - ((secondsRadius * .05f) / 2) ) * sin(secondsAngle) + center
    )

    fun minuteOffset(minutesAngle: Float) = Offset(
        (radius - ((radius * .05f) / 2) ) * cos(minutesAngle) + center,
        (radius - ((radius * .05f) / 2) ) * sin(minutesAngle) + center
    )

    fun hoursOffset(hoursAngle: Float) = Offset(
        (hoursRadius - ((hoursRadius * .05f) / 2) ) * cos(hoursAngle) + center,
        (hoursRadius - ((hoursRadius * .05f) / 2) ) * sin(hoursAngle) + center
    )

    val clockHoursHand by animateIntOffsetAsState(
        targetValue = if (hours > hours - 1) {
            IntOffset(
                hoursOffset(hoursAngle(hours)).x.dp.dpToPx().toInt(),
                hoursOffset(hoursAngle(hours)).y.dp.dpToPx().toInt())
        } else {
            IntOffset(
                hoursOffset(hoursAngle(hours - 1)).x.dp.dpToPx().toInt(),
                hoursOffset(hoursAngle(hours - 1)).y.dp.dpToPx().toInt())
        },
        label = "offset",
        animationSpec = tween(1000 * 60 * 60)
    )

    val clockMinutesHand by animateIntOffsetAsState(
        targetValue = if (minutes > minutes - 1) {
            IntOffset(
                minuteOffset(minutesAngle(minutes)).x.dp.dpToPx().toInt(),
                minuteOffset(minutesAngle(minutes)).y.dp.dpToPx().toInt())
        } else {
            IntOffset(
                minuteOffset(minutesAngle(minutes - 1)).x.dp.dpToPx().toInt(),
                minuteOffset(minutesAngle(minutes - 1)).y.dp.dpToPx().toInt())
        },
        label = "offset",
        animationSpec = tween(1000 * 60)
    )

    val clockSecondsHand by animateIntOffsetAsState(
        targetValue = if (seconds > seconds - 1) {
            IntOffset(
                secondsOffset(secondsAngle(seconds)).x.dp.dpToPx().toInt(),
                secondsOffset(secondsAngle(seconds)).y.dp.dpToPx().toInt())
        } else {
            IntOffset(
                secondsOffset(secondsAngle(seconds - 1)).x.dp.dpToPx().toInt(),
                secondsOffset(secondsAngle(seconds - 1)).y.dp.dpToPx().toInt())
        },
        label = "offset",
        animationSpec = tween(1400)
    )

    LaunchedEffect(context) {
        while (true) {
            time = LocalTime.now()

            seconds = time.second
            minutes = time.minute
            hours = time.hour

            delay(1000)
        }
    }

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

            Image(painter = painterResource(id = hoursHand),
                contentDescription = "",
                modifier = Modifier
                    .offset { clockHoursHand }
                    .rotate(hours * angleDegreeHours))

            Image(painter = painterResource(id = minutesHands),
                contentDescription = "",
                modifier = Modifier
                    .offset { clockMinutesHand }
                    .rotate(minutes * angleDegreeMinutesSeconds))

            Image(painter = painterResource(id = secondsHands),
                contentDescription = "",
                modifier = Modifier
                    //.offset(secondsOffset.x.dp, secondsOffset.y.dp)
                    .offset { clockSecondsHand }
                    .rotate(seconds * angleDegreeMinutesSeconds))
        }
    }
}