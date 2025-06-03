package ca.doophie.swipelauncher.widgets

import android.content.Context
import android.graphics.Point
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import ca.doophie.swipelauncher.data.App
import ca.doophie.swipelauncher.data.launch
import ca.doophie.swipelauncher.data.openSettings
import ca.doophie.swipelauncher.utils.pxToDp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BasicWidget(context: Context,
                imageId: Int,
                location: Point,
                appToOpen: App? = null,
                rotation: Float = 0f,
                onClick: (()->Unit) = {}) {
    Box {
        Image(painter = painterResource(id = imageId),
            contentDescription = "",
            modifier = Modifier
                .offset(location.x.pxToDp(), location.y.pxToDp())
                .rotate(rotation)
                .combinedClickable(
                    onClick = {
                        onClick.invoke()
                        appToOpen?.launch(context)
                    }, onLongClick = {
                        appToOpen?.openSettings(context)
                    }
                ))
    }
}