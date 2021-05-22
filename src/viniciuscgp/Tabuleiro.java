package viniciuscgp;

//
// Classe Tabuleiro - esta classe gerencia o Tabuleiro do Jogo
//

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

// Tabuleiro sera 3 x 3 
//    0   1   2
// 0 [ ] [ ] [ ]
// 1 [ ] [ ] [ ]
// 2 [ ] [ ] [ ]

public class Tabuleiro {
	// Constantes que representam os valores possíveis na matriz
	// do tabuleiro
	public static final int X = 1; // Representa a letra X
	public static final int O = 8; // Representa a letra O
	public static final int VAZIO = 0; // Espaço vazio

	// Estados em que o tabuleiro pode se encontrar
	//
	public static final int E_VENCEU = 1;
	public static final int E_EMPATOU = 2;
	public static final int E_JOGANDO = 3;

	private static final int W = 100; // Larg
	private static final int H = 100; // Alt

	private int x = 250;
	private int y = 100;

	private static final Color C_BG_GRADE_N = Util.cor(79, 40, 30); // normal
	private static final Color C_BG_GRADE_O = Util.cor(79, 00, 40); // com mouse
	private static final Color C_FG_X = Util.cor(70, 0, 100);
	private static final Color C_FG_O = Util.cor(70, 0, 100);

	// Vetor que representa o estado do tabuleiro
	// ---------------------------------------------------
	private int[][] matriz;
	private boolean guiTravada;
	private Jogo jogo; // Usado para enviar um sinal para o jogo qdo o jogador marcar.

	public void setGuiTravada(boolean v) {
		guiTravada = v;
	}

	public boolean getGuiTravada() {
		return guiTravada;
	}

	public Tabuleiro(Jogo jogo) {
		this.jogo = jogo;
		matriz = new int[3][3];
		guiTravada = true;
		System.out.println("Construtor do Tabuleiro");
		inicializa();
	}

	public void setPos(int x, int y) {
		this.x = x;
		this.y = y;
	}

	// Inicializa o tabuleiro para novo jogo
	// ---------------------------------------------------
	public void inicializa() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				matriz[i][j] = VAZIO;
			}
		}
	}

	// Marca com X ou O ou Vazio
	// ---------------------------------------------------
	public void celulaMarca(int linha, int coluna, int valor) {
		if (linha < 0 || linha > 2 || coluna < 0 || coluna > 2) {
			throw new RuntimeException("Tabuleiro: linha e coluna deve estar entre 0 a 2!");
		}
		if (valor != X && valor != O && valor != VAZIO) {
			throw new RuntimeException("Tabuleiro: valor de marcação incorreto!");
		}
		matriz[linha][coluna] = valor;
	}

	// Verifica qual o conteudo de uma c�lula da matriz
	// ---------------------------------------------------
	public int celulaVerifica(int linha, int coluna) {
		if (linha < 0 || linha > 2 || coluna < 0 || coluna > 2) {
			throw new RuntimeException("Tabuleiro: linha e coluna deve estar entre 0 a 2!");
		}
		return matriz[linha][coluna];
	}

	// Desenha o tabulero no contexto gráfico fornececido
	// ---------------------------------------------------
	public void desenheMe(Graphics g, int mouseX, int mouseY, boolean mouseSolta) {
		boolean humanoJogou = false;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				int v = celulaVerifica(i, j);
				int px = x + (j * W);
				int py = y + (i * H);

				if (mouseX > px && mouseX < px + W && mouseY > py && mouseY < py + H && !guiTravada) {
					g.setColor(C_BG_GRADE_O);
					if (mouseSolta) {
						if (v == VAZIO) {
							celulaMarca(i, j, X);
							humanoJogou = true;
						}
					}
				} else {
					g.setColor(C_BG_GRADE_N);
				}

				g.fillRect(px + 3, py + 3, W - 3, H - 3);

				if (v == X) {
					g.setColor(C_FG_X);
					g.setFont(new Font("Arial", Font.BOLD, 80));
					g.drawString("X", px + 26, py + g.getFontMetrics().getHeight() - 14);
				}
				if (v == O) {
					g.setColor(C_FG_O);
					g.setFont(new Font("Arial", Font.BOLD, 80));
					g.drawString("O", px + 20, py + g.getFontMetrics().getHeight() - 14);
				}
			}
		}

		if (humanoJogou) {
			jogo.humanoJogou();
			Util.playSound("/sounds/lip.wav");

		}
	}

	public int verificaEstado() {
		int estado = E_JOGANDO;
		int soma = 0;

		// Horizontal
		for (int i = 0; i < 3; i++) {
			soma = 0;
			for (int j = 0; j < 3; j++) {
				soma += matriz[i][j];
			}
			if (soma == 3 || soma == 24)
				estado = E_VENCEU;
		}

		// Vertical
		for (int i = 0; i < 3; i++) {
			soma = 0;
			for (int j = 0; j < 3; j++) {
				soma += matriz[j][i];
			}
			if (soma == 3 || soma == 24)
				estado = E_VENCEU;
		}

		// Diagonal esquerda direita
		soma = matriz[0][0] + matriz[1][1] + matriz[2][2];
		if (soma == 3 || soma == 24)
			estado = E_VENCEU;

		// Diagonal direita esquerda
		soma = matriz[0][2] + matriz[1][1] + matriz[2][0];
		if (soma == 3 || soma == 24)
			estado = E_VENCEU;

		// Verifica empate
		boolean tudoCheio = true;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (matriz[j][i] == VAZIO) {
					tudoCheio = false;
				}
			}
		}

		if (estado != E_VENCEU)
			if (tudoCheio)
				estado = E_EMPATOU;

		return estado;

	}
}
