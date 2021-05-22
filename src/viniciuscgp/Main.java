package viniciuscgp;

import java.awt.Dimension;

import javax.swing.JFrame;

//
// Classe - Ponto de entrada 
//
public class Main extends JFrame {

	private static final long serialVersionUID = 1L;
	// Tamanho da janela do jogo
	public final int W = 800;
	public final int H = 600;
	

	public Main() {
		super();
		inicializaFrame();
		pack();
	}
	

	private void inicializaFrame() {
		setPreferredSize(new Dimension(W, H));
		setSize(new Dimension(W, H));
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Jogo da Velha");
		getContentPane().add(new Jogo(this));
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new Main();
	}

}
