package viniciuscgp;

public class ComputadorMedio extends Computador {
	public ComputadorMedio() {
		System.out.println("Construtor Computador: Médio");
	}

	@Override
	public void proximaJogada(Tabuleiro tabuleiro) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getDificuldadeDescricao() {
		return "Nível de dificuldade: Médio";
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
