package co.com.sofka.test;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import co.com.sofka.test.Services.Servicio;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
public class ServicioTest {
    
    @Autowired
    Servicio servicio;

    /* La clase StepVerifier verifica casi de forma directa el contenido esperado de Mono (un elemento) y Flux (listado de elementos) */

    @Test
    void testMono() {
        Mono<String> uno = servicio.buscarUno();
        StepVerifier.create(uno).expectNext("Pedro").verifyComplete();
    }

    @Test
    void testVarios() {
        Flux<String> uno = servicio.buscarTodos();
        StepVerifier.create(uno).expectNext("Pedro").expectNext("Juan").expectNext("Carlos").expectNext("Maria").verifyComplete();
    }

    /* En el método buscarTodoLento() de manera asíncrona los elementos se irán presentando un periodo de espera definido  */

    @Test
    void testVariosLento() {
        Flux<String> uno = servicio.buscarTodosLento();
        StepVerifier.create(uno)
            .expectNext("Pedro")
            .thenAwait(Duration.ofSeconds(1))
            .expectNext("Juan")
            .thenAwait(Duration.ofSeconds(1))
            .expectNext("Carlos")
            .thenAwait(Duration.ofSeconds(1))
            .expectNext("Maria")
            .thenAwait(Duration.ofSeconds(1))
            .verifyComplete();
    }

    /* Prueba lo que ocurre cuando alguien se suscribe */
    @Test
    void testTodosFiltro() {
        Flux<String> source = servicio.buscarTodosFiltro();
        StepVerifier.create(source)
            .expectNext("JUAN")
            .expectNextMatches(name -> name.startsWith("JO"))
            .expectNext("MARK", "KATE")
            .expectComplete()
            .verify();            
    }

    /* Exceptions */

    /* Después de contar 4 elementos se espera que el flujo termine con la excepción */
    @Test
    void testTodosFiltroError() {
        Flux<String> source = servicio.buscarTodosFiltro();
        Flux<String> error = source.concatWith(
        Mono.error(new IllegalArgumentException("Mensaje de Error"))
    );
        StepVerifier.create(error)
            .expectNextCount(4)
            .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException && throwable.getMessage().equals("Mensaje de Error"))
            .verify();

    }

    /* 
    Si no es necesario verificar el tipo y el mensaje de la excepción a la vez se puede usar 

    * expectError() - espera cualquier tipo de error
    * expectError(Class<? extends Throwable> class) - espera un error de un tipo específico
    * expectErrorMessage(String errorMessage) - espera un error con un mensaje específico
    * expectErrorMatches(Predicate<Throwable> predicate) - espera un error que coincida con un predicado dado
    * expectErrorSatisfies(Consumer<Throwable> assertionConsumer) - consume un Throwable para hacer una aserción personalizada
    */

    
    /* Publicadores basados en tiempo */

    /* El constructor StepVerifier.withVirtualTime está diseñado para evitar pruebas de larga duración */

    @Test
    void testVirtualTime(){
        StepVerifier.withVirtualTime(() -> Flux.interval(Duration.ofSeconds(1)).take(2))
            .expectSubscription()
            .expectNoEvent(Duration.ofSeconds(1)) // falla cuando aparece algún evento durante la duración, la secuencia pasará con una duración determinada
            .expectNext(0L)
            .thenAwait(Duration.ofSeconds(1)) // detiene la evaluación de los pasos, pueden ocurrir nuevos eventos durante este tiempo
            .expectNext(1L)
            .verifyComplete();
    }


    /* Afirmaciones posteriores a la ejecución */
    @Test
    void testPause() {
        Flux<Integer> source = Flux.<Integer>create(emmiter -> {
            emmiter.next(1);
            emmiter.next(2);
            emmiter.next(3);
            emmiter.next(4);
        }).filter(number -> number % 2 == 0);

        StepVerifier.create(source)
            .expectNext(2)
            .expectComplete()
            .verifyThenAssertThat()
            .hasDropped(4)
            .tookLessThan(Duration.ofMillis(1050));
    }

}




