package ca.doophie.swipelauncher.widgets

import android.content.Context
import android.graphics.Point
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import ca.doophie.swipelauncher.data.ApplicationFetcher
import ca.doophie.swipelauncher.utils.pxToDp
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.types.Track


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BoomBoxWidget(context: Context,
                  location: Point,
                  fetcher: ApplicationFetcher,
                  noMedia: Int,
                  playMedia: Int,
                  pauseMedia: Int,
                  offButton: Int,
                  nextMedia: Int,
                  playPauseMedia: Int,
                  modifier: Modifier = Modifier
) {
    var remoteConnectionAttempts: Int = 0

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

    fun connectToRemote() {
        SpotifyAppRemote.connect(context, connectionParams, object : Connector.ConnectionListener {
            override fun onConnected(appRemote: SpotifyAppRemote) {
                remote = appRemote
                Log.d("BoomBox", "Connected! Yay!")

                remoteConnectionAttempts = 0

                // Subscribe to PlayerState
                appRemote.playerApi.subscribeToPlayerState().setEventCallback {
                    Log.d("BoomBox", "Updated player state: paused = ${it.isPaused}")
                    isPaused = it.isPaused

                    val track: Track = it.track
                    currentSong = (track.name + " : " + track.artist.name).uppercase()

                    if (!isPaused) {
                        showTheBand = true
                    }
                }
            }

            override fun onFailure(throwable: Throwable) {
                Log.e("BoomBox", throwable.message, throwable)

                remoteConnectionAttempts += 1

                if (remoteConnectionAttempts < 5) connectToRemote()
            }
        })
    }

    LaunchedEffect(Unit) {
        connectToRemote()
    }

    Box(modifier = modifier) {
        BasicWidget(context = context,
            imageId = if (showTheBand) {
                if (isPaused) {
                    pauseMedia//R.drawable.scenic_background_radio_display
                } else {
                    playMedia//R.drawable.scenic_background_radio_display_playing
                }
            } else
                noMedia,//R.drawable.scenic_background_radio_display_empty,
            location = Point(location.x + 130, location.y),
            appToOpen = fetcher.getApplication("spotify"))

        if (showTheBand) {
            BasicWidget(context = context,
                imageId = playPauseMedia,//R.drawable.scenic_background_speaker,
                location = Point(location.x + 60, location.y + 290),
                onClick = {  if (isPaused) remote?.playerApi?.resume() else remote?.playerApi?.pause() })

            BasicWidget(context = context,
                imageId = nextMedia,//R.drawable.scenic_background_speaker,
                location = Point(location.x + 460, location.y + 290),
                onClick = { remote?.playerApi?.skipNext() })

            BasicWidget(context = context,
                imageId = offButton,//R.drawable.scenic_background_off_button,
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