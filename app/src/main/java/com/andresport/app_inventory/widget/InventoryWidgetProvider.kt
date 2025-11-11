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

/**
 * Esta clase es para gestionar el widget de inventario.
 * Se encarga de actualizar la vista del widget y de manejar las interacciones del usuario.
 */
class InventoryWidgetProvider : AppWidgetProvider() {

    // Objeto compañero para definir constantes.
    companion object {
        // Acción para el botón de visibilidad del saldo.
        private const val ACTION_TOGGLE_VISIBILITY = "com.andresport.app_inventory.widget.ACTION_TOGGLE_VISIBILITY"
        // Nombre para las SharedPreferences del widget.
        private const val PREFS_NAME = "com.andresport.app_inventory.widget.InventoryWidget"
        // Prefijo para la clave de visibilidad del saldo.
        private const val PREF_IS_VISIBLE = "is_balance_visible_"
    }

    /**
     * La función se ejecuta cuando se actualiza el widget.
     * Itera sobre todos los widgets y llama a updateAppWidget para cada uno.
     */
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    /**
     * La función recibe todos los intents dirigidos al widget.
     * Si la acción es para cambiar la visibilidad, llama a la función correspondiente.
     */
    override fun onReceive(context: Context, intent: Intent) {
        if (ACTION_TOGGLE_VISIBILITY == intent.action) {
            val appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
            if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
                toggleVisibility(context, appWidgetId)
            }
        }
        super.onReceive(context, intent)
    }

    /**
     * Esta función es para cambiar el estado de visibilidad del saldo.
     * Guarda el nuevo estado en SharedPreferences y actualiza el widget.
     */
    private fun toggleVisibility(context: Context, appWidgetId: Int) {
        // Se obtienen las SharedPreferences.
        val prefs = context.getSharedPreferences(PREFS_NAME, 0)
        // Se obtiene el estado actual de visibilidad.
        val isVisible = prefs.getBoolean(PREF_IS_VISIBLE + appWidgetId, false)
        // Se guarda el estado opuesto de visibilidad.
        prefs.edit().putBoolean(PREF_IS_VISIBLE + appWidgetId, !isVisible).apply()

        // Se actualiza el widget.
        updateAppWidget(context, AppWidgetManager.getInstance(context), appWidgetId)
    }

    /**
     * Esta función es para actualizar la vista de un widget específico.
     * Obtiene los datos del inventario y actualiza la interfaz de usuario del widget.
     */
    private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        // Se inicializa el repositorio para acceder a los datos.
        val repository = InventoryRepository(context)
        // Se obtienen las SharedPreferences.
        val prefs = context.getSharedPreferences(PREFS_NAME, 0)
        // Se obtiene el estado actual de visibilidad.
        val isBalanceVisible = prefs.getBoolean(PREF_IS_VISIBLE + appWidgetId, false)

        // Se usa una coroutine para obtener los datos en un hilo secundario.
        CoroutineScope(Dispatchers.IO).launch {
            // Se obtiene el valor total del inventario.
            val balance = repository.getTotalInventoryValue()
            // Se crea la vista remota para el widget.
            val views = RemoteViews(context.packageName, R.layout.inventory_widget)

            // Si el saldo es visible, se formatea y se muestra.
            if (isBalanceVisible) {
                val symbols = DecimalFormatSymbols(Locale("es", "ES"))
                val formatter = DecimalFormat("#,##0.00", symbols)
                views.setTextViewText(R.id.widget_balance_text, "$ ${formatter.format(balance)}")
                views.setImageViewResource(R.id.widget_toggle_visibility, R.drawable.ic_visibility_off)
            } else {
                // Si el saldo está oculto, se muestran asteriscos.
                views.setTextViewText(R.id.widget_balance_text, "$ ****")
                views.setImageViewResource(R.id.widget_toggle_visibility, R.drawable.ic_visibility)
            }

            // Se crea un intent para manejar el clic en el ícono de visibilidad.
            val toggleIntent = Intent(context, InventoryWidgetProvider::class.java).apply {
                action = ACTION_TOGGLE_VISIBILITY
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            }
            val togglePendingIntent = PendingIntent.getBroadcast(context, appWidgetId, toggleIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            views.setOnClickPendingIntent(R.id.widget_toggle_visibility, togglePendingIntent)

            // Se crea un intent para abrir la aplicación al hacer clic en el ícono de configuración.
            val manageIntent = Intent(context, MainActivity::class.java)
            val managePendingIntent = PendingIntent.getActivity(context, 0, manageIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            views.setOnClickPendingIntent(R.id.widget_settings_icon, managePendingIntent)

            // Se actualiza el widget con las nuevas vistas.
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
