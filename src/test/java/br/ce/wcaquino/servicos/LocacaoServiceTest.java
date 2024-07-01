package br.ce.wcaquino.servicos;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import exceptions.FilmeSemEstoqueException;
import exceptions.LocadoraException;
import org.junit.*;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
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
    public void testLocacao() throws Exception {
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
    public void testLocacao_filmeSemEstoque() throws Exception {
        // Arrange
        Usuario usuario = new Usuario("Usuario 1");
        List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 0, 5.0));

        // Act
        service.alugarFilme(usuario, filmes);
    }

    @Test
    public void testLocacao_filmeSemEstoque_2() {
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
    public void testLocacao_filmeSemEstoque_3() throws Exception {
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
    public void testLocacao_usuarioVazio() throws FilmeSemEstoqueException {
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
    public void testLocacao_filmeVazio() throws FilmeSemEstoqueException, LocadoraException {
        // Arrange
        Usuario usuario = new Usuario("Usuario 1");

        // Assert
        exception.expect(LocadoraException.class);
        exception.expectMessage("Filme vazio");

        // Act
        service.alugarFilme(usuario, null);
    }
}
