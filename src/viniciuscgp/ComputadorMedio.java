package viniciuscgp;

import java.util.Random;

public class ComputadorMedio extends Computador {
	public ComputadorMedio() {
		System.out.println("Construtor Computador: Médio");
	}

	@Override
	public void proximaJogada(Tabuleiro t) {
		/*
		 * Este é um pouco mais inteligente. Primeiro ele verifica se se você esta
		 * prestes a ganhar dele. Se tiver ele bloqueia. Se não ele procura um lugar
		 * vazio aleatório. Mas ele ainda não olha nas Diagonais.
		 */
		int soma = 0;
		int vazio = 0;
		int iAchou = -1;
		int jAchou = -1;

		for (int i = 0; i < 3; i++) {
			soma = 0;
			for (int j = 0; j < 3; j++) {
				if (t.celulaVerifica(i, j) == Tabuleiro.VAZIO)
					vazio = j;
				soma += t.celulaVerifica(i, j);
			}
			if (soma == 2 || soma == 16) {
				iAchou = i;
				jAchou = vazio;
				break;
			}
		}

		if (iAchou == -1 && jAchou == -1) {
			// Vertical
			for (int i = 0; i < 3; i++) {
				soma = 0;
				for (int j = 0; j < 3; j++) {
					if (t.celulaVerifica(j, i) == Tabuleiro.VAZIO)
						vazio = j;
					soma += t.celulaVerifica(j, i);
				}
				if (soma == 2 || soma == 16) {
					iAchou = vazio;
					jAchou = i;
					break;
				}
			}
		}

		if (iAchou == -1 && jAchou == -1) {
			while (true) {
				int l = new Random().nextInt(3);
				int c = new Random().nextInt(3);
				if (t.celulaVerifica(l, c) == Tabuleiro.VAZIO) {
					iAchou = l;
					jAchou = c;
					break;
				}
			}

		}
		t.celulaMarca(iAchou, jAchou, Tabuleiro.O);

	}

	@Override
	public String getDificuldadeDescricao() {
		return "Médio";
	}

	@Override
	public String getDescricao() {
		return "Computador";
	}

	@Override
	public boolean isHumano() {
		return false;
	}

}
