package br.ce.wcaquino.servicos;

import exceptions.NaoPodeDividirPorZeroException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CalculadoraTest {

    private Calculadora calc;

    @Before
    public void setup() {
        calc = new Calculadora();
    }

    @Test
    public void deveSomarDoisValores() {
        // Arrange
        int a = 5;
        int b = 3;

        // Act
        int resultado = calc.somar(a, b);

        //Assert
        Assert.assertEquals(8, resultado);
    }

    @Test
    public void deveSubtrairDoisValores() {
        //Arrange
        int a = 5;
        int b = 3;

        // Act
        int resultado = calc.subtrair(a, b);

        //Assert
        Assert.assertEquals(2, resultado);
    }

    @Test
    public void deveDividirDoisValores() throws NaoPodeDividirPorZeroException {
        //Arrange
        int a = 6;
        int b = 3;

        //Act
        int resultado = calc.divide(a, b);

        //Assert
        Assert.assertEquals(2, resultado);
    }
    @Test(expected = NaoPodeDividirPorZeroException.class)
    public void deveLancarExcecaoAoDividirPorZero() throws NaoPodeDividirPorZeroException {
        //Arrange
        int a = 10;
        int b = 0;
        //Act
        calc.divide(a, b);
        //Assert
    }

}
