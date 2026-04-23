package com.osbarnabe.workbot;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen implements Screen {

    private Main jogo;

    // Câmera e viewport
    private OrthographicCamera camera;
    private Viewport viewport;
    private SpriteBatch batch;

    // Animações do robô
    private Animation<TextureRegion> animacaoParado;
    private Animation<TextureRegion> animacaoDireita;
    private Animation<TextureRegion> animacaoEsquerda;

    // Animações de personagens do cenário
    private Animation<TextureRegion> animacaoTrabalhador;
    private Animation<TextureRegion> animacaoTrabalhador2;
    private Animation<TextureRegion> animacaoTrabalhador3;
    private Animation<TextureRegion> animacaoPonto;
    private Animation<TextureRegion> animacaoPonto2;

    private float elapsedTime = 0f;

    // Cronômetro AFK
    private float tempoAFK = 15f;
    private final float limiteAFK = 0f;
    private BitmapFont fonte;

    // Texturas do cenário
    private Texture RoboParadoImg, RoboDirImg, RoboEsqImg;
    private Texture fabrica1Img, fabrica2Img, fabrica3Img;
    private Texture ceitImg, ceu1Img, ceu2Img, localFinalImg;
    private Texture trabalhadorImg, trabalhador2Img, trabalhador3Img;
    private Texture inicioImg, portinhaImg;
    private Texture localportaImg, localporta2Img, localporta3Img;
    private Texture portaImg;
    private Texture pontoImg;

    // Posição do robô
    private float roboX;
    private float roboY;
    private final float tamanhoRobo  = 330f;
    private final float velocidadeRobo = 400f;

    float larguraJanela = Gdx.graphics.getWidth();
    float alturaJanela  = Gdx.graphics.getHeight();

    private float tempoPressionado = 0;
    private boolean processouBotao = false;

    // Portas (Porta 1 → Puzzle1 | Porta 2 → Puzzle2)
    private Porta porta1;
    private Porta porta2;

    // Posição de retorno do robô ao voltar de cada puzzle
    private float retornoX;
    private float retornoY;

    public GameScreen(Main jogo) {
        this(jogo, 100f, 65f);
    }

    /** Construtor que permite posicionar o robô em um ponto específico (ex.: ao voltar de um puzzle). */
    public GameScreen(Main jogo, float inicioX, float inicioY) {
        this.jogo = jogo;
        batch = new SpriteBatch();

        fonte = new BitmapFont();
        fonte.getData().setScale(3f);
        fonte.setColor(Color.WHITE);

        camera = new OrthographicCamera();
        viewport = new FitViewport(larguraJanela, alturaJanela, camera);

        // Carrega texturas via AssetManager (já pré-carregadas no LoadingScreen)
        RoboParadoImg  = jogo.assets.get("roboParado.png",   Texture.class);
        RoboDirImg     = jogo.assets.get("roboDir.png",      Texture.class);
        RoboEsqImg     = jogo.assets.get("roboEsq.png",      Texture.class);
        ceitImg        = jogo.assets.get("ceit.png",         Texture.class);
        fabrica1Img    = jogo.assets.get("fabrica1.png",     Texture.class);
        fabrica2Img    = jogo.assets.get("fabrica2.png",     Texture.class);
        fabrica3Img    = jogo.assets.get("fabrica3.png",     Texture.class);
        ceu1Img        = jogo.assets.get("ceu1.png",         Texture.class);
        ceu2Img        = jogo.assets.get("ceu2.png",         Texture.class);
        inicioImg      = jogo.assets.get("inicio.png",       Texture.class);
        portinhaImg    = jogo.assets.get("portinha.png",     Texture.class);
        localportaImg  = jogo.assets.get("localporta.png",  Texture.class);
        localporta2Img = jogo.assets.get("localporta2.png", Texture.class);
        localporta3Img = jogo.assets.get("localporta3.png", Texture.class);
        trabalhadorImg = jogo.assets.get("trabalhador.png", Texture.class);
        portaImg       = jogo.assets.get("porta.png",        Texture.class);
        trabalhador2Img = jogo.assets.get("trabalhador2.png", Texture.class);
        trabalhador3Img = jogo.assets.get("trabalhador3.png", Texture.class);
        pontoImg       = jogo.assets.get("ponto.png",        Texture.class);
        localFinalImg  = jogo.assets.get("localFinal.png", Texture.class);

        // Monta as animações
        animacaoParado      = new Animation<>(0.45f, extrairFrames(RoboParadoImg,  64,  64, 7));
        animacaoDireita     = new Animation<>(0.15f, extrairFrames(RoboDirImg,     64,  64, 2));
        animacaoEsquerda    = new Animation<>(0.15f, extrairFrames(RoboEsqImg,     64,  64, 2));
        animacaoTrabalhador = new Animation<>(0.45f, extrairFrames(trabalhadorImg, 700, 700, 2));
        animacaoTrabalhador2 = new Animation<>(0.45f, extrairFrames(trabalhador2Img, 100, 100, 2));
        animacaoTrabalhador3 = new Animation<>(0.45f, extrairFrames(trabalhador3Img, 100, 100, 6));
        animacaoPonto       = new Animation<>(0.45f, extrairFrames(pontoImg,  50, 50, 8));
        animacaoPonto2      = new Animation<>(0.45f, extrairFrames(pontoImg,  50, 50, 8));

        roboX = inicioX;
        roboY = inicioY;

        // Porta 1 → leva ao Puzzle 1 (sorting de itens)
        porta1 = new Porta(2650f, 0f, 150f, alturaJanela - 259f);
        // Porta 2 → leva ao Puzzle 2 (Flappy Bird)
        porta2 = new Porta(4750f, 0f, 150f, alturaJanela - 259f);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.8f, 0.85f, 0.9f, 1f);

        // Troca de tela ao fechar a porta
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

        // Interação (esq + dir juntos)
        if (esq && dir) {
            tempoAFK = 10f;
            if (!processouBotao) {
                if (colideComPorta(porta1)) porta1.interagir();
                else if (colideComPorta(porta2)) porta2.interagir();
                else System.out.println("Nenhuma porta por perto.");
                processouBotao = true;
            }
        } else if (esq || dir) {
            tempoAFK = 10f;
            if (!processouBotao) {
                tempoPressionado += delta;
                if (tempoPressionado > 0.07f) {
                    if (esq) roboX -= velocidadeRobo * delta;
                    else     roboX += velocidadeRobo * delta;
                }
            }
        } else {
            tempoPressionado = 0;
            processouBotao   = false;
            tempoAFK -= delta;
            if (tempoAFK <= limiteAFK) jogo.setScreen(new MenuScreen(jogo));
        }

        // Limite esquerdo
        if (roboX < 0f) roboX = 0f;

        // Limite direito
        if (roboX > 6050f) roboX = 6050f;

        // Câmera segue o robô
        camera.position.x = roboX + (tamanhoRobo / 2f);
        if (camera.position.x < viewport.getWorldWidth() / 2f)
            camera.position.x = viewport.getWorldWidth() / 2f;
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        // ---- Desenho ----
        batch.begin();

        // Cenário
        batch.draw(inicioImg,      0,    0, 800, alturaJanela - 259);
        batch.draw(ceu1Img,        0,    alturaJanela - 259, 800, 259);
        batch.draw(localportaImg,  800,  0, 800, alturaJanela - 259);
        batch.draw(ceu2Img,        800,  alturaJanela - 259, 800, 259);
        batch.draw(fabrica1Img,    1600, 0, 700, alturaJanela - 259);
        batch.draw(ceu1Img,        1600, alturaJanela - 259, 700, 259);
        batch.draw(localporta2Img, 2300, 0, 700, alturaJanela - 259);
        batch.draw(portaImg,       2300, 0, 700, alturaJanela - 259); // Porta 1
        batch.draw(ceu2Img,        2300, alturaJanela - 259, 700, 259);
        batch.draw(fabrica2Img,    2900, 0, 700, alturaJanela - 259);
        batch.draw(ceu1Img,        2900, alturaJanela - 259, 700, 259);
        batch.draw(fabrica3Img,    3600, 0, 700, alturaJanela - 259);
        batch.draw(ceu2Img,        3600, alturaJanela - 259, 700, 259);
        batch.draw(localporta3Img, 4300, 0, 700, alturaJanela - 259);
        batch.draw(portaImg,       4300, 0, 700, alturaJanela - 259); // Porta 2
        batch.draw(ceu1Img,        4300, alturaJanela - 259, 700, 259);
        batch.draw(localFinalImg,  4900, 0, 700, alturaJanela-259);
        batch.draw(ceu2Img,        4900, alturaJanela-259, 700, 259);
        batch.draw(ceitImg,        5600, 0, 700, alturaJanela);

        // Trabalhadores animados
        batch.draw(animacaoTrabalhador.getKeyFrame(elapsedTime, true),  1820, 210, 260, 260);
        batch.draw(animacaoTrabalhador2.getKeyFrame(elapsedTime, true), 2970, 210, 260, 260);
        batch.draw(animacaoTrabalhador3.getKeyFrame(elapsedTime, true), 3770, 170, 380, 380);

        // Pontos de exclamação
        batch.draw(animacaoPonto.getKeyFrame(elapsedTime, true),  1900, 310, 100, 100);
        batch.draw(animacaoPonto2.getKeyFrame(elapsedTime, true), 3850, 340, 100, 100);

        // Robô
        TextureRegion frame;
        if (dir && !esq) {
            frame = animacaoDireita.getKeyFrame(elapsedTime, true);
            roboY = 65f;
        } else if (esq && !dir) {
            frame = animacaoEsquerda.getKeyFrame(elapsedTime, true);
            roboY = 65f;
        } else {
            frame = animacaoParado.getKeyFrame(elapsedTime, true);
            roboY = 70f;
        }
        batch.draw(frame, roboX, roboY, tamanhoRobo, tamanhoRobo);
        batch.draw(portinhaImg, 0, 0, 800, alturaJanela - 259);

        // Aviso AFK
        if (tempoAFK <= 5f && tempoAFK > 0f) {
            int seg = (int) Math.ceil(tempoAFK);
            fonte.draw(batch, "O jogo vai fechar em: " + seg,
                camera.position.x - 250f, camera.position.y + 400f);
        }

        batch.end();
    }

    private boolean colideComPorta(Porta porta) {
        float centroRobo = roboX + (tamanhoRobo / 2f);
        return centroRobo >= porta.x && centroRobo <= (porta.x + porta.largura);
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
        // As texturas do AssetManager são gerenciadas pelo Main; não dispose aqui
    }

    private TextureRegion[] extrairFrames(Texture tex, int lFrame, int aFrame, int qtd) {
        TextureRegion[][] matriz = TextureRegion.split(tex, lFrame, aFrame);
        TextureRegion[] frames   = new TextureRegion[qtd];
        int idx = 0;
        for (TextureRegion[] linha : matriz)
            for (TextureRegion col : linha)
                if (idx < qtd) frames[idx++] = col;
        return frames;
    }
}
