package main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.net.URL;

public class Sound {

    Clip clip;
    URL soundURL[] = new URL[30];
    private long clipPosition; // To store the position of the clip when paused
    private boolean isPlaying; // To track if the clip is currently playing
    FloatControl fc;
    int volumeScale = 4;
    float volume;

    public Sound() {

        soundURL[0] = getClass().getClassLoader().getResource("sound/BlueBoyAdventure.wav");
        soundURL[1] = getClass().getClassLoader().getResource("sound/coin.wav");
        soundURL[2] = getClass().getClassLoader().getResource("sound/powerup.wav");
        soundURL[3] = getClass().getClassLoader().getResource("sound/unlock.wav");
        soundURL[4] = getClass().getClassLoader().getResource("sound/fanfare.wav");
        soundURL[5] = getClass().getClassLoader().getResource("sound/hitmonster.wav");
        soundURL[6] = getClass().getClassLoader().getResource("sound/receivedamage.wav");
        soundURL[7] = getClass().getClassLoader().getResource("sound/swingweapon.wav");
        soundURL[8] = getClass().getClassLoader().getResource("sound/levelup.wav");
        soundURL[9] = getClass().getClassLoader().getResource("sound/cursor.wav");
        soundURL[10] = getClass().getClassLoader().getResource("sound/burning.wav");
        soundURL[11] = getClass().getClassLoader().getResource("sound/cuttree.wav");
        soundURL[12] = getClass().getClassLoader().getResource("sound/gameover.wav");
    }

    public void setFile(int i) {
        try {

            AudioInputStream AudioInputStream = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(AudioInputStream);
            isPlaying = false;
            fc = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
            checkVolume();

        }catch (Exception e) {

        }
    }

    public void play() {

        clip.start();
        isPlaying = true;

    }

    public void loop() {

        clip.loop(Clip.LOOP_CONTINUOUSLY);
        isPlaying = true;
    }

    public void stop() {

        clip.stop();
        isPlaying = false;
        clipPosition = 0;
    }

    public void pause() {

        if (isPlaying) {
            clipPosition = clip.getMicrosecondPosition(); // Store the current position
            clip.stop(); // Stop the clip
            isPlaying = false; // Update playing state
        }
    }

    public void resume() {

        if (!isPlaying) {
            clip.setMicrosecondPosition(clipPosition); // Set the clip position to the last position
            clip.start(); // Start the clip
            isPlaying = true; // Update playing state
        }
    }

    public void checkVolume() {

        switch(volumeScale) {
            case 0: volume = -80f; break;
            case 1: volume = -20f; break;
            case 2: volume = -12f; break;
            case 3: volume = -5f; break;
            case 4: volume = 1f; break;
            case 5: volume = 6f; break;
        }
        fc.setValue(volume);
    }
}
