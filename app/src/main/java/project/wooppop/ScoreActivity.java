package project.wooppop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class ScoreActivity extends ActionBarActivity {
    private int finalScore;
    private int bestScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        finalScore = this.getIntent().getExtras().getInt("score");
        bestScore = saveRecord(finalScore);

        //Typeface face = Typeface.createFromAsset(getAssets(), "fonts/Plasma11.ttf");
        TextView tv = (TextView) findViewById(R.id.score_val);
        //tv.setTypeface(face);
        tv.setText(""+finalScore);
        tv = (TextView) findViewById(R.id.best_score_val);
        tv.setText(""+bestScore);
        blinkText();
    }

    private void blinkText(){
        //TextView tx = (TextView) findViewById(R.id.clickPlay);
        ImageButton tx = (ImageButton) findViewById(R.id.playagain);

        Animation animator = new AlphaAnimation(0.0f, 1.0f);
        animator.setDuration(300);
        animator.setRepeatMode(Animation.REVERSE);
        animator.setRepeatCount(Animation.INFINITE);
        tx.startAnimation(animator);

        final ImageButton playAgainButton = (ImageButton) findViewById(R.id.playagain);
        playAgainButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent playAgain = new Intent(ScoreActivity.this, CategoryActivity.class);
                startActivity(playAgain);
                finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_score, menu);
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

    public void playAgain(View view){
        Intent playAgain = new Intent(ScoreActivity.this, HomePage.class);
        startActivity(playAgain);
        finish();
    }

    private int saveRecord(int score){
        SharedPreferences share = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = share.edit();
        int current_best = share.getInt("best_score",-1);
        if(score > current_best) editor.putInt("best_score",score);
        editor.commit();
        return share.getInt("best_score",-1);
    }
}
