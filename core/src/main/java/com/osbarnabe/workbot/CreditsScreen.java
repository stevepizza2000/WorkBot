package com.osbarnabe.workbot;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
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

    private Texture estereggDipp, estereggCosta, estereggBiondo, qrCode;

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

    private String[] jogoMobile = {
        "Para continuar jogando \nbaixe nosso jogo no seu celular",
        "Para seguir jugando, descarga nuestro\n juego en tu teléfono móvil",
        "To continue playing, download our \ngame on your mobile phone"
    };

    private String[] ajudaQrCode = {
        "Clique no arquivo .apk após escanear o código QR",
        "Haz clic en el archivo .apk después de escanear el código QR",
        "Click on the .apk file after scanning the QR code"
    };

    public CreditsScreen(Main jogo) {

        float largura = Gdx.graphics.getWidth();
        float altura = Gdx.graphics.getHeight();

        this.jogo = jogo;

        batch = new SpriteBatch();

        estereggDipp = new Texture("dipp.png");
        estereggCosta = new Texture("costa.png");
        estereggBiondo = new Texture("biondo.png");

        qrCode = new Texture("qrcodeJogoMobile.png");

        camera = new OrthographicCamera();
        viewport = new FitViewport(largura, altura, camera);

        // Criação das fontes
        FreeTypeFontGenerator generator =
            new FreeTypeFontGenerator(Gdx.files.internal("fonts/PixelifySans-SemiBold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter =
            new FreeTypeFontGenerator.FreeTypeFontParameter();

        // Configuração da fonte dos Cargos (Tamanho base 16 + Cor Amarela)
        parameter.size = 16;
        parameter.color = Color.YELLOW;
        fonteCargo = generator.generateFont(parameter);
        fonteCargo.getData().setScale(2f); // Mantém a escala proporcional ao seu design anterior

        // Configuração da fonte dos Nomes (Tamanho base 16 + Cor Branca)
        parameter.size = 16;
        parameter.color = Color.WHITE;
        fonteNome = generator.generateFont(parameter);
        fonteNome.getData().setScale(2f); // Mantém a escala proporcional ao seu design anterior

        // Limpa o gerador da memória
        generator.dispose();

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

        espacamentoY -= 300;

        fonteNome.draw(batch, jogoMobile[jogo.idioma], 10, espacamentoY, largura, Align.center, false);

        batch.draw(qrCode, (largura/2f) - 150, espacamentoY- 400, 300,300);

        fonteNome.draw(batch, ajudaQrCode[jogo.idioma], 10, espacamentoY- 550, largura, Align.center, false);

        batch.draw(
            estereggDipp,
            0,
            espacamentoY - 1400f,
            50f,
            50f
        );

        batch.draw(
            estereggCosta,
            50,
            espacamentoY - 1400f,
            50f,
            50f
        );

        batch.draw(
            estereggBiondo,
            100,
            espacamentoY - 1400f,
            50f,
            50f
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

        estereggDipp.dispose();
        estereggCosta.dispose();
        estereggBiondo.dispose();

        qrCode.dispose();
    }
}
