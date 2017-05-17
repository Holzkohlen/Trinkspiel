package lukashempel.trinkspielgedoenstestnumero2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import java.util.ArrayList;

public class MainActivity extends Activity {

    private LinearLayout ll_ver;
    private ArrayList<EditText> edTextsList;
    private LinearLayout.LayoutParams layout_params;
    private int iPlayerCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);//Auto generated
        setContentView(R.layout.activity_main);//auto generated
        iPlayerCount = 0;
        edTextsList = new ArrayList<>();
        ll_ver = (LinearLayout) findViewById(R.id.ll_ver);
        layout_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addStartingBoxes();//min 2 players => 2 boxes added from the start
    }

    public void addStartingBoxes(){
        for(int i = 0; i < 2; i++) {
            EditText edText = new EditText(this);
            edText.setId(iPlayerCount);
            iPlayerCount++;
            ll_ver.addView(edText, layout_params);
            edTextsList.add(edText);
        }
    }

    public void addBtnPress(View view){
        EditText edText = new EditText(this);
        edText.setId(iPlayerCount);
        iPlayerCount++;
        ll_ver.addView(edText, layout_params);
        edTextsList.add(edText);
        edText.requestFocus();//Cursor jumps to new EditText-field
    }

    public void startGame(View view){
        String[] playerNames;
        for(int i = 0; i < edTextsList.size(); i++){
            if(edTextsList.get(i).getText().toString().matches("")){
                edTextsList.remove(i);
            }

        }
        playerNames = new String[edTextsList.size()];
        for(int i = 0; i <  edTextsList.size(); i++){
            playerNames[i] = edTextsList.get(i).getText().toString();
        }
        Intent intent = new Intent(getApplicationContext(), GameActivity.class);
        intent.putExtra("PlayerNames", playerNames);
        startActivity(intent);
    }
}
