# Spyro The Dragon App

## Introducción
Esta aplicación permite al usuario indagar en el mundo de Spyro. Podrá conocer algunos de sus personajes, mundos, paisajes e incluso los coleccionables de los que dispone la app. Incluye además una guía interactiva para aque usuario novato en la app para que pueda acostumbrarse rápidamente a su uso y favorezca su disfrute.

## Características principales
- **Guía interactiva**: Todos los usuario al acceder por primera vez a la app tendrán a su disposición una guía o tutorial de la app. Este tutorial no es obligatorio, podrá saltarse en cualquier momento al presionar el botón "Omitir guía". Una vez finalizado el tutorial, ya sea terminando el tutorial de manera normal u omitiéndolo, el usuario tendrá total acceso a lo que ofrece la app.
- **Botones**: Durante el tutorial, el usuario podrá encontrarse con dos botones. *Omitir guía* permitirá al usuario saltarse la guía si se considera preparado para usarla libremente. El otro botón es el botón *Siguiente*, cuya función es la de permitir al usuario avanzar a la siguiente pantalla durante el tutorial. El último botón que queda es *Finalizar*, que es el botón final de la guía y su función es la de terminar la guía naturalmente para aquel usuario que haya decidido realizarla.
- **Sonidos**: La app cuenta con dos sonidos principales. El primero es un sonido al interactuar con los botones y el otro es una música de fondo que se escuchará mientras el tutorial esté activo.
- **Animaciones y transiciones**: Para hacer algo más atractiva la guía, se han añadido unos efectos de animación en los bocadillos e iconos explicativos de la guía junto con unas transiciones entre pantallas.
- **Sección de personajes**: La app cuenta con un apartado que muestra y da información acerca de los personajes que podemos encontrar en el universo de Spyro.
- **Sección de mundos**: El usuario también podrá encontrar un apartado que lista una serie de paisajes, lugares y mundos de Spyro.
- **Sección de coleccionables**: La última sección con la que cuenta la app es con la sección de los coleccionables de los que dispone la app.
- **Menu "Sobre..."**: Un menú que proporciona un poco de la información sobre la app, como por ejemplo su desarrollador.
- **Easter eggs**: Ubicados en el apartado de *Personajes* y *Coleccionables*.

## Tecnologías utilizadas:
Este proyecto fue desarrollado utilizando las siguientes herramientas y tecnologías:
- **Java**: Lenguaje principal de la app.
- **VideoView**: Para poder visualizar el vídeo del easter egg.
- **Drawables personalizados**: Creados para dibujar en la pantalla bocadillos explicativos e iconos que le facilitarán al usuario seguir correctamente la guía.
- **RecyclerView**: Para mostrar listas de manera eficiente.
- **LinearLayout**: Para la creación de las pantallas explicativas que se situarán por encima de las pantallas principales de la app (sección de personajes, mundos, etc) junto con sus bocadillos e iconos explicativos.
- **Sonidos y vídeos**: Recursos exteriores que benefician el buen uso de la app.

## Instrucciones de uso:
1. **Clonar el repositorio:**
   git clone https://github.com/corocdz/PMDM-Tarea-4-Spyro.git
2. Abrir Android Studio:
   \n-Seleccionamos "Open an Existing Project".
   -Navegamos al directorio clonado y lo abrimos.
   
3. Ya estaría listo para usar, pues se ha subido el proyecto con todos los archivos necesarios para su funcionamiento.

4. Ejecutar la aplicación:
   -Conectar un dispositivo Android o utilizador un dispositivo emulado desde Android Studio.
   -Darle a "Run" para compilar y ejecutar la aplicación.

*También es posible descargar el proyecto completo haciendo click en "Code" y luego "Download file ZIP". Una vez descargado el proyecto, lo descomprimiremos y lo abriremos en Android Studio haciendo click en "Open Project".
Luego seguimos los pasos normales para su ejecución.

## Conclusiones del desarrollador:
La app cumple con lo esperado, pero si que es verdad que no lo hace de la mejor manera, ya que el código que realiza las distintas funciones como podría ser el *TutorialFragment* podría estar elaborado de una manera mucho más eficiente. Siendo java, podría haber aprovechado mucho mejor los recursos para así poder fomentar la reusabilidad de código y reducir la redundancia.

NOTA: existe un error que no he podido solucionar. Cuando desaparecen los bocadillos con los botones de siguiente, concretamente con el paso de la sección de coleccionables al menú about, si se pulsa donde antes estaba el botón del bocadillo inferior provocará que se cierre la app de manera inesperada.

Autor: Javier Coronilla Castellano.
Repositorio: https://github.com/corocdz?tab=repositories
