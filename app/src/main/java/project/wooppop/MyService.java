package project.wooppop;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import java.util.Random;
import android.app.Notification;
import android.app.PendingIntent;

import java.util.ArrayList;
import android.content.ContentUris;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Binder;
import android.os.PowerManager;
import android.util.Log;

public class MyService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener{
    private MediaPlayer player;
    private ArrayList<Song> songs;
    private int songP;
    private final IBinder musicBind = new MusicBinder();
    private String songTitle = "";
    private static final int NOTIFY_ID = 1;
    private boolean shuffle = false;
    private Random rand;

    public MyService() {

    }

    public void onCreate(){
        super.onCreate();
        songP = 0;
        player = new MediaPlayer();
        initMusicPlayer();
        rand=new Random();
    }

    public void setShuffle(){
        if(shuffle) shuffle=false;
        else shuffle=true;
    }

    public int getPosn(){
        return player.getCurrentPosition();
    }

    public int getDur(){
        return player.getDuration();
    }

    public boolean isPng(){
        return player.isPlaying();
    }

    public void pausePlayer() {
        player.pause();
    }

    public void seek(int posn){
        player.seekTo(posn);
    }

    public void go(){
        player.start();
    }

    public void playPrev(){
        songP--;
        if(songP < 0) songP=songs.size()-1;
        playSong();
    }

    public void playNext(){
        songs.remove(0);
        playSong();
    }

    public void initMusicPlayer(){
        player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    public void setList(ArrayList<Song> theSongs){
        songs = theSongs;
    }

    public void playSong() {
        player.reset();

        Song playSong = songs.get(songP);
        songTitle=playSong.getTitle();
        long currSong = playSong.getId();
        Uri trackUri = ContentUris.withAppendedId(Uri.parse("android.resource://" + getPackageName() + "/"), currSong);
        try{
            player.setDataSource(getApplicationContext(), trackUri);
        }
        catch(Exception e){
            Log.e("MUSIC SERVICE", "Error", e);
        }
        player.prepareAsync();
    }

    public class MusicBinder extends Binder{
        MyService getService() {
            return MyService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent){
        player.stop();
        player.release();
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if(player.getCurrentPosition()>0){
            mp.reset();
            playNext();
        }
    }
    public Song getPlayingSong(){
        return this.songs.get(this.songP);
    }
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mp.reset();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        player.setOnPreparedListener(this);
        mp.start();

        Intent notIntent = new Intent(this, GameActivity.class);
        notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendInt = PendingIntent.getActivity(this, 0,
                notIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(this);

        builder.setContentIntent(pendInt)
                .setSmallIcon(R.drawable.play)
                .setTicker(songTitle)
                .setOngoing(true)
                .setContentTitle("Playing")
        .setContentText(songTitle);
        Notification not = builder.build();

        startForeground(NOTIFY_ID, not);
    }

    public void setSong(int songIndex){
        songP = songIndex;
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
    }


}
