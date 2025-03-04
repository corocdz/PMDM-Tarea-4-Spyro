package dam.pmdm.spyrothedragon.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dam.pmdm.spyrothedragon.R;
import dam.pmdm.spyrothedragon.models.Character;
import dam.pmdm.spyrothedragon.models.FireView;

public class CharactersAdapter extends RecyclerView.Adapter<CharactersAdapter.CharactersViewHolder> {

    private List<Character> list;

    public CharactersAdapter(List<Character> charactersList) {
        this.list = charactersList;
    }

    @Override
    public CharactersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
        return new CharactersViewHolder(view);
    }


    // Modificación para el easter egg de mantener pulsado a Spyro
    @Override
    public void onBindViewHolder(CharactersViewHolder holder, int position) {
        Character character = list.get(position);
        holder.nameTextView.setText(character.getName());

        // Cargar la imagen
        int imageResId = holder.itemView.getContext().getResources().getIdentifier(
                character.getImage(), "drawable",
                holder.itemView.getContext().getPackageName());
        holder.imageImageView.setImageResource(imageResId);

        // Si mantemos pulsado el personaje de Spyro, habilitamos la animación del easter egg
        if ("Spyro".equals(character.getName())) {
            holder.itemView.setOnLongClickListener(v -> {
                showFireEffect(v);
                return true;
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class CharactersViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        ImageView imageImageView;

        public CharactersViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name);
            imageImageView = itemView.findViewById(R.id.image);
        }
    }


    // Metodo con la animación del easter egg
    private void showFireEffect(View view) {
        Context context = view.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflamos el layout con la animación
        View fireViewLayout = inflater.inflate(R.layout.fire_layout, null);

        // Obtenemos la referencia del FireView
        FireView fireView = fireViewLayout.findViewById(R.id.fireView);
        fireView.setVisibility(View.VISIBLE);

        // Añadimos la vista al layout principal
        FrameLayout rootLayout = ((Activity) context).findViewById(android.R.id.content);
        rootLayout.addView(fireViewLayout);

        // Eliminamos la animación después de 2 segundos
        new Handler().postDelayed(() -> rootLayout.removeView(fireViewLayout), 2000);
    }
}
