package co.com.sofka.test;

import org.junit.jupiter.api.Test;

final TestPublisher<String> testPublisher = TestPublisher.create();

@Test
void testUpperCase() {
    UpperCaseConverter upperCaseConverter = new UpperCaseConverter(testPublisher.flux());
    StepVerifier.create(upperCaseConverter.getUpperCase())
        .then(() -> testPublisher.emit("datos", "GeNeRaDoS", "Sofka"))
        .expectNext("DATOS", "GENERAODS", "SOFKA")
        .verifyComplete();
}

/* En este ejemplo, creamos un TestPublisher de Flux de prueba en el parámetro del constructor UppercaseConverter. Luego, nuestro TestPublisher emite tres elementos y completa de esta manera la prueba de la clase sin hacer uso del publicador original de los datos */

/* También podemos simular casos de comportamientos inesperados (misbehaving) de un posible publicador */


/* Publicador que emite una serie de números, uno de los cuales va nulo en circunstancias normales este comportamiento arrojaria un NullPointException que es precisamente lo que queremos probar */

TestPulisher.createNoncompliant(TestPublisher.Violation.ALLOW_NULL).emit("1","2", null, "3");



/* 
Además de ALLOW_NULL podemos configurar algunos otros comportamientos típicos que ocasionarían errores. 
* REQUEST_OVERFLOW – permite llamar a next() sin lanzar una IllegalStateException cuando hay un número insuficiente de solicitudes.
* CLEANUP_ON_TERMINATE – permite enviar varias señales de terminación consecutivamente.
* DEFER_CANCELLATION – nos permite ignorar las señales de cancelación y continuar con la emisión de elementos 
*/
