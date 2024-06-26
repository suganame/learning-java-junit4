package br.ce.wcaquino.servicos;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import exceptions.FilmeSemEstoqueException;
import exceptions.LocadoraException;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import java.util.Date;

import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static br.ce.wcaquino.utils.DataUtils.obterDataComDiferencaDias;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class LocacaoServiceTest {

    @Rule
    public ErrorCollector error = new ErrorCollector();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testLocacao() throws Exception {
        // Arrange
        LocacaoService service = new LocacaoService();
        Usuario usuario = new Usuario("Usuario 1");
        Filme filme = new Filme("Filme 1", 2, 5.0);

        // Act
        Locacao locacao = service.alugarFilme(usuario, filme);
        // Assert
        assertEquals(5, locacao.getValor(), 0.01);
        error.checkThat(locacao.getValor(), is(equalTo(5.0)));
        error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
        error.checkThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));
    }

    @Test(expected = FilmeSemEstoqueException.class)
    public void testLocacao_filmeSemEstoque() throws Exception {
        // Arrange
        LocacaoService service = new LocacaoService();
        Usuario usuario = new Usuario("Usuario 1");
        Filme filme = new Filme("Filme 1", 0, 5.0);

        // Act
        service.alugarFilme(usuario, filme);
    }

    @Test
    public void testLocacao_filmeSemEstoque_2() {
        // Arrange
        LocacaoService service = new LocacaoService();
        Usuario usuario = new Usuario("Usuario 1");
        Filme filme = new Filme("Filme 1", 0, 5.0);

        try {
            // Act
            service.alugarFilme(usuario, filme);
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
        LocacaoService service = new LocacaoService();
        Usuario usuario = new Usuario("Usuario 1");
        Filme filme = new Filme("Filme 1", 0, 5.0);

        exception.expect(Exception.class);
        exception.expectMessage("Filme sem estoque");

        // Act
        service.alugarFilme(usuario, filme);
    }

    // Forma Robusta - forma que possui mais poder sobre a execução
    @Test
    public void testLocacao_usuarioVazio() throws FilmeSemEstoqueException {
        // Arrange
        LocacaoService service = new LocacaoService();
        Filme filme = new Filme("Filme 2", 1, 4.0);

        try {
            // Act
            service.alugarFilme(null, filme);
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
        LocacaoService service = new LocacaoService();
        Usuario usuario = new Usuario("Usuario 1");

        // Assert
        exception.expect(LocadoraException.class);
        exception.expectMessage("Filme vazio");

        // Act
        service.alugarFilme(usuario, null);
    }
}
