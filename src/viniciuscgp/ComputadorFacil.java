package viniciuscgp;

public class ComputadorFacil extends Computador {

	public ComputadorFacil() {
		System.out.println("Construtor Computador: Fácil");
	}
	
	@Override
	public void proximaJogada(Tabuleiro tabuleiro) {
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
