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
    private final float tempoMaxAFK = 20f;
    private final float limiteAFK = 0f;
    private BitmapFont fonte;

    // Texturas do cenário
    private Texture RoboParadoImg, RoboDirImg, RoboEsqImg;
    private Texture fabrica1Img, fabrica2Img, fabrica3Img;
    private Texture ceitImg, ceu1Img, ceu2Img, localFinalImg, portinha2Img;
    private Texture trabalhadorImg, trabalhador2Img, trabalhador3Img;
    private Texture inicioImg, portinhaImg;
    private Texture localportaImg, localporta2Img, localporta3Img;
    private Texture portaImg;
    private Texture pontoImg;
    private Texture fabrica3SpriteImg;

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

    private boolean dialogoAtivo = false;
    private Texture balaoNPC1;

    private float npc1X = 1850f; // mesma posição do trabalhador
    private float npc1Y = 150f;
    private float raioInteracao = 150f;

    private boolean podeInteragir = true;

    private Animation<TextureRegion> animacaoBalao;

    float tempoAviso = dialogoAtivo ? 15f : 5f;

    private boolean bloqueioNPC = true;

    private boolean dialogoNPC3 = false;

    private float npc3X = 3800f; // usa a mesma posição do trabalhador3
    private float npc3Y = 140f;

    private Texture balaoNPC3;
    private Animation<TextureRegion> animacaoBalaoNPC3;

    private Texture botaoIntImg;
    private Animation<TextureRegion> animacaoBotao;

    private Texture balaoNPC1_2;
    private Animation<TextureRegion> animacaoBalao2;

    private Animation<TextureRegion> animacaoFabrica3;

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
        fabrica3SpriteImg = jogo.assets.get("Fabrica3sprite.png", Texture.class);
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
        portinha2Img   = jogo.assets.get("portinha2.png", Texture.class);
        balaoNPC1 = jogo.assets.get("BalaoFala_NPC1.png", Texture.class);
        balaoNPC3 = jogo.assets.get("BalaoFala_NPC3.png", Texture.class);
        balaoNPC1_2 = jogo.assets.get("BalaoFala_NPC1_2.png", Texture.class);

        animacaoFabrica3 = new Animation<>(0.15f,
            extrairFrames(
                fabrica3SpriteImg, // ✅ agora sim correto
                fabrica3SpriteImg.getWidth() / 5,
                fabrica3SpriteImg.getHeight(),
                5
            )
        );

        animacaoBalao = new Animation<>(0.4f,
            extrairFrames(balaoNPC1,
                balaoNPC1.getWidth(),
                balaoNPC1.getHeight() / 2,
                2
            )
        );
        botaoIntImg = jogo.assets.get("botaoInt.png", Texture.class);

        // Monta as animações
        animacaoParado      = new Animation<>(0.45f, extrairFrames(RoboParadoImg,  64,  64, 7));
        animacaoDireita     = new Animation<>(0.15f, extrairFrames(RoboDirImg,     64,  64, 2));
        animacaoEsquerda    = new Animation<>(0.15f, extrairFrames(RoboEsqImg,     64,  64, 2));
        animacaoTrabalhador = new Animation<>(0.45f, extrairFrames(trabalhadorImg, 700, 700, 2));
        animacaoTrabalhador2 = new Animation<>(0.45f, extrairFrames(trabalhador2Img, 100, 100, 2));
        animacaoTrabalhador3 = new Animation<>(0.45f, extrairFrames(trabalhador3Img, 100, 100, 6));
        animacaoPonto       = new Animation<>(0.45f, extrairFrames(pontoImg,  50, 50, 8));
        animacaoPonto2      = new Animation<>(0.45f, extrairFrames(pontoImg,  50, 50, 8));
        animacaoBotao = new Animation<>(0.3f,
            extrairFrames(botaoIntImg,
                300,  // largura inteira
                106,  // metade da altura
                2     // 2 frames
            )
        );
        animacaoBalaoNPC3 = new Animation<>(0.4f,
            extrairFrames(
                balaoNPC3,
                balaoNPC3.getWidth(),       // largura inteira
                balaoNPC3.getHeight() / 2,  // dividido verticalmente
                2
            )
        );
        animacaoBalao2 = new Animation<>(0.4f,
            extrairFrames(
                balaoNPC1_2,
                balaoNPC1_2.getWidth(),
                balaoNPC1_2.getHeight() / 2,
                2
            )
        );

        roboX = inicioX;
        roboY = inicioY;

        // Porta 1 → leva ao Puzzle 1 (sorting de itens)
        porta1 = new Porta(2600f, 0f, 150f, alturaJanela - 259f);
        // Porta 2 → leva ao Puzzle 2 (Flappy Bird)
        porta2 = new Porta(4600f, 0f, 150f, alturaJanela - 259f);
    }

    private boolean pertoDoNPC(float npcX) {
        float centroRobo = roboX + (tamanhoRobo / 2f);
        float centroNPC = npcX + (190f / 2f); // largura do NPC

        return Math.abs(centroRobo - centroNPC) < raioInteracao;
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

        //interação NPCs
        if (esq && dir) {
            tempoAFK = 10f;

            if (podeInteragir) {

                if (dialogoAtivo) {

                    dialogoAtivo = false;

                    // PRIMEIRA VEZ
                    if (!jogo.npc1Completo) {
                        jogo.npc1Completo = true;
                        bloqueioNPC = false;
                    }

                    // SEGUNDA VEZ (pós puzzle)
                    else if (jogo.puzzle1Completo) {
                        jogo.npc1PosPuzzleFalou = true;

                        // 👇 OPCIONAL: permitir rejogar o puzzle
                        jogo.puzzle1Completo = false;
                    }
                }

                else if (pertoDoNPC(npc1X)) {

                    // 🟡 PRIMEIRA INTERAÇÃO
                    if (!jogo.npc1Completo) {
                        dialogoAtivo = true;
                        tempoAFK = tempoMaxAFK;
                    }

                    // 🔵 SEGUNDA INTERAÇÃO (depois do puzzle)
                    else if (jogo.puzzle1Completo && !jogo.npc1PosPuzzleFalou) {
                        dialogoAtivo = true;
                        tempoAFK = tempoMaxAFK;
                    }
                }

                else if (pertoDoNPC(npc3X)) {

                    if (dialogoNPC3) {
                        // FECHA
                        dialogoNPC3 = false;
                    } else {
                        // ABRE
                        dialogoNPC3 = true;
                    }
                    jogo.npc3Liberado = true;
                }
                else if (colideComPorta(porta1)) {
                    porta1.interagir();
                }
                else if (colideComPorta(porta2)) {
                    porta2.interagir();
                }

                podeInteragir = false;

            }
        } else {
            podeInteragir = true;
        }

        if (dialogoAtivo || dialogoNPC3) {
            esq = false;
            dir = false;
            tempoAFK = tempoMaxAFK;
        }

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

        bloqueioNPC = !jogo.npc1Completo;

        //barreira NPCs
        if (bloqueioNPC) {
            float limite = 2300f; // posição da porta 1 (ajusta se precisar)

            if (roboX + tamanhoRobo > limite) {
                roboX = limite - tamanhoRobo;
            }
        }

        // 🚧 NOVA BARREIRA (PUZZLE 1)
        if (!jogo.npc1PosPuzzleFalou) {

            float limitePuzzle = 3000f;

            if (roboX + tamanhoRobo > limitePuzzle) {
                roboX = limitePuzzle - tamanhoRobo;
            }
        }

        if (!jogo.npc3Liberado) {
            float limiteNPC3 = 4300f; // posição da porta (ajusta se precisar)

            if (roboX + tamanhoRobo > limiteNPC3) {
                roboX = limiteNPC3 - tamanhoRobo;
            }
        }

        // 🚧 BARREIRA APÓS PORTA 2 (PUZZLE 2)
        if (!jogo.puzzle2Completo) {
            float limiteDepoisPorta2 = 5000f; // ajusta se precisar

            if (roboX + tamanhoRobo > limiteDepoisPorta2) {
                roboX = limiteDepoisPorta2 - tamanhoRobo;
            }
        }

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

        TextureRegion frameFabrica = animacaoFabrica3.getKeyFrame(elapsedTime, true);

        batch.draw(frameFabrica, 3600, 0, 700, alturaJanela - 259);


        batch.draw(ceu2Img,        3600, alturaJanela - 259, 700, 259);
        batch.draw(localporta3Img, 4300, 0, 700, alturaJanela - 259);
        batch.draw(portaImg,       4300, 0, 700, alturaJanela - 259); // Porta 2
        batch.draw(ceu1Img,        4300, alturaJanela - 259, 700, 259);
        batch.draw(localFinalImg,  4900, 0, 700, alturaJanela-259);
        batch.draw(ceu2Img,        4900, alturaJanela-259, 700, 259);
        batch.draw(ceitImg,        5600, 0, 700, alturaJanela-259);
        batch.draw(ceu1Img,        5600, alturaJanela-259,700, 259);

        // Trabalhadores animados
        batch.draw(animacaoTrabalhador2.getKeyFrame(elapsedTime, true), 2970, 210, 260, 260); // <-- Moises da silva santos junior ndv??
        batch.draw(animacaoTrabalhador3.getKeyFrame(elapsedTime, true), 3770, 170, 380, 380);

        // BOTOES E PONTO DE EXCLAMAÇÃO
        if (pertoDoNPC(npc1X)) {

            TextureRegion frameBotao = animacaoBotao.getKeyFrame(elapsedTime, true);

            float escala = 0.45f;
            float largura = frameBotao.getRegionWidth() * escala;
            float altura  = frameBotao.getRegionHeight() * escala;

            float x = npc1X + (200f / 2f) - (largura / 2f);
            float y = npc1Y + 320f;

            batch.draw(frameBotao, x, y, largura, altura);

        }
        else if (
            !jogo.npc1Completo || // antes de falar
                (jogo.puzzle1Completo && !jogo.npc1PosPuzzleFalou) // depois do puzzle
        ) {
            batch.draw(animacaoPonto.getKeyFrame(elapsedTime, true),
                1904, 437, 100, 100);
        }
        batch.draw(animacaoTrabalhador.getKeyFrame(elapsedTime, true),  1820, 210, 260, 260);

        // BOTÃO PORTA 1
        if (colideComPorta(porta1)) {

            TextureRegion frameBotao = animacaoBotao.getKeyFrame(elapsedTime, true);

            float escala = 0.5f;
            float largura = frameBotao.getRegionWidth() * escala;
            float altura  = frameBotao.getRegionHeight() * escala;

            float x = porta1.x + (porta1.largura / 2f) - (largura / 2f) - 45f;
            float y = 520f;

            batch.draw(frameBotao, x, y, largura, altura);
        }

        // BOTÃO PORTA 2
        if (colideComPorta(porta2)) {

            TextureRegion frameBotao = animacaoBotao.getKeyFrame(elapsedTime, true);

            float escala = 0.55f;
            float largura = frameBotao.getRegionWidth() * escala;
            float altura  = frameBotao.getRegionHeight() * escala;

            float x = porta2.x + (porta2.largura / 2f) - (largura / 2f) - 45;
            float y = 5200f;

            batch.draw(frameBotao, x, y, largura, altura);
        }

        // BALÃO DE FALA
        if (dialogoAtivo) {

            TextureRegion frame;

            // PRIMEIRO DIÁLOGO
            if (!jogo.npc1Completo) {
                frame = animacaoBalao.getKeyFrame(elapsedTime, true);
            }

            // SEGUNDO DIÁLOGO
            else {
                frame = animacaoBalao2.getKeyFrame(elapsedTime, true);
            }

            float larguraBalao = 800f;
            float alturaBalao  = 400f;

            float x = camera.position.x - larguraBalao / 2f;
            float y = camera.position.y + (alturaJanela / 2f) - alturaBalao - 50f;

            batch.draw(frame, x, y, larguraBalao, alturaBalao);
        }

        if (dialogoNPC3) {
            TextureRegion frame = animacaoBalaoNPC3.getKeyFrame(elapsedTime, true);

            float larguraBalao = 800f;
            float alturaBalao = 400f;

            float x = camera.position.x - larguraBalao / 2f;
            float y = camera.position.y + (alturaJanela / 2f) - alturaBalao - 50f;

            batch.draw(frame, x, y, larguraBalao, alturaBalao);
        }

        // BOTÃO DE INTERAÇÃO NPC3
        if (pertoDoNPC(npc3X)) {

            // 🔘 BOTÃO (continua normal)
            TextureRegion frameBotao = animacaoBotao.getKeyFrame(elapsedTime, true);

            float escala = 0.6f;
            float largura = frameBotao.getRegionWidth() * escala;
            float altura  = frameBotao.getRegionHeight() * escala;

            float x = 3850 + (100f / 2f) - (largura / 2f);
            float y = 510;

            batch.draw(frameBotao, x, y, largura, altura);

        } else if (!jogo.npc3Liberado) {

            // ❗ SÓ aparece se NUNCA falou com o NPC
            batch.draw(animacaoPonto2.getKeyFrame(elapsedTime, true),
                3830, 470, 150, 150);
        }

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
        batch.draw(portinhaImg,    0, 0, 800, alturaJanela - 259);
        batch.draw(portinha2Img,   4900, 0, 700, alturaJanela-259);

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
