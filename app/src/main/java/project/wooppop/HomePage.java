package project.wooppop;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.MediaController.MediaPlayerControl;

import java.util.ArrayList;


public class HomePage extends AppCompatActivity{

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
        setContentView(R.layout.activity_home_page);
        blinkText();

        //songList = new ArrayList<Song>();
        //getSongList();
        //start();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void renderCategory(View view){
        Intent categoryActivity = new Intent(this, CategoryActivity.class);
        //Bundle extras = new Bundle();
        //extras.putInt("score",0);
        //categoryActivity.putExtras(extras);
        startActivity(categoryActivity);
        finish();
    }

    private void blinkText(){
        //TextView tx = (TextView) findViewById(R.id.clickPlay);
        ImageView tx = (ImageView) findViewById(R.id.imageWoop);

        Animation animator = new AlphaAnimation(0.0f, 1.0f);
        animator.setDuration(300);
        animator.setRepeatMode(Animation.REVERSE);
        animator.setRepeatCount(Animation.INFINITE);
        tx.startAnimation(animator);
    }

}
