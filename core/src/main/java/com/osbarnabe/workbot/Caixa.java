package com.osbarnabe.workbot;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Representa uma caixa de destino no Puzzle1.
 * aceitaCorreto = true  → caixa verde (aceita ferramentas)
 * aceitaCorreto = false → caixa vermelha (aceita lixo)
 */
public class Caixa {

    public float x, y;
    public boolean aceitaCorreto;

    private Texture textura;

    public Caixa(float x, float y, boolean aceitaCorreto) {
        this.x = x;
        this.y = y;
        this.aceitaCorreto = aceitaCorreto;
        textura = new Texture(aceitaCorreto ? "caixa_verde.png" : "caixa_vermelha.png");
    }

    public void draw(SpriteBatch batch) {
        batch.draw(textura, x, y, 150, 120);
    }

    public void dispose() {
        textura.dispose();
    }
}
