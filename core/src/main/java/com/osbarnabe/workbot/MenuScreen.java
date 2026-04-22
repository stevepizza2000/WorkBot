package com.osbarnabe.workbot;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class MenuScreen implements Screen {

    private Main jogo;
    private SpriteBatch batch;
    private BitmapFont fontAviso;
    private BitmapFont fontAFK;

    private float tempoAFK = 10f;
    private final float limiteAFK = 0f;

    private Texture btnInicio;
    private Texture btnInicioSelect;
    private Texture btnCreditos;
    private Texture btnCreditosSelect;
    private Texture btnOpcao;
    private Texture btnOpcaoSelect;
    private Texture fundo;

    float larguraJanela = Gdx.graphics.getWidth();
    float alturaJanela  = Gdx.graphics.getHeight();

    private boolean botoesLiberados = false;
    private int opcaoSelecionada   = 0;
    private float tempoPressionado = 0;
    private boolean processouBotao = false;

    public MenuScreen(Main jogo) {
        this.jogo = jogo;
        batch = new SpriteBatch();

        fontAviso = new BitmapFont();
        fontAviso.getData().setScale(4f);

        fontAFK = new BitmapFont();
        fontAFK.getData().setScale(2f);
        fontAFK.setColor(Color.RED);

        btnInicio       = new Texture("BotaoInicio.png");
        btnInicioSelect = new Texture("BotaoInicioSelect.png");
        btnCreditos       = new Texture("BotaoCreditos.png");
        btnCreditosSelect = new Texture("BotaoCreditosSelect.png");
        btnOpcao       = new Texture("BotaoOpcao.png");
        btnOpcaoSelect = new Texture("BotaoOpcaoSelect.png");
        fundo = new Texture("fundo.png");
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        boolean esq = Gdx.input.isKeyPressed(Input.Keys.LEFT);
        boolean dir = Gdx.input.isKeyPressed(Input.Keys.RIGHT);

        if (!esq && !dir) botoesLiberados = true;

        if (botoesLiberados) {
            if (esq && dir) {
                if (!processouBotao) {
                    switch (opcaoSelecionada) {
                        case 0: jogo.setScreen(new LoadingScreen(jogo)); break;
                        case 1: jogo.setScreen(new CreditsScreen(jogo)); break;
                        case 2: jogo.setScreen(new Opcoes(jogo));        break;
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
                            if (opcaoSelecionada > 2) opcaoSelecionada = 0;
                        } else {
                            opcaoSelecionada--;
                            tempoAFK = 10f;
                            if (opcaoSelecionada < 0) opcaoSelecionada = 2;
                        }
                        processouBotao = true;
                    }
                }
            } else {
                tempoPressionado = 0;
                processouBotao   = false;
            }
        }

        tempoAFK -= delta;
        if (tempoAFK <= limiteAFK) Gdx.app.exit();

        batch.begin();
        batch.draw(fundo, 0, 0, larguraJanela, alturaJanela);

        // Botão Início
        batch.draw(opcaoSelecionada == 0 ? btnInicioSelect : btnInicio, larguraJanela/2 - 180, 600, 400, 200);
        // Botão Créditos
        batch.draw(opcaoSelecionada == 1 ? btnCreditosSelect : btnCreditos, larguraJanela/2 - 180, 350, 400, 200);
        // Botão Opções
        batch.draw(opcaoSelecionada == 2 ? btnOpcaoSelect : btnOpcao, larguraJanela/2 - 180, 100, 400, 200);

        // Aviso AFK
        if (tempoAFK <= 5f && tempoAFK > 0f) {
            int seg = (int) Math.ceil(tempoAFK);
            fontAviso.draw(batch, "O jogo vai fechar em: " + seg, 620f, 930f);
        }
        batch.end();
    }

    @Override public void show() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        fontAviso.dispose();
        fontAFK.dispose();
        btnInicio.dispose();
        btnInicioSelect.dispose();
        btnCreditos.dispose();
        btnCreditosSelect.dispose();
        btnOpcao.dispose();
        btnOpcaoSelect.dispose();
        fundo.dispose();
    }
}
