package dam.pmdm.spyrothedragon.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import dam.pmdm.spyrothedragon.MainActivity;
import dam.pmdm.spyrothedragon.R;

/**
 * Fragmento de tutorial que guía al usuario a través de diferentes pantallas de introducción al uso de la app y sus secciones.
 * Incluye animaciones, efectos de sonido y un par de transiciones entre pantallas. Administra la navegación y la configuración de preferencias.
 * IMPORTANTE. todas las pantallas del tutorial ha sido diseñadas en un único fragment xml (framgent_tutorial.xml). Esto no es muy práctico ni recomendable en la realidad ya que supone una organización tediosa de todos los elementos, ademas que no contribuye mucho a la reutilización de código que se verá en este código. Lamento haberlo hecho así pero no voy bien de tiempo y "mudar o fraccioanr" esto me llevarí más tiempo que no tengo.
 */

public class TutorialFragment extends Fragment {

    // Vistas del tutorial o pantallas
    private View tutorialWelcome, tutorialCharacters, tutorialWorlds, tutorialCollectibles, tutorialAbout, tutorialEnd;

    // Sonido de interacción y música de fondo
    private MediaPlayer soundEffect;
    private MediaPlayer backgroundMusic;

    // Preferencias del usuario
    private SharedPreferences preferences;

    // Listener para el evento de completar el tutorial
    private OnTutorialCompleteListener tutorialListener;

    // Control del listener
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnTutorialCompleteListener) {
            tutorialListener = (OnTutorialCompleteListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnTutorialCompleteListener");
        }
    }

    // Interfaz que permite el uso de showInfoDialog en MainAcitivty para mostrar la información del menú about de manera automática
    public interface OnTutorialCompleteListener {
        void onShowAboutDialog();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflamos el diseño del tutorial
        return inflater.inflate(R.layout.fragment_tutorial, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Iniciamos la música de fondo desde el inicio del tutorial
        backgroundMusic = MediaPlayer.create(requireContext(), R.raw.spyro_main_theme);
        backgroundMusic.setLooping(true); // Hacemos de la música un bucle por si fuera necesario
        backgroundMusic.start();

        // Inicializamos las vistas del tutorial
        tutorialWelcome = view.findViewById(R.id.tutorial_welcome);
        tutorialCharacters = view.findViewById(R.id.tutorial_characters);
        tutorialWorlds = view.findViewById(R.id.tutorial_worlds);
        tutorialCollectibles = view.findViewById(R.id.tutorial_collectibles);
        tutorialAbout = view.findViewById(R.id.tutorial_about);
        tutorialEnd = view.findViewById(R.id.tutorial_end);

        // Inicializamos los botones y vistas de iconos como el círculo
        View btnNextWelcome = view.findViewById(R.id.btnNextWelcome);
        View btnNextCharacters = view.findViewById(R.id.btnNextCharacters);
        View btnNextWorlds = view.findViewById(R.id.btnNextWorlds);
        View btnNextCollectibles = view.findViewById(R.id.btnNextCollectibles);
        View btnNextAbout = view.findViewById(R.id.btnNextAbout);
        View btnSkip = view.findViewById(R.id.btnSkip);
        View btnFinish = view.findViewById(R.id.btnFinish);

        View circulo = view.findViewById(R.id.circle_highlight);
        circulo.setVisibility(View.INVISIBLE);


        // Configuración de preferencias
        preferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        boolean tutorialSkipped = preferences.getBoolean("guide_skipped", false);

        // Si el tutorial ha terminado, lo cerramos
        if (tutorialSkipped) {
            closeTutorial();
            return;
        }

        // Acción al pulsar el botón de "Siguiente" en la pantalla 1: Bienvenida
        btnNextWelcome.setOnClickListener(v -> {
            // Lanzamos sonido al apretar botón
            playSoundEffect();

            // Mostramos la siguiente pantalla antes de ocultar la actual
            tutorialCharacters.setVisibility(View.VISIBLE);

            // Aplicamos una animación de desvanecimiento sobre la siguiente pantalla
            Animation fadeIn = new AlphaAnimation(0f, 1f);
            fadeIn.setDuration(800);
            fadeIn.setFillAfter(true);
            tutorialCharacters.startAnimation(fadeIn);

            // Animación de desvanecimiento más notoria para la pantalla que se cierra (la actual)
            Animation fadeOut = new AlphaAnimation(1f, 0f);
            fadeOut.setDuration(600);
            fadeOut.setFillAfter(true);
            tutorialWelcome.startAnimation(fadeOut);

            // Ocultamos la pantalla después de la animación
            tutorialWelcome.postDelayed(() -> tutorialWelcome.setVisibility(View.GONE), 600);

            // Llamada a método de animación de la siguiente pantalla.
            animateCharacterTutorial(view);
        });

        // Acción al pulsar el botón de "Siguiente" en la pantalla 2: Sección de Personajes
        btnNextCharacters.setOnClickListener(v -> {

            // Sonido al pulsar sobre el botón
            playSoundEffect();

            // Mostramos la siguiente pantalla antes de ocultar la actual
            tutorialWorlds.setVisibility(View.VISIBLE);


            // Animación de desvanecimiento
            Animation fadeIn = new AlphaAnimation(0f, 1f);
            fadeIn.setDuration(800);
            fadeIn.setFillAfter(true);
            tutorialWorlds.startAnimation(fadeIn);

            // Ocultamos la pantalla actual
            tutorialCharacters.postDelayed(() -> tutorialCharacters.setVisibility(View.GONE), 200);
            hideCharactersTutorial(view);

            // Movemos el círculo directamente a  unas coordenadas específicas
            ImageView circle = view.findViewById(R.id.circle_highlight);
            circle.setX(550);
            circle.setY(2596);

            // Navegamos automáticamente a la pantalla de mundos de Spyro
            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate(R.id.navigation_worlds);

            // Llamada a método de animación de la siguiente pantalla.
            animateWorldsTutorial(view);

        });

        // Acción al pulsar el botón de "Siguiente" en la pantalla 3: Sección de Mundos
        btnNextWorlds.setOnClickListener(v -> {

            playSoundEffect();

            // Mostramos la siguiente pantalla
            tutorialCollectibles.setVisibility(View.VISIBLE);

            // Animación de desvanecimiento
            Animation fadeIn = new AlphaAnimation(0f, 1f);
            fadeIn.setDuration(800);
            fadeIn.setFillAfter(true);
            tutorialCollectibles.startAnimation(fadeIn);

            // Ocultamos pantalla actual
            tutorialWorlds.postDelayed(() -> tutorialWorlds.setVisibility(View.GONE), 200);
            hideWorldsTutorial(view);

            // Movemos el círculo directamente a coordenadas específicas
            ImageView circle = view.findViewById(R.id.circle_highlight);
            circle.setX(1000);
            circle.setY(2596);

            // Navegamos automáticamente a la pantalla de los coleccionables
            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate(R.id.navigation_collectibles);

            // Llamada a método de animación de la siguiente pantalla.
            animateCollectiblesTutorial(view);

        });


        // Acción al pulsar el botón de "Siguiente" en la pantalla 4: Sección de Coleccionables
        btnNextCollectibles.setOnClickListener(v -> {

            playSoundEffect();

            tutorialAbout.setVisibility(View.VISIBLE);

            Animation fadeIn = new AlphaAnimation(0f, 1f);
            fadeIn.setDuration(800);
            fadeIn.setFillAfter(true);
            tutorialAbout.startAnimation(fadeIn);

            tutorialCollectibles.postDelayed(() -> tutorialCollectibles.setVisibility(View.GONE), 200);
            hideCollectiblesTutorial(view);

            ImageView circle = view.findViewById(R.id.circle_highlight);
            circle.setX(1140);
            circle.setY(-25);

            // Cambio de tamaño al circulo
            circle.setScaleX(0.7f);
            circle.setScaleY(0.7f);

            // Aseguramos que el círculo está por encima de la ActionBar
            circle.bringToFront();
            circle.setElevation(10);

            animateAboutTutorial(view);

        });

        // Acción al pulsar el botón de "Siguiente" en la pantalla 5: Intro al menú About
        btnNextAbout.setOnClickListener(v -> {

            // Mostramos la informacion del menu About
            if (tutorialListener != null) {
                tutorialListener.onShowAboutDialog();
            }

            tutorialEnd.setVisibility(View.VISIBLE);

            Animation fadeIn = new AlphaAnimation(0f, 1f);
            fadeIn.setDuration(800);
            fadeIn.setFillAfter(true);
            tutorialEnd.startAnimation(fadeIn);
            tutorialAbout.postDelayed(() -> tutorialAbout.setVisibility(View.GONE), 200);
            hideAboutTutorial(view);

            playSoundEffect();

        });

        // Acción al pulsar el botón de "Finalizar" en la pantalla 6: Resumen de la guía
        btnFinish.setOnClickListener(v -> {

            // Paramos la música de fondo
            stopBackgroundMusic();

            // Llamamos a los métodos de MainActivity para activar el bottomNavigationMenu y la toolbar
            ((MainActivity) requireActivity()).enableBottomNavigation();
            ((MainActivity) requireActivity()).enableToolbar();

            // Aplicamos cambio a las preferencias del usuario. El tutorial con este valor "true" significa que ha acabado
            preferences.edit().putBoolean("guide_skipped", true).apply();

            // Cerramos tutorial
            closeTutorial();

            // Llamamos al método de MainActivity para activar los elemenos de bottomNavigationMenu y ToolBar por si acaso
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).onTutorialSkipped();
            }

        });


        // Acción de pulsar en "Omitir Guía" en cualquiera de las 5 primeras pantallas
        btnSkip.setOnClickListener(v -> {

            // Funcionamiento similar al btnFinish

            stopBackgroundMusic();
            preferences.edit().putBoolean("guide_skipped", true).apply();
            closeTutorial();


            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).onTutorialSkipped();
            }

        });
    }

    // Método que nos permite controlar ciertos aspectos de la pantalla. En este caso serán el bocadillo con el diálogo de la instrucciones y un icono de un círculo.
    private void animateCharacterTutorial(View view) {

        // Iniciamos vistas
        View bocadillo = view.findViewById(R.id.tutorial_characters);
        View circulo = view.findViewById(R.id.circle_highlight);

        // Las hacemos visibles
        bocadillo.setVisibility(View.VISIBLE);
        circulo.setVisibility(View.VISIBLE);

        // Añadimos unas animaciones a las vistas también
        Animation fadeIn = new AlphaAnimation(0f, 1f);
        fadeIn.setDuration(800);
        fadeIn.setFillAfter(true);
        bocadillo.startAnimation(fadeIn);
        circulo.startAnimation(fadeIn);

        // Animación de "rebote" para el icono del círculo
        TranslateAnimation bounce = new TranslateAnimation(0, 0, -20, 0);
        bounce.setDuration(600);
        bounce.setInterpolator(new BounceInterpolator());
        bounce.setRepeatMode(Animation.REVERSE);
        bounce.setRepeatCount(Animation.INFINITE);
        circulo.startAnimation(bounce);
    }

    // Método que nos permite controlar ciertos aspectos de la pantalla. En este caso serán el bocadillo con el diálogo de la instrucciones y un icono de un círculo.
    private void animateWorldsTutorial(View view) {
        View bocadillo = view.findViewById(R.id.tutorial_worlds);
        View circulo = view.findViewById(R.id.circle_highlight);

        bocadillo.setVisibility(View.VISIBLE);
        circulo.setVisibility(View.VISIBLE);

        Animation fadeIn = new AlphaAnimation(0f, 1f);
        fadeIn.setDuration(800);
        fadeIn.setFillAfter(true);

        bocadillo.startAnimation(fadeIn);
        circulo.startAnimation(fadeIn);

        TranslateAnimation bounce = new TranslateAnimation(0, 0, -20, 0);
        bounce.setDuration(600);
        bounce.setInterpolator(new BounceInterpolator());
        bounce.setRepeatMode(Animation.REVERSE);
        bounce.setRepeatCount(Animation.INFINITE);
        circulo.startAnimation(bounce);
    }

    // Método que nos permite controlar ciertos aspectos de la pantalla. En este caso serán el bocadillo con el diálogo de la instrucciones y un icono de un círculo.
    private void animateCollectiblesTutorial(View view) {
        View bocadillo = view.findViewById(R.id.tutorial_collectibles);
        View circulo = view.findViewById(R.id.circle_highlight);

        bocadillo.setVisibility(View.VISIBLE);
        circulo.setVisibility(View.VISIBLE);

        Animation fadeIn = new AlphaAnimation(0f, 1f);
        fadeIn.setDuration(800);
        fadeIn.setFillAfter(true);

        bocadillo.startAnimation(fadeIn);
        circulo.startAnimation(fadeIn);

        TranslateAnimation bounce = new TranslateAnimation(0, 0, -20, 0);
        bounce.setDuration(600);
        bounce.setInterpolator(new BounceInterpolator());
        bounce.setRepeatMode(Animation.REVERSE);
        bounce.setRepeatCount(Animation.INFINITE);
        circulo.startAnimation(bounce);
    }

    // Método que nos permite controlar ciertos aspectos de la pantalla. En este caso serán el bocadillo con el diálogo de la instrucciones y un icono de un círculo.
    private void animateAboutTutorial(View view) {
        View bocadillo = view.findViewById(R.id.tutorial_about);
        View circulo = view.findViewById(R.id.circle_highlight);

        bocadillo.setVisibility(View.VISIBLE);
        circulo.setVisibility(View.VISIBLE);

        Animation fadeIn = new AlphaAnimation(0f, 1f);
        fadeIn.setDuration(800);
        fadeIn.setFillAfter(true);

        bocadillo.startAnimation(fadeIn);
        circulo.startAnimation(fadeIn);

        TranslateAnimation bounce = new TranslateAnimation(0, 0, -10, 0);
        bounce.setDuration(600);
        bounce.setInterpolator(new BounceInterpolator());
        bounce.setRepeatMode(Animation.REVERSE);
        bounce.setRepeatCount(Animation.INFINITE);
        circulo.startAnimation(bounce);
    }

    // Metodo para borrar el contenido (bocadillos e iconos) generados por una vista
    private void hideCollectiblesTutorial(View view) {
        View bocadillo = view.findViewById(R.id.tutorial_collectibles);
        View circulo = view.findViewById(R.id.circle_highlight);

        // Si bocadillo e icono no son nulos y son visibles, procedemos a deshabilitar su funcionamiento
        if (bocadillo != null && circulo != null) {
            if (bocadillo.getVisibility() == View.VISIBLE || circulo.getVisibility() == View.VISIBLE) {

                // Animación de desvanecimiento
                Animation fadeOut = new AlphaAnimation(1f, 0f);
                fadeOut.setDuration(500);
                fadeOut.setFillAfter(true);

                bocadillo.startAnimation(fadeOut);
                circulo.startAnimation(fadeOut);

                // Deshabilitamos interacciones
                bocadillo.setEnabled(false);
                bocadillo.setClickable(false);
                bocadillo.setFocusable(false);
                bocadillo.setFocusableInTouchMode(false);

                // Deshabilitamos el botón dentro del bocadillo
                Button btnNext4 = view.findViewById(R.id.btnNextCollectibles);
                if (btnNext4 != null) {
                    btnNext4.setEnabled(false);
                    btnNext4.setClickable(false);
                    btnNext4.setFocusable(false);
                }

                // Los hacemos invisibles y removemos su espacio ocupado
                bocadillo.postDelayed(() -> {
                    bocadillo.setVisibility(View.GONE);
                    bocadillo.setLayoutParams(new ViewGroup.LayoutParams(0, 0));
                }, 500);

                circulo.postDelayed(() -> {
                    circulo.setVisibility(View.GONE);
                    circulo.setLayoutParams(new ViewGroup.LayoutParams(0, 0));
                }, 500);
            }
        }
    }

    // Metodo para borrar el contenido (bocadillos e iconos) generados por una vista
    private void hideAboutTutorial(View view) {
        View bocadillo = view.findViewById(R.id.tutorial_about);
        View circulo = view.findViewById(R.id.circle_highlight);

        // Comportamiento ya visto anteriormente

        if (bocadillo != null && circulo != null) {
            if (bocadillo.getVisibility() == View.VISIBLE || circulo.getVisibility() == View.VISIBLE) {

                Animation fadeOut = new AlphaAnimation(1f, 0f);
                fadeOut.setDuration(500);
                fadeOut.setFillAfter(true);

                bocadillo.startAnimation(fadeOut);
                circulo.startAnimation(fadeOut);

                bocadillo.setEnabled(false);
                bocadillo.setClickable(false);
                bocadillo.setFocusable(false);
                bocadillo.setFocusableInTouchMode(false);

                Button btnNext4 = view.findViewById(R.id.btnNextCollectibles);
                if (btnNext4 != null) {
                    btnNext4.setEnabled(false);
                    btnNext4.setClickable(false);
                    btnNext4.setFocusable(false);
                }

                Button btnSkip = view.findViewById(R.id.btnSkip);
                if (btnSkip != null) {
                    btnSkip.setEnabled(false);
                    btnSkip.setClickable(false);
                    btnSkip.setFocusable(false);
                    btnSkip.setVisibility(View.GONE);
                }

                bocadillo.postDelayed(() -> {
                    bocadillo.setVisibility(View.GONE);
                    bocadillo.setLayoutParams(new ViewGroup.LayoutParams(0, 0));
                }, 500);

                circulo.postDelayed(() -> {
                    circulo.setVisibility(View.GONE);
                    circulo.setLayoutParams(new ViewGroup.LayoutParams(0, 0));
                }, 500);
            }
        }
    }

    private void hideWorldsTutorial(View view) {
        View bocadillo = view.findViewById(R.id.tutorial_worlds);
        View circulo = view.findViewById(R.id.circle_highlight);

        if (bocadillo != null && circulo != null) {
            if (bocadillo.getVisibility() == View.VISIBLE || circulo.getVisibility() == View.VISIBLE) {

                Animation fadeOut = new AlphaAnimation(1f, 0f);
                fadeOut.setDuration(500);
                fadeOut.setFillAfter(true);

                bocadillo.startAnimation(fadeOut);
                circulo.startAnimation(fadeOut);

                bocadillo.postDelayed(() -> bocadillo.setVisibility(View.GONE), 500);
                circulo.postDelayed(() -> circulo.setVisibility(View.GONE), 500);
            }
        }
    }

    private void hideCharactersTutorial(View view) {
        View bocadillo = view.findViewById(R.id.tutorial_characters);
        View circulo = view.findViewById(R.id.circle_highlight);

        if (bocadillo != null && circulo != null) {
            if (bocadillo.getVisibility() == View.VISIBLE || circulo.getVisibility() == View.VISIBLE) {

                Animation fadeOut = new AlphaAnimation(1f, 0f);
                fadeOut.setDuration(500);
                fadeOut.setFillAfter(true);

                bocadillo.startAnimation(fadeOut);
                circulo.startAnimation(fadeOut);

                bocadillo.postDelayed(() -> bocadillo.setVisibility(View.GONE), 500);
                circulo.postDelayed(() -> circulo.setVisibility(View.GONE), 500);
            }
        }
    }

    // Metodo que reproduce un efecto de sonido
    private void playSoundEffect() {
        if (soundEffect == null) {
            soundEffect = MediaPlayer.create(requireContext(), R.raw.tutorial_sound);
        }
        soundEffect.start();
    }

    // Metodo que detiene y libera la música de fondo
    private void stopBackgroundMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.stop();
            backgroundMusic.release();
            backgroundMusic = null;
        }
    }

    // Metodo que cierra el tutorial y lo elimina de la vista
    private void closeTutorial() {
        requireActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }

    // Metodo que se encarga de asegurar que se destruyan los sonidos de la vista.
    @Override
    public void onDestroy() {
        super.onDestroy();
        stopBackgroundMusic();
        if (soundEffect != null) {
            soundEffect.release();
            soundEffect = null;
        }
    }
}
