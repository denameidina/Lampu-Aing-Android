package id.co.altheastudio.lampuaing;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.widget.Toast;

import java.util.Locale;

public class TTS implements TextToSpeech.OnInitListener {

    private static final String TAG = "TTS";

    private TextToSpeech textToSpeech;
    private boolean isTalking = false;
    private boolean isReady = false;

    private Context context;

    public TTS(Context context){
        this.context = context;
        textToSpeech = new TextToSpeech(context, this);
        textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String s) {
                isTalking = true;
            }

            @Override
            public void onDone(String s) {
                isTalking = false;
            }

            @Override
            public void onError(String s) {
                isTalking = false;
            }
        });
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(Locale.getDefault());
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(context, "TTS not supported", Toast.LENGTH_SHORT).show();
            }
            isReady = true;
            speak("Lampu aing nya kumaha aing");
        } else {
            Toast.makeText(context, "Initilization Failed!", Toast.LENGTH_SHORT).show();
        }
    }

    public void speak(String s) {
        if(!isTalking) {
            String str = s;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Bundle map = new Bundle();
                map.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "id");
                textToSpeech.speak(str, TextToSpeech.QUEUE_FLUSH, map, "UniqueID");
            } else {
                textToSpeech.speak(str, TextToSpeech.QUEUE_FLUSH, null);
            }
        }else{
            Toast.makeText(context, "Masih ngomong kalem hela kehed!!",Toast.LENGTH_SHORT).show();
        }
    }


    public void destroy(){
        textToSpeech.shutdown();
    }

    public TextToSpeech getTextToSpeech() {
        return textToSpeech;
    }

    public boolean getIsTalking(){
        return isTalking;
    }

    public boolean getIsReady(){
        return isReady;
    }
}
