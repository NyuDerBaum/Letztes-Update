package de.gruppe16.stundenplaner.ui.planer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;

import java.util.List;

import de.gruppe16.stundenplaner.R;

import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.GradientDrawable;

// Adapter für die Anzeige von Rasterelementen in einer RecyclerView
public class GridInsertionAdapter extends RecyclerView.Adapter<GridInsertionAdapter.MyViewHolder> {

    final List<TimetableCell> dataList; // Liste der Daten (TimetableCell-Objekte)
    private final Context context; // Kontext für Layoutinflation und Ressourcen
    private final int numberOfColumns;
    private final int numberOfRows;
    private final GridItemClickListener clickListener; // Klick-Listener für Rasterelemente

    // Interface für Klick-Events auf Rasterelementen
    public interface GridItemClickListener {
        void onGridItemClick(int position); // Methode zur Behandlung von Klicks
    }

    // Konstruktor für den Adapter
    public GridInsertionAdapter(List<TimetableCell> dataList, Context context, int numberOfColumns, int numberOfRows, GridItemClickListener clickListener) {
        this.dataList = dataList; // Setze die Datenliste
        this.context = context; // Setze den Kontext
        this.numberOfColumns = numberOfColumns; // Setze die Anzahl der Spalten
        this.clickListener = clickListener; // Setze den Klick-Listener
        this.numberOfRows = numberOfRows; // Setze die Anzahl der Zeilen
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate das Layout für jedes Rasterelement
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new MyViewHolder(view); // Rückgabe des ViewHoldings
    }

    // Methode zur Ermittlung der ActionBar-Größe
    public static int getActionBarSize(Context context) {
        TypedValue tv = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true); // Aktionbar-Größe aus den Attributen holen
        return TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics()); // Umrechnung in Pixel
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // Setze die Textwerte für die TextViews basierend auf den Daten
        holder.textView1.setText(dataList.get(position).getTitle());
        holder.textView2.setText(dataList.get(position).getRoom());
        holder.textView3.setText(dataList.get(position).getProf());

        // Set the background resource with the border
        holder.itemView.setBackgroundResource(R.drawable.item_border);

        // Set the background color of the item_background layer
        int backgroundColor = dataList.get(position).getColor();
        LayerDrawable layerDrawable = (LayerDrawable) holder.itemView.getBackground();
        GradientDrawable itemBackground = (GradientDrawable) layerDrawable.findDrawableByLayerId(R.id.item_background);
        itemBackground.setColor(backgroundColor);


        // Berechnung der Breite und Höhe der Rasterelemente
        int screenWidth = context.getResources().getDisplayMetrics().widthPixels; // Bildschirmbreite abrufen
        int itemWidth = screenWidth / numberOfColumns; // Breite eines Elements
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams(); // LayoutParams des Elements abrufen
        layoutParams.width = itemWidth; // Breite setzen

        int itemHeight = (context.getResources().getDisplayMetrics().heightPixels - getActionBarSize(context)) / numberOfRows; // Höhe eines Elements

        if (position < numberOfColumns) {  // Wenn das Element in der ersten Reihe ist
            layoutParams.height = (int) (itemHeight * 0.75); // Höhe anpassen
        } else {
            layoutParams.height = itemHeight; // Standardhöhe für andere Zeilen
        }
        holder.itemView.setLayoutParams(layoutParams); // LayoutParams anwenden

        // Setze einen OnClickListener für das Rasterelement
        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onGridItemClick(position); // Ruft die Methode des ClickListeners auf
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size(); // Anzahl der Elemente in der Liste zurückgeben
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView); // Aufruf der Superklasse
        GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager(); // Abrufen des LayoutManagers
        if (layoutManager != null) {
            layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    // Elemente in der ersten Reihe nehmen die volle Spaltenbreite ein
                    return 1; // Ein Element pro Spalte
                }
            });
        }
    }

    // ViewHolder-Klasse zur Speicherung der Referenzen auf die Views
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView1; // TextView für den Titel
        TextView textView2; // TextView für den Raum
        TextView textView3; // TextView für den Professor

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialisiere die TextViews
            textView1 = itemView.findViewById(R.id.textView1);
            textView2 = itemView.findViewById(R.id.textView2);
            textView3 = itemView.findViewById(R.id.textView3);
        }
    }
}
