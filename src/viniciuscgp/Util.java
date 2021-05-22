package viniciuscgp;

//
// Classe - Utiliário
//

import java.awt.Color;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public final class Util {

	public static final Color C_BRANCO = cor(0, 0, 100);
	public static final Color C_PRETO = cor(0, 0, 0);
	public static final Color C_AMARELO = cor(58, 100, 100);
	public static final Color C_VERMELHO = cor(359, 100, 100);

	// Fiz essa funçao pra converter aqueles pickers de cores que tem
	// na internet para os parametros do getHSBColor()
	// acho mais simples
	// ph 0-360 ps: % pb: %

	public static Color cor(double ph, double ps, double pb) {
		float _ph = ((float) ph / 360 * 100) / 100;
		float _ps = (float) ps / 100;
		float _pb = (float) pb / 100;

		return Color.getHSBColor(_ph, _ps, _pb);

	}

	// Overload pra poder chamar facilmente se nao precisar de retorno
	// Cada clip roda em sua Thread, nao se preocupe.
	public static void playSound(String nomeArquivo) {
		playSound(nomeArquivo, 0);
	}

	public static Clip playSound(String nomeArquivo, int loop) {
		try {
			System.out.println("Tocarei...");
			Clip clip = AudioSystem.getClip();
			// Mixer mixer = AudioSystem.getMixer(null);
			AudioInputStream inputStream = AudioSystem.getAudioInputStream(Main.class.getResourceAsStream(nomeArquivo));
			clip.open(inputStream);
			clip.loop(loop);
			clip.start();
			System.out.println("Tocando...");
			return clip;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
}
