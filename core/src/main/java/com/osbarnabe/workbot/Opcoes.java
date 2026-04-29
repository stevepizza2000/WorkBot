package com.osbarnabe.workbot;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class Opcoes implements Screen {

    private Main jogo;
    private SpriteBatch batch;
    private BitmapFont font;

    private float tempoAFK = 10f;
    private final float limiteAFK = 0f;
    private float tempoPressionado = 0f;

    private Texture btnVoltar, btnVoltarSelect;
    private Texture btnPortugues, btnPortuguesSelect;
    private Texture btnIngles, btnInglesSelect;
    private Texture btnEspanhol, btnEspanholSelect;

    private boolean botoesLiberados = false;
    private int opcaoSelecionada = 0;
    private boolean processouBotao = false;

    float larguraJanela = Gdx.graphics.getWidth();
    float alturaJanela = Gdx.graphics.getHeight();

    // 🔥 TEXTOS TRADUZIDOS
    private String[] textosAFK = {
        "As opcoes vao fechar em: ",
        "Las opciones se cerraran en: ",
        "The options will close in: "
    };

    public Opcoes(Main jogo) {
        this.jogo = jogo;
        batch = new SpriteBatch();

        font = new BitmapFont();
        font.getData().setScale(4f);

        atualizarTexturasIdioma(); // 🔥 importante
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0f, 0f, 0f, 1f);

        boolean esq = Gdx.input.isKeyPressed(Input.Keys.LEFT);
        boolean dir = Gdx.input.isKeyPressed(Input.Keys.RIGHT);

        if (!esq && !dir) botoesLiberados = true;

        if (botoesLiberados) {
            if (esq && dir) {
                if (!processouBotao) {
                    switch (opcaoSelecionada) {

                        case 0: // Português
                            jogo.idioma = 0;
                            atualizarTexturasIdioma();
                            break;

                        case 1: // Espanhol
                            jogo.idioma = 1;
                            atualizarTexturasIdioma();
                            break;

                        case 2: // Inglês
                            jogo.idioma = 2;
                            atualizarTexturasIdioma();
                            break;

                        case 3: // Voltar
                            jogo.setScreen(new MenuScreen(jogo));
                            break;
                    }
                    processouBotao = true;
                }

            } else if (esq || dir) {

                if (!processouBotao) {
                    tempoPressionado += delta;

                    if (tempoPressionado > 0.02f) {

                        if (dir) {
                            opcaoSelecionada++;
                            if (opcaoSelecionada > 3) opcaoSelecionada = 0;
                        } else {
                            opcaoSelecionada--;
                            if (opcaoSelecionada < 0) opcaoSelecionada = 3;
                        }

                        tempoAFK = 10f;
                        processouBotao = true;
                    }
                }

            } else {
                tempoPressionado = 0f;
                processouBotao = false;
            }
        }

        tempoAFK -= delta;
        if (tempoAFK <= limiteAFK) jogo.setScreen(new MenuScreen(jogo));

        batch.begin();

        batch.draw(opcaoSelecionada == 0 ? btnPortuguesSelect : btnPortugues,
            larguraJanela/2 - 180, alturaJanela/2 + 250, 400, 200);

        batch.draw(opcaoSelecionada == 1 ? btnEspanholSelect : btnEspanhol,
            larguraJanela/2 - 180, alturaJanela/2, 400, 200);

        batch.draw(opcaoSelecionada == 2 ? btnInglesSelect : btnIngles,
            larguraJanela/2 - 180, alturaJanela/2 - 250, 400, 200);

        batch.draw(opcaoSelecionada == 3 ? btnVoltarSelect : btnVoltar,
            larguraJanela/2 - 180, alturaJanela/2 - 500, 400, 200);

        // 🔥 AFK traduzido
        if (tempoAFK <= 5f && tempoAFK > 0f) {
            int seg = (int) Math.ceil(tempoAFK);
            font.draw(batch,
                textosAFK[jogo.idioma] + seg,
                larguraJanela/2 - 300,
                alturaJanela - 80);
        }

        batch.end();
    }

    // 🔥 TROCA DINÂMICA DE TEXTURAS
    private void atualizarTexturasIdioma() {

        // Limpa antigas
        if (btnVoltar != null) {
            btnVoltar.dispose(); btnVoltarSelect.dispose();
            btnPortugues.dispose(); btnPortuguesSelect.dispose();
            btnIngles.dispose(); btnInglesSelect.dispose();
            btnEspanhol.dispose(); btnEspanholSelect.dispose();
        }

        if (jogo.idioma == 0) {
            // PORTUGUÊS
            btnVoltar = new Texture("BotaoVoltar.png");
            btnVoltarSelect = new Texture("BotaoVoltarSelect.png");

            btnPortugues = new Texture("BotaoPortugues.png");
            btnPortuguesSelect = new Texture("BotaoPortSelect.png");

            btnIngles = new Texture("BotaoIngles.png");
            btnInglesSelect = new Texture("BotaoInglesSelect.png");

            btnEspanhol = new Texture("BotaoEspanhol.png");
            btnEspanholSelect = new Texture("BotaoEspanholSelect.png");

        } else if (jogo.idioma == 1) {
            // ESPANHOL
            btnVoltar = new Texture("BotaoVoltar_ES.png");
            btnVoltarSelect = new Texture("BotaoVoltarSelect_ES.png");

            btnPortugues = new Texture("BotaoPortugues_ES.png");
            btnPortuguesSelect = new Texture("BotaoPortSelect_ES.png");

            btnIngles = new Texture("BotaoIngles_ES.png");
            btnInglesSelect = new Texture("BotaoInglesSelect_ES.png");

            btnEspanhol = new Texture("BotaoEspanhol_ES.png");
            btnEspanholSelect = new Texture("BotaoEspanholSelect_ES.png");

        } else {
            // INGLÊS
            btnVoltar = new Texture("BotaoVoltar_EN.png");
            btnVoltarSelect = new Texture("BotaoVoltarSelect_EN.png");

            btnPortugues = new Texture("BotaoPortugues_EN.png");
            btnPortuguesSelect = new Texture("BotaoPortSelect_EN.png");

            btnIngles = new Texture("BotaoIngles_EN.png");
            btnInglesSelect = new Texture("BotaoInglesSelect_EN.png");

            btnEspanhol = new Texture("BotaoEspanhol_EN.png");
            btnEspanholSelect = new Texture("BotaoEspanholSelect_EN.png");
        }
    }

    @Override public void show() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();

        if (btnVoltar != null) {
            btnVoltar.dispose(); btnVoltarSelect.dispose();
            btnPortugues.dispose(); btnPortuguesSelect.dispose();
            btnIngles.dispose(); btnInglesSelect.dispose();
            btnEspanhol.dispose(); btnEspanholSelect.dispose();
        }
    }
}
