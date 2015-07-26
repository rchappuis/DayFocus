package co.chappuis.dayfocus;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;


public class MainActivity extends ActionBarActivity {
    private String tag = "DayFocus";

    //UI elements
    private TextView dailyGreeting;
    private EditText editFocus;
    private Button submitButton;
    private String dailyFocus;

    //SP to store focuse(s)
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    //Date to compare to keys in SP
    private Calendar cal;
    private String today;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set up UI elements
        setContentView(R.layout.activity_main);
        dailyGreeting = (TextView) findViewById(R.id.greeting);
        editFocus = (EditText) findViewById(R.id.edit_focus);
        submitButton = (Button) findViewById(R.id.submit_button);
        //Set up focus storage bits
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        cal = Calendar.getInstance();
        today = ""+cal.get(Calendar.DATE);
        //Set dailyGreeting to focus if there is one, or to a greeting
            dailyGreeting.setText(sharedPref.getString(today, getString(R.string.greeting_text)));
        if(sharedPref.contains(today)) {
            editFocus.setVisibility(View.INVISIBLE);
            submitButton.setVisibility(View.INVISIBLE);
        } else {
            submitButton.setVisibility(View.VISIBLE);
            editFocus.setVisibility(View.VISIBLE);
            //Clears any previous focuses
            editor = sharedPref.edit();
            editor.clear();
            editor.apply();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        today = ""+cal.get(Calendar.DATE);
        //Set dailyGreeting to focus if there is one, or to a greeting
        dailyGreeting.setText(sharedPref.getString(today, getString(R.string.greeting_text)));
        if(sharedPref.contains(today)) {
            editFocus.setVisibility(View.INVISIBLE);
            submitButton.setVisibility(View.INVISIBLE);
        } else {
            submitButton.setVisibility(View.VISIBLE);
            editFocus.setVisibility(View.VISIBLE);
            //Clears any previous focuses
            editor = sharedPref.edit();
            editor.clear();
            editor.apply();
        }
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
        String enteredFocus = editFocus.getText().toString().toUpperCase();
        if (enteredFocus.equals("")) {
            //Display a toast telling the user that they entered an empty focus
            Context context = getApplicationContext();
            CharSequence text = "You can't have no focus!";
            Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            toast.show();
            //Log.d(tag, "Entered focus is empty");
        } else {
            //Stores the focus until next day
            editor = sharedPref.edit();
            editor.putString(today, enteredFocus);
            editor.apply();
            //TODO Start persistent notification
            //For debugging purposes only
            dailyGreeting.setText(enteredFocus);
            //Log.d(tag, "Entered focus is " + enteredFocus);

            //Hide button and EditText until next day
            submitButton.setVisibility(View.INVISIBLE);
            editFocus.setVisibility(View.INVISIBLE);

            createNotification(enteredFocus);
        }
    }

    private void createNotification(String contentTitle) {
        //Create notification
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.icon));
        mBuilder.setSmallIcon(android.R.color.transparent);
        mBuilder.setContentTitle(contentTitle);
        mBuilder.setContentText("Have you stayed focused today?");
        //Make it persistent
        mBuilder.setOngoing(true);
        //Make sure priority is default
        mBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Sets an ID for the notification
        int mNotificationId = 001;
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

}
