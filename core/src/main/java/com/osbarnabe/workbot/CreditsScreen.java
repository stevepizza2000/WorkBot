package com.osbarnabe.workbot;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class CreditsScreen implements Screen {

    private Main jogo;
    private OrthographicCamera camera;
    private Viewport viewport;
    private SpriteBatch batch;

    private BitmapFont fonteCargo;
    private BitmapFont fonteNome;

    private Texture btnVoltar;
    private Texture btnVoltarSelect;
    private boolean botaoSelecionado = false;

    private float rolagemY           = -200f;
    private final float velocidadeRolagem = 150f;
    private final float limiteRolagem     = 3450f;

    public CreditsScreen(Main jogo) {
        float largura = Gdx.graphics.getWidth();
        float altura  = Gdx.graphics.getHeight();
        this.jogo = jogo;
        batch     = new SpriteBatch();
        camera    = new OrthographicCamera();
        viewport  = new FitViewport(largura, altura, camera);

        btnVoltar       = new Texture("BotaoVoltar.png");
        btnVoltarSelect = new Texture("BotaoVoltarSelect.png");

        fonteCargo = new BitmapFont();
        fonteCargo.getData().setScale(2f);

        fonteNome = new BitmapFont();
        fonteNome.getData().setScale(3f);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0f, 0f, 0f, 1f);

        // Rolagem automática
        if (rolagemY < limiteRolagem) {
            rolagemY += velocidadeRolagem * delta;
        } else {
            botaoSelecionado = true;
        }

        boolean esq = Gdx.input.isKeyPressed(Input.Keys.LEFT);
        boolean dir = Gdx.input.isKeyPressed(Input.Keys.RIGHT);

        // Acelera rolagem ao segurar qualquer botão
        if ((esq || dir) && rolagemY < limiteRolagem) {
            rolagemY += (velocidadeRolagem * 5f) * delta;
        }

        // Volta ao menu após os créditos
        if (botaoSelecionado && esq && dir) {
            jogo.setScreen(new MenuScreen(jogo));
        }

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        float espacamentoY = rolagemY;
        float largura      = Gdx.graphics.getWidth();

        // Bloco 1 – Criadores
        fonteCargo.draw(batch, "Criadores", 0, espacamentoY, largura, Align.center, false);
        fonteNome.draw(batch, "Arthur B. Freitag",     0, espacamentoY -  50, largura, Align.center, false);
        fonteNome.draw(batch, "Arthur O. Deitos",      0, espacamentoY - 110, largura, Align.center, false);
        fonteNome.draw(batch, "Artur D. Costa",        0, espacamentoY - 160, largura, Align.center, false);
        fonteNome.draw(batch, "Bruno Darsie",          0, espacamentoY - 210, largura, Align.center, false);
        fonteNome.draw(batch, "Felipe Biondo",         0, espacamentoY - 260, largura, Align.center, false);
        fonteNome.draw(batch, "Gabriel A. Dieterich",  0, espacamentoY - 310, largura, Align.center, false);
        fonteNome.draw(batch, "Miguel R. Hoff",        0, espacamentoY - 360, largura, Align.center, false);
        espacamentoY -= 500;

        // Bloco 2 – Arte e Design
        fonteCargo.draw(batch, "Arte e Design", 0, espacamentoY, largura, Align.center, false);
        fonteNome.draw(batch, "Arthur O. Deitos",     0, espacamentoY -  50, largura, Align.center, false);
        fonteNome.draw(batch, "Gabriel A. Dieterich", 0, espacamentoY - 100, largura, Align.center, false);
        fonteNome.draw(batch, "Felipe Biondo",        0, espacamentoY - 150, largura, Align.center, false);
        fonteNome.draw(batch, "Bruno Darsie",         0, espacamentoY - 200, largura, Align.center, false);
        fonteNome.draw(batch, "Miguel R. Hoff",       0, espacamentoY - 250, largura, Align.center, false);
        espacamentoY -= 380;

        // Bloco 3 – Programação
        fonteCargo.draw(batch, "Programacao", 0, espacamentoY, largura, Align.center, false);
        fonteNome.draw(batch, "Artur D. Costa",    0, espacamentoY -  50, largura, Align.center, false);
        fonteNome.draw(batch, "Arthur B. Freitag", 0, espacamentoY - 100, largura, Align.center, false);
        espacamentoY -= 220;

        // Bloco 4 – Roteiro
        fonteCargo.draw(batch, "Roteiro", 0, espacamentoY, largura, Align.center, false);
        fonteNome.draw(batch, "Felipe Biondo", 0, espacamentoY -  50, largura, Align.center, false);
        fonteNome.draw(batch, "Miguel R. Hoff", 0, espacamentoY - 100, largura, Align.center, false);
        fonteNome.draw(batch, "Bruno Darsie",   0, espacamentoY - 150, largura, Align.center, false);
        espacamentoY -= 250;

        // Bloco 5 – Agradecimentos
        fonteCargo.draw(batch, "Agradecimentos Especiais", 0, espacamentoY, largura, Align.center, false);
        fonteNome.draw(batch, "Tramontina CEIT", 0, espacamentoY - 50, largura, Align.center, false);
        espacamentoY -= 400;

        // Botão Voltar
        Texture texturaAtual = botaoSelecionado ? btnVoltarSelect : btnVoltar;
        batch.draw(texturaAtual, (largura / 2f) - 150f, espacamentoY, 300f, 150f);

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
        fonteCargo.dispose();
        fonteNome.dispose();
        btnVoltar.dispose();
        btnVoltarSelect.dispose();
    }
}
