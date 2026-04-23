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

    public Opcoes(Main jogo) {
        this.jogo = jogo;
        batch = new SpriteBatch();

        font = new BitmapFont();
        font.getData().setScale(4f);

        btnVoltar         = new Texture("BotaoVoltar.png");
        btnVoltarSelect   = new Texture("BotaoVoltarSelect.png");
        btnPortugues      = new Texture("BotaoPortugues.png");
        btnPortuguesSelect = new Texture("BotaoPortSelect.png");
        btnIngles         = new Texture("BotaoIngles.png");
        btnInglesSelect   = new Texture("BotaoInglesSelect.png");
        btnEspanhol       = new Texture("BotaoEspanhol.png");
        btnEspanholSelect = new Texture("BotaoEspanholSelect.png");
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
                        case 0: /* Português – futuro: aplicar idioma */ break;
                        case 1: /* Espanhol  – futuro: aplicar idioma */ break;
                        case 2: /* Inglês    – futuro: aplicar idioma */ break;
                        case 3: jogo.setScreen(new MenuScreen(jogo)); break;
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
        batch.draw(opcaoSelecionada == 2 ? btnInglesSelect    : btnIngles,    larguraJanela/2 - 180, alturaJanela/2 -250, 400, 200);
        batch.draw(opcaoSelecionada == 3 ? btnVoltarSelect    : btnVoltar,    larguraJanela/2 - 180, alturaJanela/2 - 500, 400, 200);

        if (tempoAFK <= 5f && tempoAFK > 0f) {
            int seg = (int) Math.ceil(tempoAFK);
            font.draw(batch, "As opções vão fechar em: " + seg, larguraJanela/2 - 300, alturaJanela - 80);
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
        font.dispose();
        btnVoltar.dispose();         btnVoltarSelect.dispose();
        btnPortugues.dispose();      btnPortuguesSelect.dispose();
        btnIngles.dispose();         btnInglesSelect.dispose();
        btnEspanhol.dispose();       btnEspanholSelect.dispose();
    }
}
