package viniciuscgp;

public class BotaoPequeno extends Botao {
	public BotaoPequeno(String texto, int x, int y) {
		super(texto, x, y);
		System.out.println("Construtor BotaoMedio");
		this.w = 42;
	}

}
