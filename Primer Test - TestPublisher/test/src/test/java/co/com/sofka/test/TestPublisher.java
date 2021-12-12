package co.com.sofka.test;

import org.junit.jupiter.api.Test;

@SpringBootTest
public class TestPublisher {

    final TestPublisher<String> testPublisher = TestPublisher.create();

    @Test
    void testUpperCase() {
        UpperCaseConverter upperCaseConverter = new UpperCaseConverter(testPublisher.flux());
        StepVerifier.create(upperCaseConverter.getUpperCase())
            .then(() -> testPublisher.emit("datos", "GeNeRaDoS", "Sofka"))
            .expectNext("DATOS", "GENERAODS", "SOFKA")
            .verifyComplete();
    }
}

/* En este ejemplo, creamos un TestPublisher de Flux de prueba en el parámetro del constructor UppercaseConverter. Luego, nuestro TestPublisher emite tres elementos y completa de esta manera la prueba de la clase sin hacer uso del publicador original de los datos */

