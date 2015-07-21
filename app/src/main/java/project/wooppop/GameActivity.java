package project.wooppop;

import android.animation.AnimatorInflater;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import android.widget.MediaController.MediaPlayerControl;
import android.widget.TextView;


public class GameActivity extends ActionBarActivity implements MediaPlayerControl{
    private final int delayShow = 250;
    private int score;
    private int type;
    private ArrayList<Song> songList;
    private MyService musicSrv;
    private Intent playIntent;
    private boolean musicBound = false;
    private MusicController controller;
    private boolean paused=false, playbackPaused=false;
    private long seed;
    private boolean playerBoolean;
    private CountDownTimer cdt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        score = this.getIntent().getExtras().getInt("score");
        type = this.getIntent().getExtras().getInt("type");

        songList = new ArrayList<Song>();
        getSongList(type);
        seed = System.nanoTime();
        Collections.shuffle(songList, new Random(seed));
        ArrayList<String> songTitles = new ArrayList<String>();
        String principal = songList.get(0).getTitle(); //+ " - " + songList.get(0).getArtist();
        songTitles.add(songList.get(0).getTitle()); // + " - " + songList.get(0).getArtist());
        songTitles.add(songList.get(1).getTitle()); // + " - " + songList.get(1).getArtist());
        songTitles.add(songList.get(2).getTitle()); // + " - " + songList.get(2).getArtist());
        songTitles.add(songList.get(3).getTitle()); // + " - " + songList.get(3).getArtist());
        seed = System.nanoTime();
        Collections.shuffle(songTitles, new Random(seed));

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.options);
        int correct = 0;

        final RadioButton rb1 = (RadioButton) findViewById(R.id.radioButton);
        rb1.setText(songTitles.get(0));
        if(songTitles.get(0).equals(principal)) correct = R.id.radioButton;
        final RadioButton rb2 = (RadioButton) findViewById(R.id.radioButton2);
        rb2.setText(songTitles.get(1));
        if(songTitles.get(1).equals(principal)) correct = R.id.radioButton2;
        final RadioButton rb3 = (RadioButton) findViewById(R.id.radioButton3);
        rb3.setText(songTitles.get(2));
        if(songTitles.get(2).equals(principal)) correct = R.id.radioButton3;
        final RadioButton rb4 = (RadioButton) findViewById(R.id.radioButton4);
        rb4.setText(songTitles.get(3));
        if(songTitles.get(3).equals(principal)) correct = R.id.radioButton4;

        final int var = correct;
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (musicSrv.isPng()) musicSrv.pausePlayer();
                RadioButton rb = (RadioButton) findViewById(checkedId);

                if (checkedId == var) {
                    rb.setBackgroundResource(R.drawable.green_triangle);
                    score++;
                    next(false, type);
                } else {
                    rb.setBackgroundResource(R.drawable.red_triangle);
                    next(true, type);
                }
            }
        });

        final Animation rotate = AnimationUtils.loadAnimation(this, R.anim.rotate);
        setController();
        final ImageButton playButton = (ImageButton) findViewById(R.id.imageButton2);
        playerBoolean = true;
        playButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                playButton.setEnabled(false);

                playButton.startAnimation(rotate);
                start();
            }

        });

        final TextView tx = (TextView) findViewById(R.id.numbers);
        final CountDownTimer countDown = new CountDownTimer(20000, 1000) {

            public void onTick(long millisUntilFinished) {
                tx.setText("" + millisUntilFinished / 1000);
                if(millisUntilFinished > 10000){
                    tx.setTextColor(Color.GREEN);
                } else {
                    tx.setTextColor(Color.RED);
                }

            }

            public void onFinish() {
                tx.setText("00");
            }
        };

        rotate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                countDown.start();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                rb1.setBackgroundResource(R.drawable.red_triangle);
                rb2.setBackgroundResource(R.drawable.red_triangle);
                rb3.setBackgroundResource(R.drawable.red_triangle);
                rb4.setBackgroundResource(R.drawable.red_triangle);
                next(true, 0);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void next(final boolean end, final int type){
        final GameActivity ga = this;
        new Thread(){
            public void run(){
                try {
                    sleep(delayShow);
                    if(end) {
                        Intent scoreActivity = new Intent(ga, ScoreActivity.class);
                        scoreActivity.putExtra("score",score);
                        startActivity(scoreActivity);
                        finish();
                    }else{
                        Intent nextSong = new Intent(ga, GameActivity.class);
                        nextSong.putExtra("score",score);
                        nextSong.putExtra("type", type);
                        startActivity(nextSong);
                        finish();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    private void playNext(){
        musicSrv.playNext();

        if(playbackPaused){
            setController();
            playbackPaused=false;
        }
        controller.show(0);
    }

    private void playPrev(){
        musicSrv.playPrev();
        if(playbackPaused){
            setController();
            playbackPaused=false;
        }
        controller.show(0);
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    protected void onPause(){
        super.onPause();
        paused=true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    @Override
    public int getDuration() {
        if(musicSrv!=null && musicBound && musicSrv.isPng())
            return musicSrv.getDur();
        else return 0;
    }

    @Override
    public boolean isPlaying() {
        if(musicSrv!=null && musicBound)
            return musicSrv.isPng();
        return false;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public void pause() {
        playbackPaused = true;
        musicSrv.pausePlayer();
    }

    @Override
    public void seekTo(int pos) {
        musicSrv.seek(pos);
    }

    @Override
    public void start() {
        musicSrv.go();
    }

    @Override
    public int getCurrentPosition() {
        if(musicSrv!=null && musicBound && musicSrv.isPng())
            return musicSrv.getPosn();
        else return 0;
    }

    private void setController(){
        controller = new MusicController(this);
        controller.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNext();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPrev();
            }
        });
        controller.setMediaPlayer(this);
        controller.setEnabled(true);
    }

    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyService.MusicBinder binder = (MyService.MusicBinder)service;
            musicSrv = binder.getService();
            musicSrv.setList(songList);
            musicSrv.setSong(0);
            musicSrv.playSong();
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    public void songPicked(View view){
        musicSrv.setSong(Integer.parseInt(view.getTag().toString()));
        musicSrv.playSong();
        if(playbackPaused){
            setController();
            playbackPaused=false;
        }

    }

    @Override
    protected void onStart(){
        super.onStart();
        if(playIntent==null){
            playIntent = new Intent(this, MyService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }

    }

    public void getSongList(int type) {

        if(type == 0){
            songList.add(new Song(R.raw.chiquitita, "Chiquitita", "", 20000));
            songList.add(new Song(R.raw.dancingqueen, "Dancing Queen", "", 20000));
            songList.add(new Song(R.raw.fernando, "Fernando", "", 20000));
            songList.add(new Song(R.raw.gimmegimmegimme, "Gimme Gimme Gimme", "", 20000));
        } else if (type == 1){
            songList.add(new Song(R.raw.happynewyear, "Happy New Year", "", 20000));
            songList.add(new Song(R.raw.honeyhoney, "Honey, Honey", "", 20000));
            songList.add(new Song(R.raw.idoidoido, "I Do, I Do, I Do, I Do, I Do", "", 20000));
            songList.add(new Song(R.raw.ihaveadream, "I Have a Dream", "", 20000));
        } else {
            songList.add(new Song(R.raw.layallyourloveonme, "Lay All Your Love On Me", "", 20000));
            songList.add(new Song(R.raw.mammamia, "Mamma Mia", "", 20000));
            songList.add(new Song(R.raw.moneymoneymoney, "Money, Money, Money", "", 20000));
            songList.add(new Song(R.raw.nameofthegame, "Name of the Game", "", 20000));
        }
        /*songList.add(new Song(R.raw.chiquitita, "Chiquitita", "", 20000));
        songList.add(new Song(R.raw.dancingqueen, "Dancing Queen", "", 20000));
        songList.add(new Song(R.raw.fernando, "Fernando", "", 20000));
        songList.add(new Song(R.raw.gimmegimmegimme, "Gimme Gimme Gimme", "", 20000));
        songList.add(new Song(R.raw.happynewyear, "Happy New Year", "", 20000));
        songList.add(new Song(R.raw.honeyhoney, "Honey, Honey", "", 20000));
        songList.add(new Song(R.raw.idoidoido, "I Do, I Do, I Do, I Do, I Do", "", 20000));
        songList.add(new Song(R.raw.ihaveadream, "I Have a Dream", "", 20000));
        songList.add(new Song(R.raw.layallyourloveonme, "Lay All Your Love On Me", "", 20000));
        songList.add(new Song(R.raw.mammamia, "Mamma Mia", "", 20000));
        songList.add(new Song(R.raw.moneymoneymoney, "Money, Money, Money", "", 20000));
        songList.add(new Song(R.raw.nameofthegame, "Name of the Game", "", 20000));
        songList.add(new Song(R.raw.ourlastsummer, "Our Last Summer", "", 20000));
        songList.add(new Song(R.raw.ringring, "Ring Ring", "", 20000));
        songList.add(new Song(R.raw.sos, "S.O.S.", "", 20000));
        songList.add(new Song(R.raw.supertrouper, "Super Trouper", "", 20000));
        songList.add(new Song(R.raw.takeachanceonme, "Take a Chance On Me", "", 20000));
        songList.add(new Song(R.raw.thewinnertakesitall, "The Winner Takes It All", "", 20000));
        songList.add(new Song(R.raw.voulezvouz, "Voulez Vouz", "", 20000));
        songList.add(new Song(R.raw.waterloo, "Waterloo", "", 20000));
        */
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(paused){
            setController();
            paused=false;
        }
    }

    @Override
    protected void onStop() {
        controller.hide();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        stopService(playIntent);
        musicSrv=null;
        super.onDestroy();
    }
}
