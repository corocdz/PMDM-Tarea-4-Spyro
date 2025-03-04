package dam.pmdm.spyrothedragon;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import dam.pmdm.spyrothedragon.databinding.ActivityMainBinding;
import dam.pmdm.spyrothedragon.ui.TutorialFragment;

/**
 * Actividad principal de la aplicación. Se encarga de gestionar la navegación, el tutorial de bienvenida y las interacciones del usuario con la app en ciertos momentos.
 * Implementa la interfaz de OnTutorialCompleteListener para detectar la finalización del tutorial y así gestionar con éxito el comportamiento permitido para el usuairo con la app.
 */

public class MainActivity extends AppCompatActivity implements TutorialFragment.OnTutorialCompleteListener {

    private ActivityMainBinding binding; // Enlace con el diseño de la actividad
    NavController navController = null; // Controlador de navegación para gestionar los fragmentos
    private boolean isTutorialActive = true; // Variable para controlar si el tutorial sigue activo


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Vinculación de la vista usando ViewBinding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Configuramos la Toolbar como ActionBar
        setSupportActionBar(binding.toolbar);

        // Recuperamos las preferencias del usuario. Con esto podremos controlar si el tutorial fue omitido o completado.
        SharedPreferences preferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        boolean tutorialSkipped = preferences.getBoolean("guide_skipped", false);

        //La sentencia comentada inmediatamente inferior servia para "resetear" las preferencias del usuario para poder comprobar el comportamiento de la app, los diseños, etc.
        //preferences.edit().remove("guide_skipped").apply();

        // Verificamos si el tutorial fue omitido O FINALIZADO (tutorialSkipped equivale realmente a "tutorial saltado", entendiendo por esto a sinónimo de terminado, sea cual sea el camino)
        if (tutorialSkipped) { // Si el tutorial fue terminado, habilitamos los elementos del navigation menu y la toolbar.
            enableBottomNavigation();
            enableToolbar();
        } else { // Si no ha terminado, dichos elementos estarán bloqueados hasta que el usuario termine el tutorial.
            disableBottomNavigation();
            disableToolbar();
        }

        // Si no hay algún estado guardado, agregamos el fragmento del tutorial al inicio de la app
        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(android.R.id.content, new TutorialFragment(), "TutorialFragment")
                    .commit();

        }

        // Configuramos el controlador de navegación
        Fragment navHostFragment = getSupportFragmentManager().findFragmentById(R.id.navHostFragment);
        if (navHostFragment != null) {
            navController = NavHostFragment.findNavController(navHostFragment);
            NavigationUI.setupWithNavController(binding.navView, navController);
            NavigationUI.setupActionBarWithNavController(this, navController);
        }

        // Maneja la selección de los elementos del menú de navegación inferior
        binding.navView.setOnItemSelectedListener(this::selectedBottomMenu);

        // Si se accede desde VideoActivity con "goToCollectibles", navegamos a la pestaña de coleccionables
        if (getIntent().getBooleanExtra("goToCollectibles", false)) {
            navController.navigate(R.id.navigation_collectibles);
        }

        // Maneja la visibilidad del botón de retroceso en la ActionBar según la pantalla actual en ese momento
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.navigation_characters ||
                    destination.getId() == R.id.navigation_worlds ||
                    destination.getId() == R.id.navigation_collectibles) {
                // Para las pantallas de los tabs, no queremos que aparezca la flecha de atrás
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            } else {
                // Si se navega a una pantalla donde se desea mostrar la flecha de atrás, habilítala
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        });

    }

    private boolean selectedBottomMenu(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.nav_characters)
            navController.navigate(R.id.navigation_characters);
        else if (menuItem.getItemId() == R.id.nav_worlds)
            navController.navigate(R.id.navigation_worlds);
        else
            navController.navigate(R.id.navigation_collectibles);
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Infla el menú de opciones
        getMenuInflater().inflate(R.menu.about_menu, menu);

        // Si el tutorial está activo, bloqueamos los elementos del menú
        if (isTutorialActive) {
            for (int i = 0; i < menu.size(); i++) {
                menu.getItem(i).setEnabled(false);
            }
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Si el tutorial sigue activo, bloqueamos la interacción
        if (isTutorialActive) {
            return false; // No permitimos interacción
        }


        if (item.getItemId() == R.id.action_info) {
            showInfoDialog();  // Muestra el diálogo de información
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onShowAboutDialog() {
        showInfoDialog();  // Muestra el cuadro del diálogo del Menu About
    }


    // Muestra la información del menu about.
    private void showInfoDialog() {
        // Crear un diálogo de información
        new AlertDialog.Builder(this)
                .setTitle(R.string.title_about)
                .setMessage(R.string.text_about)
                .setPositiveButton(R.string.accept, null)
                .show();
    }

    /**
     * Deshabilita la interacción con el BottomNavigationView (Durante el tutorial)
     */
    public void disableBottomNavigation() {
        binding.navView.setEnabled(false);
        binding.navView.setClickable(false);
        for (int i = 0; i < binding.navView.getMenu().size(); i++) {
            binding.navView.getMenu().getItem(i).setEnabled(false); // Deshabilita cada ítem del menú
        }
    }

    /**
     * Habilita la interacción con el BottomNavigationView (Luego del tutorial)
     */
    public void enableBottomNavigation() {
        binding.navView.setEnabled(true);
        binding.navView.setClickable(true);
        for (int i = 0; i < binding.navView.getMenu().size(); i++) {
            binding.navView.getMenu().getItem(i).setEnabled(true); // Habilita cada ítem del menú
        }
    }

    /**
     * Bloquea la Toolbar para que no sea interactuable (Durante el tutorial)
     */
    public void disableToolbar() {
        isTutorialActive = true;
        invalidateOptionsMenu(); // Refrescamos el menú
    }

    /**
     * Desbloquea la Toolbar una vez finalizado el tutorial (Luego del tutorial)
     */
    public void enableToolbar() {
        isTutorialActive = false;
        invalidateOptionsMenu(); // Refrescamos el menú
    }


    /**
     * Método invocado cuando el usuario omite o termina el tutorial.
     * Habilita la barra de navegación y la Toolbar haciendo uso de los métodos vistos anteriormente.
     */
    public void onTutorialSkipped() {
        enableBottomNavigation();
        enableToolbar();
    }

}
