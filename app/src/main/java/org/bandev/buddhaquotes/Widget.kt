package org.bandev.buddhaquotes

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

class Widget : AppWidgetProvider() {

    var widgetButton: String = "org.bandev.buddhaquotes.WIDGET_BUTTON"
    private var widgetLike: String = "org.bandev.buddhaquotes.WIDGET_LIKE"
    private var quoteCurrent: String = "Test"
    private var x = 0

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {

        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action.equals(widgetButton, ignoreCase = true)) {
            val views = RemoteViews(context.packageName, R.layout.widget)
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(
                ComponentName(
                    context,
                    Widget::class.java
                )
            )
            x++
            quoteCurrent = Quotes().random(0)
            views.setTextViewText(R.id.appwidget_text, quoteCurrent)
            appWidgetManager.updateAppWidget(appWidgetIds, views)
        } else if (intent.action.equals(widgetLike, ignoreCase = true)) {
            val views = RemoteViews(context.packageName, R.layout.widget)
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(
                ComponentName(
                    context,
                    Widget::class.java
                )
            )
            x++
            Core(context).Lists().newItemString("Favourites", quoteCurrent)
            views.setImageViewResource(R.id.like, R.drawable.heart_full_red)
            appWidgetManager.updateAppWidget(appWidgetIds, views)
        }
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        // When the user deletes the widget, delete the preference associated with it.
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
// Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.widget)
    views.setImageViewResource(R.id.refresh, R.drawable.ic_refresh_black)
    //views.setImageViewResource(R.id.like, R.drawable.like)
    views.setOnClickPendingIntent(R.id.refresh, getPenIntent(context))
    views.setImageViewResource(R.id.logo, R.drawable.ic_buddha)
    //views.setOnClickPendingIntent(R.id.like, getPenIntent2(context))

    views.setTextViewText(R.id.appwidget_text, Quotes().random(0))

    getPenIntent(context)
    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}

fun getPenIntent(context: Context): PendingIntent {
    val intent = Intent(context, Widget::class.java)
    intent.action = Widget().widgetButton
    return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
}

