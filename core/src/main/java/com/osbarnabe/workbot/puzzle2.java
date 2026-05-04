package com.osbarnabe.workbot;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

    private float birdY    = 200f;
    private float velY     = 0f;
    private final float GRAVIDADE = -750f;
    private final float PULO      = 250f;
    private float velX = 0f;
    private final float REBOTE_X = -150f;

    float pipeWidth = 35f;
    float margem    = 5f;

    // 🔥 TAMANHO DA FACA (NOVO)
    private float facaLargura = 40f;
    private float facaAltura  = 40f;

    private static class Cano {
        float x;
        float gapY;
        boolean passou = false;
        boolean temFaca = false;
    }

    private Array<Cano> canos;
    private final float velocidadeCano = 120f;
    private float tempoSpawn = 0f;
    private final float gap  = 120f;

    private int score = 0;

    public puzzle2(Main jogo) {
        this.jogo = jogo;

        batch  = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, WIDTH, HEIGHT);
        font   = new BitmapFont();

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
        Boolean esq = Gdx.input.isKeyPressed(Input.Keys.LEFT);
        Boolean dir = Gdx.input.isKeyPressed(Input.Keys.RIGHT);
        velY += GRAVIDADE * delta;
        birdY += velY * delta;

        if (esq && dir) {
            velY = PULO;
        }

        if (recuoX > 0) {
            recuoX -= 800f * delta;
            if (recuoX < 0) recuoX = 0;
        }

        float vEfetiva = velocidadeCano - recuoX;

        distanciaPercorrida += vEfetiva * delta;

        if (distanciaPercorrida >= DISTANCIA_ENTRE_CANOS) {
            distanciaPercorrida = 0;
            canosGerados++;

            Cano c = new Cano();
            c.x = WIDTH;
            c.gapY = MathUtils.random(120, 340);

            if (canosGerados >= 5) {
                c.temFaca = true;
            }

            canos.add(c);
        }

        for (Cano c : canos) {
            c.x -= vEfetiva * delta;
        }

        if (canos.size > 0 && canos.first().x < -60f) canos.removeIndex(0);

        bird.set(85f, birdY + 5f, 30f, 30f);

        for (Cano c : canos) {

            float bottomHeight = c.gapY - gap / 2f;
            float topY = c.gapY + gap / 2f;

            pipeBottom.set(c.x + margem, 0f, pipeWidth - margem * 2f, bottomHeight);
            pipeTop.set(c.x + margem, topY, pipeWidth - margem * 2f, HEIGHT - topY);

            // 🔥 POSIÇÃO CENTRALIZADA DA FACA (NOVO)
            if (c.temFaca) {
                float offsetX = 10f;   // ajusta aqui
                float offsetY = -6f;  // ajusta aqui

                float facaX = c.x + (pipeWidth / 2f) - (facaLargura / 2f) + offsetX;
                float facaY = c.gapY - (facaAltura / 2f) + offsetY;

                facaRect.set(facaX, facaY, facaLargura, facaAltura);

                if (bird.overlaps(facaRect) && !pegouFaca) {
                    pegouFaca = true;
                }
            }

            if (bird.overlaps(pipeBottom) || bird.overlaps(pipeTop)) {
                float birdRight = 85f + 30f;
                float pipeLeft = c.x + margem;
                float penetracaoX = birdRight - pipeLeft;

                if (penetracaoX < 15f) {
                    if (recuoX < 400f) {
                        recuoX = 500f;
                    }
                    if (birdY + 15f < c.gapY) velY = -50f;
                    else velY = 50f;
                } else {
                    if (bird.overlaps(pipeBottom)) {
                        velY = 180f;
                    } else if (bird.overlaps(pipeTop)) {
                        velY = -180f;
                    }
                }
            }

            if (c.x + pipeWidth < 80f && !c.passou) {
                score++;
                c.passou = true;
            }
        }

        if (birdY < 0f) {
            birdY = 0f;
            if (velY < 0) velY = 0;
        }

        if (birdY > HEIGHT - 40f) {
            birdY = HEIGHT - 40f;
            if (velY > 0) velY = 0;
        }

        if (pegouFaca) {
            transicaoAlpha += delta * 1.5f;
            if (transicaoAlpha >= 1f) {
                transicaoAlpha = 1f;
                jogo.puzzle2Completo = true;
                jogo.npc3PosPuzzleFalou = false;
                jogo.setScreen(new GameScreen(jogo, 4665f, 65f));
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

        for (Cano c : canos) {

            float bottomHeight = c.gapY - gap / 2f;
            float topY         = c.gapY + gap / 2f;

            batch.draw(texPipeDown, c.x, 0f, pipeWidth, bottomHeight);
            batch.draw(texPipeUp, c.x, topY, pipeWidth, HEIGHT - topY);

            // 🔥 DESENHO CENTRALIZADO DA FACA (NOVO)
            if (c.temFaca && !pegouFaca) {
                float facaX = c.x + (pipeWidth / 2f) - (facaLargura / 2f);
                float facaY = c.gapY - (facaAltura / 2f);

                batch.draw(texFaca, facaX+5, facaY, facaLargura, facaAltura);
            }
        }

        if (canosGerados < 5) {
            font.draw(batch, "Passe pelos canos: " + score + "/5", 10f, HEIGHT - 10f);
        } else {
            font.draw(batch, "Pegue o kit de facas!", 10f, HEIGHT - 10f);
        }

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
