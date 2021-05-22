package viniciuscgp;

//
// Função: Controla as ações do Jogador
//

public class JogadorHumano extends Oponente {

	@Override
	public String getDescricao() {
		return "Jogador";
	}

	@Override
	public String getDificuldadeDescricao() {
		return "";
	}

	@Override
	public boolean isHumano() {
		return true;
	}

	@Override
	public void proximaJogada(Tabuleiro tabuleiro) {
		// Sera feita pelo próprio ser humano com mouse.
		return;
	}

}
