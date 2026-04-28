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

/**
 * Puzzle 2 – Flappy Bird.
 * O jogador controla um pássaro com ESPAÇO e deve passar por 10 canos sem colidir.
 * Ao atingir score 10, volta ao GameScreen posicionando o robô perto da Porta 2.
 */
public class puzzle2 implements Screen {

    private Main jogo;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private BitmapFont font;

    // Texturas
    private Texture texBird;
    private Texture texPipeDown;
    private Texture texPipeUp;
    private Texture texBackground;
    private Texture texFaca;

    //variaveis pra transição

    private float transicaoAlpha = 0f; // Controla a transparência do preto (0 a 1)
    private boolean pegouFaca = false; // Flag para saber se a transição começou
    private Texture texPreto;          // Uma textura simples de 1x1 pixel preta

    //variaveis para a colisão dos canos
    private float recuoX = 0f;          // Velocidade extra de empurrão (horizontal)
    private float distanciaPercorrida = 0f; // Substitui o tempoSpawn
    private final float DISTANCIA_ENTRE_CANOS = 220f; // Espaço fixo entre um cano e outro
    private int canosGerados = 0;


    //retangulos
    private Rectangle bird;
    private Rectangle pipeBottom;
    private Rectangle pipeTop;
    private Rectangle facaRect;

    // Dimensões da viewport do mini-jogo
    public static final int WIDTH  = 300;
    public static final int HEIGHT = 480;

    // Pássaro
    private float birdY    = 200f;
    private float velY     = 0f;
    private final float GRAVIDADE = -750f;
    private final float PULO      = 250f;
    private float velX = 0f; // Velocidade horizontal para o rebote
    private final float REBOTE_X = -150f; // Força do empurrão para trás

    // Canos
    float pipeWidth = 35f;
    float margem    = 5f;
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

        // Carrega texturas
        texBird       = new Texture("bird.png");
        texPipeDown   = new Texture("pipe_down.png");
        texPipeUp     = new Texture("pipe_up.png");
        texBackground = new Texture("fundo_puzzle2.png");
        texFaca       = new Texture("Faca.png");

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(0, 0, 0, 1); // Cor preta
        pixmap.fill();
        texPreto = new Texture(pixmap);
        pixmap.dispose();

        facaRect = new Rectangle();
        bird = new Rectangle();
        pipeBottom = new Rectangle();
        pipeTop = new Rectangle();
        facaRect = new Rectangle();
        canos = new Array<>();
    }

    private void resetGame() {
        birdY      = 200f;
        velY       = 0f;
        tempoSpawn = 0f;
        score      = 0;
        canos.clear();
    }

    private void update(float delta) {
        // 1. FÍSICA DO PÁSSARO
        velY += GRAVIDADE * delta;
        birdY += velY * delta;

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            velY = PULO;
        }

        // 2. FÍSICA DO RECUO
        if (recuoX > 0) {
            recuoX -= 800f * delta;
            if (recuoX < 0) recuoX = 0;
        }

        float vEfetiva = velocidadeCano - recuoX;

        // 3. SPAWN POR DISTÂNCIA
        distanciaPercorrida += vEfetiva * delta;

        if (distanciaPercorrida >= DISTANCIA_ENTRE_CANOS) {
            distanciaPercorrida = 0;
            canosGerados++;

            Cano c = new Cano();
            c.x = WIDTH;
            c.gapY = MathUtils.random(120, 340);

            if (canosGerados >= 10) {
                c.temFaca = true;
            }

            canos.add(c);
        }

        // 4. MOVIMENTAÇÃO DOS CANOS
        for (Cano c : canos) {
            c.x -= vEfetiva * delta;
        }

        if (canos.size > 0 && canos.first().x < -60f) canos.removeIndex(0);

        // 5. COLISÃO (Dentro do laço: apenas verifica se bateu)
        bird.set(85f, birdY + 5f, 30f, 30f);

        for (Cano c : canos) {
            float bottomHeight = c.gapY - gap / 2f;
            float topY = c.gapY + gap / 2f;

            pipeBottom.set(c.x + margem, 0f, pipeWidth - margem * 2f, bottomHeight);
            pipeTop.set(c.x + margem, topY, pipeWidth - margem * 2f, HEIGHT - topY);

            if (c.temFaca) {
                facaRect.set(c.x + pipeWidth/2f - 15f, c.gapY - 15f, 30f, 30f);

                // AQUI DENTRO FICA SÓ O AVISO DE QUE PEGOU A FACA
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

            // 6. PONTUAÇÃO
            if (c.x + pipeWidth < 80f && !c.passou) {
                score++;
                c.passou = true;
            }
        }

        // Limites de tela
        if (birdY < 0f) {
            birdY = 0f;
            if (velY < 0) velY = 0;
        }

        if (birdY > HEIGHT - 40f) {
            birdY = HEIGHT - 40f;
            if (velY > 0) velY = 0;
        }

        // === LÓGICA DA TRANSIÇÃO ===
        // Totalmente fora do laço "for", para rodar independente dos canos
        if (pegouFaca) {
            transicaoAlpha += delta * 1.5f;
            if (transicaoAlpha >= 1f) {
                transicaoAlpha = 1f;
                jogo.setScreen(new GameScreen(jogo, 4600f, 50f));
                jogo.puzzle2Completo = true;
                jogo.npc3PosPuzzleFalou = false;
            }
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.4f, 0.7f, 1f, 1f);

        // 1. Atualiza a lógica toda primeiro (1 única vez)
        update(delta);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        // 2. Abre o batch UMA única vez
        batch.begin();

        // Fundo
        batch.draw(texBackground, 0, 0, WIDTH, HEIGHT);

        // Pássaro
        batch.draw(texBird, 80f, birdY, 40f, 40f);

        // Canos
        // Canos
        for (Cano c : canos) {
            float bottomHeight = c.gapY - gap / 2f;
            float topY         = c.gapY + gap / 2f;

            // --- LÓGICA PROFISSIONAL DE CORTE (CROPPING) ---
            // Em vez de espremer a imagem inteira no espaço, calculamos a proporção
            // exata de pixels originais que precisamos puxar para preencher o vão.

            // 1. CANO DE BAIXO
            // A "boca" fica no topo da imagem (pixel Y = 0 no LibGDX).
            float ratioDown = texPipeDown.getWidth() / pipeWidth;
            int srcHeightDown = (int) (bottomHeight * ratioDown);

            batch.draw(texPipeDown,
                c.x, 0f, pipeWidth, bottomHeight,                 // Onde e qual tamanho desenhar na tela
                0, 0, texPipeDown.getWidth(), srcHeightDown,      // Quais pixels originais da imagem recortar
                false, false);

            // 2. CANO DE CIMA
            // A "boca" fica no fundo da imagem, então precisamos calcular de baixo pra cima.
            float ratioUp = texPipeUp.getWidth() / pipeWidth;
            int alturaTelaCima = (int) (HEIGHT - topY);
            int srcHeightUp = (int) (alturaTelaCima * ratioUp);
            int srcYUp = texPipeUp.getHeight() - srcHeightUp;

            batch.draw(texPipeUp,
                c.x, topY, pipeWidth, alturaTelaCima,
                0, srcYUp, texPipeUp.getWidth(), srcHeightUp,
                false, false);

            // Se esse cano tiver a faca E a transição ainda não engoliu a tela
            if (c.temFaca && !pegouFaca) {
                batch.draw(texFaca, c.x + pipeWidth/2f - 15f, c.gapY - 15f, 30f, 30f);
            }
        }

        // HUD
        if (canosGerados < 10) {
            font.draw(batch, "Sobreviva aos canos: " + score + "/10", 10f, HEIGHT - 10f);
        } else {
            font.draw(batch, "Pegue o kit de facas!", 10f, HEIGHT - 10f);
        }

        // === DESENHO DA TRANSIÇÃO ===
        if (transicaoAlpha > 0) {
            batch.setColor(1, 1, 1, transicaoAlpha);
            batch.draw(texPreto, 0, 0, WIDTH, HEIGHT);
            batch.setColor(1, 1, 1, 1); // Reseta a cor para o próximo frame
        }

        // 3. Fecha o batch
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
