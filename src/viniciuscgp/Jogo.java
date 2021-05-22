package viniciuscgp;

//
// Classe Jogo - Controle o estado do Jogo
// 

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
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

	// Uso para acessar dados
	Main frame;

	// Composicao
	// -----------------------------------
	Tabuleiro tabuleiro;
	Oponente jogador1, jogador2;
	Oponente jogadorAtual;
	Botao botoesMenu[];
	Botao botoesFinal[];
	Botao botaoPlay;
	Timer fps;
	String dificuldadeAtual;
	Banner infoRodape;

	private int mouseX, mouseY;
	private boolean mousePress = false;
	private boolean mouseSolta = false;

	BufferedImage imgVelhinha = null;
	BufferedImage imgPlay = null;

	int estado;
	int dificuldade;
	int ultimoEstadoTabuleiro;
	String fonteEspecial;
	Clip musica = null;

	// Estados do jogo
	private static final int E_MENU = 1; // Estou no menu
	private static final int E_JOGO = 2; // Estou jogando
	private static final int E_FIM_PARTIDA = 3; // Alguem venceu (tempo pra mostrar algo)
	private static final int E_FINAL = 4; // Tela de fim de jogo e analise

	// Cores usadas no jogo
	private static final Color C_BG_MENU = Util.cor(79, 60, 90);
	private static final Color C_FG_MENU = Util.cor(70, 0, 0);

	private static final Color C_BG_JOGO = Util.cor(70, 60, 90);

	private static final Color C_BG_HUD = Util.cor(79, 90, 90);
	private static final Color C_FG_HUD = Util.cor(241, 70, 90);

	private static final Color C_BG_FINAL = Util.cor(59, 50, 100);
	private static final Color C_FG_FINAL = Util.cor(349, 100, 59);

	// Construtor
	public Jogo(Main f) {
		this.frame = f;
		System.out.println("Construtor Jogo");
		configuraJanela();
		carregaRecursos();
		telaInicial();
	}

	// Esta funcao so precisa executar uma vez
	// ----------------------------------------
	private void configuraJanela() {
		// Este objeto vai ouvir os eventos
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		// Para o pack funcionar
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

		try {
			imgPlay = ImageIO.read(getClass().getResource("/images/play.png"));
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

		botoesFinal = new Botao[1];
		botoesFinal[0] = new BotaoMedio("Jogar novamente", 270, 450);
		botoesFinal[0].setVisivel(false);
		botoesFinal[0].centralizaH(frame.W);

		botaoPlay = new BotaoPequeno("", 10, 10);
		botaoPlay.setImagem(imgPlay);

		// Carrega uma fonte externa (ttf)
		// ------------------------------------------------------------------------------
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		fonteEspecial = "Arial";

		try {
			ge.registerFont(
					Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/other/Niconne-Regular.ttf")));
			fonteEspecial = "Niconne";
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}

		// Bobeirinha de hud giratorio
		// ------------------------------------------------------------------------------
		infoRodape = new Banner("By Vinicius César - Twitter @viniciuscgp - !■ ESC ■> para sair a qualquer momento", 0,
				frame.H - 40 - 30, frame.W, 70);

		// Inicializa uma thread de contagem que vai dar repaint no panel
		inicializaFps();
		musica = Util.playSound("/sounds/mr_clown.wav", Clip.LOOP_CONTINUOUSLY);
	}

	// Inicia no modo menu
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
			// Facil - Sempre começamos
			jogador2 = new ComputadorFacil();
			jogadorAtual = jogador1;
			break;
		case 1:
			// Medio - As vezes o computador começa
			jogador2 = new ComputadorMedio();
			if (new Random().nextBoolean())
				jogadorAtual = jogador1;
			else
				jogadorAtual = jogador2;
			break;
		case 2:
			// Dificil - As vezes o computador começa
			jogador2 = new ComputadorDificil();
			if (new Random().nextBoolean())
				jogadorAtual = jogador1;
			else
				jogadorAtual = jogador2;
			break;
		}

		if (!jogador1.isHumano())
			dificuldadeAtual = jogador1.getDificuldadeDescricao();
		else
			dificuldadeAtual = jogador2.getDificuldadeDescricao();

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

		}, 0, 17);
		System.out.println("Timer (FPS) criado.");
	}

	// Funções de processamento de cada Estado
	// ------------------------------------------
	private void botaoPlay() {
		if (mousePress) {
			if (botaoPlay.pontoSobre(mouseX, mouseY)) {
				if (musica != null) {
					if (musica.isRunning())
						musica.stop();
					else
						musica.start();
				}
			}
		}

	}

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

		botaoPlay();
	}

	private void estadoJogo() {
		botaoPlay();
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
		botaoPlay();

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

		g2.setFont(new Font(fonteEspecial, Font.BOLD, 64));
		drawCenterW(g, "O jogo da velha", 80);

		if (imgVelhinha != null)
			g2.drawImage(imgVelhinha, (frame.W - 300) / 2, (frame.H - 300) / 2 - 60, 300, 300, null, null);

		// Desenha os botoes
		for (int i = 0; i < botoesMenu.length; i++) {
			botoesMenu[i].drawMe(g, mouseX, mouseY, mousePress);
		}

		g2.setColor(C_FG_MENU);
		g2.setFont(new Font("Arial BOLD", Font.BOLD, 18));
		drawCenterW(g, "Selecione a dificuldade", centerY(g) + 130);

		infoRodape.drawMe(g);

		botaoPlay.drawMe(g, mouseX, mouseY, mousePress);

	}

	// Estado = JOGO
	private void desenhaJogo(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2.setColor(C_BG_JOGO);
		g2.fillRect(0, 0, frame.W, frame.H);

		g2.setFont(new Font("Arial Black", Font.BOLD, 32));
		g2.setColor(C_FG_HUD);
		drawCenterW(g, "Quem joga agora é o " + jogadorAtual.getDescricao(), 60);

		tabuleiro.desenheMe(g, mouseX, mouseY, mouseSolta);

		if (imgVelhinha != null)
			g.drawImage(imgVelhinha, 60, 200, 200, 200, null, null);

		g2.setColor(C_BG_HUD);
		g2.fillRect(0, frame.H - 150, frame.W, frame.H);
		g2.setFont(new Font("Arial Black", Font.BOLD, 32));
		g2.setColor(C_FG_HUD);
		drawCenterW(g, "Modo de dificuldade: " + dificuldadeAtual, frame.H - 90);
		infoRodape.drawMe(g);
		botaoPlay.drawMe(g, mouseX, mouseY, mousePress);
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
		g2.setFont(new Font(fonteEspecial, Font.BOLD, 88));
		g2.drawString("Fim", 320, 180);
		g2.drawString("de Jogo", 270, 260);

		/*
		 * if (imgVelhinha != null) g2.drawImage(imgVelhinha, 10, (frame.H - 300) / 2 -
		 * 100, 300, 300, null, null);
		 */

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

		infoRodape.drawMe(g);
		botaoPlay.drawMe(g, mouseX, mouseY, mousePress);
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
			if (musica != null) {
				musica.stop();
				musica.flush();
			}
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
