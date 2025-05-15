package ca.doophie.swipelauncher.data

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class SpotifyBroadcastReceiver : BroadcastReceiver() {
    internal object BroadcastTypes {
        const val SPOTIFY_PACKAGE: String = "com.spotify.music"
        const val PLAYBACK_STATE_CHANGED: String = SPOTIFY_PACKAGE + ".playbackstatechanged"
        const val QUEUE_CHANGED: String = SPOTIFY_PACKAGE + ".queuechanged"
        const val METADATA_CHANGED: String = SPOTIFY_PACKAGE + ".metadatachanged"
    }

    override fun onReceive(context: Context, intent: Intent) {
        // This is sent with all broadcasts, regardless of type. The value is taken from
        // System.currentTimeMillis(), which you can compare to in order to determine how
        // old the event is.
        val timeSentInMs = intent.getLongExtra("timeSent", 0L)

        Log.d("BROAD", "Broadcast received")

        val action = intent.action

        if (action == BroadcastTypes.METADATA_CHANGED) {
            val trackId = intent.getStringExtra("id")
            val artistName = intent.getStringExtra("artist")
            val albumName = intent.getStringExtra("album")
            val trackName = intent.getStringExtra("track")
            val trackLengthInSec = intent.getIntExtra("length", 0)
            // Do something with extracted information...
        } else if (action == BroadcastTypes.PLAYBACK_STATE_CHANGED) {
            val playing = intent.getBooleanExtra("playing", false)
            val positionInMs = intent.getIntExtra("playbackPosition", 0)
            // Do something with extracted information
        } else if (action == BroadcastTypes.QUEUE_CHANGED) {
            // Sent only as a notification, your app may want to respond accordingly.
        }
    }
}