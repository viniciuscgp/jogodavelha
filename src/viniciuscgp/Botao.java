package viniciuscgp;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

//
// Componente da GUI - Bot�o b�sico - Feito especificamente para esse projeto, 
// eu n�o quiz usar o do Swing pq ia ficar feio no caso de um game.

public class Botao {
	private static Color C_BG_N = Util.cor(201, 60, 80); // Fundo normal angulo, sturacao, brilho 
	private static Color C_BG_O = Util.cor(201, 70, 100); // Fundo mouse sobre 
	private static Color C_BG_P = Util.cor(201, 60, 50); // Fundo pressionado
	private static Color C_FG_T = Util.cor(52, 10, 100); // Cor do texto
	private int x, y, w, h;
	private String texto;
	private int grupo;
	
	public Botao(String texto, int x, int y, int w, int h) {
		System.out.println("Construtor Botao");
		this.texto = texto;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.grupo = 0;
	}
	
	public boolean pontoSobre(int x, int y) {
		return (x > this.x && x < this.x + this.w && y > this.y && y < this.y + this.h);
	}
	
	public boolean drawMe(Graphics g, int mx, int my, boolean mouseB) {
		int dx, dy;
		g.setFont(new Font("Arial BOLD", Font.BOLD, 18));
		dx = 0; dy = 0;
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

}
