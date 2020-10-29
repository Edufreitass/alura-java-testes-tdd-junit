package br.com.caelum.leilao.servico;

import static org.junit.Assert.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.leilao.builder.CriadorDeLeilao;
import br.com.caelum.leilao.dominio.Lance;
import br.com.caelum.leilao.dominio.Leilao;
import br.com.caelum.leilao.dominio.Usuario;

public class AvaliadorTest {

	private Avaliador leiloeiro;
	private Usuario joao;
	private Usuario jose;
	private Usuario maria;

	@Before
	public void criaAvaliador() {
		this.leiloeiro = new Avaliador();
		this.joao = new Usuario("João");
		this.jose = new Usuario("José");
		this.maria = new Usuario("Maria");
	}
	
	@Test(expected = RuntimeException.class)
	public void naoDeveAvaliarLeiloesSemNenhumLance() {
		Leilao leilao = new CriadorDeLeilao().para("Playstation 3 Novo").constroi();
		
		leiloeiro.avalia(leilao);
	}

	@Test
	public void deveEntenderLancesEmOrdemCrescente() {
		// parte 1: cenario
		Leilao leilao = new CriadorDeLeilao().para("Playstation 3 Novo")
				.lance(joao, 250.0)
				.lance(jose, 300.0)
				.lance(maria, 400.0)
				.constroi();
		
		// parte 2: acao
		leiloeiro.avalia(leilao);

		// parte 3: validacao
		double maiorEsperado = 400;
		double menorEsperado = 250;

		// Usando Hamcrest
		assertThat(leiloeiro.getMaiorLance(), equalTo(maiorEsperado));
		assertThat(leiloeiro.getMenorLance(), equalTo(menorEsperado));

	}

	@Test
	public void deveEntenderLeilaoComApenasUmLance() {
		// parte 1: cenário
		Leilao leilao = new CriadorDeLeilao().para("Playstation 3 Novo")
				.lance(joao, 1000.0)
				.constroi();

		// parte 2: acao
		leiloeiro.avalia(leilao);

		// parte 3: validacao
		assertThat(leiloeiro.getMenorLance(), equalTo(leiloeiro.getMaiorLance()));
	}

	@Test
	public void deveEntenderLeilaoComLancesEmOrdemRandomica() {
		// cenario
		Leilao leilao = new CriadorDeLeilao().para("Playstation 3 Novo")
				.lance(joao, 200.0)
				.lance(maria, 450.0)
				.lance(joao, 120.0)
				.lance(maria, 700.0)
				.lance(joao, 630.0)
				.lance(maria, 230.0)
				.constroi();

		// acao
		leiloeiro.avalia(leilao);

		// validacao
		assertEquals(700.0, leiloeiro.getMaiorLance(), 0.0001);
		assertEquals(120.0, leiloeiro.getMenorLance(), 0.0001);
	}

	@Test
	public void deveEntenderLeilaoComLancesEmOrdemDecrescente() {
		Leilao leilao = new CriadorDeLeilao().para("Playstation 3 Novo")
				.lance(joao, 400.0)
				.lance(maria, 300.0)
				.lance(joao, 200.0)
				.lance(maria, 100.0)
				.constroi();

		leiloeiro.avalia(leilao);

		assertEquals(400.0, leiloeiro.getMaiorLance(), 0.0001);
		assertEquals(100.0, leiloeiro.getMenorLance(), 0.0001);
	}

	@Test
	public void deveEncontrarOsTresMaioresLances() {
		// parte 1: cenário
		Leilao leilao = new CriadorDeLeilao().para("Playstation 3 Novo")
				.lance(joao, 100.0)
				.lance(maria, 200.0)
				.lance(joao, 300.0)
				.lance(maria, 400.0)
				.constroi();

		// parte 2: acao
		leiloeiro.avalia(leilao);

		// parte 3: validacao
		List<Lance> maiores = leiloeiro.getTresMaiores();
		assertEquals(3, maiores.size());
		assertThat(maiores, hasItems(
				new Lance(maria, 400),
				new Lance(joao, 300),
				new Lance(maria, 200)));
	}

	@Test
	public void deveDevolverTodosLancesCasoNaoHajaNoMinimo3() {
		Leilao leilao = new CriadorDeLeilao().para("Playstation 3 Novo")
				.lance(joao, 100.0)
				.lance(maria, 200.0)
				.constroi();

		leiloeiro.avalia(leilao);

		List<Lance> maiores = leiloeiro.getTresMaiores();

		assertEquals(2, maiores.size());
		assertEquals(200, maiores.get(0).getValor(), 0.00001);
		assertEquals(100, maiores.get(1).getValor(), 0.00001);
	}

	@Test(expected = RuntimeException.class)
	public void deveDevolverListaVaziaCasoNaoHajaLances() {
		Leilao leilao = new Leilao("Playstation 3 Novo");

		leiloeiro.avalia(leilao);

		List<Lance> maiores = leiloeiro.getTresMaiores();

		assertEquals(0, maiores.size());
	}

	@Test
	public void deveCalcularAMedia() {
		// parte 1 - cenario: 3 lances em ordem crescente
		Leilao leilao = new CriadorDeLeilao().para("Playstation 3 Novo")
				.lance(maria, 300.0)
				.lance(joao, 400.0)
				.lance(jose, 500.0)
				.constroi();

		// parte 2 - acao: executando a acao
		leiloeiro.avalia(leilao);

		// parte 3 - validacao: comparando a saida com o esperado
		assertEquals(400, leiloeiro.getMedia(), 0.0001);
	}

	@Test(expected = RuntimeException.class)
	public void testaMediaDeZeroLance() {
		// cenario
		Leilao leilao = new Leilao("Iphone 12");

		// acao
		leiloeiro.avalia(leilao);

		// validacao
		assertEquals(0, leiloeiro.getMedia(), 0.0001);
	}

}
