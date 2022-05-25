package todaybrian;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import java.io.File;

public class SoundPlayer {
    private Clip clip;

    public SoundPlayer(){
        try {
            clip = AudioSystem.getClip();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playSound(String fileName){
        try {
            File file = new File(fileName);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
            DataLine.Info info = new DataLine.Info(Clip.class, audioInputStream.getFormat());
            clip = (Clip) AudioSystem.getLine(info);
            clip.open(audioInputStream);
        }catch (Exception e){
            e.printStackTrace();
        }
        clip.start();
    }

    public void stopSound(){
        clip.stop();
    }
}
