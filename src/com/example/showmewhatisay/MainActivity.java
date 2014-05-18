package com.example.showmewhatisay;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends Activity {

	private TextView mTextView;
	private SpeechRecognizer mSpeechRecognizer;
	private Intent mSpeechIntent;

	boolean mListening = false;
	boolean mKeyWordSpoken = false;

	static final String TAG = "SHOWMEWHATISAY";

	@Override
	protected void onPause() {
		super.onPause();
		if (mSpeechRecognizer != null && mListening) {
			mSpeechRecognizer.stopListening();
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		if (mSpeechRecognizer != null) {
			initSpeech();
			mTextView.setText("Tap and I will listen");

		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mTextView = (TextView) findViewById(R.id.textView1);
		Log.d(TAG, mTextView.getText().toString());

		initSpeech();

	}

	private void initSpeech() {
		mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
		AbstractMainRecognitionListener mRecognitionListener = new AbstractMainRecognitionListener();
		mSpeechRecognizer.setRecognitionListener(mRecognitionListener);
		mSpeechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		mSpeechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US"); //
		// i18n
		mSpeechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
				RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		mSpeechIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
		mSpeechIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 10); // To
																		// loop
																		// every
																		// X
																		// results
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_CENTER:
			mTextView.setText("I'm listening ...");
			startListening();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void startListening() {
		mSpeechRecognizer.startListening(mSpeechIntent);
	}

	public class AbstractMainRecognitionListener implements RecognitionListener {

		@Override
		public void onReadyForSpeech(Bundle params) {
			Log.d(TAG, "ready for speech");
			mListening = true;
		}

		@Override
		public void onBeginningOfSpeech() {
			Log.d(TAG, "beginning of speech");
		}

		@Override
		public void onRmsChanged(float rmsdB) {
			// Log.d(TAG, "rms changed");
		}

		@Override
		public void onBufferReceived(byte[] buffer) {
			Log.d(TAG, "buffer received");
		}

		@Override
		public void onEndOfSpeech() {
			Log.d(TAG, "end of speech");
			mListening = false;
		}

		@Override
		public void onError(int error) {
			Log.d(TAG, "error: " + error);
			mSpeechRecognizer.stopListening();
			mTextView.setText("error ... tap to try again");
			mListening = false;
		}

		@Override
		public void onResults(Bundle results) {

			List<String> items = results
					.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

			items.add(" [Tap and I'll listen again] ");
			if (!mKeyWordSpoken) {
				showItems(items);
			} else {
				mKeyWordSpoken = false;
			}
		}

		private void showItems(List<String> items) {
			String allItems = "";
			for (String item : items) {
				Log.d(TAG, "item: " + item);
				allItems += item + " ";
			}
			mTextView.setText(allItems);
		}

		@Override
		public void onPartialResults(Bundle partialResults) {
			ArrayList<String> items = partialResults
					.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

			for (String item : items) {
				Log.d(TAG, "partial item: " + item);
				if (item.toLowerCase().contains("bitcoin")) {
					mTextView
							.setText("To the moon! [I heard Bitcoin, tap and I'll listen again, and try not to get distracted]");
					Log.d(TAG, "bitcoin was spoken");
					mSpeechRecognizer.stopListening();
					mKeyWordSpoken = true;
				} else {
					showItems(items);
				}
			}
		}

		@Override
		public void onEvent(int eventType, Bundle params) {
			Log.d(TAG, "event" + eventType);
		}
	}

}
