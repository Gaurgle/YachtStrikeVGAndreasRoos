package ServerSide;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;
import Audio.AudioEnum;

public class Audio implements LineListener {

    private final AudioEnum AudioEnum;
    private Clip clip;
    private boolean isMuted = false;
    private int songPosition = 0;
    private final boolean loopAudio;

    public Audio(String fileName, boolean loopAudio, AudioEnum AudioEnum) throws UnsupportedAudioFileException {
        this.loopAudio = loopAudio;
        this.AudioEnum = AudioEnum;

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("Audio/" + fileName);
        if (inputStream == null) {
            throw new UnsupportedAudioFileException("Audio file" + fileName +"is not found");
        }

        // Audio converter, adjusts to local audio settings.
        try (AudioInputStream originalStream = AudioSystem.getAudioInputStream(inputStream)) {
            AudioFormat baseFormat = originalStream.getFormat();
            AudioFormat compatibleFormat = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,
                    baseFormat.getSampleRate(), 16,
                    baseFormat.getChannels(), baseFormat.getChannels() * 2,
                    baseFormat.getSampleRate(), false);

            AudioInputStream convertedStream = AudioSystem.getAudioInputStream(compatibleFormat, originalStream);

            DataLine.Info info = new DataLine.Info(Clip.class, compatibleFormat);
            clip = (Clip) AudioSystem.getLine(info);
            clip.addLineListener(this);
            clip.open(convertedStream);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play() {
        if (clip != null) {
            clip.setFramePosition(0);
            if (loopAudio) {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
                clip.start();
            } else {
                clip.start();
            }
        }
    }

    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    // mutes function using frame position instead of volume.
    public void mute() {
        if (isMuted) {
            clip.setFramePosition(songPosition); // sets song position
            if (loopAudio) {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            }
            clip.start();
        } else {
            songPosition = clip.getFramePosition(); // starts clip from position
            clip.stop();
        }
        isMuted = !isMuted;
    }

    // volume adjustment
    public void setVolume(float targetVolumeDB, int steps, long sweepMS) {
        if (clip == null) return;
            try {
                FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                float currentVolume = volumeControl.getValue();
                float stepSize = (targetVolumeDB - currentVolume) / steps;

                for (int i = 0; i < steps; i++) {
                    currentVolume += stepSize;
                    volumeControl.setValue(currentVolume);
                    Thread.sleep(sweepMS);
                }

                volumeControl.setValue(targetVolumeDB);

            } catch (IllegalArgumentException e) {
                System.out.println("Volume control error");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }


    @Override
    public void update(LineEvent event) {
        if (event.getType() == LineEvent.Type.STOP) {
            clip.stop();
        }
    }
}