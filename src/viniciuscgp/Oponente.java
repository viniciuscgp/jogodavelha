package viniciuscgp;

//
// Função: Class abstrata m�e de todos os oponentes. Isso permite que possa-se usar uma unica variavel
// para armazenar o oponente atual. 
//

public abstract class Oponente {
	public abstract void proximaJogada(Tabuleiro tabuleiro);
	public abstract String getDescricao();
	public abstract String getDificuldadeDescricao();
	public abstract boolean isHumano();
}
