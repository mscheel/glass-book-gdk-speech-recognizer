package com.example.showmewhatisay;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;

public class MainActivity extends Activity {

	private TextView mTextView;
	private Intent mSpeechIntent;

	boolean mListening = false;

	static final String TAG = "SHOWMEWHATISAY";
	private static final int SPEECH_REQUEST = 303;

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mTextView = (TextView) findViewById(R.id.textView1);
		Log.d(TAG, mTextView.getText().toString());

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == SPEECH_REQUEST && resultCode == RESULT_OK) {
			List<String> items = data
					.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

			showItems(items);
		} else if (requestCode == SPEECH_REQUEST && resultCode == RESULT_CANCELED) {
			mTextView.setText("Tap and I will listen, cancel edition");
		}
	}

	private void showItems(List<String> items) {
		String allItems = "";
		// only one item containing all words in this implementation
		for (String item : items) {
			Log.d(TAG, "item: " + item);
			allItems += item + " ";
			if (item.toLowerCase().contains("bitcoin")) {
				mTextView
						.setText("To the moon! [I heard Bitcoin, tap and I'll listen again, and try not to get distracted]");
				Log.d(TAG, "bitcoin was spoken");
				return;
			}
		}
		mTextView.setText(allItems + "\nTap again and I will listen.");
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
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "I'm listening");
		startActivityForResult(intent, SPEECH_REQUEST);
	}

}
