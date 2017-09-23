package audio;

import java.io.File;
import java.util.HashMap;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class JukeBox {
	
	private static HashMap<String, Clip> clips;
	private static int gap;
	private static boolean mute = false;
	
	public static void init() {
		clips = new HashMap<String, Clip>();
		gap = 0;
		load("./sounds/mainMusic.wav", "mainMusic");
		load("./sounds/playerhit.wav", "playerShoot");
		load("./sounds/enemyhit.wav", "enemyHit");
		load("./sounds/menuoption.wav", "option");
		load("./sounds/menuselect.wav", "select");
		load("./sounds/lose.wav", "lose");
		load("./sounds/powerup.wav", "powerup");
		load("./sounds/fireBurning.wav", "fireBurning");
		load("./sounds/bossShot.wav", "bossShot");
		
		changeVolume("mainMusic", -10.0f);
		changeVolume("fireBurning", -10.0f);
		changeVolume("lose", -5.0f);
	}
	
	public static void playFire(){
		play("fireBurning");
	}
	
	public static void playBossShot(){
		play("bossShot");
	}
	
	public static void playMainMusic(){
		play("mainMusic");
	}
	
	public static void playMenuSelection(){
		play("select");
	}
	
	public static void playMenuOption(){
		play("option");
	}
	
	public static void playLose(){
		play("lose");
	}
	
	public static void playPlayerShoot(){
		play("playerShoot");
	}
	
	public static void playEnemyHit(){
		play("enemyHit");
	}
	
	public static void playPowerUp(){
		play("powerup");
	}
	
	public static void stopMainMusic(){
		stop("mainMusic");
	}
	
	public static void stopAllSounds(){
		for(String sound : clips.keySet()){
			stop(sound);
		}
	}
	
	public static void stopFireSound(){
		stop("fireBurning");
	}
	
	public static void loopMainMusic(){
		loop("mainMusic");
	}
	
	public static void resumeMainMusic(){
		resume("mainMusic");
	}
	
	public static void resumeFireSound(){
		resume("fireBurning");
	}
	
	public static boolean isRunning(String sound){
		return clips.get(sound).isRunning();
	}

	public static boolean isFireSoundRunning(){
		return isRunning("fireBurning"); // TODO constant for predefined strings
	}
	
	public static boolean isActive(String sound){
		return clips.get(sound).isActive();
	}
	
	public static boolean hasBeenActivated(String sound){
		return clips.get(sound).getLongFramePosition() > 0;
	}
	
	public static boolean hasFireSoundBeenActivated(){
		return hasBeenActivated("fireBurning");
	}
	
	public static void load(String s, String n) {
		if(clips.get(n) != null) return;
		Clip clip;
		try {			
			AudioInputStream ais =
				AudioSystem.getAudioInputStream(
						new File(s)
					//JukeBox.class.getResourceAsStream(s)
				);
			AudioFormat baseFormat = ais.getFormat();
			AudioFormat decodeFormat = new AudioFormat(
				AudioFormat.Encoding.PCM_SIGNED,
				baseFormat.getSampleRate(),
				16,
				baseFormat.getChannels(),
				baseFormat.getChannels() * 2,
				baseFormat.getSampleRate(),
				false
			);
			AudioInputStream dais = AudioSystem.getAudioInputStream(decodeFormat, ais);
			clip = AudioSystem.getClip();
			clip.open(dais);
			clips.put(n, clip);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Reduce or increase the volume of the s sound
	 * @param s the sound
	 * @param volume to add or decrease (+ / -)
	 */
	public static boolean changeVolume(String s, float volume){
		if(clips.get(s) == null) return false;
		Clip c = clips.get(s);
		FloatControl control = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
		control.setValue(volume);
		return true;
	}
	
	public static void play(String s) {
		play(s, gap);
	}
	
	public static void play(String s, int i) {
		if(mute) return;
		Clip c = clips.get(s);
		if(c == null) return;
		if(c.isRunning()) c.stop();
		c.setFramePosition(i);
		while(!c.isRunning()) c.start();
	}
	
	public static void stop(String s) {
		if(clips.get(s) == null) return;
		if(clips.get(s).isRunning()) clips.get(s).stop();
	}
	
	public static void resume(String s) {
		if(mute) return;
		if(clips.get(s).isRunning()) return;
		clips.get(s).start();
	}
	
	public static void loop(String s) {
		loop(s, gap, gap, clips.get(s).getFrameLength() - 1);
	}
	
	public static void loop(String s, int frame) {
		loop(s, frame, gap, clips.get(s).getFrameLength() - 1);
	}
	
	public static void loop(String s, int start, int end) {
		loop(s, gap, start, end);
	}
	
	public static void loop(String s, int frame, int start, int end) {
		stop(s);
		if(mute) return;
		clips.get(s).setLoopPoints(start, end);
		clips.get(s).setFramePosition(frame);
		clips.get(s).loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	public static void setPosition(String s, int frame) {
		clips.get(s).setFramePosition(frame);
	}
	
	public static int getFrames(String s) { return clips.get(s).getFrameLength(); }
	public static int getPosition(String s) { return clips.get(s).getFramePosition(); }
	
	public static void close(String s) {
		stop(s);
		clips.get(s).close();
	}
	
	public static void mute(){
		mute = true;
	}
	
	public static void unmute(){
		mute = false;
	}
	
	public static boolean isMuted(){
		return mute;
	}
	
}