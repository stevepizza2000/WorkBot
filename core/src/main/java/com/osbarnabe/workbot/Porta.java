package com.osbarnabe.workbot;
/**
 * Representa uma porta que o robô pode interagir.
 * Quando interagida, sinaliza a troca de tela para o puzzle correspondente.
 */
public class Porta {

    public float x;
    public float y;
    public float largura;
    public float altura;
    public boolean estaAberta = true;

    public Porta(float x, float y, float largura, float altura) {
        this.x       = x;
        this.y       = y;
        this.largura = largura;
        this.altura  = altura;
    }

    /** Chamado quando o robô interage com a porta. Fecha-a para disparar a troca de tela. */
    public void interagir() {
        if (estaAberta) {
            System.out.println("Entrando no Puzzle!");
            estaAberta = false;
        }
    }
}
