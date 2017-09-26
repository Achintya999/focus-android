package org.mozilla.focus.activity;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RemoteViews;
import android.widget.Toast;

import org.mozilla.focus.R;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Achintya on 9/26/2017.
 */

public class FocusWidgetActivity extends Activity {

    private static final int REQ_CODE_SPEECH_INPUT = 100;
    int widgetId;
    private EditText ed;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.focus_widget);

        setResult(RESULT_CANCELED);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {

            widgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);


        }

        final AppWidgetManager widgetManager = AppWidgetManager.getInstance(this);

        final RemoteViews views = new RemoteViews(this.getPackageName(), R.layout.search_widget);

        ed = (EditText) findViewById(R.id.edt_text);
        Button b = (Button) findViewById(R.id.button);
        ImageButton speechButton = (ImageButton) findViewById(R.id.microphone);
        speechButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSpeechToText();
            }
        });


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ed.getText().toString()));
                PendingIntent pendingIntent = PendingIntent.getActivity(FocusWidgetActivity.this, 0, intent, 0);

                views.setOnClickPendingIntent(R.id.search_focus, pendingIntent);

                widgetManager.updateAppWidget(widgetId, views);

                Intent result = new Intent();
                result.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
                setResult(RESULT_OK, result);
                finish();
                Toast.makeText(getApplicationContext(), ed.getText().toString() + " is set and you are ready to go ", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void startSpeechToText() {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak The Site Where You Want To Go!");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT:
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    ed.setText("http://" + result.get(0));
//                    if (ed != null) {
//                        Intent intent = new Intent(Intent.ACTION_VIEW);
//                        intent.setData(Uri.parse("http://" + ed.getText().toString()));
//                        startActivity(intent);
//                    }

                }
                break;
        }

    }
}

