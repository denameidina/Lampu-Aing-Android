package id.co.altheastudio.lampuaing;

import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, InterfaceSpeech {

    private static final String TAG = "MainAvtivity";
    private Speech speechRecognition;
    private TTS tts;

    private Button on, off, speech;
    private TextView status, icon;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private ConnectivityManager connectivityManager;
    private boolean connected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        status = findViewById(R.id.status);
        icon = findViewById(R.id.icon);
        on = findViewById(R.id.on);
        off = findViewById(R.id.off);
        speech = findViewById(R.id.speech);
        on.setOnClickListener(this);
        off.setOnClickListener(this);
        speech.setOnClickListener(this);

        speechRecognition = new Speech(this, this);
        tts = new TTS(this);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("STATUS_LAMPU");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int value = dataSnapshot.getValue(Integer.class);
                if(value == 2){
                    icon.setBackgroundResource(R.drawable.lampu_on);
                    status.setText("HURUNG");
                    status.setTextColor(Color.GREEN);
                    if(tts.getIsReady()) {
                        tts.speak("lampu dihurungkeun");
                    }
                }else if(value == 1){
                    icon.setBackgroundResource(R.drawable.lampu_off);
                    status.setText("PAREUM");
                    status.setTextColor(Color.RED);
                    if(tts.getIsReady()) {
                        tts.speak("lampu dipareuman");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "Failed to read value");
            }
        });

        connectivityManager = (ConnectivityManager)getSystemService(this.CONNECTIVITY_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else
            connected = false;

        if(connected){
            Toast.makeText(this, "Internet konek, mantap bray", Toast.LENGTH_SHORT).show();
        }else{
            tts.speak("teu konek atuh sia teh kehed");
            Toast.makeText(this, "Henteu konek, konekeun heula atuh goblog", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(tts.getIsReady()) {
            tts.speak("mantog goblog");
        }
        icon.setBackgroundResource(R.drawable.lampu_off);
        status.setText("LOADING . . .");
        status.setTextColor(Color.BLACK);
    }

    @Override
    protected void onDestroy() {
        speechRecognition.destroy();
        tts.destroy();
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.on:
                // Write a message to the database
                setStatusLampu(2);
                break;
            case R.id.off:
                // Write a message to the database
                setStatusLampu(1);
                break;
            case R.id.speech:
                tts.speak("sok ngomong");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                speechRecognition.getSpeechRecognizer().cancel();
                speechRecognition.getSpeechRecognizer().startListening(speechRecognition.getRecognizerIntent());
                break;

        }
    }

    @Override
    public void setStatusLampu(int i) {
        myRef.setValue(i);
    }
}
