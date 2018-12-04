package spaceinvaders;

import java.io.*;
import java.net.URL;
import javax.sound.sampled.*;
import javax.swing.*;
   
// To play sound using Clip, the process need to be alive.
// Hence, we use a Swing application.
public class Pew{
   
   // Constructor
   public Pew() {
//	   this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//	      this.setTitle("Test Sound Clip");
//	      this.setSize(300, 200);
//	      this.setVisible(true);
	      
      try {
         // Open an audio input stream.
    	  AudioInputStream audioIn = AudioSystem.getAudioInputStream((new File("/Users/jessicajiang/Senior/ECE 30862/Centipede/centipede/src/pewpew1final.wav")).getAbsoluteFile());
    	  //System.out.println(audioIn == null);
         // Get a sound clip resource.
         Clip clip = AudioSystem.getClip();
         // Open audio clip and load samples from the audio input stream.
         clip.open(audioIn);
         clip.start();
      } catch (UnsupportedAudioFileException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      } catch (LineUnavailableException e) {
         e.printStackTrace();
      }
   }
   
   public static void main(String[] args) {
      new Pew();
   }
}