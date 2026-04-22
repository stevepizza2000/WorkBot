package com.osbarnabe.workbot;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Objeto que cai do topo da tela no Puzzle1.
 * correto = true  → ferramenta (deve ir para a caixa verde)
 * correto = false → lixo      (deve ir para a caixa vermelha)
 */
public class ObjetoCaindo {

    float alturaJanela  = Gdx.graphics.getHeight();
    float larguraJanela = Gdx.graphics.getWidth();

    public float x, y;
    public float velocidadeY = 150f;
    public boolean correto;

    private Texture textura;

    public ObjetoCaindo(float x, float y, boolean correto) {
        this.x      = x;
        this.y      = y;
        this.correto = correto;
        textura = new Texture(correto ? "ferramenta.png" : "lixo.png");
    }

    public void update(float delta) {
        y -= velocidadeY * delta;
    }

    /** Move o objeto para o lado direito da tela (caixa verde). */
    public void irDireita() {
        x = larguraJanela / 2f + 250f;
    }

    /** Move o objeto para o lado esquerdo da tela (caixa vermelha). */
    public void irEsquerda() {
        x = larguraJanela / 2f - 150f;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(textura, x, y, 64, 64);
    }

    public void dispose() {
        textura.dispose();
    }
}
