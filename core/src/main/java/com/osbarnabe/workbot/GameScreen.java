// 🔥 GAME SCREEN UNIFICADO (MAIN + LINGUAS)

package com.osbarnabe.workbot;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen implements Screen {

    private Main jogo;

    private OrthographicCamera camera;
    private Viewport viewport;
    private SpriteBatch batch;

    private Animation<TextureRegion> animacaoParado, animacaoDireita, animacaoEsquerda;
    private Animation<TextureRegion> animacaoTrabalhador, animacaoTrabalhador2, animacaoTrabalhador3;
    private Animation<TextureRegion> animacaoPonto, animacaoPonto2;

    private float elapsedTime = 0f;

    private float tempoAFK = 15f;
    private final float tempoMaxAFK = 20f;
    private final float limiteAFK = 0f;

    private BitmapFont fonte;

    // TEXTOS TRADUZIDOS
    private String[] textosAFK = {
        "O jogo vai fechar em: ",
        "El juego se cerrara en: ",
        "The game will close in: "
    };

    // TEXTURAS
    private Texture RoboParadoImg, RoboDirImg, RoboEsqImg;
    private Texture fabrica1Img, fabrica2Img, fabrica3SpriteImg;
    private Texture ceitImg, ceu1Img, ceu2Img, localFinalImg, portinha2Img;
    private Texture trabalhadorImg, trabalhador2Img, trabalhador3Img;
    private Texture inicioImg, portinhaImg;
    private Texture localportaImg, localporta2Img, localporta3Img;
    private Texture portaImg, pontoImg;

    // BALÕES (COM IDIOMA)
    private Texture balaoNPC1, balaoNPC3, balaoNPC1_2, balaoNPC3_2;

    private Animation<TextureRegion> animacaoBalao, animacaoBalao2;
    private Animation<TextureRegion> animacaoBalaoNPC3, animacaoBalaoNPC3_2;
    private Animation<TextureRegion> animacaoBotao, animacaoFabrica3;

    private Texture botaoIntImg;

    private float roboX, roboY;
    private final float tamanhoRobo = 330f;
    private final float velocidadeRobo = 400f;

    float larguraJanela = Gdx.graphics.getWidth();
    float alturaJanela = Gdx.graphics.getHeight();

    private float tempoPressionado = 0;
    private boolean processouBotao = false;

    private Porta porta1, porta2;

    private boolean dialogoAtivo = false;
    private boolean dialogoNPC3 = false;

    private float npc1X = 1850f;
    private float npc1Y = 150f;

    private float npc3X = 3800f;
    private float npc3Y = 140f;

    private float raioInteracao = 150f;

    private boolean podeInteragir = true;
    private boolean bloqueioNPC = true;

    private boolean debugSemBarreira = false;

    public GameScreen(Main jogo) {
        this(jogo, 100f, 65f);
    }

    public GameScreen(Main jogo, float inicioX, float inicioY) {
        this.jogo = jogo;
        batch = new SpriteBatch();

        fonte = new BitmapFont();
        fonte.getData().setScale(3f);
        fonte.setColor(Color.WHITE);

        camera = new OrthographicCamera();
        viewport = new FitViewport(larguraJanela, alturaJanela, camera);

        // 🔥 IDIOMA
        String sufixo = "";
        if (jogo.idioma == 1) sufixo = "_ES";
        else if (jogo.idioma == 2) sufixo = "_EN";

        // TEXTURAS
        RoboParadoImg = jogo.assets.get("roboParado.png", Texture.class);
        RoboDirImg = jogo.assets.get("roboDir.png", Texture.class);
        RoboEsqImg = jogo.assets.get("roboEsq.png", Texture.class);

        fabrica1Img = jogo.assets.get("fabrica1.png", Texture.class);
        fabrica2Img = jogo.assets.get("fabrica2.png", Texture.class);
        fabrica3SpriteImg = jogo.assets.get("Fabrica3sprite.png", Texture.class);

        ceitImg = jogo.assets.get("ceit.png", Texture.class);
        ceu1Img = jogo.assets.get("ceu1.png", Texture.class);
        ceu2Img = jogo.assets.get("ceu2.png", Texture.class);

        inicioImg = jogo.assets.get("inicio.png", Texture.class);
        portinhaImg = jogo.assets.get("portinha.png", Texture.class);

        localportaImg = jogo.assets.get("localporta.png", Texture.class);
        localporta2Img = jogo.assets.get("localporta2.png", Texture.class);
        localporta3Img = jogo.assets.get("localporta3.png", Texture.class);

        trabalhadorImg = jogo.assets.get("trabalhador.png", Texture.class);
        trabalhador2Img = jogo.assets.get("trabalhador2.png", Texture.class);
        trabalhador3Img = jogo.assets.get("trabalhador3.png", Texture.class);

        portaImg = jogo.assets.get("porta.png", Texture.class);
        pontoImg = jogo.assets.get("ponto.png", Texture.class);

        localFinalImg = jogo.assets.get("localFinal.png", Texture.class);
        portinha2Img = jogo.assets.get("portinha2.png", Texture.class);

        botaoIntImg = jogo.assets.get("botaoInt.png", Texture.class);

        // 🔥 BALÕES COM IDIOMA
        balaoNPC1 = jogo.assets.get("BalaoFala_NPC1" + sufixo + ".png", Texture.class);
        balaoNPC3 = jogo.assets.get("BalaoFala_NPC3" + sufixo + ".png", Texture.class);
        balaoNPC1_2 = jogo.assets.get("BalaoFala_NPC1_2" + sufixo + ".png", Texture.class);
        balaoNPC3_2 = jogo.assets.get("BalaoFala_NPC3_2" + sufixo + ".png", Texture.class);

        // ANIMAÇÕES
        animacaoParado = new Animation<>(0.45f, extrairFrames(RoboParadoImg, 64, 64, 7));
        animacaoDireita = new Animation<>(0.15f, extrairFrames(RoboDirImg, 64, 64, 2));
        animacaoEsquerda = new Animation<>(0.15f, extrairFrames(RoboEsqImg, 64, 64, 2));

        animacaoTrabalhador = new Animation<>(0.45f, extrairFrames(trabalhadorImg, 700, 700, 2));
        animacaoTrabalhador2 = new Animation<>(0.45f, extrairFrames(trabalhador2Img, 100, 100, 2));
        animacaoTrabalhador3 = new Animation<>(0.45f, extrairFrames(trabalhador3Img, 100, 100, 6));

        animacaoPonto = new Animation<>(0.45f, extrairFrames(pontoImg, 50, 50, 8));
        animacaoPonto2 = new Animation<>(0.45f, extrairFrames(pontoImg, 50, 50, 8));

        animacaoBotao = new Animation<>(0.3f, extrairFrames(botaoIntImg, 300, 106, 2));

        animacaoBalao = new Animation<>(0.4f, extrairFrames(balaoNPC1, balaoNPC1.getWidth(), balaoNPC1.getHeight() / 2, 2));
        animacaoBalao2 = new Animation<>(0.4f, extrairFrames(balaoNPC1_2, balaoNPC1_2.getWidth(), balaoNPC1_2.getHeight() / 2, 2));

        animacaoBalaoNPC3 = new Animation<>(0.4f, extrairFrames(balaoNPC3, balaoNPC3.getWidth(), balaoNPC3.getHeight() / 2, 2));
        animacaoBalaoNPC3_2 = new Animation<>(0.4f, extrairFrames(balaoNPC3_2, balaoNPC3_2.getWidth(), balaoNPC3_2.getHeight() / 2, 2));

        animacaoFabrica3 = new Animation<>(0.15f,
            extrairFrames(fabrica3SpriteImg,
                fabrica3SpriteImg.getWidth() / 5,
                fabrica3SpriteImg.getHeight(),
                5));

        roboX = inicioX;
        roboY = inicioY;

        porta1 = new Porta(2600f, 0f, 150f, alturaJanela - 259f);
        porta2 = new Porta(4600f, 0f, 150f, alturaJanela - 259f);
    }

    private boolean pertoDoNPC(float npcX) {
        float centroRobo = roboX + (tamanhoRobo / 2f);
        float centroNPC = npcX + (190f / 2f);
        return Math.abs(centroRobo - centroNPC) < raioInteracao;
    }

    @Override
    public void render(float delta) {

        ScreenUtils.clear(0.8f, 0.85f, 0.9f, 1f);

        if (Gdx.input.isKeyJustPressed(Input.Keys.H)) {
            debugSemBarreira = !debugSemBarreira;
        }

        if (!porta1.estaAberta) {
            jogo.setScreen(new puzzle1(jogo));
            return;
        }
        if (!porta2.estaAberta) {
            jogo.setScreen(new puzzle2(jogo));
            return;
        }

        boolean esq = Gdx.input.isKeyPressed(Input.Keys.LEFT);
        boolean dir = Gdx.input.isKeyPressed(Input.Keys.RIGHT);

        elapsedTime += delta;

        // (MESMA LÓGICA AVANÇADA DO MAIN MANTIDA)

        if (esq && dir) {
            tempoAFK = 10f;

            if (podeInteragir) {

                if (dialogoAtivo) {
                    dialogoAtivo = false;

                    if (!jogo.npc1Completo) {
                        jogo.npc1Completo = true;
                        bloqueioNPC = false;
                    } else if (jogo.puzzle1Completo) {
                        jogo.npc1PosPuzzleFalou = true;
                    }
                }

                else if (pertoDoNPC(npc1X)) {
                    dialogoAtivo = true;
                    tempoAFK = tempoMaxAFK;
                }

                else if (pertoDoNPC(npc3X)) {
                    dialogoNPC3 = !dialogoNPC3;
                    jogo.npc3Liberado = true;
                }

                podeInteragir = false;
            }
        } else {
            podeInteragir = true;
        }

        if (dialogoAtivo || dialogoNPC3) {
            esq = false;
            dir = false;
            tempoAFK = tempoMaxAFK;
        }

        if (esq || dir) {
            tempoAFK = 10f;
            if (esq) roboX -= velocidadeRobo * delta;
            if (dir) roboX += velocidadeRobo * delta;
        } else {
            tempoAFK -= delta;
            if (tempoAFK <= 0) jogo.setScreen(new MenuScreen(jogo));
        }

        if (roboX < 0) roboX = 0;
        if (roboX > 6050f) roboX = 6050f;

        camera.position.x = roboX + tamanhoRobo / 2f;
        camera.update();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // DESENHO (mantido)

        TextureRegion frame;
        if (dir && !esq) frame = animacaoDireita.getKeyFrame(elapsedTime, true);
        else if (esq && !dir) frame = animacaoEsquerda.getKeyFrame(elapsedTime, true);
        else frame = animacaoParado.getKeyFrame(elapsedTime, true);

        batch.draw(frame, roboX, roboY, tamanhoRobo, tamanhoRobo);

        // 🔥 TEXTO AFK TRADUZIDO
        if (tempoAFK <= 5f && tempoAFK > 0f) {
            int seg = (int) Math.ceil(tempoAFK);
            fonte.draw(batch,
                textosAFK[jogo.idioma] + seg,
                camera.position.x - 250f,
                camera.position.y + 400f);
        }

        batch.end();
    }

    @Override public void resize(int w, int h) { viewport.update(w, h, true); }
    @Override public void show() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        fonte.dispose();
    }

    private TextureRegion[] extrairFrames(Texture tex, int l, int a, int qtd) {
        TextureRegion[][] m = TextureRegion.split(tex, l, a);
        TextureRegion[] f = new TextureRegion[qtd];
        int i = 0;
        for (TextureRegion[] linha : m)
            for (TextureRegion col : linha)
                if (i < qtd) f[i++] = col;
        return f;
    }
}
