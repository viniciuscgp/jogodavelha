package viniciuscgp;

//
// Fun��o: Class abstrata que devera ser implementada para as jogadas do computador.
//

public abstract class Computador extends Oponente {
	public abstract void proximaJogada(Tabuleiro tabuleiro);
	public abstract String getDificuldadeDescricao();
}
