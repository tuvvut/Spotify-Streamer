package com.tuvvut.udacity.spotify;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;

import static com.tuvvut.udacity.spotify.MusicController.getInstance;

/**
 * Created by wu on 2015/10/05
 */
public class MusicReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && !TextUtils.isEmpty(intent.getAction())) {
            if (intent.getAction().equals(Intent.ACTION_MEDIA_BUTTON)) {
                KeyEvent keyEvent = (KeyEvent) intent.getExtras().get(Intent.EXTRA_KEY_EVENT);
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    MusicController musicController = getInstance();
                    switch (keyEvent.getKeyCode()) {
                        case KeyEvent.KEYCODE_HEADSETHOOK:
                        case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                        case KeyEvent.KEYCODE_MEDIA_PLAY:
                        case KeyEvent.KEYCODE_MEDIA_PAUSE:
                            musicController.playOrPause();
                            break;
                        case KeyEvent.KEYCODE_MEDIA_STOP:
                            musicController.stop();
                            break;
                        case KeyEvent.KEYCODE_MEDIA_NEXT:
                            musicController.next();
                            break;
                        case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                            musicController.previous();
                            break;
                    }
                }
            }
        }
    }
}
