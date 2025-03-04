package dam.pmdm.spyrothedragon.models;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Vista creada para el easter egg. Simula una animación de fuego con partículas.
 */

public class FireView extends View {

    // Variables como la forma de la llama, su altura inicial, el desplazamiento de la animación de onda, etc
    private Paint firePaint;
    private Path firePath;
    private int flameHeight = 180;
    private Random random = new Random();
    private float waveOffset = 0;

    private List<Particle> particles = new ArrayList<>(); // Lista de partículas
    private Paint particlePaint; // Pincel que dibuja las partículas

    // Constructores

    public FireView(Context context) {
        super(context);
        init();
    }

    public FireView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    // Metodo que inicializa los parámetros de la vista además de configurar la animación del fuego
    private void init() {
        firePaint = new Paint();
        firePaint.setStyle(Paint.Style.FILL);
        firePaint.setAntiAlias(true);

        firePath = new Path();

        particlePaint = new Paint();
        particlePaint.setAntiAlias(true);

        // Animación para cambiar la altura de la llama y generar unas pequeñas partículas para hacerlo más realista
        ValueAnimator animator = ValueAnimator.ofInt(150, 250);
        animator.setDuration(600); // Duración de la animación
        animator.setRepeatCount(ValueAnimator.INFINITE); // Bucle
        animator.setInterpolator(new LinearInterpolator()); // Suavizamos un poco la animación
        animator.addUpdateListener(animation -> {
            flameHeight = (int) animation.getAnimatedValue(); // Actualizamos la altura de la llama
            waveOffset += 0.1; // Cambiamos la forma de la llama haciendo uso de ondas
            addParticles(); // Generamos nuevas partículas
            invalidate(); // Redibujamos la vsita
        });
        animator.start(); // Iniciamos la animación
    }

    /**
     * Método encargado de dibujar la llama y las partículas en el lienzo
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        firePath.reset();
        firePath.moveTo(width / 2f, height * 0.8f); // Punto base de la llama

        float waveFactor = (float) (Math.sin(waveOffset) * 40); // Onda que simula el movimiento de una llama

        // Dibujamos la forma de la llama
        firePath.quadTo(width / 2f - 100 + waveFactor, height * 0.8f - flameHeight,
                width / 2f, height * 0.4f);
        firePath.quadTo(width / 2f + 100 - waveFactor, height * 0.8f - flameHeight,
                width / 2f, height * 0.8f);

        firePath.close();

        // Colores que tomará la llama
        int red = 255;
        int green = random.nextInt(80) + 50; // Reducimos el verde (50-130)
        int blue = random.nextInt(30); // Azul casi inexistente (0-30)
        firePaint.setColor(Color.rgb(red, green, blue));

        canvas.drawPath(firePath, firePaint);

        // Partículas
        drawParticles(canvas);
    }

    // Generamos nuevas partículas con un cierto porcentaje de probabilidad
    private void addParticles() {
        if (random.nextFloat() < 0.3f) { // 30% de probabilidad para generar o no la partícula
            particles.add(new Particle(getWidth() / 2f + random.nextInt(60) - 30,
                    getHeight() * 0.8f - flameHeight,
                    random.nextInt(3) + 2, // Tamaño
                    random.nextInt(100) + 100)); // Opacidad
        }
    }

    // Se dibujan las partículas con un movimiento hacia arriba y con reducción de opacidad
    private void drawParticles(Canvas canvas) {

        for (int i = 0; i < particles.size(); i++) {
            Particle particle = particles.get(i);

            particle.y -= 4; // Movimiento de la partícula hacia arriba
            particle.alpha -= 5; // Reducimos opacidad

            // Colores de las partículas, con probabilidad también
            if (random.nextFloat() < 0.7) {
                particlePaint.setColor(Color.argb(particle.alpha, 255, random.nextInt(80) + 50, 0));
            } else {
                particlePaint.setColor(Color.argb(particle.alpha, 100, 100, 100)); // Humo gris
            }

            canvas.drawCircle(particle.x, particle.y, particle.size, particlePaint);

            if (particle.alpha <= 0) { // Eliminamos aquellas partículas invisibles
                particles.remove(i);
                i--;
            }
        }
    }

    /**
     * Clase interna que representa una partícula de fuego o humo.
     */
    private static class Particle {
        float x, y; // Posición de la partícula
        int size; // Tamaño de la misma
        int alpha; // opacidad

        // Constructor
        public Particle(float x, float y, int size, int alpha) {
            this.x = x;
            this.y = y;
            this.size = size;
            this.alpha = alpha;
        }
    }
}
