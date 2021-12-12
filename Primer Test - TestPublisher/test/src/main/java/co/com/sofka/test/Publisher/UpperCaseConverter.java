package co.com.sofka.test.Publisher;

import reactor.core.publisher.Flux;

/* 
En ocasiones es posible que necesitemos datos especiales para activar ciertos comportamientos concretos que se quieran probar. TestPublisher<T> permite activar señales de prueba como si de un publicador real se tratara. Métodos más comunes:

* next(T value) o next(T value, T rest) - envía una o más señales a los suscriptores
* emit(T value) - igual que next(T) pero invoca complete() al finalizar
* complete() - termina la fuente con la señal completar
* error(Throwable tr) - termina una fuente con un error
* flux() - método para encolver un TestPublisher en Flux
* mono() - lo mismo que flux() pero envolviéndolo en Mono
*/

/* 
TestPublisher.<String>create()
    .next("Primero", "Segundo", "Tercero")
    .error(new RuntimeException("Message"))
*/


/* Convierte un flujo de Strings a mayúsculas */

public class UpperCaseConverter {
    private final Flux<String> source;

    UpperCaseConverter(Flux<String> source) {
        this.source = source;
    }

    Flux<String> getUpperCase() {
        return source.map(String::toUpperCase);
    }
}

/* También podemos simular casos de comportamientos inesperados (misbehaving) de un posible publicador */


/* Publicador que emite una serie de números, uno de los cuales va nulo en circunstancias normales este comportamiento arrojaria un NullPointException que es precisamente lo que queremos probar */

// TestPulisher.createNoncompliant(TestPublisher.Violation.ALLOW_NULL).emit("1","2", null, "3");



/* 
Además de ALLOW_NULL podemos configurar algunos otros comportamientos típicos que ocasionarían errores. 
* REQUEST_OVERFLOW – permite llamar a next() sin lanzar una IllegalStateException cuando hay un número insuficiente de solicitudes.
* CLEANUP_ON_TERMINATE – permite enviar varias señales de terminación consecutivamente.
* DEFER_CANCELLATION – nos permite ignorar las señales de cancelación y continuar con la emisión de elementos 
*/
