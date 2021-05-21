package viniciuscgp;
//

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Jogo extends JPanel implements KeyListener, MouseListener, MouseMotionListener {
	private static final long serialVersionUID = 1L;

	// Teclas pressionadas
	private boolean teclas[];

	// parent aqui � o Janela principal classe Main.
	// Uso para acessar dados
	Main par;

	// Composicao
	// -----------------------------------
	Tabuleiro tabuleiro;
	Oponente jogador1, jogador2;
	Oponente jogadorAtual;
	Botao botoesMenu[];

	private int mouseX, mouseY;
	private boolean mousePress = false;
	private boolean mouseSolta = false;

	// Estados do jogo
	private static final int MENU = 1;
	private static final int JOGO = 2;
	private static final int FINAL = 3;

	// Cores usadas no jogo
	private static final Color C_BG_MENU = Util.cor(79, 0, 100);
	private static final Color C_FG_MENU = Util.cor(70, 0, 0);

	private static final Color C_BG_JOGO = Util.cor(79, 80, 100);

	private static final Color C_BG_HUD = Util.cor(79, 90, 90);
	private static final Color C_FG_HUD = Util.cor(261, 70, 90);

	BufferedImage imgVelhinha = null;

	int estado;
	int dificuldade;

	// Construtor
	public Jogo(Main par) {
		// Este objeto vai ouvir os eventos
		System.out.println("Construtor Jogo");
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		// Frame pai
		this.par = par;
		configuraJanela();
		inicio();
	}

	private void configuraJanela() {
		setPreferredSize(new Dimension(par.W, par.H));
		requestFocus();
		carregaRecursos();
	}

	private void carregaRecursos() {
		try {
			imgVelhinha = ImageIO.read(getClass().getResource("/images/velhinha.png"));
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		teclas = new boolean[255];
		for (int i = 0; i < 255; i++) {
			teclas[i] = false;
		}
		botoesMenu = new Botao[3];
		botoesMenu[0] = new Botao("Facil", 170 + 100, 450, 80, 30);
		botoesMenu[1] = new Botao("Médio", 170 + 190, 450, 80, 30);
		botoesMenu[2] = new Botao("Difícil", 170 + 280, 450, 80, 30);
	}

	private void inicio() {
		estado = MENU;
		dificuldade = 0;
		inicializaFps();
	}

	private void novoJogo(int dif) {
		dificuldade = dif;
		tabuleiro = new Tabuleiro();
		estado = JOGO;
		
		jogador1 = new JogadorHumano();
		// Herança e polimorfismo
		switch (dif) {
		case 0:
			jogador2 = new ComputadorFacil();
			jogadorAtual = jogador1;
			break;
		case 1:
			jogador2 = new ComputadorMedio();
			if (new Random().nextBoolean())
				jogadorAtual = jogador1;
			else
				jogadorAtual = jogador2;
			break;
		case 2:
			jogador2 = new ComputadorDificil();
			if (new Random().nextBoolean())
				jogadorAtual = jogador1;
			else
				jogadorAtual = jogador2;
			break;
		}
		mudouJogador();
	}

	private void mudouJogador() {
		// Permite ou não que o usuário interaja com o tabuleiro
		// Dependendo se é a maquina ou você
		tabuleiro.setGuiTravada(!jogadorAtual.isHumano());
		if (!jogadorAtual.isHumano()) {
			
		}
	}

	private void inicializaFps() {
		Timer fps = new Timer();
		fps.schedule(new TimerTask() {
			@Override
			public void run() {
				Jogo.this.processaEstado();
				Jogo.this.repaint();
			}

		}, 0, 5);
	}

	// Funções de processamento de cada Estado
	// ------------------------------------------

	private void processaEstado() {
		switch (estado) {
		case MENU:
			estadoMenu();
			break;
		case JOGO:
			estadoJogo();
			break;
		case FINAL:
			estadoFinal();
			break;
		}
	}

	private void estadoMenu() {
		int j = -1;
		if (mousePress) {
			for (int i = 0; i < botoesMenu.length; i++) {
				if (botoesMenu[i].pontoSobre(mouseX, mouseY)) {
					j = i;
					break;
				}
			}
		}
		if (j != -1)
			novoJogo(j);
	}

	private void estadoJogo() {

	}

	private void estadoFinal() {

	}

	// ------------------------------------------
	// Funções de desenho de cada Estado
	// ------------------------------------------
	private void desenhaEstado(Graphics g) {
		// super.paint(g);
		switch (estado) {
		case MENU:
			desenhaMenu(g);
			break;
		case JOGO:
			desenhaJogo(g);
			break;
		case FINAL:
			desenhaFinal(g);
			break;
		}
		mouseSolta = false;
		mousePress = false;		
	}
	
	// Estado = MENU
	private void desenhaMenu(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g2.setColor(C_BG_MENU);
		g2.fillRect(0, 0, par.W, par.H);
		g2.setColor(C_FG_MENU);

		g2.setFont(new Font("Arial BOLD", Font.BOLD, 32));
		drawCenterW(g, "JOGO DA VELHA", centerY(g) - 150);

		if (imgVelhinha != null)
			g2.drawImage(imgVelhinha, (par.W - 200) / 2, (par.H - 200) / 2, 200, 200, null, null);

		// Desenha os botoes
		for (int i = 0; i < botoesMenu.length; i++) {
			botoesMenu[i].drawMe(g, mouseX, mouseY, mousePress);
		}

		g2.setColor(C_FG_MENU);
		g2.setFont(new Font("Arial BOLD", Font.BOLD, 18));
		drawCenterW(g, "Selecione a dificuldade", centerY(g) + 130);

	}

	// Estado = JOGO
	private void desenhaJogo(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g2.setColor(C_BG_JOGO);
		g2.fillRect(0, 0, par.W, par.H);
		
		g2.setFont(new Font("Arial Black", Font.BOLD, 32));
		g2.setColor(C_FG_HUD);
		drawCenterW(g, "Vez do: " + jogadorAtual.getDescricao(), 60);
		
		tabuleiro.desenheMe(g, mouseX, mouseY, mouseSolta);
		
		if (imgVelhinha != null)
			g.drawImage(imgVelhinha, 60, 200, 200, 200, null, null);

		g2.setColor(C_BG_HUD);
		g2.fillRect(0, par.H - 150, par.W, par.H);
		g2.setFont(new Font("Arial Black", Font.BOLD, 32));
		g2.setColor(C_FG_HUD);
		drawCenterW(g, jogadorAtual.getDificuldadeDescricao(), par.H - (150 / 2));
	}

	// Estado = FINAL
	private void desenhaFinal(Graphics g) {

	}

	private void drawCenterW(Graphics g, String str, int y) {
		g.drawString(str, centerX(g, str), y);
	}

	private int centerX(Graphics g, String str) {
		return (par.W - g.getFontMetrics().stringWidth(str)) / 2;
	}

	private int centerY(Graphics g) {
		return (par.H - g.getFontMetrics().getHeight()) / 2;
	}

	private void queroSair() {
		int input = JOptionPane.showConfirmDialog(null, "Deseja sair ?", "Jogo da Velha", JOptionPane.YES_NO_OPTION);
		if (input == 0) {
			par.dispose();
		}
	}

	// -------------------------------------------
	// Eventos interceptados
	// -------------------------------------------

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_ESCAPE) {
			queroSair();
		}

		if (key >= 0 && key <= 255)
			teclas[key] = true;

	}

	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		if (key >= 0 && key <= 255)
			teclas[key] = false;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		mousePress = true;
		mouseSolta = false;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mousePress = false;
		mouseSolta = true;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

	// Sem isso n�o funciona os eventos de foco.
	@Override
	public boolean isFocusTraversable() {
		return true;
	}
	
	@Override
	public void paint(Graphics g) {
		// super.paint(g);
		desenhaEstado(g);
	}
	
}
