package com.osbarnabe.workbot;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class LoadingScreen implements Screen {

    private Main jogo;
    private SpriteBatch batch;
    private BitmapFont fonte;
    private OrthographicCamera camera;
    private Viewport viewport;

    // --- ARRAY COM OS TEXTOS DE LOADING TRADUZIDOS ---
    private String[] textosLoading = {
        "Carregando... ", // 0 = PT
        "Cargando... ",   // 1 = ES
        "Loading... "     // 2 = EN
    };

    public LoadingScreen(Main jogo) {
        this.jogo = jogo;
        batch = new SpriteBatch();
        fonte = new BitmapFont();
        fonte.getData().setScale(3f);

        camera = new OrthographicCamera();
        viewport = new FitViewport(1080f, 1920f, camera);

        // --- DEFININDO O SUFIXO DE IDIOMA ---
        String sufixo = "";
        if (jogo.idioma == 1) sufixo = "_ES";
        else if (jogo.idioma == 2) sufixo = "_EN";

        // Pré-carregamento de todos os assets do GameScreen
        jogo.assets.load("roboParado.png",   Texture.class);
        jogo.assets.load("roboDir.png",      Texture.class);
        jogo.assets.load("roboEsq.png",      Texture.class);
        jogo.assets.load("fabrica1.png",     Texture.class);
        jogo.assets.load("fabrica2.png",     Texture.class);
        jogo.assets.load("fabrica3.png",     Texture.class);
        jogo.assets.load("ceit.png",         Texture.class);
        jogo.assets.load("ceu1.png",         Texture.class);
        jogo.assets.load("ceu2.png",         Texture.class);
        jogo.assets.load("inicio.png",       Texture.class);
        jogo.assets.load("portinha.png",     Texture.class);
        jogo.assets.load("localporta.png",   Texture.class);
        jogo.assets.load("localporta2.png",  Texture.class);
        jogo.assets.load("localporta3.png",  Texture.class);
        jogo.assets.load("trabalhador.png",  Texture.class);
        jogo.assets.load("trabalhador2.png", Texture.class);
        jogo.assets.load("trabalhador3.png", Texture.class);
        jogo.assets.load("porta.png",        Texture.class);
        jogo.assets.load("ponto.png",        Texture.class);
        jogo.assets.load("trabalhador1flnd.png", Texture.class);
        jogo.assets.load("localFinal.png", Texture.class);
        jogo.assets.load("portinha2.png", Texture.class);
        jogo.assets.load("botaoInt.png", Texture.class);
        jogo.assets.load("final.png", Texture.class);

        // --- CARREGANDO OS BALÕES DE ACORDO COM O IDIOMA ---
        jogo.assets.load("BalaoFala_NPC1" + sufixo + ".png", Texture.class);
        jogo.assets.load("BalaoFala_NPC1_2" + sufixo + ".png", Texture.class);
        jogo.assets.load("BalaoFala_NPC3" + sufixo + ".png", Texture.class);
    }

    @Override
    public void render(float delta) {
        // Fundo azul Tramontina ou preto (se quiser pode usar 0.0f, 0.12f, 0.37f, 1f)
        ScreenUtils.clear(0f, 0f, 0f, 1f);

        if (jogo.assets.update()) {
            jogo.setScreen(new GameScreen(jogo));
        }

        float progresso   = jogo.assets.getProgress();
        int porcentagem   = (int) (progresso * 100);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        // Desenha o texto de acordo com o idioma salvo em jogo.idioma
        fonte.draw(batch, textosLoading[jogo.idioma] + porcentagem + "%", 365f, 960f);
        batch.end();
    }

    @Override public void resize(int width, int height) { viewport.update(width, height, true); }
    @Override public void show() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        fonte.dispose();
    }
}
