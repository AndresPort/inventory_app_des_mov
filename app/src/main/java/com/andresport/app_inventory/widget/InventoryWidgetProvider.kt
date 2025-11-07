package com.andresport.app_inventory.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.andresport.app_inventory.R
import com.andresport.app_inventory.model.InventoryRepository
import com.andresport.app_inventory.view.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

class InventoryWidgetProvider : AppWidgetProvider() {

    companion object {
        private const val ACTION_TOGGLE_VISIBILITY = "com.andresport.app_inventory.widget.ACTION_TOGGLE_VISIBILITY"
        private const val PREFS_NAME = "com.andresport.app_inventory.widget.InventoryWidget"
        private const val PREF_IS_VISIBLE = "is_balance_visible_"
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (ACTION_TOGGLE_VISIBILITY == intent.action) {
            val appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
            if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
                toggleVisibility(context, appWidgetId)
            }
        }
        super.onReceive(context, intent)
    }

    private fun toggleVisibility(context: Context, appWidgetId: Int) {
        val prefs = context.getSharedPreferences(PREFS_NAME, 0)
        val isVisible = prefs.getBoolean(PREF_IS_VISIBLE + appWidgetId, false)
        prefs.edit().putBoolean(PREF_IS_VISIBLE + appWidgetId, !isVisible).apply()

        updateAppWidget(context, AppWidgetManager.getInstance(context), appWidgetId)
    }

    private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val repository = InventoryRepository(context)
        val prefs = context.getSharedPreferences(PREFS_NAME, 0)
        val isBalanceVisible = prefs.getBoolean(PREF_IS_VISIBLE + appWidgetId, false)

        // Usamos una coroutine para llamar a la base de datos en un hilo secundario
        CoroutineScope(Dispatchers.IO).launch {
            val balance = repository.getTotalInventoryValue()
            val views = RemoteViews(context.packageName, R.layout.inventory_widget)

            if (isBalanceVisible) {
                val symbols = DecimalFormatSymbols(Locale("es", "ES"))
                val formatter = DecimalFormat("#,##0.00", symbols)
                views.setTextViewText(R.id.widget_balance_text, "$ ${formatter.format(balance)}")
                views.setImageViewResource(R.id.widget_toggle_visibility, R.drawable.ic_visibility_off)
            } else {
                views.setTextViewText(R.id.widget_balance_text, "$ ****")
                views.setImageViewResource(R.id.widget_toggle_visibility, R.drawable.ic_visibility)
            }

            // Intent para manejar el clic en el ícono del ojo
            val toggleIntent = Intent(context, InventoryWidgetProvider::class.java).apply {
                action = ACTION_TOGGLE_VISIBILITY
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            }
            val togglePendingIntent = PendingIntent.getBroadcast(context, appWidgetId, toggleIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            views.setOnClickPendingIntent(R.id.widget_toggle_visibility, togglePendingIntent)

            // Intent para ir a la pantalla de login
            val manageIntent = Intent(context, MainActivity::class.java)
            val managePendingIntent = PendingIntent.getActivity(context, 0, manageIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            views.setOnClickPendingIntent(R.id.widget_settings_icon, managePendingIntent)

            // Actualizamos el widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
