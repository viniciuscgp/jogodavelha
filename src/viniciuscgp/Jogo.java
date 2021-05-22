package viniciuscgp;

//
// Classe Jogo - Controle o estado do Jogo
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
import javax.sound.sampled.Clip;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Jogo extends JPanel implements KeyListener, MouseListener, MouseMotionListener {
	private static final long serialVersionUID = 1L;

	// Teclas pressionadas
	private boolean teclas[];

	// parent aqui � o Janela principal classe Main.
	// Uso para acessar dados
	Main frame;

	// Composicao
	// -----------------------------------
	Tabuleiro tabuleiro;
	Oponente jogador1, jogador2;
	Oponente jogadorAtual;
	Botao botoesMenu[];
	Botao botoesFinal[];
	Timer fps;

	private int mouseX, mouseY;
	private boolean mousePress = false;
	private boolean mouseSolta = false;

	// Estados do jogo
	private static final int E_MENU = 1; // Estou no menu
	private static final int E_JOGO = 2; // Estou jogando
	private static final int E_FIM_PARTIDA = 3; // Alguem venceu (tempo pra mostrar algo)
	private static final int E_FINAL = 4; // Tela de fim de jogo e analise

	// Cores usadas no jogo
	private static final Color C_BG_MENU = Util.cor(79, 0, 100);
	private static final Color C_FG_MENU = Util.cor(70, 0, 0);

	private static final Color C_BG_JOGO = Util.cor(79, 80, 100);

	private static final Color C_BG_HUD = Util.cor(79, 90, 90);
	private static final Color C_FG_HUD = Util.cor(261, 70, 90);

	private static final Color C_BG_FINAL = Util.cor(59, 50, 100);
	private static final Color C_FG_FINAL = Util.cor(50, 20, 0);

	BufferedImage imgVelhinha = null;

	int estado;
	int dificuldade;
	int ultimoEstadoTabuleiro;
	Clip musica;

	// Construtor
	public Jogo(Main f) {
		// Este objeto vai ouvir os eventos
		this.frame = f;
		System.out.println("Construtor Jogo");
		configuraJanela();
		carregaRecursos();
		telaInicial();
	}

	// Esta funcao so precisa executar uma vez
	// ----------------------------------------
	private void configuraJanela() {
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		// Frame pai
		setPreferredSize(new Dimension(frame.W, frame.H));
		requestFocus();
	}

	// Esta funcao so precisa executar uma vez
	// ----------------------------------------
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
		botoesMenu[0] = new Botao("Facil", 240, 450);
		botoesMenu[1] = new Botao("Médio", botoesMenu[0].getNext(), botoesMenu[0].getY());
		botoesMenu[2] = new Botao("Difícil", botoesMenu[1].getNext(), botoesMenu[1].getY());

		botoesFinal = new Botao[2];
		botoesFinal[0] = new BotaoMedio("Jogar novamente", 270, 450);
		botoesFinal[1] = new Botao("Sair", botoesFinal[0].getNext(), botoesFinal[0].getY());

		botoesFinal[0].setVisivel(false);
		botoesFinal[1].setVisivel(false);

		inicializaFps();
		// musica = Util.playSound("/sounds/mr_clown.wav", Clip.LOOP_CONTINUOUSLY);
	}

	private void telaInicial() {
		estado = E_MENU;
		dificuldade = 0;
		ultimoEstadoTabuleiro = 0;
		for (int i = 0; i < botoesMenu.length; i++) {
			botoesMenu[i].setVisivel(true);
		}

	}

	// Inicia o jogos preparand todos os objetos
	// -------------------------------------------

	private void comecaJogo(int dif) {
		dificuldade = dif;
		tabuleiro = new Tabuleiro(this);
		estado = E_JOGO;

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

	// Cada vez que muda de turno
	// -------------------------------------------
	private void mudouJogador() {
		// Permite ou não que o usuário interaja com o tabuleiro
		// Dependendo se é a maquina ou você
		tabuleiro.setGuiTravada(!jogadorAtual.isHumano());
		if (!jogadorAtual.isHumano()) {
			Timer fps = new Timer();
			fps.schedule(new TimerTask() {
				@Override
				public void run() {
					Jogo.this.computadorJoga();
				}

			}, 2000);
		}
	}

	public void humanoJogou() {
		analisaTabuleiro();
	}

	private void computadorJoga() {
		jogadorAtual.proximaJogada(tabuleiro);
		analisaTabuleiro();
	}

	private void analisaTabuleiro() {
		ultimoEstadoTabuleiro = tabuleiro.verificaEstado();
		if (ultimoEstadoTabuleiro == Tabuleiro.E_JOGANDO) {
			if (jogadorAtual == jogador1)
				jogadorAtual = jogador2;
			else
				jogadorAtual = jogador1;
			mudouJogador();
		} else {
			tabuleiro.setGuiTravada(true);
			Timer fps = new Timer();
			fps.schedule(new TimerTask() {
				@Override
				public void run() {
					for (int i = 0; i < botoesMenu.length; i++) {
						botoesMenu[i].setVisivel(false);
					}
					for (int i = 0; i < botoesFinal.length; i++) {
						botoesFinal[i].setVisivel(true);
					}
					estado = E_FINAL;
				}

			}, 2000);

		}
	}

	private void inicializaFps() {
		fps = new Timer();
		fps.schedule(new TimerTask() {
			@Override
			public void run() {
				Jogo.this.processaEstado();
				Jogo.this.repaint();
			}

		}, 0, 5);
		System.out.println("Timer (FPS) criado.");
	}

	// Funções de processamento de cada Estado
	// ------------------------------------------

	private void processaEstado() {
		switch (estado) {
		case E_MENU:
			estadoMenu();
			break;
		case E_JOGO:
			estadoJogo();
			break;
		case E_FIM_PARTIDA:
			estadoFimPartida();
			break;
		case E_FINAL:
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
		if (j != -1) {
			Util.playSound("/sounds/click.wav");
			comecaJogo(j);
		}
	}

	private void estadoJogo() {

	}

	private void estadoFimPartida() {

	}

	private void estadoFinal() {
		int j = -1;
		if (mousePress) {
			for (int i = 0; i < botoesFinal.length; i++) {
				if (botoesFinal[i].pontoSobre(mouseX, mouseY)) {
					j = i;
					break;
				}
			}
		}
		// Jogar novamente
		if (j == 0) {
			for (int i = 0; i < botoesMenu.length; i++) {
				botoesMenu[i].setVisivel(false);
			}
			telaInicial();
		}
		// Sair
		if (j == 1) {
			queroSair();
		}

	}

	// ------------------------------------------
	// Funções de desenho de cada Estado
	// ------------------------------------------
	private void desenhaEstado(Graphics g) {
		// super.paint(g);
		switch (estado) {
		case E_MENU:
			desenhaMenu(g);
			break;
		case E_JOGO:
			desenhaJogo(g);
			break;
		case E_FIM_PARTIDA:
			desenhaFimPartida(g);
			break;
		case E_FINAL:
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
		g2.fillRect(0, 0, frame.W, frame.H);
		g2.setColor(C_FG_MENU);

		g2.setFont(new Font("Arial", Font.BOLD, 32));
		drawCenterW(g, "JOGO DA VELHA", centerY(g) - 150);

		if (imgVelhinha != null)
			g2.drawImage(imgVelhinha, (frame.W - 200) / 2, (frame.H - 200) / 2, 200, 200, null, null);

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
		g2.fillRect(0, 0, frame.W, frame.H);

		g2.setFont(new Font("Arial Black", Font.BOLD, 32));
		g2.setColor(C_FG_HUD);
		drawCenterW(g, "Vez do: " + jogadorAtual.getDescricao(), 60);

		tabuleiro.desenheMe(g, mouseX, mouseY, mouseSolta);

		if (imgVelhinha != null)
			g.drawImage(imgVelhinha, 60, 200, 200, 200, null, null);

		g2.setColor(C_BG_HUD);
		g2.fillRect(0, frame.H - 150, frame.W, frame.H);
		g2.setFont(new Font("Arial Black", Font.BOLD, 32));
		g2.setColor(C_FG_HUD);
		drawCenterW(g, jogadorAtual.getDificuldadeDescricao(), frame.H - (150 / 2));
	}

	// Estado = FIM_PARTIDA
	private void desenhaFimPartida(Graphics g) {
		// TODO colocar efeitos entre o termino e a tela final
	}

	// Estado = FINAL
	private void desenhaFinal(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2.setColor(C_BG_FINAL);
		g2.fillRect(0, 0, frame.W, frame.H);
		g2.setColor(C_FG_FINAL);

		g2.setFont(new Font("Arial", Font.BOLD, 32));
		drawCenterW(g, "FIM DE JOGO", centerY(g) - 150);

		if (imgVelhinha != null)
			g2.drawImage(imgVelhinha, (frame.W - 200) / 2, (frame.H - 300) / 2, 200, 200, null, null);

		// Desenha os botoes
		for (int i = 0; i < botoesMenu.length; i++) {
			botoesMenu[i].drawMe(g, mouseX, mouseY, mousePress);
		}

		g2.setColor(C_FG_FINAL);
		g2.setFont(new Font("Arial", Font.BOLD, 38));
		if (ultimoEstadoTabuleiro == Tabuleiro.E_EMPATOU) {
			drawCenterW(g, "Empate !", centerY(g) + 110);

		} else {
			if (jogadorAtual.isHumano())
				drawCenterW(g, "Parabéns, você venceu o computador!", centerY(g) + 110);
			else
				drawCenterW(g, "Que pena, o Computador venceu!", centerY(g) + 110);
		}
		for (int i = 0; i < botoesFinal.length; i++) {
			botoesFinal[i].drawMe(g, mouseX, mouseY, mousePress);
		}

	}

	private void drawCenterW(Graphics g, String str, int y) {
		g.drawString(str, centerX(g, str), y);
	}

	private int centerX(Graphics g, String str) {
		return (frame.W - g.getFontMetrics().stringWidth(str)) / 2;
	}

	private int centerY(Graphics g) {
		return (frame.H - g.getFontMetrics().getHeight()) / 2;
	}

	private void queroSair() {
		fps.cancel();
		fps.purge();
		int input = JOptionPane.showConfirmDialog(null, "Deseja sair ?", "Jogo da Velha", JOptionPane.YES_NO_OPTION);
		if (input == 0) {
			frame.dispose();
		} else {
			inicializaFps();
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
