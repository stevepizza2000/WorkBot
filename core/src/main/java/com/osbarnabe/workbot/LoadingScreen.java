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

    public LoadingScreen(Main jogo) {
        this.jogo = jogo;
        batch = new SpriteBatch();
        fonte = new BitmapFont();
        fonte.getData().setScale(3f);

        camera = new OrthographicCamera();
        viewport = new FitViewport(1080f, 1920f, camera);

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
        jogo.assets.load("BalaoFala_NPC1.png", Texture.class);
        jogo.assets.load("BalaoFala_NPC3.png", Texture.class);
        jogo.assets.load("botaoInt.png", Texture.class);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0f, 0f, 0f, 1f);

        if (jogo.assets.update()) {
            jogo.setScreen(new GameScreen(jogo));
        }

        float progresso   = jogo.assets.getProgress();
        int porcentagem   = (int) (progresso * 100);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        fonte.draw(batch, "Carregando... " + porcentagem + "%", 365f, 960f);
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
