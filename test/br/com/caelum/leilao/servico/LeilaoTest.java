package br.com.caelum.leilao.servico;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import br.com.caelum.leilao.dominio.Lance;
import br.com.caelum.leilao.dominio.Leilao;
import br.com.caelum.leilao.dominio.Usuario;

public class LeilaoTest {

	@Test
	public void deveReceberUmLance() {
		// cenario
		Leilao leilao = new Leilao("Macbook Pro 15");
		// Garante que não existe nenhum lance!
		assertEquals(0, leilao.getLances().size());

		// acao
		leilao.propoe(new Lance(new Usuario("Steve Jobs"), 2000));

		// validacao
		assertEquals(1, leilao.getLances().size());
		assertEquals(2000.0, leilao.getLances().get(0).getValor(), 0.00001);
	}

	@Test
	public void deveReceberVariosLances() {
		// cenario
		Leilao leilao = new Leilao("Macbook Pro 15");

		// acao
		leilao.propoe(new Lance(new Usuario("Steve Jobs"), 2000));
		leilao.propoe(new Lance(new Usuario("Steve Wozniak"), 3000));

		// validacao
		assertEquals(2, leilao.getLances().size());
		assertEquals(2000.0, leilao.getLances().get(0).getValor(), 0.00001);
		assertEquals(3000.0, leilao.getLances().get(1).getValor(), 0.00001);
	}

	// Cenário: Uma pessoa não pode dar 2 lances
	@Test
	public void naoDeveAceitarDoisLancesSeguidosDoMesmoUsuario() {
		// cenario
		Leilao leilao = new Leilao("Macbook Pro 15");
		Usuario steveJobs = new Usuario("Steve Jobs");

		// acao
		leilao.propoe(new Lance(steveJobs, 2000.0));
		leilao.propoe(new Lance(steveJobs, 3000.0));

		// validacao
		assertEquals(1, leilao.getLances().size());
		assertEquals(2000.0, leilao.getLances().get(0).getValor(), 0.00001);
	}

	// Cenário: Uma pessoa não pode dar mais do que 5 lances
	@Test
	public void naoDeveAceitarMaisDoQue5LancesDeUmMesmoUsuario() {
		// cenario
		Leilao leilao = new Leilao("Macbook Pro 15");
		Usuario steveJobs = new Usuario("Steve Jobs");
		Usuario billGates = new Usuario("Bill Gates");

		// acao
		leilao.propoe(new Lance(steveJobs, 2000.0));
		leilao.propoe(new Lance(billGates, 3000.0));

		leilao.propoe(new Lance(steveJobs, 4000.0));
		leilao.propoe(new Lance(billGates, 5000.0));

		leilao.propoe(new Lance(steveJobs, 6000.0));
		leilao.propoe(new Lance(billGates, 7000.0));

		leilao.propoe(new Lance(steveJobs, 8000.0));
		leilao.propoe(new Lance(billGates, 9000.0));

		leilao.propoe(new Lance(steveJobs, 10000.0));
		leilao.propoe(new Lance(billGates, 11000.0));

		// deve ser ignorado
		leilao.propoe(new Lance(steveJobs, 12000.0));

		assertEquals(10, leilao.getLances().size());
		assertEquals(11000.0, leilao.getLances().get(leilao.getLances().size() - 1).getValor(), 0.00001);
	}

	@Test
	public void deveDobrarOUltimoLanceDado() {
		Leilao leilao = new Leilao("Macbook Pro 15");
		Usuario steveJobs = new Usuario("Steve Jobs");
		Usuario billGates = new Usuario("Bill Gates");

		leilao.propoe(new Lance(steveJobs, 2000));
		leilao.propoe(new Lance(billGates, 3000));
		leilao.dobraLance(steveJobs);

		assertEquals(4000, leilao.getLances().get(2).getValor(), 0.00001);
	}

	@Test
	public void naoDeveDobrarCasoNaoHajaLanceAnterior() {
		Leilao leilao = new Leilao("Macbook Pro 15");
		Usuario steveJobs = new Usuario("Steve Jobs");

		leilao.dobraLance(steveJobs);

		assertEquals(0, leilao.getLances().size());
	}

}
