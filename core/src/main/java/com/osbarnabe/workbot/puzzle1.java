package com.osbarnabe.workbot;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * Puzzle 1 – Sorting de itens.
 * O jogador direciona objetos caindo para a caixa correta (verde = ferramenta, vermelha = lixo).
 * Ao acertar 3 itens, o puzzle é concluído e o robô pode andar para sair.
 */
public class puzzle1 implements Screen {

    float alturaJanela  = Gdx.graphics.getHeight();
    float larguraJanela = Gdx.graphics.getWidth();

    private Main jogo;
    private SpriteBatch batch;
    private BitmapFont fonte;

    // Texturas e animações do robô
    private Texture RoboParadoImg, RoboDirImg, RoboEsqImg;
    private Animation<TextureRegion> animacaoParado, animacaoDireita, animacaoEsquerda;
    private float elapsedTime = 0f;

    // Cenário
    private Texture estoqueImg;
    private Texture BarraFerroImg;

    // Robô
    private float roboX, roboY;
    private final float tamanhoRobo    = 330f;
    private final float velocidadeRobo = 400f;
    private float tempoPressionado     = 0f;

    // Puzzle
    private boolean mostrandoTutorial  = true;
    private float protecaoTutorial     = 1.0f;
    private ObjetoCaindo objeto;
    private Caixa caixaVerde, caixaVermelha;
    private boolean escolheu           = false;
    private int itensCertos            = 0;
    private boolean puzzleFinalizado   = false;

    // Variáveis da Barra de Ferro caindo
    private float barraX;
    private float barraY;

    public puzzle1(Main jogo) {
        this.jogo = jogo;
        batch = new SpriteBatch();

        fonte = new BitmapFont();
        fonte.getData().setScale(3f);

        estoqueImg   = new Texture("estoque.png");
        BarraFerroImg = new Texture("BarraFerro.png");

        RoboParadoImg = new Texture("roboParado.png");
        RoboDirImg    = new Texture("roboDir.png");
        RoboEsqImg    = new Texture("roboEsq.png");

        animacaoParado   = new Animation<>(0.45f, extrairFrames(RoboParadoImg, 64, 64, 7));
        animacaoDireita  = new Animation<>(0.15f, extrairFrames(RoboDirImg,    64, 64, 2));
        animacaoEsquerda = new Animation<>(0.15f, extrairFrames(RoboEsqImg,    64, 64, 2));

        roboX = 70f;
        roboY = 0f;

        caixaVerde    = new Caixa(larguraJanela / 2f + 200f, 50f, true);
        caixaVermelha = new Caixa(larguraJanela / 2f - 200f, 50f, false);
        spawnObjeto();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0f, 0f, 0.5f, 1f);
        elapsedTime += delta;

        boolean esq = Gdx.input.isKeyPressed(Input.Keys.LEFT);
        boolean dir = Gdx.input.isKeyPressed(Input.Keys.RIGHT);

        // --- Tutorial ---
        if (mostrandoTutorial) {
            if (protecaoTutorial > 0) {
                protecaoTutorial -= delta;
            } else if (esq && dir) {
                mostrandoTutorial = false;
            }
        }
        // --- Jogo ativo ---
        else {
            if (!puzzleFinalizado) {
                // Controla o objeto caindo
                if (!escolheu) {
                    if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
                        objeto.irEsquerda();
                        escolheu = true;
                    } else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
                        objeto.irDireita();
                        escolheu = true;
                    }
                }

                objeto.update(delta);

                // Objeto chegou na caixa
                if (objeto.y <= 100f) {
                    if (objeto.x > larguraJanela / 2f + 150f)      verificarAcerto(caixaVerde);
                    else if (objeto.x < larguraJanela / 2f - 100f) verificarAcerto(caixaVermelha);

                    if (itensCertos >= 3) {
                        puzzleFinalizado = true; // DESTRANCA O JOGO

                        // Define onde a barra de ferro nasce
                        barraX = larguraJanela / 2f - 50f;
                        barraY = alturaJanela / 2f - 280f; // Mesma altura que os objetos caem
                    } else {
                        objeto.dispose();
                        spawnObjeto();
                        escolheu = false;
                    }
                }
            } else {
                // Puzzle concluído: libera o robô para andar
                if (esq || dir) {
                    tempoPressionado += delta;
                    if (tempoPressionado > 0.05f) {
                        if (esq) roboX -= velocidadeRobo * delta;
                        if (dir) roboX += velocidadeRobo * delta;
                    }
                } else {
                    tempoPressionado = 0f;
                }

                // LÓGICA: A barra cai e o robô pega!
                if (barraY > 50f) {
                    barraY -= 150f * delta; // Velocidade de queda
                }

                float larguraBarra = 100f;
                float alturaBarra = 64f;

                // AJUSTE DA HIT BOX DO ROBÔ
                float margemX = 120f;
                float alturaCorpoRobo = 100f;

                // Checagem de Colisão
                boolean colidiuX = (roboX + margemX) < (barraX + larguraBarra) && (roboX + tamanhoRobo - margemX) > barraX;
                boolean colidiuY = roboY < (barraY + alturaBarra) && (roboY + alturaCorpoRobo) > barraY;

                // Se tocou, volta pro GameScreen na posição da Porta 1 (X=2650f, Y=50f)
                if (colidiuX && colidiuY) {
                    jogo.npc1Completo = true; // 🔥 ESSENCIAL
                    jogo.puzzle1Completo = true;
                    jogo.setScreen(new GameScreen(jogo, 2650f, 65f));
                }
            }

            // Limites horizontais do robô
            if (roboX < 0f) roboX = 0f;
            if (roboX > larguraJanela - tamanhoRobo) roboX = larguraJanela - tamanhoRobo;
        }

        // --- Desenho ---
        batch.begin();

        if (mostrandoTutorial) batch.setColor(0.3f, 0.3f, 0.3f, 1f);

        batch.draw(estoqueImg, 0, 0, larguraJanela, alturaJanela);

        // AS CAIXAS SÓ SÃO DESENHADAS SE O PUZZLE AINDA NÃO ACABOU (Assim elas somem no final!)
        if (!puzzleFinalizado) {
            caixaVerde.draw(batch);
            caixaVermelha.draw(batch);
        }

        if (!puzzleFinalizado && !mostrandoTutorial) objeto.draw(batch);

        // Animação do robô
        TextureRegion frame;
        if (mostrandoTutorial) {
            roboY = 5;
            frame = animacaoParado.getKeyFrame(elapsedTime, true);
        } else if (puzzleFinalizado && dir && !esq) {
            frame = animacaoDireita.getKeyFrame(elapsedTime, true);
            roboY = 0;
        } else if (puzzleFinalizado && esq && !dir) {
            frame = animacaoEsquerda.getKeyFrame(elapsedTime, true);
            roboY = 0;
        } else {
            frame = animacaoParado.getKeyFrame(elapsedTime, true);
            roboY = 5;
        }
        batch.draw(frame, roboX, roboY, tamanhoRobo, tamanhoRobo);

        batch.setColor(Color.WHITE);

        // Tela de tutorial
        if (mostrandoTutorial) {
            fonte.setColor(Color.WHITE);
            fonte.getData().setScale(2f);
            fonte.draw(batch, "COMO JOGAR", larguraJanela / 2f - 100f, alturaJanela - 150f);

            fonte.getData().setScale(1.5f);
            fonte.draw(batch, "Use a seta DIREITA para mandar o objeto para a direita",
                larguraJanela / 2f - 200f, alturaJanela / 2f + 100f);
            fonte.draw(batch, "Use a seta ESQUERDA para mandar para a esquerda",
                larguraJanela / 2f - 200f, alturaJanela / 2f + 30f);

            fonte.setColor(Color.YELLOW);
            fonte.getData().setScale(1.2f);
            fonte.draw(batch, "Pressione os dois botoes (ESQ + DIR) juntos para iniciar!",
                larguraJanela / 2f - 200f, alturaJanela / 2f - 60f);

            fonte.setColor(Color.WHITE);
            fonte.getData().setScale(3f);
        }
        // HUD do jogo
        else {
            if (!puzzleFinalizado) {
                fonte.setColor(Color.YELLOW);
                fonte.draw(batch, "Acertos: " + itensCertos + " / 3", 50f, alturaJanela - 50f);
            }

            //quando o jogo finalizar
            if (puzzleFinalizado) {
                fonte.setColor(Color.GREEN);
                fonte.draw(batch, "MISSAO CUMPRIDA!", larguraJanela / 2f - 170f, alturaJanela - 100f);
                fonte.getData().setScale(2f);
                fonte.draw(batch, "Pegue o ferro para o trabalhador!", larguraJanela / 2f - 180f, alturaJanela - 200f);

                fonte.getData().setScale(4f);

                // DESENHA A BARRA CAINDO
                batch.draw(BarraFerroImg, barraX, barraY, 100f, 64f);
            }
        }

        batch.end();
    }

    private void spawnObjeto() {
        objeto = new ObjetoCaindo(larguraJanela / 2f, alturaJanela / 2f - 280f, Math.random() > 0.5);
    }

    private void verificarAcerto(Caixa caixa) {
        if (objeto.correto == caixa.aceitaCorreto) itensCertos++;
    }

    @Override
    public void dispose() {
        batch.dispose();
        fonte.dispose();
        estoqueImg.dispose();
        BarraFerroImg.dispose();
        RoboParadoImg.dispose();
        RoboDirImg.dispose();
        RoboEsqImg.dispose();

        // AQUI SIM FICA O DISPOSE DAS CAIXAS!
        caixaVerde.dispose();
        caixaVermelha.dispose();

        if (objeto != null) objeto.dispose(); //pedro lucas antonio
    }

    @Override public void show() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

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
