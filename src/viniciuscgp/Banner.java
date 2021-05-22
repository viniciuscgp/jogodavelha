package viniciuscgp;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class Banner {
	private int x;
	private int y;
	private int w;
	private int h;
	private int dx;
	private String texto;

	public Banner(String texto, int x, int y, int w, int h) {
		this.x = x;
		this.y = y;

		this.w = w;
		this.h = h;

		this.texto = texto;
		dx = 0;
	}

	public void drawMe(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2.setFont(new Font("Arial Black", Font.PLAIN, 14));

		g2.setColor(Util.C_BRANCO);
		g2.fillRect(x, y, w, h);

		int py = (h - (g2.getFontMetrics().getHeight() + g2.getFontMetrics().getDescent())) / 2;

		g2.setFont(new Font("Arial", Font.PLAIN, 14));
		g2.setColor(Util.C_PRETO);
		g2.drawString(texto, dx, y + py);
		dx -= 1;
		if (dx < -g2.getFontMetrics().stringWidth(texto)) {
			dx = x + w + 10;
		}
	}
}
