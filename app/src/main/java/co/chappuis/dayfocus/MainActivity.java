package co.chappuis.dayfocus;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
    private String tag = "DayFocus";

    //UI elements
    private TextView dailyGreeting;
    private EditText editFocus;
    private Button submitButton;
    private String dailyFocus;

    //SP to store focuse(s)
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dailyGreeting = (TextView) findViewById(R.id.greeting);
        editFocus = (EditText) findViewById(R.id.edit_focus);
        submitButton = (Button) findViewById(R.id.submit_button);

        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        //Set the edit text to the stored focus if there is one, otherwise leave it empty
        editFocus.setText(sharedPref.getString(getString(R.string.pref_file_key), ""));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void setFocus(View view) {
        String enteredFocus =  editFocus.getText().toString();
        if(enteredFocus.equals("")) {
            //Display a toast telling the user that they entered an empty focus
            Context context = getApplicationContext();
            CharSequence text = "You can't have no focus!";
            Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            toast.show();
            Log.d(tag, "Entered focus is empty");
        } else {
            //Stores the focus TODO until next day
            editor.putString(getString(R.string.pref_file_key), enteredFocus);
            editor.commit();
            //TODO Start persistent notification
            //For debugging purposes only
            dailyGreeting.setText(enteredFocus);
            Log.d(tag, "Entered focus is " + enteredFocus);

            //Hide button TODO until next day
            submitButton.setVisibility(View.INVISIBLE);
        }
    }
}
