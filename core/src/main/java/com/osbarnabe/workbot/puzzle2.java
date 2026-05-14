package com.osbarnabe.workbot;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
    private Texture texPipeDown;
    private Texture texPipeUp;
    private Texture texBackground;
    private Texture texFaca;

    private float transicaoAlpha = 0f;
    private boolean pegouFaca = false;
    private Texture texPreto;

    private float recuoX = 0f;
    private float distanciaPercorrida = 0f;

    private final float DISTANCIA_ENTRE_CANOS = 220f;

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

    float pipeWidth = 45f;
    float margem    = 5f;

    private float facaLargura = 40f;
    private float facaAltura  = 40f;

    private final float gap = 120f;

    private int score = 0;

    private static class Cano {
        float x;
        float gapY;
        boolean passou = false;
        boolean temFaca = false;
    }

    private Array<Cano> canos;

    private final float velocidadeCano = 120f;

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

    public puzzle2(Main jogo) {

        this.jogo = jogo;

        batch = new SpriteBatch();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, WIDTH, HEIGHT);

        // ===== FONTE =====
        FreeTypeFontGenerator generator =
            new FreeTypeFontGenerator(
                Gdx.files.internal("assets/fonts/PixelifySans-Regular.ttf")
            );

        FreeTypeFontGenerator.FreeTypeFontParameter parameter =
            new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = 20;
        parameter.color = Color.WHITE;

        font = generator.generateFont(parameter);

        // deixa a fonte mais larga horizontalmente
        font.getData().setScale(1.0f, 1.0f);

        generator.dispose();

        // ===== TEXTURAS =====
        texBird       = new Texture("bird.png");
        texPipeDown   = new Texture("pipe_down.png");
        texPipeUp     = new Texture("pipe_up.png");
        texBackground = new Texture("fundo_puzzle2.png");
        texFaca       = new Texture("Faca.png");

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

            if (canosGerados >= 6) {
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

        // HITBOX DO PÁSSARO
        bird.set(85f, birdY + 5f, 40f, 40f);

        for (Cano c : canos) {

            float bottomHeight = c.gapY - gap / 2f;
            float topY         = c.gapY + gap / 2f;

            pipeBottom.set(
                c.x + margem,
                0f,
                pipeWidth - margem * 2f,
                bottomHeight
            );

            pipeTop.set(
                c.x + margem,
                topY,
                pipeWidth - margem * 2f,
                HEIGHT - topY
            );

            // ===== FACA =====
            if (c.temFaca) {

                float offsetX = 10f;
                float offsetY = -6f;

                float facaX =
                    c.x + (pipeWidth / 2f) - (facaLargura / 2f) + offsetX;

                float facaY =
                    c.gapY - (facaAltura / 2f) + offsetY;

                facaRect.set(
                    facaX,
                    facaY,
                    facaLargura,
                    facaAltura
                );

                if (bird.overlaps(facaRect) && !pegouFaca) {
                    pegouFaca = true;
                }
            }

            // ===== COLISÃO =====
            if (bird.overlaps(pipeBottom) || bird.overlaps(pipeTop)) {

                float birdRight = 85f + 30f;
                float pipeLeft  = c.x + margem;

                float penetracaoX = birdRight - pipeLeft;

                if (penetracaoX < 15f) {

                    if (recuoX < 400f) {
                        recuoX = 500f;
                    }

                    if (birdY + 15f < c.gapY) {
                        velY = -50f;
                    } else {
                        velY = 50f;
                    }

                } else {

                    if (bird.overlaps(pipeBottom)) {
                        velY = 180f;
                    }

                    else if (bird.overlaps(pipeTop)) {
                        velY = -180f;
                    }
                }
            }

            // SCORE
            if (c.x + pipeWidth < 80f && !c.passou) {

                score++;
                c.passou = true;
            }
        }

        // LIMITES
        if (birdY < 0f) {

            birdY = 0f;

            if (velY < 0) {
                velY = 0;
            }
        }

        if (birdY > HEIGHT - 40f) {

            birdY = HEIGHT - 40f;

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

                jogo.setScreen(
                    new GameScreen(jogo, 5020f, 65f)
                );
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

        batch.draw(texBird, 80f, birdY, 55f, 50f);

        float alturaCano = 300f;

        for (Cano c : canos) {

            float bottomHeight = c.gapY - gap / 2f;
            float topY         = c.gapY + gap / 2f;

            batch.draw(
                texPipeDown,
                c.x,
                bottomHeight - alturaCano,
                pipeWidth,
                alturaCano
            );

            batch.draw(
                texPipeUp,
                c.x,
                topY,
                pipeWidth,
                alturaCano
            );

            // DESENHA FACA
            if (c.temFaca && !pegouFaca) {

                float facaX =
                    c.x + (pipeWidth / 2f) - (facaLargura / 2f);

                float facaY =
                    c.gapY - (facaAltura / 2f);

                batch.draw(
                    texFaca,
                    facaX + 5,
                    facaY,
                    facaLargura,
                    facaAltura
                );
            }
        }

        // TEXTO
        if (canosGerados < 6) {

            font.draw(
                batch,
                pontos[jogo.idioma] + score + "/6",
                10f,
                HEIGHT - 10f
            );

        } else {

            font.draw(
                batch,
                pegarKit[jogo.idioma],
                10f,
                HEIGHT - 10f
            );
        }

        // TRANSIÇÃO PRETA
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
}
