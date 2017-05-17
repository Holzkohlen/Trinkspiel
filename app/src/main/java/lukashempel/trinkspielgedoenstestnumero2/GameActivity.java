package lukashempel.trinkspielgedoenstestnumero2;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import java.io.InputStream;


public class GameActivity extends Activity {
    private CGame Game;
    private TextView tvQuestion;
    private TextView tvType;
    private TextView tvActiveRule;
    private TextView tvDebug;
    private Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_game);

        InputStream inputStream = getResources().openRawResource(R.raw.gamerules);
        String[] playerNames = getIntent().getStringArrayExtra("PlayerNames");
        Game = new CGame(playerNames, inputStream);
        tvQuestion = (TextView) findViewById(R.id.tvQuestion);
        tvType = (TextView) findViewById(R.id.tvType);
        tvActiveRule = (TextView) findViewById(R.id.tvActiveRule);
        btnNext = (Button)findViewById(R.id.btnNext);
        tvDebug = (TextView) findViewById(R.id.tvDebug);
        tvDebug.setText("");
        if (Game.getPlayerArrayLength() < 2) {
            tvQuestion.setText(R.string.notenoughplayers);
        } else {
            newQuestion();
        }
    }

    public void nextQuestion(View view) {
        newQuestion();
    }

    private void newQuestion() {
        String[] sArray = Game.getQuestion();
        tvQuestion.setText(sArray[0]);
        tvType.setText(sArray[1]);
        if (sArray[1].equals("REGEL AUFGEHOBEN")) {
            tvActiveRule.setText(R.string.activerule);
        } else if (sArray[1].equals("NEUE REGEL")) {
            tvActiveRule.setText(sArray[0]);
        }else if(sArray[1].equals("FINISHED")){
            btnNext.setEnabled(false);
            tvActiveRule.setText("Seid ihr endlich besoffen? Nein? Dann trinkt jeder nochmal 5 Schlücke!");
        }
        //tvDebug.setText("Debug: " + sArray[2]);
    }
}
