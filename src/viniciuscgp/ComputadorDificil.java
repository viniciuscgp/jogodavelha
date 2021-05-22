package viniciuscgp;

import java.util.Random;

//
//Classe - Computador Dificil
//

public class ComputadorDificil extends Computador {

	public ComputadorDificil() {
		System.out.println("Construtor Computador: Dificil");
	}

	@Override
	public void proximaJogada(Tabuleiro t) {
		/*
		 * Esse é quase igual ao Médio. Mas ele é um pouco mais esperto porque olha as
		 * diagonais.
		 * 
		 */
		int soma = 0;
		int vazio = 0;
		int iAchou = -1;
		int jAchou = -1;

		// Analise Horizontal
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

		// Analise Vertical
		if (iAchou == -1 && jAchou == -1) {
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

		// Analise Diagonal
		// 0,0 1,1 2,2 -> Esquerda pra direita
		int li[] = { 0, 1, 2, 0, 1, 2 };
		int co[] = { 0, 1, 2, 2, 1, 0 };

		if (iAchou == -1 && jAchou == -1) {
			soma = 0;
			for (int i = 0; i < 3; i++) {
				if (t.celulaVerifica(li[i], co[i]) == Tabuleiro.VAZIO)
					vazio = i;
				soma += t.celulaVerifica(li[i], co[i]);
			}
			if (soma == 2 || soma == 16) {
				iAchou = li[vazio];
				jAchou = co[vazio];
			}
		}

		// 0,2 1,1 2,0 -> Direita pra esquerda
		if (iAchou == -1 && jAchou == -1) {
			soma = 0;
			for (int i = 3; i < 6; i++) {
				if (t.celulaVerifica(li[i], co[i]) == Tabuleiro.VAZIO)
					vazio = i;
				soma += t.celulaVerifica(li[i], co[i]);
			}
			if (soma == 2 || soma == 16) {
				iAchou = li[vazio];
				jAchou = co[vazio];
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
		return "Dificil";
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
