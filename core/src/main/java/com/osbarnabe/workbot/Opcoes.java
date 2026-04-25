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

    private float tempoAFK        = 10f;
    private final float limiteAFK = 0f;
    private float tempoPressionado = 0f;

    private Texture btnVoltar, btnVoltarSelect;
    private Texture btnPortugues, btnPortuguesSelect;
    private Texture btnIngles,   btnInglesSelect;
    private Texture btnEspanhol, btnEspanholSelect;

    private boolean botoesLiberados = false;
    private int opcaoSelecionada   = 0;
    private boolean processouBotao = false;
    float larguraJanela = Gdx.graphics.getWidth();
    float alturaJanela  = Gdx.graphics.getHeight();

    // Textos do aviso AFK traduzidos (sem acento para evitar erros na fonte padrão)
    private String[] textosAFK = {
        "As opcoes vao fechar em: ",     // 0 = PT
        "Las opciones se cerraran en: ", // 1 = ES
        "The options will close in: "    // 2 = EN
    };

    public Opcoes(Main jogo) {
        this.jogo = jogo;
        batch = new SpriteBatch();

        font = new BitmapFont();
        font.getData().setScale(4f);

        // Ao invés de carregar fixo aqui, chama o método que verifica o idioma atual do jogo!
        atualizarTexturasIdioma();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0f, 0f, 0f, 1f); // Dica: Pode colocar o azul Tramontina aqui! (0.0f, 0.12f, 0.37f, 1f)

        boolean esq = Gdx.input.isKeyPressed(Input.Keys.LEFT);
        boolean dir = Gdx.input.isKeyPressed(Input.Keys.RIGHT);

        if (!esq && !dir) botoesLiberados = true;

        if (botoesLiberados) {
            if (esq && dir) {
                if (!processouBotao) {
                    switch (opcaoSelecionada) {
                        case 0: jogo.idioma = 0; atualizarTexturasIdioma(); break; // Português
                        case 1: jogo.idioma = 1; atualizarTexturasIdioma(); break; // Espanhol
                        case 2: jogo.idioma = 2; atualizarTexturasIdioma(); break; // Inglês
                        case 3: jogo.setScreen(new MenuScreen(jogo)); break;       // Voltar
                    }
                    processouBotao = true;
                }
            } else if (esq || dir) {
                if (!processouBotao) {
                    tempoPressionado += delta;
                    if (tempoPressionado > 0.02f) {
                        if (dir) {
                            opcaoSelecionada++;
                            tempoAFK = 10f;
                            if (opcaoSelecionada > 3) opcaoSelecionada = 0;
                        } else {
                            opcaoSelecionada--;
                            tempoAFK = 10f;
                            if (opcaoSelecionada < 0) opcaoSelecionada = 3;
                        }
                        processouBotao = true;
                    }
                }
            } else {
                tempoPressionado = 0f;
                processouBotao   = false;
            }
        }

        tempoAFK -= delta;
        if (tempoAFK <= limiteAFK) jogo.setScreen(new MenuScreen(jogo));

        batch.begin();

        batch.draw(opcaoSelecionada == 0 ? btnPortuguesSelect : btnPortugues, larguraJanela/2 - 180, alturaJanela/2 + 250, 400, 200);
        batch.draw(opcaoSelecionada == 1 ? btnEspanholSelect  : btnEspanhol,  larguraJanela/2 - 180, alturaJanela/2, 400, 200);
        batch.draw(opcaoSelecionada == 2 ? btnInglesSelect    : btnIngles,    larguraJanela/2 - 180, alturaJanela/2 - 250, 400, 200);
        batch.draw(opcaoSelecionada == 3 ? btnVoltarSelect    : btnVoltar,    larguraJanela/2 - 180, alturaJanela/2 - 500, 400, 200);

        // Desenha o aviso AFK na língua correta
        if (tempoAFK <= 5f && tempoAFK > 0f) {
            int seg = (int) Math.ceil(tempoAFK);
            font.draw(batch, textosAFK[jogo.idioma] + seg, larguraJanela/2 - 300, alturaJanela - 80);
        }

        batch.end();
    }

    // --- NOVO MÉTODO: ATUALIZA AS TEXTURAS CONFORME O IDIOMA ---
    private void atualizarTexturasIdioma() {
        // 1. Limpa as texturas antigas da memória para não travar o PC do Senai/Tramontina
        if (btnVoltar != null) {
            btnVoltar.dispose(); btnVoltarSelect.dispose();
            btnPortugues.dispose(); btnPortuguesSelect.dispose();
            btnIngles.dispose(); btnInglesSelect.dispose();
            btnEspanhol.dispose(); btnEspanholSelect.dispose();
        }

        // 2. Carrega as novas imagens baseadas na variável global
        if (jogo.idioma == 0) {
            // PORTUGUÊS
            btnVoltar         = new Texture("BotaoVoltar.png");
            btnVoltarSelect   = new Texture("BotaoVoltarSelect.png");
            btnPortugues      = new Texture("BotaoPortugues.png");
            btnPortuguesSelect= new Texture("BotaoPortSelect.png");
            btnIngles         = new Texture("BotaoIngles.png");
            btnInglesSelect   = new Texture("BotaoInglesSelect.png");
            btnEspanhol       = new Texture("BotaoEspanhol.png");
            btnEspanholSelect = new Texture("BotaoEspanholSelect.png");

        } else if (jogo.idioma == 1) {
            // ESPANHOL (Adicione sufixo _ES nas suas imagens na pasta assets)
            btnVoltar         = new Texture("BotaoVoltar_ES.png");
            btnVoltarSelect   = new Texture("BotaoVoltarSelect_ES.png");
            btnPortugues      = new Texture("BotaoPortugues_ES.png");
            btnPortuguesSelect= new Texture("BotaoPortSelect_ES.png");
            btnIngles         = new Texture("BotaoIngles_ES.png");
            btnInglesSelect   = new Texture("BotaoInglesSelect_ES.png");
            btnEspanhol       = new Texture("BotaoEspanhol_ES.png");
            btnEspanholSelect = new Texture("BotaoEspanholSelect_ES.png");

        } else {
            // INGLÊS (Adicione sufixo _EN nas suas imagens na pasta assets)
            btnVoltar         = new Texture("BotaoVoltar_EN.png");
            btnVoltarSelect   = new Texture("BotaoVoltarSelect_EN.png");
            btnPortugues      = new Texture("BotaoPortugues_EN.png");
            btnPortuguesSelect= new Texture("BotaoPortSelect_EN.png");
            btnIngles         = new Texture("BotaoIngles_EN.png");
            btnInglesSelect   = new Texture("BotaoInglesSelect_EN.png");
            btnEspanhol       = new Texture("BotaoEspanhol_EN.png");
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
        // Evita erro se a tela for fechada antes das texturas carregarem
        if (btnVoltar != null) {
            btnVoltar.dispose();         btnVoltarSelect.dispose();
            btnPortugues.dispose();      btnPortuguesSelect.dispose();
            btnIngles.dispose();         btnInglesSelect.dispose();
            btnEspanhol.dispose();       btnEspanholSelect.dispose();
        }
    }
}
