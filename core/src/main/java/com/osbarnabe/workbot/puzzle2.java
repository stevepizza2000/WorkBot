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
    private final float DISTANCIA_ENTRE_CANOS = 280f;
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

    float pipeWidth = 65f;
    float margem    = 5f;

    // 🔥 TAMANHO DA FACA (NOVO)
    private float facaLargura = 65f;
    private float facaAltura  = 65f;

    private static class Cano {
        float x;
        float gapY;
        boolean passou = false;
        boolean temFaca = false;
    }

    private Array<Cano> canos;
    private final float velocidadeCano = 160f;
    private float tempoSpawn = 0f;
private final float gap  = 120f;

    private int score = 0;

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
        tempoAnimacao += delta;

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
            canosGerados++; // Contador total de canos criados

            Cano c = new Cano();
            c.x = WIDTH;
            c.gapY = MathUtils.random(120, 340);

            // 🔥 SPAWNA A FACA EXATAMENTE NO CANO 6
            if (canosGerados == 6) {
                c.temFaca = true;
            }

            canos.add(c);
        }

        for (Cano c : canos) {
            c.x -= vEfetiva * delta;
        }

        if (canos.size > 0 && canos.first().x < -60f) canos.removeIndex(0);

        bird.set(80f, birdY, 65f, 50f); // X e tamanho iguais ao desenho

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
                // 🔥 Só aumenta o score se ainda não chegou em 5
                if (score < 5) {
                    score++;
                }
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
                jogo.setScreen(new GameScreen(jogo, 5020f, 65f));
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
        batch.draw(texBird, 80f, birdY, 75f, 50f);

        float alturaCano = 300f;

        for (Cano c : canos) {

            float bottomHeight = c.gapY - gap / 2f;
            float topY = c.gapY + gap / 2f;

            batch.draw(texPipeDown, c.x, bottomHeight - alturaCano, pipeWidth, alturaCano);
            batch.draw(texPipeUp, c.x, topY, pipeWidth, alturaCano);

// 🔥 DESENHO DA FACA COM TODAS AS ANIMAÇÕES UNIFICADAS
            if (c.temFaca && !pegouFaca) {
                // 1. Cálculos de Animação
                float flutuarY = MathUtils.sin(tempoAnimacao * 4f) * 8f;
                float escala = 1f + MathUtils.sin(tempoAnimacao * 2f) * 0.1f;
                float rotacaO = MathUtils.sin(tempoAnimacao * 2f) * 10f;

                float largAnimada = facaLargura * escala;
                float altAnimada = facaAltura * escala;
                float facaX = c.x + (pipeWidth / 2f) - (largAnimada / 2f);
                float facaY = c.gapY - (altAnimada / 2f) + flutuarY;

                // 2. Desenhar o Brilho (Atrás)
                float brilhoAlpha = 0.3f + MathUtils.sin(tempoAnimacao * 5f) * 0.2f;
                batch.setColor(1, 1, 0, brilhoAlpha); // Amarelo neon
                batch.draw(texFaca, facaX - 2, facaY - 2, largAnimada + 4, altAnimada + 4);

                // 3. Desenhar a Faca Principal (Com rotação e escala)
                batch.setColor(1, 1, 1, 1); // Resetar cor para branco total
                batch.draw(texFaca,
                    facaX, facaY,
                    largAnimada / 2f, altAnimada / 2f,
                    largAnimada, altAnimada,
                    1f, 1f,
                    rotacaO,
                    0, 0, texFaca.getWidth(), texFaca.getHeight(),
                    false, false);
            }


            // 🔥 DESENHO CENTRALIZADO DA FACA (NOVO)
            if (c.temFaca && !pegouFaca) {
                float facaX = c.x + (pipeWidth / 2f) - (facaLargura / 2f);
                float facaY = c.gapY - (facaAltura / 2f);
            }
        }

        // ... (fim do loop for dos canos)

        // 🔥 LÓGICA DAS MENSAGENS (Canto Superior Esquerdo)
        // Reduzimos a margemX para 5f para encostar mais no canto
        float margemX = 5f;
        // Subimos um pouco o texto base (quase no limite da tela)
        float alturaBase = HEIGHT - 10f;

        // 1. Contador principal
        font.draw(batch, pontos[jogo.idioma] + score + "/5", margemX, alturaBase);

        // 2. Mensagem secundária (diminuímos o espaço de 35f para 20f)
        if (score >= 5) {
            font.draw(batch, pegarKit[jogo.idioma], margemX, alturaBase - 22f);
        }

        // Transição de saída (Fade Out)
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
