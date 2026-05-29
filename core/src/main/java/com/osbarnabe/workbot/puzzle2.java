package com.osbarnabe.workbot;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

public class puzzle2 implements Screen {

    private Main jogo;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private BitmapFont font;

    private Texture texBird;
    private Animation<TextureRegion> animacaoBird;

    private Texture texPipeDown;
    private Texture texPipeUp;
    private Texture texBackground;
    private Texture texFaca;

    private float transicaoAlpha = 0f;
    private boolean pegouFaca = false;
    private Texture texPreto;

    private float recuoX = 0f;
    private float distanciaPercorrida = 0f;

    private final float DISTANCIA_ENTRE_CANOS = 280f;

    private int canosGerados = 0;

    private Rectangle bird;
    private Rectangle pipeBottom;
    private Rectangle pipeTop;
    private Rectangle facaRect;

    public static final int WIDTH  = 300;
    public static final int HEIGHT = 480;

    private float birdY = 200f;
    private float velY  = 0f;

    private final float GRAVIDADE = -750f;
    private final float PULO      = 250f;

    float pipeWidth = 65f;
    float margem    = 5f;

    // 🔥 TAMANHO DA FACA
    private float facaLargura = 65f;
    private float facaAltura  = 65f;

    // 🤖 TAMANHO DE RENDERIZAÇÃO DO ROBÔ
    // Ajustado para manter a proporção do novo robô com asas
    private float larguraBird = 90f;
    private float alturaBird  = 90f;

    private final float gap = 120f;

    private int score = 0;

    private static class Cano {
        float x;
        float gapY;
        boolean passou = false;
        boolean temFaca = false;
    }

    private Array<Cano> canos;

    private final float velocidadeCano = 160f;

    private String[] pontos = {
        "Passe pelos canos: ",
        "Pasar por las tuberías: ",
        "Go through the pipes: "
    };

    private String[] pegarKit = {
        "Pegue o kit de facas!",
        "¡Coge el juego de cuchillos!",
        "Grab the knife set!"
    };

    private float tempoAnimacao = 0f;

    public puzzle2(Main jogo) {

        this.jogo = jogo;

        batch = new SpriteBatch();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, WIDTH, HEIGHT);

        // ===== FONTE =====
        FreeTypeFontGenerator generator =
            new FreeTypeFontGenerator(
                Gdx.files.internal("fonts/PixelifySans-SemiBold.ttf")
            );

        FreeTypeFontGenerator.FreeTypeFontParameter parameter =
            new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = 16;
        parameter.color = Color.WHITE;

        font = generator.generateFont(parameter);
        font.getData().setScale(1.0f, 1.0f);
        generator.dispose();

        // ===== TEXTURAS =====
        // Certifique-se de que o arquivo "bird.png" na pasta assets agora seja essa nova imagem!
        texBird       = new Texture("bird.png");
        texPipeDown   = new Texture("pipe_down.png");
        texPipeUp     = new Texture("pipe_up.png");
        texBackground = new Texture("fundo_puzzle2.png");
        texFaca       = new Texture("Faca.png");

        // ===== CONFIGURAÇÃO DA ANIMAÇÃO DO ROBÔ (BIRD) =====
        // Lógica adaptada para a sua nova imagem (grade 2x2 com 3 frames úteis)
        int colunas = 2;
        int linhas = 2;
        int qtdFrames = 3;

        TextureRegion[] framesBird = extrairFrames(
            texBird,
            texBird.getWidth() / colunas,
            texBird.getHeight() / linhas,
            qtdFrames
        );

        animacaoBird = new Animation<>(0.10f, framesBird); // 0.10f deixa o bater de asas rapidinho
        animacaoBird.setPlayMode(Animation.PlayMode.LOOP);

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(0, 0, 0, 1);
        pixmap.fill();
        texPreto = new Texture(pixmap);
        pixmap.dispose();

        facaRect = new Rectangle();
        bird = new Rectangle();
        pipeBottom = new Rectangle();
        pipeTop = new Rectangle();

        canos = new Array<>();
    }

    private void update(float delta) {

        boolean esq = Gdx.input.isKeyPressed(Input.Keys.LEFT);
        boolean dir = Gdx.input.isKeyPressed(Input.Keys.RIGHT);

        velY += GRAVIDADE * delta;
        birdY += velY * delta;
        tempoAnimacao += delta;

        // PULO
        if (esq && dir) {
            velY = PULO;
        }

        // RECUO
        if (recuoX > 0) {
            recuoX -= 800f * delta;
            if (recuoX < 0) {
                recuoX = 0;
            }
        }

        float vEfetiva = velocidadeCano - recuoX;

        // SPAWN DOS CANOS
        distanciaPercorrida += vEfetiva * delta;

        if (distanciaPercorrida >= DISTANCIA_ENTRE_CANOS) {
            distanciaPercorrida = 0;
            canosGerados++;

            Cano c = new Cano();
            c.x = WIDTH;
            c.gapY = MathUtils.random(120, 340);

            if (canosGerados == 6) {
                c.temFaca = true;
            }

            canos.add(c);
        }

        // MOVIMENTO DOS CANOS
        for (Cano c : canos) {
            c.x -= vEfetiva * delta;
        }

        // REMOVE CANOS
        if (canos.size > 0 && canos.first().x < -60f) {
            canos.removeIndex(0);
        }

        // Hitbox menor que o desenho visual (0.6f na largura e 0.45f na altura)
        // para ignorar as áreas transparentes ao redor das asas do robô.
        bird.set(80f + (larguraBird * 0.2f), birdY + (alturaBird * 0.25f), larguraBird * 0.6f, alturaBird * 0.45f);

        for (Cano c : canos) {

            float bottomHeight = c.gapY - gap / 2f;
            float topY         = c.gapY + gap / 2f;

            pipeBottom.set(c.x, 0f, pipeWidth, bottomHeight);
            pipeTop.set(c.x, topY, pipeWidth, HEIGHT - topY);

            // ===== FACA =====
            if (c.temFaca) {
                float offsetX = 10f;
                float offsetY = -6f;

                float facaX = c.x + (pipeWidth / 2f) - (facaLargura / 2f) + offsetX;
                float facaY = c.gapY - (facaAltura / 2f) + offsetY;

                facaRect.set(facaX, facaY, facaLargura, facaAltura);

                if (bird.overlaps(facaRect) && !pegouFaca) {
                    pegouFaca = true;
                }
            }

            // ===== COLISÃO =====
            if (bird.overlaps(pipeBottom) || bird.overlaps(pipeTop)) {

                float birdRight = bird.x + bird.width;
                float pipeLeft  = c.x + margem;
                float penetracaoX = birdRight - pipeLeft;

                if (penetracaoX < 15f) {
                    if (recuoX < 400f) {
                        recuoX = 500f;
                    }
                    if (birdY + (alturaBird/2f) < c.gapY) {
                        velY = -50f;
                    } else {
                        velY = 50f;
                    }
                } else {
                    if (bird.overlaps(pipeBottom)) {
                        velY = 180f;
                    } else if (bird.overlaps(pipeTop)) {
                        velY = -180f;
                    }
                }
            }

            // SCORE
            if (c.x + pipeWidth < 80f && !c.passou) {
                if (score < 5) {
                    score++;
                }
                c.passou = true;
            }
        }

        // LIMITES
        if (birdY < -20f) {
            birdY = -20f;
            if (velY < 0) {
                velY = 0;
            }
        }

        if (birdY > HEIGHT - alturaBird + 20f) {
            birdY = HEIGHT - alturaBird + 20f;
            if (velY > 0) {
                velY = 0;
            }
        }

        // TRANSIÇÃO
        if (pegouFaca) {
            transicaoAlpha += delta * 1.5f;
            if (transicaoAlpha >= 1f) {
                transicaoAlpha = 1f;
                jogo.puzzle2Completo = true;
                jogo.npc3PosPuzzleFalou = false;
                jogo.setScreen(new GameScreen(jogo, 5820f, 65f));
            }
        }
    }

    @Override
    public void render(float delta) {

        ScreenUtils.clear(0.4f, 0.7f, 1f, 1f);

        update(delta);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        batch.draw(texBackground, 0, 0, WIDTH, HEIGHT);

        float alturaCano = 300f;

        for (Cano c : canos) {
            float bottomHeight = c.gapY - gap / 2f;
            float topY = c.gapY + gap / 2f;

            batch.draw(texPipeDown, c.x, bottomHeight - alturaCano, pipeWidth, alturaCano);
            batch.draw(texPipeUp, c.x, topY, pipeWidth, alturaCano);

            // 🔥 DESENHO DA FACA
            if (c.temFaca && !pegouFaca) {

                float flutuarY = MathUtils.sin(tempoAnimacao * 4f) * 8f;
                float escala = 1f + MathUtils.sin(tempoAnimacao * 2f) * 0.1f;
                float rotacaO = MathUtils.sin(tempoAnimacao * 2f) * 10f;

                float largAnimada = facaLargura * escala;
                float altAnimada = facaAltura * escala;

                float facaX = c.x + (pipeWidth / 2f) - (largAnimada / 2f);
                float facaY = c.gapY - (altAnimada / 2f) + flutuarY;

                float pulse = MathUtils.sin(tempoAnimacao * 3f);

                batch.setColor(1f, 1f, 1f, 0.10f + pulse * 0.02f);
                batch.draw(texFaca, facaX - 18f, facaY - 18f, largAnimada + 36f, altAnimada + 36f);

                batch.setColor(1f, 1f, 1f, 0.16f + pulse * 0.03f);
                batch.draw(texFaca, facaX - 10f, facaY - 10f, largAnimada + 20f, altAnimada + 20f);

                batch.setColor(1f, 1f, 1f, 0.25f + pulse * 0.04f);
                batch.draw(texFaca, facaX - 4f, facaY - 4f, largAnimada + 8f, altAnimada + 8f);

                batch.setColor(1f, 1f, 1f, 1f);

                batch.draw(texFaca,
                    facaX, facaY,
                    largAnimada / 2f, altAnimada / 2f,
                    largAnimada, altAnimada,
                    1f, 1f,
                    rotacaO,
                    0, 0, texFaca.getWidth(), texFaca.getHeight(),
                    false, false);
            }
        }

        // 🤖 RENDERIZAÇÃO DO ROBÔ VOADOR (Desenhado DEPOIS dos canos para ele não sumir atrás deles)
        TextureRegion frameAtualBird = animacaoBird.getKeyFrame(tempoAnimacao, true);

        // Aplica uma rotação leve dependendo se ele está caindo ou subindo (efeito Flappy Bird)
        float rotacaoRobo = velY * 0.05f;

        batch.draw(frameAtualBird,
            80f, birdY,
            larguraBird / 2f, alturaBird / 2f,
            larguraBird, alturaBird,
            1f, 1f,
            rotacaoRobo);

        // MENSAGENS
        float margemX = 10f;
        float alturaBase = HEIGHT - 10f;

        font.draw(batch, pontos[jogo.idioma] + score + "/5", margemX, alturaBase);

        if (score >= 5) {
            font.draw(batch, pegarKit[jogo.idioma], margemX, alturaBase - 28f);
        }

        // Fade Out
        if (transicaoAlpha > 0) {
            batch.setColor(1, 1, 1, transicaoAlpha);
            batch.draw(texPreto, 0, 0, WIDTH, HEIGHT);
            batch.setColor(1, 1, 1, 1);
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
        texBackground.dispose();
        texBird.dispose();
        texPipeDown.dispose();
        texPipeUp.dispose();
        texFaca.dispose();
        texPreto.dispose();
    }

    // 🛠️ MÉTODO AUXILIAR PARA CORTAR OS FRAMES DA SPRITE SHEET
    private TextureRegion[] extrairFrames(Texture tex, int lFrame, int aFrame, int qtd) {
        TextureRegion[][] matriz = TextureRegion.split(tex, lFrame, aFrame);
        TextureRegion[] frames   = new TextureRegion[qtd];
        int idx = 0;
        for (TextureRegion[] linha : matriz) {
            for (TextureRegion reg : linha) {
                if (idx < qtd) {
                    frames[idx++] = reg;
                }
            }
        }
        return frames;
    }
}
