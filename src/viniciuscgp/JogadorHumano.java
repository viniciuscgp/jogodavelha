package viniciuscgp;
//
// Fun��o: Controla as a��es do Jogador
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
		return;
	}

}
