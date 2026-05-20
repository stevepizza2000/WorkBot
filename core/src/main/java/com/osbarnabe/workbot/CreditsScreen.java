package com.osbarnabe.workbot;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class CreditsScreen implements Screen {

    private Main jogo;
    private OrthographicCamera camera;
    private Viewport viewport;
    private SpriteBatch batch;

    private BitmapFont fonteCargo;
    private BitmapFont fonteNome;

    private Texture btnVoltar;
    private Texture btnVoltarSelect;

    private boolean botaoSelecionado = false;

    private float rolagemY = -200f;
    private final float velocidadeRolagem = 150f;
    private final float limiteRolagem = 4050f;

    // =========================
    // TEXTOS LOCALIZADOS
    // =========================

    private String[] cargoCriadores = {
        "Criadores",
        "Creadores",
        "Creators"
    };

    private String[] cargoArteDesign = {
        "Arte e Design",
        "Arte y Diseño",
        "Art and Design"
    };

    private String[] cargoProgramacao = {
        "Programacao",
        "Programación",
        "Programming"
    };

    private String[] cargoRoteiro = {
        "Roteiro",
        "Guión",
        "Script"
    };

    private String[] cargoAgradecimentos = {
        "Agradecimentos Especiais",
        "Agradecimientos Especiales",
        "Special Thanks"
    };

    public CreditsScreen(Main jogo) {

        float largura = Gdx.graphics.getWidth();
        float altura = Gdx.graphics.getHeight();

        this.jogo = jogo;

        batch = new SpriteBatch();

        camera = new OrthographicCamera();
        viewport = new FitViewport(largura, altura, camera);

        fonteCargo = new BitmapFont();
        fonteCargo.getData().setScale(2f);

        fonteNome = new BitmapFont();
        fonteNome.getData().setScale(3f);

        atualizarTexturasIdioma();
    }

    @Override
    public void render(float delta) {

        ScreenUtils.clear(0f, 0f, 0f, 1f);

        // Rolagem automática
        if (rolagemY < limiteRolagem) {
            rolagemY += velocidadeRolagem * delta;
        } else {
            botaoSelecionado = true;
        }

        boolean esq = Gdx.input.isKeyPressed(Input.Keys.LEFT);
        boolean dir = Gdx.input.isKeyPressed(Input.Keys.RIGHT);

        // Acelera rolagem
        if ((esq || dir) && rolagemY < limiteRolagem) {
            rolagemY += (velocidadeRolagem * 6f) * delta;
        }

        // Voltar ao menu
        if (botaoSelecionado && esq && dir) {
            jogo.setScreen(new MenuScreen(jogo));
        }

        camera.update();

        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        float espacamentoY = rolagemY;
        float largura = Gdx.graphics.getWidth();

        // =====================================
        // BLOCO 1 - CRIADORES
        // =====================================

        fonteCargo.draw(
            batch,
            cargoCriadores[jogo.idioma],
            0,
            espacamentoY,
            largura,
            Align.center,
            false
        );

        fonteNome.draw(batch, "Arthur B. Freitag",    0, espacamentoY -  50, largura, Align.center, false);
        fonteNome.draw(batch, "Arthur O. Deitos",     0, espacamentoY - 110, largura, Align.center, false);
        fonteNome.draw(batch, "Artur D. Costa",       0, espacamentoY - 160, largura, Align.center, false);
        fonteNome.draw(batch, "Bruno Darsie",         0, espacamentoY - 210, largura, Align.center, false);
        fonteNome.draw(batch, "Felipe Biondo",        0, espacamentoY - 260, largura, Align.center, false);
        fonteNome.draw(batch, "Gabriel A. Dieterich", 0, espacamentoY - 310, largura, Align.center, false);
        fonteNome.draw(batch, "Miguel R. Hoff",       0, espacamentoY - 360, largura, Align.center, false);

        espacamentoY -= 500;

        // =====================================
        // BLOCO 2 - ARTE E DESIGN
        // =====================================

        fonteCargo.draw(
            batch,
            cargoArteDesign[jogo.idioma],
            0,
            espacamentoY,
            largura,
            Align.center,
            false
        );

        fonteNome.draw(batch, "Arthur B. Freitag",    0, espacamentoY -  50, largura, Align.center, false);
        fonteNome.draw(batch, "Arthur O. Deitos",     0, espacamentoY - 110, largura, Align.center, false);
        fonteNome.draw(batch, "Artur D. Costa",       0, espacamentoY - 160, largura, Align.center, false);
        fonteNome.draw(batch, "Bruno Darsie",         0, espacamentoY - 210, largura, Align.center, false);
        fonteNome.draw(batch, "Felipe Biondo",        0, espacamentoY - 260, largura, Align.center, false);
        fonteNome.draw(batch, "Gabriel A. Dieterich", 0, espacamentoY - 310, largura, Align.center, false);
        fonteNome.draw(batch, "Miguel R. Hoff",       0, espacamentoY - 360, largura, Align.center, false);

        espacamentoY -= 500;

        // =====================================
        // BLOCO 3 - PROGRAMACAO
        // =====================================

        fonteCargo.draw(
            batch,
            cargoProgramacao[jogo.idioma],
            0,
            espacamentoY,
            largura,
            Align.center,
            false
        );

        fonteNome.draw(batch, "Arthur B. Freitag",    0, espacamentoY -  50, largura, Align.center, false);
        fonteNome.draw(batch, "Arthur O. Deitos",     0, espacamentoY - 110, largura, Align.center, false);
        fonteNome.draw(batch, "Artur D. Costa",       0, espacamentoY - 160, largura, Align.center, false);
        fonteNome.draw(batch, "Bruno Darsie",         0, espacamentoY - 210, largura, Align.center, false);
        fonteNome.draw(batch, "Felipe Biondo",        0, espacamentoY - 260, largura, Align.center, false);
        fonteNome.draw(batch, "Gabriel A. Dieterich", 0, espacamentoY - 310, largura, Align.center, false);
        fonteNome.draw(batch, "Miguel R. Hoff",       0, espacamentoY - 360, largura, Align.center, false);

        espacamentoY -= 480;

        // =====================================
        // BLOCO 4 - ROTEIRO
        // =====================================

        fonteCargo.draw(
            batch,
            cargoRoteiro[jogo.idioma],
            0,
            espacamentoY,
            largura,
            Align.center,
            false
        );

        fonteNome.draw(batch, "Arthur B. Freitag",    0, espacamentoY -  50, largura, Align.center, false);
        fonteNome.draw(batch, "Arthur O. Deitos",     0, espacamentoY - 110, largura, Align.center, false);
        fonteNome.draw(batch, "Artur D. Costa",       0, espacamentoY - 160, largura, Align.center, false);
        fonteNome.draw(batch, "Bruno Darsie",         0, espacamentoY - 210, largura, Align.center, false);
        fonteNome.draw(batch, "Felipe Biondo",        0, espacamentoY - 260, largura, Align.center, false);
        fonteNome.draw(batch, "Gabriel A. Dieterich", 0, espacamentoY - 310, largura, Align.center, false);
        fonteNome.draw(batch, "Miguel R. Hoff",       0, espacamentoY - 360, largura, Align.center, false);

        espacamentoY -= 480;

        // =====================================
        // BLOCO 5 - AGRADECIMENTOS
        // =====================================

        fonteCargo.draw(
            batch,
            cargoAgradecimentos[jogo.idioma],
            0,
            espacamentoY,
            largura,
            Align.center,
            false
        );

        fonteNome.draw(
            batch,
            "Tramontina CEIT",
            0,
            espacamentoY - 50,
            largura,
            Align.center,
            false
        );

        espacamentoY -= 400;

        // =====================================
        // BOTAO VOLTAR
        // =====================================

        Texture texturaAtual =
            botaoSelecionado ? btnVoltarSelect : btnVoltar;

        batch.draw(
            texturaAtual,
            (largura / 2f) - 150f,
            espacamentoY,
            300f,
            150f
        );

        batch.end();
    }

    public void atualizarTexturasIdioma() {

        if (jogo.idioma == 0) {

            btnVoltar = new Texture("BotaoVoltar.png");
            btnVoltarSelect = new Texture("BotaoVoltarSelect.png");

        } else if (jogo.idioma == 1) {

            btnVoltar = new Texture("BotaoVoltar_ES.png");
            btnVoltarSelect = new Texture("BotaoVoltarSelect_ES.png");

        } else {

            btnVoltar = new Texture("BotaoVoltar_EN.png");
            btnVoltarSelect = new Texture("BotaoVoltarSelect_EN.png");
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void show() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {

        batch.dispose();

        fonteCargo.dispose();
        fonteNome.dispose();

        btnVoltar.dispose();
        btnVoltarSelect.dispose();
    }
}
