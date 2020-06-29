package com.aei.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class SpeechRecognize {
    private SpeechRecognizer recognizer;
    private RecognizeNewWordCallback callback;

    public SpeechRecognize setCallback(RecognizeNewWordCallback callback) {
        this.callback = callback;
        return this;
    }

    public interface RecognizeNewWordCallback{
        void onNewWord(List<String> words);
        void onFinish(List<String> words);
    }

    public SpeechRecognize(Context context){
        recognizer = SpeechRecognizer.createSpeechRecognizer(context);
        recognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {
                Log.e("Speech", "Ready");
            }

            @Override
            public void onBeginningOfSpeech() {
                Log.e("Speech", "Begin");
            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onResults(Bundle results) {
                Log.e("Speech", "Finished");
                if(callback != null)
                    callback.onFinish(results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION));
            }

            @Override
            public void onPartialResults(Bundle partialResults) {
                if(callback != null)
                    callback.onNewWord(partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION));
            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });
    }

    public void start(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 1000);
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 1000);
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 1500);
//        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hi speak something");
        recognizer.startListening(intent);
    }

    public void stop(){
        recognizer.stopListening();
    }
}
