package com.osbarnabe.workbot;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
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

    // Texturas – carregadas diretamente (sem AssetManager)
    private Texture texBird;
    private Texture texPipeDown;
    private Texture texPipeUp;
    private Texture texBackground;

    //variaveis para a colisão dos canos
    private float recuoX = 0f;          // Velocidade extra de empurrão (horizontal)
    private float distanciaPercorrida = 0f; // Substitui o tempoSpawn
    private final float DISTANCIA_ENTRE_CANOS = 220f; // Espaço fixo entre um cano e outro


    //retangulos
    private Rectangle bird;
    private Rectangle pipeBottom;
    private Rectangle pipeTop;

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

        // Carrega texturas diretamente
        texBird       = new Texture("bird.png");
        texPipeDown   = new Texture("pipe_down.png");
        texPipeUp     = new Texture("pipe_up.png");
        texBackground = new Texture("fundo_puzzle2.png");

        bird = new Rectangle();
        pipeBottom = new Rectangle();
        pipeTop = new Rectangle();

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
        // 1. FÍSICA DO PÁSSARO (Mantém o pulo original e natural)
        velY += GRAVIDADE * delta;
        birdY += velY * delta;

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            velY = PULO; // O pulo agora funciona independente da colisão
        }

        // 2. FÍSICA DO RECUO (Suaviza o impacto sem teleportar)
        if (recuoX > 0) {
            recuoX -= 800f * delta; // "Atrito" que faz o recuo parar suavemente
            if (recuoX < 0) recuoX = 0;
        }

        // VELOCIDADE REAL DO MUNDO: Velocidade padrão menos o recuo da batida
        float vEfetiva = velocidadeCano - recuoX;

        // 3. SPAWN POR DISTÂNCIA (Resolve o erro dos canos grudados)
        // Só acumulamos distância se a velocidade efetiva for positiva
        distanciaPercorrida += vEfetiva * delta;

        if (distanciaPercorrida >= DISTANCIA_ENTRE_CANOS) {
            distanciaPercorrida = 0; // Reseta o contador de distância
            Cano c = new Cano();
            c.x = WIDTH;
            c.gapY = MathUtils.random(120, 340);
            canos.add(c);
        }

        // 4. MOVIMENTAÇÃO DOS CANOS
        for (Cano c : canos) {
            c.x -= vEfetiva * delta;
        }

        if (canos.size > 0 && canos.first().x < -60f) canos.removeIndex(0);

        // 5. COLISÃO (Diferencia frente x dentro)
        bird.set(85f, birdY + 5f, 30f, 30f);

        for (Cano c : canos) {
            float bottomHeight = c.gapY - gap / 2f;
            float topY = c.gapY + gap / 2f;

            pipeBottom.set(c.x + margem, 0f, pipeWidth - margem * 2f, bottomHeight);
            pipeTop.set(c.x + margem, topY, pipeWidth - margem * 2f, HEIGHT - topY);

            if (bird.overlaps(pipeBottom) || bird.overlaps(pipeTop)) {

                // Calcula se bateu de frente ou se já está dentro
                float birdRight = 85f + 30f; // Lado direito do pássaro
                float pipeLeft = c.x + margem; // Lado esquerdo do cano
                float penetracaoX = birdRight - pipeLeft; // O quanto o pássaro entrou no cano

                // Se entrou menos de 15 pixels, consideramos que bateu de frente (EXTERNO)
                if (penetracaoX < 15f) {
                    if (recuoX < 400f) {
                        recuoX = 500f; // Joga o mundo para trás
                    }
                    // Pequeno ajuste vertical de susto
                    if (birdY + 15f < c.gapY) velY = -50f;
                    else velY = 50f;

                } else {
                    // Já passou da borda, então bateu por dentro (INTERNO)
                    if (bird.overlaps(pipeBottom)) {
                        velY = 180f; // Bateu no cano de baixo, sobe suavemente
                    } else if (bird.overlaps(pipeTop)) {
                        velY = -180f; // Bateu a cabeça no teto, desce suavemente
                    }
                }
            }

            // 6. PONTUAÇÃO (Pontua sempre que passar)
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

        if (score >= 10) {
            jogo.setScreen(new GameScreen(jogo, 4600f, 50f));
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.4f, 0.7f, 1f, 1f);

        update(delta);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        // Fundo
        batch.draw(texBackground, 0, 0, WIDTH, HEIGHT);

        // Pássaro
        batch.draw(texBird, 80f, birdY, 40f, 40f);

        // Canos
        for (Cano c : canos) {
            float bottomHeight = c.gapY - gap / 2f;
            float topY         = c.gapY + gap / 2f;
            batch.draw(texPipeDown, c.x, 0f,  pipeWidth, bottomHeight);
            batch.draw(texPipeUp,   c.x, topY, pipeWidth, HEIGHT - topY);
        }

        // HUD
        font.draw(batch, "Pontuação: " + score + " / 10", 10f, HEIGHT - 10f);

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
    }
}
