package viniciuscgp;

//
//Classe - Computador Dificil
//

public class ComputadorDificil extends Computador {

	public ComputadorDificil() {
		System.out.println("Construtor Computador: Dificil");
	}
	
	@Override
	public void proximaJogada(Tabuleiro tabuleiro) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getDificuldadeDescricao() {
		return "Nível de dificuldade: Dificil";
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
