package viniciuscgp;

public class BotaoMedio extends Botao {
	public BotaoMedio(String texto, int x, int y) {
		super(texto, x, y);
		System.out.println("Construtor BotaoMedio");
		this.w = 170;
	}

}
