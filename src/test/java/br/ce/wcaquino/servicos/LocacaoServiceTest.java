package br.ce.wcaquino.servicos;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.utils.DataUtils;
import exceptions.FilmeSemEstoqueException;
import exceptions.LocadoraException;
import org.junit.*;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static br.ce.wcaquino.utils.DataUtils.obterDataComDiferencaDias;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class LocacaoServiceTest {

    private LocacaoService service;

    @Rule
    public ErrorCollector error = new ErrorCollector();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setup() {
        service = new LocacaoService();
    }

    @Test
    public void deveAlugarFilme() throws Exception {
        Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
        // Arrange
        Usuario usuario = new Usuario("Usuario 1");
        List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 5.0));

        // Act
        Locacao locacao = service.alugarFilme(usuario, filmes);
        // Assert
        assertEquals(5, locacao.getValor(), 0.01);
        error.checkThat(locacao.getValor(), is(equalTo(5.0)));
        error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
        error.checkThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));
    }

    @Test(expected = FilmeSemEstoqueException.class)
    public void naoDeveAlugarFilmeSemEstoque() throws Exception {
        // Arrange
        Usuario usuario = new Usuario("Usuario 1");
        List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 0, 5.0));

        // Act
        service.alugarFilme(usuario, filmes);
    }

    @Test
    public void naoDeveAlugarFilmeSemEstoque2() {
        // Arrange
        Usuario usuario = new Usuario("Usuario 1");
        List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 0, 5.0));

        try {
            // Act
            service.alugarFilme(usuario, filmes);
            // Assert
            Assert.fail("Deveria ter lançado uma exceção");
        } catch (Exception e) {
            // Assert
            assertThat(e.getMessage(), is("Filme sem estoque"));
        }
    }

    // Forma elegante - utilizada para excecoes que sao lancadas somente por aquele motivo
    @Test
    public void naoDeveAlugarFilmeSemEstoque3() throws Exception {
        // Arrange
        Usuario usuario = new Usuario("Usuario 1");
        List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 0, 5.0));

        exception.expect(Exception.class);
        exception.expectMessage("Filme sem estoque");

        // Act
        service.alugarFilme(usuario, filmes);
    }

    // Forma Robusta - forma que possui mais poder sobre a execução
    @Test
    public void naoDeveAlugarFilmeSemUsuario() throws FilmeSemEstoqueException {
        // Arrange
        List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 1, 4.0));

        try {
            // Act
            service.alugarFilme(null, filmes);
            // Assert
            Assert.fail();
        } catch (LocadoraException e) {
            // Assert
            Assert.assertThat(e.getMessage(), is("Usuario vazio"));
        }
    }

    @Test
    public void naoDeveAlugarFilmeSemFilme() throws FilmeSemEstoqueException, LocadoraException {
        // Arrange
        Usuario usuario = new Usuario("Usuario 1");

        // Assert
        exception.expect(LocadoraException.class);
        exception.expectMessage("Filme vazio");

        // Act
        service.alugarFilme(usuario, null);
    }

    @Test
    public void devePagar75PctNoFilme3() throws FilmeSemEstoqueException, LocadoraException {
        //Arrange
        Usuario usuario = new Usuario("Usuario 1");
        List<Filme> filmes = Arrays.asList(
                new Filme("Filme1", 2, 4.0),
                new Filme("Filme2", 2, 4.0),
                new Filme("Filme3", 2, 4.0)
        );

        //Act
        Locacao resultado = service.alugarFilme(usuario, filmes);

        //Assert
        assertThat(resultado.getValor(), is(11.0));
    }

    @Test
    public void devePagar50PctNoFilme4() throws FilmeSemEstoqueException, LocadoraException {
        //Arrange
        Usuario usuario = new Usuario("Usuario 1");
        List<Filme> filmes = Arrays.asList(
                new Filme("Filme1", 2, 4.0),
                new Filme("Filme2", 2, 4.0),
                new Filme("Filme3", 2, 4.0),
                new Filme("Filme4", 2, 4.0)
        );

        //Act
        Locacao resultado = service.alugarFilme(usuario, filmes);

        //Assert
        assertThat(resultado.getValor(), is(13.0));
    }

    @Test
    public void devePagar25PctNoFilme5() throws FilmeSemEstoqueException, LocadoraException {
        //Arrange
        Usuario usuario = new Usuario("Usuario 1");
        List<Filme> filmes = Arrays.asList(
                new Filme("Filme1", 2, 4.0),
                new Filme("Filme2", 2, 4.0),
                new Filme("Filme3", 2, 4.0),
                new Filme("Filme4", 2, 4.0),
                new Filme("Filme5", 2, 4.0)
        );

        //Act
        Locacao resultado = service.alugarFilme(usuario, filmes);

        //Assert
        assertThat(resultado.getValor(), is(14.0));
    }

    @Test
    public void devePagar0PctNoFilme6() throws FilmeSemEstoqueException, LocadoraException {
        //Arrange
        Usuario usuario = new Usuario("Usuario 1");
        List<Filme> filmes = Arrays.asList(
                new Filme("Filme1", 2, 4.0),
                new Filme("Filme2", 2, 4.0),
                new Filme("Filme3", 2, 4.0),
                new Filme("Filme4", 2, 4.0),
                new Filme("Filme5", 2, 4.0),
                new Filme("Filme6", 2, 4.0)
        );

        //Act
        Locacao resultado = service.alugarFilme(usuario, filmes);

        //Assert
        assertThat(resultado.getValor(), is(14.0));
    }

    @Test
//    @Ignore
    public void deveDevolverNaSegundaAoAlugarNoSabado() throws FilmeSemEstoqueException, LocadoraException {
        Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
        //Arrange
        Usuario usuario = new Usuario("Usuario 1");
        List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 1, 5.0));

        //Act
        Locacao retorno = service.alugarFilme(usuario, filmes);

        //Assert
        boolean ehSegunda = DataUtils.verificarDiaSemana(retorno.getDataRetorno(), Calendar.MONDAY);
        Assert.assertTrue(ehSegunda);
    }
}
