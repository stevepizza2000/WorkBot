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

    // Dimensões da viewport do mini-jogo
    public static final int WIDTH  = 300;
    public static final int HEIGHT = 480;

    // Pássaro
    private float birdY    = 200f;
    private float velY     = 0f;
    private final float GRAVIDADE = -750f;
    private final float PULO      = 250f;

    // Canos
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
        // Física do pássaro
        velY  += GRAVIDADE * delta;
        birdY += velY * delta;

        // Pulo com ESPAÇO
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            velY = PULO;
        }

        // Spawn de canos
        tempoSpawn += delta;
        if (tempoSpawn > 2f) {
            tempoSpawn = 0f;
            Cano c = new Cano();
            c.x    = WIDTH;
            c.gapY = MathUtils.random(100, 350);
            canos.add(c);
        }

        // Move canos
        for (Cano c : canos) c.x -= velocidadeCano * delta;

        // Remove cano que saiu da tela
        if (canos.size > 0 && canos.first().x < -50f) canos.removeIndex(0);

        // Colisões
        Rectangle bird = new Rectangle(85f, birdY + 5f, 30f, 30f);
        float pipeWidth = 50f;
        float margem    = 5f;

        for (Cano c : canos) {
            float bottomHeight = c.gapY - gap / 2f;
            float topY         = c.gapY + gap / 2f;
            float topHeight    = HEIGHT - topY;

            Rectangle pipeBottom = new Rectangle(c.x + margem, 0f,   pipeWidth - margem * 2f, bottomHeight);
            Rectangle pipeTop    = new Rectangle(c.x + margem, topY, pipeWidth - margem * 2f, topHeight);

            if (bird.overlaps(pipeBottom) || bird.overlaps(pipeTop)) {
                resetGame();
                return;
            }

            // Contagem de score
            if (c.x + pipeWidth < 80f && !c.passou) {
                if (score < 10) score++;
                c.passou = true;
            }
        }

        // Caiu no chão
        if (birdY < 0f) resetGame();

        // Condição de vitória → volta ao GameScreen próximo à Porta 2
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
            batch.draw(texPipeDown, c.x, 0f,  50f, bottomHeight);
            batch.draw(texPipeUp,   c.x, topY, 50f, HEIGHT - topY);
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
