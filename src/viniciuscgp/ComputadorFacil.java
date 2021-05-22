package viniciuscgp;

//
// Classe - Computador Facil
//


public class ComputadorFacil extends Computador {

	public ComputadorFacil() {
		System.out.println("Construtor Computador: Fácil");
	}

	@Override
	public void proximaJogada(Tabuleiro tabuleiro) {
		// No modo facil, o computador apenas marca o primeiro espaço vazio que ele
		// econtrar
		try {
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					if (tabuleiro.celulaVerifica(i, j) == Tabuleiro.VAZIO) {
						tabuleiro.celulaMarca(i, j, Tabuleiro.O);
						throw new RuntimeException("Marcou.");
					}
				}
			}
		} catch (Exception e) {
			// nada aqui
		}
	}

	@Override
	public String getDificuldadeDescricao() {
		return "Nível de dificuldade: Fácil";
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
