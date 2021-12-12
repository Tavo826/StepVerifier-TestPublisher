package co.com.sofka.test.Services;

import java.time.Duration;

import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class Servicio {

    public Mono<String> buscarUno() {
        return Mono.just("Pedro");
    }

    public Flux<String> buscarTodos() {
        return Flux.just("Pedro", "Juan", "Carlos", "Maria");
    }

    public Flux<String> buscarTodosLento() {
        return Flux.just("Pedro", "Juan", "Carlos", "Maria").delaySequence(Duration.ofSeconds(20));
    }

    /* Genera solo nombres de cuatro letras mapeadas en mayúsculas */
    public Flux<String> buscarTodosFiltro() {
        Flux<String> source = Flux.just("Pedro", "Juan", "Carlos", "Maria", "John", "Monica", "Mark", "Kate").filter(name -> name.length() == 4).map(String::toUpperCase);

        return source;
    }

    /* Exceptions */

    /* Concatenando un Mono a un Flux de maner que el Mono termine con un error cuando se suscriba al publicador de nombre */
    Flux<String> error = buscarTodosFiltro().concatWith(
        Mono.error(new IllegalArgumentException("Mensaje de Error"))
    );

    /* Afirmaciones posteriores a la ejecución */

    /* Se emiten algunos elementos luego de completar, pausará y emitirá un elemento más */
    Flux<Integer> source = Flux.<Integer>create(emmiter -> {
        emmiter.next(1);
        emmiter.next(2);
        emmiter.next(3);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        emmiter.next(4);
    }).filter(number -> number % 2 == 0);
}
