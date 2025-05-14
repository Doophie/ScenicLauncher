package ca.doophie.swipelauncher.widgets

import android.content.Context
import android.graphics.Point
import android.graphics.PointF
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ca.doophie.swipelauncher.data.App
import ca.doophie.swipelauncher.R
import ca.doophie.swipelauncher.data.launch
import ca.doophie.swipelauncher.views.pxToDp
import ca.doophie.swipelauncher.ui.theme.PondBlue
import kotlin.math.cos
import kotlin.math.sin


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