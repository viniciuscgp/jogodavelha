package viniciuscgp;

//
//Componente da GUI - Botão básico - Feito especificamente para esse projeto, 
//eu não quiz usar o do Swing pq ia ficar feio no caso de um game.

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Botao {
	private final static Color C_BG_N = Util.cor(225, 60, 80); // Fundo normal angulo, sturacao, brilho
	private final static Color C_BG_O = Util.cor(225, 70, 100); // Fundo mouse sobre
	private final static Color C_BG_P = Util.cor(225, 60, 50); // Fundo pressionado
	private final static Color C_FG_T = Util.cor(52, 100, 100); // Cor do texto
	private final static int GAP = 10;
	protected int x, y, w, h;
	protected String texto;
	protected int grupo;
	protected boolean visivel;

	public Botao(String texto, int x, int y) {
		System.out.println("Construtor Botao");
		this.texto = texto;
		this.x = x;
		this.y = y;
		this.w = 100;
		this.h = 35;
		this.grupo = 0;
		this.visivel = true;
	}

	public boolean pontoSobre(int x, int y) {
		return (x > this.x && x < this.x + this.w && y > this.y && y < this.y + this.h) && visivel;
	}

	public boolean drawMe(Graphics g, int mx, int my, boolean mouseB) {
		int dx, dy;
		if (!visivel)
			return false;
		g.setFont(new Font("Arial BOLD", Font.BOLD, 18));
		/*
		 * int w = g.getFontMetrics().stringWidth(texto) +
		 * g.getFontMetrics().stringWidth("XX"); if (w > this.w) this.w = w;
		 */

		dx = 0;
		dy = 0;
		if (!pontoSobre(mx, my))
			g.setColor(C_BG_N);
		else {
			if (!mouseB)
				g.setColor(C_BG_O);
			else {
				dx = 1;
				dy = 1;
				g.setColor(C_BG_P);
			}
		}

		g.fillRect(x + dx, y + dy, w, h);
		int px, py;
		px = x + dx + (w - g.getFontMetrics().stringWidth(texto)) / 2;
		py = y + dy - (h - g.getFontMetrics().getHeight()) / 2 - g.getFontMetrics().getMaxDescent();
		g.setColor(C_FG_T);

		g.drawString(texto, px, py + h);
		return mouseB;
	}

	public void setGrupo(int g) {
		this.grupo = g;
	}

	public int getGrupo() {
		return this.grupo;
	}

	public void setVisivel(boolean v) {
		this.visivel = v;
	}

	public boolean getVisivel() {
		return this.visivel;
	}

	public int getW() {
		return w;
	}

	public int getNext() {
		return x + w + GAP;
	}

	public int getH() {
		return h;
	}

	public int getY() {
		return y;
	}
}
