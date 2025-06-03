package ca.doophie.swipelauncher.views

import android.content.Context
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ca.doophie.swipelauncher.data.App
import ca.doophie.swipelauncher.R
import ca.doophie.swipelauncher.data.launch
import ca.doophie.swipelauncher.utils.getBitmapFromDrawable

@Composable
fun AllApplicationsList(context: Context,
                        applications: List<App>,
                        intOffsets: List<State<IntOffset>>,
                        modifier: Modifier = Modifier) {



    LazyColumn(modifier = modifier) {
       items(applications.count()) { index ->
            ApplicationItem(
                context = context,
                application = applications.sortedBy { it.name }[index],
                modifier = Modifier.offset { intOffsets[index].value }
            )
        }
    }
}

@Composable
private fun ApplicationItem(context: Context, application: App, modifier: Modifier = Modifier) {
    Box(modifier = modifier.offset()) {
        Image(painter = painterResource(id = R.drawable.scenic_background_plane),
            contentDescription = "",
            modifier = Modifier
                .clickable { application.launch(context) })

        Row(verticalAlignment = Alignment.Bottom) {
            Spacer(modifier = Modifier.weight(1.5f))

            Text(color = Color.Black,
                fontSize = 12.sp,
                text = application.name,
                modifier = Modifier.weight(2f).offset(0.dp, 26.dp))

            Image(
                getBitmapFromDrawable(application.icon!!), "Icon",
                modifier = Modifier
                    .width(24.dp)
                    .height(24.dp)
                    .offset(0.dp, 26.dp)
                    .weight(1.3f))
        }
    }

}
