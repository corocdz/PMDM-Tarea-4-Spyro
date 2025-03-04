package dam.pmdm.spyrothedragon.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dam.pmdm.spyrothedragon.R;
import dam.pmdm.spyrothedragon.models.Collectible;
import dam.pmdm.spyrothedragon.VideoActivity;

public class CollectiblesAdapter extends RecyclerView.Adapter<CollectiblesAdapter.CollectiblesViewHolder> {

    private List<Collectible> list;
    private int gemClickCount = 0; // Número de toques en la gema
    private long lastClickTime = 0; // Tiempo del último click
    private static final long CLICK_THRESHOLD = 1000; // 1 segundo entre toques permitidos
    private static final int REQUIRED_CLICKS = 4; // Número de clicks requeridos

    public CollectiblesAdapter(List<Collectible> collectibleList) {
        this.list = collectibleList;
    }

    @Override
    public CollectiblesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
        return new CollectiblesViewHolder(view);
    }

    // Aquí hemos modificado el comportamiento para añadir el easter egg del video
    @Override
    public void onBindViewHolder(CollectiblesViewHolder holder, int position) {
        Collectible collectible = list.get(position);
        holder.nameTextView.setText(collectible.getName());

        Context context = holder.itemView.getContext();
        int imageResId = context.getResources().getIdentifier(collectible.getImage(), "drawable", context.getPackageName());
        holder.imageImageView.setImageResource(imageResId);

        // Detectamos si el item pulsado es la gema
        if ("gems".equals(collectible.getImage())) {
            holder.imageImageView.setOnClickListener(view -> {
                long currentTime = System.currentTimeMillis();

                // Si el tiempo entre clics es mayor al umbral (1 segundo), reseteamos el contador
                if (currentTime - lastClickTime > CLICK_THRESHOLD) {
                    gemClickCount = 0;
                }

                gemClickCount++; // Aumentamos contador de toques en gema
                lastClickTime = currentTime; // Cambiamos el tiempo del último toque

                if (gemClickCount >= REQUIRED_CLICKS) { // Si se consiguen el número de toques en la gema en el tiempo establecido, lanzamos easter egg del video
                    gemClickCount = 0; // Reiniciamos el contador
                    Intent intent = new Intent(context, VideoActivity.class);
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class CollectiblesViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        ImageView imageImageView;

        public CollectiblesViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name);
            imageImageView = itemView.findViewById(R.id.image);
        }
    }
}
