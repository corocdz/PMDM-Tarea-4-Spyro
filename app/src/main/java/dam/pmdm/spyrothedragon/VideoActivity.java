package dam.pmdm.spyrothedragon;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


/**
 * Actividad encargada de mostrar un video lanzado mediante la realización del easter egg.
 * Una vez finalizado el video, redirige al usuario a la pantalla de coleccionables en MainActivity.
 */
public class VideoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video); // Layout del diseño de la actividad

        // Referencia del videoView en diseño
        VideoView videoView = findViewById(R.id.videoView);

        // Construye el URI del  video almacenado en tal directorio
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.easter_egg);
        videoView.setVideoURI(videoUri);

        // Listener que se activa cuando el video termina de reproducirse
        videoView.setOnCompletionListener(mp -> {
            // Cuando el video termine, redirigimos al fragmento de Coleccionables
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("goToCollectibles", true); // Redirigimos a coleccionables
            startActivity(intent); // Inicia MainActivity
            finish(); // Finaliza VideoActivity para que no se quede en la pila de actividades
        });

        videoView.start(); // Comienza la reproducción del video.
    }
}
