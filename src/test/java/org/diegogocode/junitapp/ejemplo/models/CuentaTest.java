package org.diegogocode.junitapp.ejemplo.models;

import org.diegogocode.junitapp.ejemplo.exception.DineroInsuficienteException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CuentaTest {
    private Cuenta cuenta;

    @BeforeAll
    static void beforeAll() {
        System.out.println("Inicializando el Test");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("Finalizando el Test");
    }

    @BeforeEach
    void initMetodoTest(){
        this.cuenta = new Cuenta("Diego", new BigDecimal("1111.2323"));
        System.out.println("Iniciando el metodo");
    }

    @AfterEach
    void tearDown() {
        System.out.println("Finalizando el metodo");
    }

    @Test
    @DisplayName("Test para probar el nombre de la cuenta")
    void testNombreCuenta() {
        String esperado = "Diego";
        String real = cuenta.getPersona();
        assertNotNull(real, () -> "La cuenta no puede ser nula");
        assertEquals(esperado, real, () -> "El valor esperado es " + esperado + ", valor erroneo: " + real);
    }

    @Test
    void testSalsoCuenta() {
        assertNotNull(cuenta.getSaldo());
        assertEquals(1111.2323, cuenta.getSaldo().doubleValue());
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void testReferenciaCuenta() {
        Cuenta cuenta = new Cuenta("Laura", new BigDecimal("8900.9997"));
        Cuenta cuenta2 = new Cuenta("Laura", new BigDecimal("8900.9997"));
        //assertNotEquals(cuenta2, cuenta);
        assertEquals(cuenta2, cuenta);
    }

    @Test
    void testDebitoCuenta() {
        Cuenta cuenta = new Cuenta("Diego", new BigDecimal("1000.12345"));
        cuenta.debito(new BigDecimal(100));
        assertNotNull(cuenta.getSaldo());
        assertEquals(900, cuenta.getSaldo().intValue());
        assertEquals("900.12345", cuenta.getSaldo().toPlainString());
    }

    @Test
    void testCreditoCuenta() {
        Cuenta cuenta = new Cuenta("Diego", new BigDecimal("1000.12345"));
        cuenta.credito(new BigDecimal(100));
        assertNotNull(cuenta.getSaldo());
        assertEquals(1100, cuenta.getSaldo().intValue());
        assertEquals("1100.12345", cuenta.getSaldo().toPlainString());
    }

    @Test
    void testDineroInsuficienteException() {
        Cuenta cuenta = new Cuenta("Diego", new BigDecimal("1000.12345"));
        Exception exception = assertThrows(DineroInsuficienteException.class, () -> {
            cuenta.debito(new BigDecimal(1500));
        });
        String actual = exception.getMessage();
        String esperado = "Dinero Insuficiente";
        assertEquals(esperado, actual);
    }

    @Test
    void testTransferirDineroCuentas() {
        Cuenta cuenta1 = new Cuenta("Diego", new BigDecimal("2500"));
        Cuenta cuenta2 = new Cuenta("Laura", new BigDecimal("6500"));
        Banco banco = new Banco();
        banco.setNombre("BBVA");
        banco.transferir(cuenta2, cuenta1, new BigDecimal("100"));
        assertEquals("6400", cuenta2.getSaldo().toPlainString());
        assertEquals("2600", cuenta1.getSaldo().toPlainString());
    }

    @Test
    void testRelacionBancoCuentas() {
        Cuenta cuenta1 = new Cuenta("Diego", new BigDecimal("2500"));
        Cuenta cuenta2 = new Cuenta("Laura", new BigDecimal("6500"));

        Banco banco = new Banco();
        banco.addCuenta(cuenta1);
        banco.addCuenta(cuenta2);
        banco.setNombre("BBVA");
        banco.transferir(cuenta2, cuenta1, new BigDecimal("100"));

        //assertAll se utiliza para verificar dentro de varios assertions cuales fallaron y cuales no,
        // esto debido a que sin esta instruccion solo muestra cual falla de primero.
        assertAll(
            () -> assertEquals("6400", cuenta2.getSaldo().toPlainString(),
                    () -> "El saldo para la cuenta2 no es el esperado"),
            () -> assertEquals("2600", cuenta1.getSaldo().toPlainString(),
                    () -> "El saldo para la cuenta1 no es el esperado"),
            () -> assertEquals(2, banco.getCuentas().size(),
                    () -> "Las numero de cuentas para el banco " + banco.getNombre() + " no es el esperado"),
            () -> assertEquals("BBVA", cuenta1.getBanco().getNombre(),
                    () -> "El banco asociado a la cuenta no es el esperado"),
            () -> assertEquals("Diego", banco.getCuentas().stream()
                    .filter(c -> c.getPersona().equals("Diego"))
                    .findFirst()
                    .get().getPersona()),
            () -> assertTrue(banco.getCuentas().stream().anyMatch(c -> c.getPersona().equals("Diego")))
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"100", "200", "300", "400", "1110.2323"})
    void testDebidoCuenta(String monto){
        cuenta.debito(new BigDecimal(monto));
        assertNotNull(cuenta.getSaldo());
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }
}