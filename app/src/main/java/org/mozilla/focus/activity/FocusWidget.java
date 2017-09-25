package org.mozilla.focus.activity;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import org.mozilla.focus.R;

/**
 * Created by Achintya on 9/25/2017.
 */

public class FocusWidget  extends AppWidgetProvider {




    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {


        for(int i = 0; i < appWidgetIds.length; i++) {

            int appWidgetId = appWidgetIds[i];
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.search_widget);
            views.setOnClickPendingIntent(R.id.search_focus, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }

    }
}