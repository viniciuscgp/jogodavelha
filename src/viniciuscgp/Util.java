package viniciuscgp;

import java.awt.Color;

public final class Util {
	
	// Fiz essa função pra converter aqueles pickers de cores que tem
	// na internet para os parametros do getHSBColor()
	// acho mais simples
	// ph 0-360 ps: % pb: %

	public static Color cor(double ph, double ps, double pb) {
		float _ph = ((float) ph / 360 * 100) / 100;
		float _ps = (float) ps / 100;
		float _pb = (float) pb / 100;

		return Color.getHSBColor(_ph, _ps, _pb);

	}
}
