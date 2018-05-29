package id.co.altheastudio.lampuaing;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

import java.util.ArrayList;

public class Speech implements RecognitionListener {

    private static final String TAG = "Speech";

    private SpeechRecognizer speechRecognizer;
    private Intent recognizerIntent;
    private InterfaceSpeech interfaceSpeech;

    public Speech(Context context, InterfaceSpeech interfaceSpeech){

        this.interfaceSpeech = interfaceSpeech;

        //SpeechRecognizer
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
        speechRecognizer.setRecognitionListener(this);

        //Intent
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "id-ID");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, false);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.getPackageName());
    }

    public void destroy(){
        if (speechRecognizer != null) {
            speechRecognizer.cancel();
            speechRecognizer.destroy();
            speechRecognizer = null;
        }
    }

    public SpeechRecognizer getSpeechRecognizer(){
        return speechRecognizer;
    }

    public Intent getRecognizerIntent(){
        return recognizerIntent;
    }


    @Override
    public void onReadyForSpeech(Bundle bundle) {

    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onRmsChanged(float v) {

    }

    @Override
    public void onBufferReceived(byte[] bytes) {

    }

    @Override
    public void onEndOfSpeech() {

    }

    @Override
    public void onError(int i) {

    }

    @Override
    public void onResults(Bundle results) {
        ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String text;

        if (matches != null) {
            text = matches.get(0);
        } else {
            return;
        }

        text = text.toLowerCase();

        Log.d(TAG, text);
        if(text.contains("nyalakan")){
            interfaceSpeech.setStatusLampu(2);
        }else if(text.contains("matikan")){
            interfaceSpeech.setStatusLampu(1);
        }
    }

    @Override
    public void onPartialResults(Bundle bundle) {

    }

    @Override
    public void onEvent(int i, Bundle bundle) {

    }
}
