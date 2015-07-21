package project.wooppop;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;


public class CategoryActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        final Button startSlow = (Button) findViewById(R.id.startGameSlow);
        startSlow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent startGame = new Intent(CategoryActivity.this, GameActivity.class);
                Bundle extras = new Bundle();
                extras.putInt("score", 0);
                extras.putInt("type", 0);
                startGame.putExtras(extras);
                startActivity(startGame);
                finish();
            }
        });

        final Button startReverse = (Button) findViewById(R.id.startGameReverse);
        startReverse.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent startGame = new Intent(CategoryActivity.this, GameActivity.class);
                Bundle extras = new Bundle();
                extras.putInt("score",0);
                extras.putInt("type", 1);
                startGame.putExtras(extras);
                startActivity(startGame);
                finish();
            }
        });

        final Button startMix = (Button) findViewById(R.id.startGameMix);
        startMix.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent startGame = new Intent(CategoryActivity.this, GameActivity.class);
                Bundle extras = new Bundle();
                extras.putInt("score",0);
                extras.putInt("type", 2);
                startGame.putExtras(extras);
                startActivity(startGame);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_category, menu);
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
}
