package viniciuscgp;

import java.util.Random;

//

// Classe - Computador Facil
//

public class ComputadorFacil extends Computador {

	public ComputadorFacil() {
		System.out.println("Construtor Computador: Fácil");
	}

	@Override
	public void proximaJogada(Tabuleiro tabuleiro) {
		// No modo facil, o computador apenas marca algum espaço vazio aleatorio
		while (true) {
			int l = new Random().nextInt(3);
			int c = new Random().nextInt(3);
			if (tabuleiro.celulaVerifica(l, c) == Tabuleiro.VAZIO) {
				tabuleiro.celulaMarca(l, c, Tabuleiro.O);
				break;
			}
		}
	}

	@Override
	public String getDificuldadeDescricao() {
		return "Fácil";
	}

	@Override
	public String getDescricao() {
		return "Computador";
	}

	@Override
	public boolean isHumano() {
		return false;
	}
}
