package br.com.caelum.leilao.servico;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import br.com.caelum.leilao.dominio.Lance;
import br.com.caelum.leilao.dominio.Leilao;

/*
 * Para criar um matcher, basta criar uma classe que seja filha de
 * TypeSafeMatcher. No tipo genérico, você deve passar a classe que esse matcher
 * verificará. Por exemplo, se quisermos que nosso matcher verifique a classe
 * Leilao, precisamos fazer:
 */
public class LeilaoMatcher extends TypeSafeMatcher<Leilao> {

	private final Lance lance;

	// Para receber o Lance que procuraremos dentro do Leilão, criaremos um
	// construtor nesse matcher:
	public LeilaoMatcher(Lance lance) {
		this.lance = lance;
	}

	/*
	 * Precisamos sobrescrever dois métodos: boolean matchesSafely(Leilao item) e
	 * void describeTo(Description description).
	 */

	// Agora adicionaremos a descrição desse matcher:
	@Override
	public void describeTo(Description description) {
		description.appendText("leilao com lance " + lance.getValor());
	}

	// Vamos agora começar pelo método matchesSafely que retorna verdadeiro caso o
	// lance exista ou falso caso não exista.
	@Override
	protected boolean matchesSafely(Leilao item) {
		return item.getLances().contains(lance);
	}

	// Por fim, vamos criar o método que instanciará nosso matcher nos testes:
	public static Matcher<Leilao> temUmLance(Lance lance) {
		return new LeilaoMatcher(lance);
	}
}
