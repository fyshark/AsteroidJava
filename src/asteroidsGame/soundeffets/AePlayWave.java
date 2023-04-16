package asteroidsGame.soundeffets;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AePlayWave extends Thread {
    //filename is used to store the name of the audio file
    private String filename;


    public AePlayWave(String wavfile) {
    //Constructor: takes the audio file name as an argument and assigns it to the filename variable
        filename = wavfile;

    }

    public void run() {
    //When the thread is started, the run method will be called. Here the audio playback is done

        //Reading audio files
        File soundFile = new File(filename);
        AudioInputStream audioInputStream = null;
        try {
            audioInputStream = AudioSystem.getAudioInputStream(soundFile);
        } catch (Exception e1) {
            e1.printStackTrace();
            return;
        }

        //SourceDataLine is the object used to play the audio data
        AudioFormat format = audioInputStream.getFormat();
        SourceDataLine auline = null;
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

        try {
            auline = (SourceDataLine) AudioSystem.getLine(info);
            auline.open(format);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        auline.start();
        int nBytesRead = 0;
        //This is buffering
        byte[] abData = new byte[512];

        try {
            while (nBytesRead != -1) {
                nBytesRead = audioInputStream.read(abData, 0, abData.length);
                if (nBytesRead >= 0)
                    auline.write(abData, 0, nBytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        } finally {
            auline.drain();
            auline.close();
        }

    }
}
