package com.osbarnabe.workbot;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.Align;

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
    private Animation<TextureRegion> animacaoEntrando;

    // Animações de personagens do cenário
    private Animation<TextureRegion> animacaoTrabalhador;
    private Animation<TextureRegion> animacaoTrabalhador2;
    private Animation<TextureRegion> animacaoTrabalhador3;
    private Animation<TextureRegion> animacaoPonto;
    private Animation<TextureRegion> animacaoPonto2;

    private float elapsedTime = 0f;

    // Mensagem quando bate na barreira
    private String mensagemBarreira = "";
    private float tempoMensagemBarreira = 0f;

    // Cronômetro AFK
    private float tempoAFK = 15f;
    private final float tempoMaxAFK = 20f;
    private final float limiteAFK = 0f;
    private BitmapFont fonte;

    // Texturas do cenário
    private Texture RoboParadoImg, RoboDirImg, RoboEsqImg, RoboEntrandoImg;
    private Texture ceu1Img, ceu2Img, portinha2Img, finalImg;
    private Texture trabalhadorImg, trabalhador2Img, trabalhador3Img;

    private Texture tutorialImg;
    private Texture placaD, placaDTutorial, placaI, placaITutorial, placaE, placaETutorial;

    private Texture portinhaImg;

    private Texture portaImg;
    private Texture pontoImg;
    private Texture fabrica3SpriteImg;

    // sprite sheets
    private Texture inicioSpriteImg;
    private Animation<TextureRegion> animacaoInicio;

    private Texture localportaSpriteImg;
    private Animation<TextureRegion> animacaoLocalPorta;

    private Texture localporta2SpriteImg;
    private Animation<TextureRegion> animacaoLocalPorta2;

    private Texture ceitSpriteImg, ceitSpriteFechandoImg;
    private Animation<TextureRegion> animacaoCeit;
    private Animation<TextureRegion> animacaoCeitFechando;

    // --- CONTROLE DO FINAL (CEIT) ---
    private float tempoCeit = 0f;
    private boolean ceitAbrindo = false;

    // --- CONTROLE DA ANIMAÇÃO DE ENTRADA NO CEIT ---
    private boolean roboEntrando = false;
    private float tempoEntrando = 0f;


    private Texture fabrica2SpriteImg;
    private Animation<TextureRegion> animacaoFabrica2;

    private Texture fabrica1SpriteImg;
    private Animation<TextureRegion> animacaoFabrica1;

    private Texture localporta3SpriteImg;
    private Animation<TextureRegion> animacaoLocalPorta3;

    private Texture localFinalSpriteImg;
    private Animation<TextureRegion> animacaoLocalFinal;

    // Posição do robô
    private float roboX;
    private float roboY;
    private final float tamanhoRobo  = 330f;
    private float velocidadeRobo = 500f;

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

    // +800 em relação ao original (2580 → 3380)
    private float npc1X = 3380f;
    private float npc1Y = 150f;
    private float raioInteracao = 150f;

    private boolean podeInteragir = true;

    private Animation<TextureRegion> animacaoBalao;

    float tempoAviso = dialogoAtivo ? 15f : 5f;

    private boolean bloqueioNPC = true;

    private boolean dialogoNPC3 = false;

    // +800 em relação ao original (4300 → 5100)
    private float npc3X = 5100f;
    private float npc3Y = 140f;

    private Texture balaoNPC3;
    private Animation<TextureRegion> animacaoBalaoNPC3;

    private Texture botaoIntImg;
    private Animation<TextureRegion> animacaoBotao;

    private Texture balaoNPC1_2;
    private Animation<TextureRegion> animacaoBalao2;

    private Animation<TextureRegion> animacaoFabrica3;

    private Texture balaoNPC3_2;
    private Animation<TextureRegion> animacaoBalaoNPC3_2;

    // --- CONTROLE DAS PLACAS DO TUTORIAL ---
    private boolean passouPlacaI = false;
    private boolean mostrandoPlacaI = false;

    ////DEBUG
    private boolean debugSemBarreira = false;

    private String[] textosAFK = {
        "O jogo vai fechar em: ",    // PT
        "El juego se cerrara en: ",  // ES
        "The game will close in: "   // EN
    };

    private boolean velocidadeRapida = false;

    private boolean dialogoNPC2 = false;
    private float npc2X = 4070f; // Posição X de exemplo (coloque onde o trabalhador 2 está)
    private float npc2Y = 210f;  // Posição Y (perto dos 210px que você usou no batch.draw do trabalhador 2)

    private Texture balaoNPC2;
    private Animation<TextureRegion> animacaoBalaoNPC2;

    private Texture procuradoImg;          // Armazena a imagem do cartaz
    private boolean npc2LiberouArvore = false; // Indica se já falou com o NPC 2
    private boolean exibirProcurado = false;   // Controla se o cartaz está visível na tela

    public GameScreen(Main jogo) {
        this(jogo, 0f, 65f);
    }

    /** Construtor que permite posicionar o robô em um ponto específico (ex.: ao voltar de um puzzle). */
    public GameScreen(Main jogo, float inicioX, float inicioY) {

        String sufixo = "";
        if (jogo.idioma == 1) sufixo = "_ES";
        else if (jogo.idioma == 2) sufixo = "_EN";

        this.jogo = jogo;
        batch = new SpriteBatch();

        FreeTypeFontGenerator generator =
            new FreeTypeFontGenerator(Gdx.files.internal("assets/fonts/PixelifySans-Regular.ttf"));

        FreeTypeFontGenerator.FreeTypeFontParameter parameter =
            new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = 32;
        parameter.color = Color.WHITE;

        fonte = generator.generateFont(parameter);

        generator.dispose();

        camera = new OrthographicCamera();
        viewport = new FitViewport(larguraJanela, alturaJanela, camera);

        // Carrega texturas via AssetManager (já pré-carregadas no LoadingScreen)
        RoboParadoImg  = jogo.assets.get("roboParado.png",   Texture.class);
        RoboDirImg     = jogo.assets.get("roboDir.png",      Texture.class);
        RoboEsqImg     = jogo.assets.get("roboEsq.png",      Texture.class);
        RoboEntrandoImg= jogo.assets.get("roboPorta.png", Texture.class);
        fabrica3SpriteImg = jogo.assets.get("fabrica3_sprite.png", Texture.class);
        ceu1Img        = jogo.assets.get("ceu1.png",         Texture.class);
        ceu2Img        = jogo.assets.get("ceu2.png",         Texture.class);
        portinhaImg    = jogo.assets.get("portinha.png",     Texture.class);
        trabalhadorImg = jogo.assets.get("trabalhador.png", Texture.class);
        portaImg       = jogo.assets.get("porta.png",        Texture.class);
        trabalhador2Img = jogo.assets.get("trabalhador2.png", Texture.class);
        trabalhador3Img = jogo.assets.get("trabalhador3.png", Texture.class);
        pontoImg       = jogo.assets.get("ponto.png",        Texture.class);
        portinha2Img   = jogo.assets.get("portinha2.png", Texture.class);
        tutorialImg    = jogo.assets.get("tutorial.png", Texture.class);
        finalImg = jogo.assets.get("final.png", Texture.class);

        //animações fundo
        ceitSpriteImg = jogo.assets.get("ceit_sprite.png", Texture.class);
        ceitSpriteFechandoImg = jogo.assets.get("ceit_sprite_fechando.png", Texture.class);
        fabrica2SpriteImg = jogo.assets.get("fabrica2_sprite.png", Texture.class);
        fabrica1SpriteImg = jogo.assets.get("fabrica1_sprite.png", Texture.class);
        localportaSpriteImg = jogo.assets.get("localporta_sprite.png", Texture.class);
        inicioSpriteImg = jogo.assets.get("inicio_sprite.png", Texture.class);
        localporta3SpriteImg = jogo.assets.get("localporta3_sprite.png", Texture.class);
        localporta2SpriteImg = jogo.assets.get("localporta2_sprite.png", Texture.class);
        localFinalSpriteImg = jogo.assets.get("localFinal_sprite.png", Texture.class);

        //placas
        placaD         = jogo.assets.get("placaRight" + sufixo + ".png", Texture.class);
        placaE         = jogo.assets.get("placaLeft" + sufixo + ".png", Texture.class);
        placaI         = jogo.assets.get("placaInteragir"+sufixo+".png", Texture.class);

        placaDTutorial = jogo.assets.get("placaRightTutorial" + sufixo + ".png", Texture.class);
        placaETutorial = jogo.assets.get("placaLeftTutorial" + sufixo + ".png", Texture.class);
        placaITutorial = jogo.assets.get("placaInteragirTutorial" + sufixo + ".png", Texture.class);

        balaoNPC1 = jogo.assets.get("BalaoFala_NPC1" + sufixo + ".png", Texture.class);
        balaoNPC3 = jogo.assets.get("BalaoFala_NPC3" + sufixo + ".png", Texture.class);
        balaoNPC1_2 = jogo.assets.get("BalaoFala_NPC1_2" + sufixo + ".png", Texture.class);
        balaoNPC3_2 = jogo.assets.get("BalaoFala_NPC3_2" + sufixo + ".png", Texture.class);
        balaoNPC2 = jogo.assets.get("BalaoFala_NPC2" + sufixo + ".png", Texture.class);
        procuradoImg = jogo.assets.get("Procurado" + sufixo + ".png", Texture.class);


        animacaoFabrica3 = new Animation<>(0.10f,
            extrairFrames(
                fabrica3SpriteImg,
                fabrica3SpriteImg.getWidth() / 8,
                fabrica3SpriteImg.getHeight(),
                8
            )
        );

        //inicio
        animacaoInicio = new Animation<>(0.10f,
            extrairFrames(
                inicioSpriteImg,
                inicioSpriteImg.getWidth() / 8,
                inicioSpriteImg.getHeight(),
                8
            )
        );

        //animação local porta
        animacaoLocalPorta = new Animation<>(0.10f,
            extrairFrames(
                localportaSpriteImg,
                localportaSpriteImg.getWidth() / 8,
                localportaSpriteImg.getHeight(),
                8
            )
        );

        //animação fabrica 1
        animacaoFabrica1 = new Animation<>(0.10f,
            extrairFrames(
                fabrica1SpriteImg,
                fabrica1SpriteImg.getWidth() / 8,
                fabrica1SpriteImg.getHeight(),
                8
            )
        );

        //animação fabrica 2
        animacaoFabrica2 = new Animation<>(0.10f,
            extrairFrames(
                fabrica2SpriteImg,
                fabrica2SpriteImg.getWidth() / 8,
                fabrica2SpriteImg.getHeight(),
                8
            )
        );

        //local port 2
        animacaoLocalPorta2 = new Animation<>(0.10f,
            extrairFrames(
                localporta2SpriteImg,
                localporta2SpriteImg.getWidth() / 8,
                localporta2SpriteImg.getHeight(),
                8
            )
        );

        //animação ceit
        animacaoCeit = new Animation<>(0.15f,
            extrairFrames(
                ceitSpriteImg,
                ceitSpriteImg.getWidth() / 8,
                ceitSpriteImg.getHeight(),
                8
            )
        );

        animacaoCeitFechando = new Animation<>(0.15f,
            extrairFrames(
                ceitSpriteFechandoImg,
                ceitSpriteFechandoImg.getWidth() / 8,
                ceitSpriteFechandoImg.getHeight(),
                8
            )
        );

        //animação local porta 3
        animacaoLocalPorta3 = new Animation<>(0.10f,
            extrairFrames(
                localporta3SpriteImg,
                localporta3SpriteImg.getWidth() / 8,
                localporta3SpriteImg.getHeight(),
                8
            )
        );

        // final da fabrica
        animacaoLocalFinal = new Animation<>(0.10f,
            extrairFrames(
                localFinalSpriteImg,
                localFinalSpriteImg.getWidth() / 8,
                localFinalSpriteImg.getHeight(),
                8
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
        animacaoEntrando    = new Animation<>(0.15f, extrairFrames(RoboEntrandoImg,     100,  100, 2));

        animacaoTrabalhador = new Animation<>(0.45f, extrairFrames(trabalhadorImg, 700, 700, 2));
        animacaoTrabalhador2 = new Animation<>(0.45f, extrairFrames(trabalhador2Img, 100, 100, 2));
        animacaoTrabalhador3 = new Animation<>(0.45f, extrairFrames(trabalhador3Img, 100, 100, 6));
        animacaoPonto       = new Animation<>(0.45f, extrairFrames(pontoImg,  50, 50, 8));
        animacaoPonto2      = new Animation<>(0.45f, extrairFrames(pontoImg,  50, 50, 8));
        animacaoBotao = new Animation<>(0.3f,
            extrairFrames(botaoIntImg,
                300,
                106,
                2
            )
        );
        animacaoBalaoNPC3 = new Animation<>(0.4f,
            extrairFrames(
                balaoNPC3,
                balaoNPC3.getWidth(),
                balaoNPC3.getHeight() / 2,
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
        animacaoBalaoNPC3_2 = new Animation<>(0.4f,
            extrairFrames(
                balaoNPC3_2,
                balaoNPC3_2.getWidth(),
                balaoNPC3_2.getHeight() / 2,
                2
            )
        );

        animacaoBalaoNPC2 = new Animation<>(0.4f,
            extrairFrames(
                balaoNPC2,
                balaoNPC2.getWidth(),       // largura inteira
                balaoNPC2.getHeight() / 2,  // dividido verticalmente por 2 frames
                2
            )
        );

        roboX = inicioX;
        roboY = inicioY;

        // Porta 1 → leva ao Puzzle 1 (sorting de itens)  — original 2750 + 800 = 3550
        porta1 = new Porta(3550f, 0f, 150f, alturaJanela - 259f);
        // Porta 2 → leva ao Puzzle 2 (Flappy Bird)       — original 5100 + 800 = 5900
        porta2 = new Porta(5900f, 0f, 150f, alturaJanela - 259f);
    }

    private boolean pertoDoNPC(float npcX, float offsetX) {
        // Pega o centro real do robô (X atual + metade da sua largura)
        float centroRobo = roboX + (tamanhoRobo / 2f);

        // Pega o centro real do NPC somando a metade da largura padrão de um trabalhador (260f / 2f = 130f)
        // O offsetX serve para ajustes manuais específicos de cada um
        float centroNPC = npcX + 130f + offsetX;

        return Math.abs(centroRobo - centroNPC) < raioInteracao;
    }


    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.8f, 0.85f, 0.9f, 1f);

        // DEBUG
        if (Gdx.input.isKeyJustPressed(Input.Keys.H)) {
            debugSemBarreira = !debugSemBarreira;
            System.out.println("DEBUG barreiras: " + debugSemBarreira);
        }

        // Lógica para alternar velocidade com a tecla S
        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            velocidadeRapida = !velocidadeRapida;
            if (velocidadeRapida) {
                velocidadeRobo = 2500f;
            } else {
                velocidadeRobo = 500f;
            }

            // Opcional: print para confirmar no console
            System.out.println("Velocidade alterada para: " + velocidadeRobo);
        }

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

// --- CONTROLE DO TEMPO DA ANIMAÇÃO DE ENTRADA ---
        if (roboEntrando) {
            tempoEntrando += delta;
            // Aguarda 1.2 segundos rodando a animação antes de ir para os créditos
            if (tempoEntrando >= 1.2f) {
                jogo.setScreen(new CreditsScreen(jogo));
                return;
            }
        }

        float centroRobo = roboX + (tamanhoRobo / 2f);

        boolean pertoCeit = (centroRobo >= 7400f && centroRobo <= 7800f);

        if (pertoCeit) {
            if (!ceitAbrindo) {
                ceitAbrindo = true;
                tempoCeit = 0f; // Reseta para começar a abrir do primeiro frame
            }
            tempoCeit += delta;
        } else {
            if (ceitAbrindo) {
                ceitAbrindo = false;
                tempoCeit = 0f; // Reseta para começar a fechar do primeiro frame
            }
            tempoCeit += delta;
        }

        if (tempoMensagemBarreira > 0) {
            tempoMensagemBarreira -= delta;
        }

        // --- LÓGICA DO POP-UP DA PLACA DE INTERAÇÃO (Trava o robô) ---
        // Se passar pelo X entre 540 e 660 e ainda não tiver interagido
        if (centroRobo >= 590f && centroRobo <= 710f && !passouPlacaI) {
            mostrandoPlacaI = true;
        } else {
            // Caso ele saia de perto da placa (apenas por garantia)
            mostrandoPlacaI = false;
        }

        // Interação NPCs e Placa
        if (esq && dir) {
            tempoAFK = 10f;

            if (podeInteragir) {

                // Destrava a placa de interação!
                if (mostrandoPlacaI) {
                    passouPlacaI = true;
                    mostrandoPlacaI = false;
                }
                else if (dialogoAtivo) {

                    dialogoAtivo = false;

                    // PRIMEIRA FALA
                    if (!jogo.npc1Completo) {
                        jogo.npc1Completo = true;
                    }

                    // SEGUNDA FALA
                    else if (jogo.puzzle1Completo && !jogo.npc1PosPuzzleFalou) {
                        jogo.npc1PosPuzzleFalou = true;
                    }

                    // TERCEIRA FALA
                    else if (jogo.npc1PosPuzzleFalou && !jogo.npc1Fase2Falou) {
                        jogo.npc1Fase2Falou = true;
                    }
                }

                else if (pertoDoNPC(npc1X, -180f)) {

                    if (!dialogoAtivo) {
                        if (
                            !jogo.npc1Completo ||
                                (jogo.puzzle1Completo && !jogo.npc1PosPuzzleFalou) ||
                                (jogo.npc1PosPuzzleFalou && !jogo.npc1Fase2Falou)
                        ) {
                            dialogoAtivo = true;
                            tempoAFK = tempoMaxAFK;
                        }
                    }
                }

                else if (pertoDoNPC(npc2X, 0f)) {
                    if (dialogoNPC2) {
                        dialogoNPC2 = false;
                        npc2LiberouArvore = true; // Libera a árvore quando o diálogo fecha
                    } else {
                        dialogoNPC2 = true;
                        tempoAFK = tempoMaxAFK;
                    }
                }

// Interação com a árvore (só funciona se já falou com o NPC 2)
                else if (npc2LiberouArvore && pertoDoNPC(800f, -30f)) {
                    if (exibirProcurado) {
                        exibirProcurado = false; // Fecha o cartaz
                    } else {
                        exibirProcurado = true;  // Abre o cartaz
                        tempoAFK = tempoMaxAFK;
                    }
                }

                else if (pertoDoNPC(npc3X, 400f)) {

                    if (dialogoNPC3) {

                        dialogoNPC3 = false;

                        // PRIMEIRA VEZ
                        if (!jogo.npc3Falou) {
                            jogo.npc3Falou = true;
                        }
                        // PÓS-PUZZLE
                        else if (jogo.puzzle2Completo) {
                            jogo.npc3PosPuzzleFalou = true;
                        }

                    } else {

                        // PRIMEIRA INTERAÇÃO
                        if (!jogo.npc3Falou) {
                            dialogoNPC3 = true;
                        }
                        // DEPOIS DO PUZZLE → SEM LIMITE
                        else if (jogo.puzzle2Completo) {
                            dialogoNPC3 = true;
                        }
                    }

                    jogo.npc3Liberado = true;
                }
                else if (centroRobo >= 7400f && centroRobo <= 7800f) {
                    if (!roboEntrando) {
                        roboEntrando = true;
                        tempoEntrando = 0f; // Inicializa o cronômetro da animação
                    }
                }

                podeInteragir = false;

            }
        } else {
            podeInteragir = true;
        }

        if (dialogoAtivo || dialogoNPC2 || roboEntrando || mostrandoPlacaI || dialogoNPC3 || exibirProcurado) { // <--- Adicionado aqui
            mensagemBarreira = "";
            tempoMensagemBarreira = 0f;
            esq = false;
            dir = false;
            tempoAFK = tempoMaxAFK;
        }

        // Interação com portas (esq + dir juntos, garantindo que não estamos presos)
        if (esq && dir) {
            tempoAFK = 10f;
            if (!processouBotao) {
                if (colideComPorta(porta1)) porta1.interagir();
                else if (colideComPorta(porta2)) porta2.interagir();
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
        if (roboX > 7650f) roboX = 7650f;

        bloqueioNPC = !jogo.npc1Completo;

        // Barreira NPC1 — original 2750 + 800 = 3550
        if (bloqueioNPC && !debugSemBarreira) {
            float limite = 3550f;
            if (roboX + tamanhoRobo > limite) {
                roboX = limite - tamanhoRobo;
                mensagemBarreira = "Fale com o trabalhador primeiro!";
                tempoMensagemBarreira = 3f;
            }
        }

        // Barreira pós Puzzle 1 — original 3200 + 800 = 4000
        if (!jogo.npc1PosPuzzleFalou && !debugSemBarreira) {
            float limitePuzzle = 4000f;
            if (roboX + tamanhoRobo > limitePuzzle) {
                roboX = limitePuzzle - tamanhoRobo;
                if (!jogo.puzzle1Completo) {
                    mensagemBarreira = "Complete o Puzzle 1 primeiro!";
                } else {
                    mensagemBarreira = "Converse com o trabalhador \nnovamente!";
                }
                tempoMensagemBarreira = 3f;
            }
        }

        // Barreira NPC3 — original 5150 + 800 = 5950
        if (!jogo.npc3Liberado && !debugSemBarreira) {
            float limiteNPC3 = 5950f;
            if (roboX + tamanhoRobo > limiteNPC3) {
                roboX = limiteNPC3 - tamanhoRobo;
                mensagemBarreira = "Converse com o trabalhador!";
                tempoMensagemBarreira = 3f;
            }
        }

        // Barreira pós Porta 2 — original 5550 + 800 = 6350
        if (!jogo.npc3PosPuzzleFalou && !debugSemBarreira) {
            float limiteDepoisPorta2 = 6350f;
            if (roboX + tamanhoRobo > limiteDepoisPorta2) {
                roboX = limiteDepoisPorta2 - tamanhoRobo;
                if (!jogo.puzzle2Completo) {
                    mensagemBarreira = "Finalize o Puzzle 2 primeiro!";
                } else {
                    mensagemBarreira = "Converse com o trabalhador \nnovamente!";
                }
                tempoMensagemBarreira = 3f;
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
        batch.draw(tutorialImg,    0,    0, 800, alturaJanela - 259); //inicio
        batch.draw(ceu2Img,        0,    alturaJanela - 259, 800, 259);
        batch.draw(ceu1Img,        800,  alturaJanela - 259, 800, 259);
        batch.draw(ceu2Img,        1600, alturaJanela - 259, 800, 259);
        batch.draw(ceu1Img,        2400, alturaJanela - 259, 800, 259);
        batch.draw(portaImg,       3200, 0, 800, alturaJanela - 259); // Porta 1
        batch.draw(ceu2Img,        3200, alturaJanela - 259, 800, 259);
        batch.draw(ceu1Img,        4000, alturaJanela - 259, 800, 259);

        TextureRegion frameFabrica = animacaoFabrica3.getKeyFrame(elapsedTime, true);
        batch.draw(frameFabrica,   4800, 0, 800, alturaJanela - 259);

        batch.draw(ceu2Img,        4800, alturaJanela - 259, 800, 259);
        batch.draw(portaImg,       5600, 0, 800, alturaJanela - 259); // Porta 2
        batch.draw(ceu1Img,        5600, alturaJanela - 259, 800, 259);
        batch.draw(ceu2Img,        6400, alturaJanela - 259, 800, 259);
        batch.draw(ceu1Img,        7200, alturaJanela - 259, 800, 259);
        batch.draw(ceu2Img,        8000, alturaJanela - 259, 800, 259);
        batch.draw(finalImg, 8000, 0, 800, alturaJanela - 259);

        //fundo animado
        TextureRegion frameCeitAtual;
        if (ceitAbrindo) {
            // false indica que não vai repetir em loop. Para no último frame (aberto)
            frameCeitAtual = animacaoCeit.getKeyFrame(tempoCeit, false);
        } else {
            // false faz parar no último frame de fechamento (fechado)
            frameCeitAtual = animacaoCeitFechando.getKeyFrame(tempoCeit, false);
        }
        batch.draw(frameCeitAtual, 7200, 0, 800, alturaJanela - 259);


// ---- BOTÃO DE INTERAÇÃO NA PORTA DO CEIT ----
        if (centroRobo >= 7550f && centroRobo <= 7700f) {
            TextureRegion frameBotao = animacaoBotao.getKeyFrame(elapsedTime, true);

            float escala = 0.45f;
            float larguraB = frameBotao.getRegionWidth() * escala;
            float alturaB  = frameBotao.getRegionHeight() * escala;

            batch.draw(frameBotao, 7565, 500, larguraB, alturaB);
        }

        TextureRegion frameFabrica1 = animacaoFabrica1.getKeyFrame(elapsedTime, true);
        batch.draw(frameFabrica1, 2400, 0, 800, alturaJanela - 259);

        TextureRegion frameFabrica2 = animacaoFabrica2.getKeyFrame(elapsedTime, true);
        batch.draw(frameFabrica2, 4000, 0, 800, alturaJanela - 259);

        TextureRegion frameLocalPorta = animacaoLocalPorta.getKeyFrame(elapsedTime, true);
        batch.draw(frameLocalPorta, 1600, 0, 800, alturaJanela - 259);

        TextureRegion frameInicio = animacaoInicio.getKeyFrame(elapsedTime, true);
        batch.draw(frameInicio, 800, 0, 800, alturaJanela - 259);

        TextureRegion frameLocalPorta3 = animacaoLocalPorta3.getKeyFrame(elapsedTime, true);
        batch.draw(frameLocalPorta3, 5600, 0, 800, alturaJanela - 259);

        TextureRegion frameLocalPorta2 = animacaoLocalPorta2.getKeyFrame(elapsedTime, true);
        batch.draw(frameLocalPorta2, 3200, 0, 800, alturaJanela - 259);

        TextureRegion frameLocalFinal = animacaoLocalFinal.getKeyFrame(elapsedTime, true);
        batch.draw(frameLocalFinal, 6400, 0, 800, alturaJanela - 259);

        // Trabalhadores animados — original 3270/4570 + 800 = 4070/5370
        batch.draw(animacaoTrabalhador2.getKeyFrame(elapsedTime, true), 4070, 210, 260, 260);
        batch.draw(animacaoTrabalhador3.getKeyFrame(elapsedTime, true), 5370, 170, 380, 380);

        // BOTÕES E PONTO DE EXCLAMAÇÃO — NPC1
        if (pertoDoNPC(npc1X, -180f)) {

            TextureRegion frameBotao = animacaoBotao.getKeyFrame(elapsedTime, true);

            float escala = 0.45f;
            float largura = frameBotao.getRegionWidth() * escala;
            float altura  = frameBotao.getRegionHeight() * escala;

            float x = npc1X + (-140f / 2f) - (largura / 2f);
            float y = npc1Y + 320f;

            batch.draw(frameBotao, x, y, largura, altura);

        } else if (
            !jogo.npc1Completo ||
                (jogo.puzzle1Completo && !jogo.npc1PosPuzzleFalou)
        ) {
            // original 2466 + 800 = 3266
            batch.draw(animacaoPonto.getKeyFrame(elapsedTime, true),
                3266, 437, 100, 100);
        }
        // Trabalhador NPC1 — original 2380 + 800 = 3180
        batch.draw(animacaoTrabalhador.getKeyFrame(elapsedTime, true), 3180, 210, 260, 260);

        // BOTÃO PORTA 1
        if (colideComPorta(porta1)) {

            TextureRegion frameBotao = animacaoBotao.getKeyFrame(elapsedTime, true);

            float escala = 0.5f;
            float largura = frameBotao.getRegionWidth() * escala;
            float altura  = frameBotao.getRegionHeight() * escala;

            float x = porta1.x + (porta1.largura / 2f) - (largura / 2f) - 50f;
            float y = 520f;

            batch.draw(frameBotao, x, y, largura, altura);
        }

        // BOTÃO PORTA 2
        if (colideComPorta(porta2)) {

            TextureRegion frameBotao = animacaoBotao.getKeyFrame(elapsedTime, true);

            float escala = 0.55f;
            float largura = frameBotao.getRegionWidth() * escala;
            float altura  = frameBotao.getRegionHeight() * escala;

            float x = porta2.x + (porta2.largura / 2f) - (largura / 2f);
            float y = 520f;

            batch.draw(frameBotao, x, y, largura, altura);
        }

        // BALÃO DE FALA NPC1
        if (dialogoAtivo) {

            TextureRegion frame;

            if (!jogo.npc1Completo) {
                frame = animacaoBalao.getKeyFrame(elapsedTime, true);
            } else if (jogo.puzzle1Completo && !jogo.npc1PosPuzzleFalou) {
                frame = animacaoBalao2.getKeyFrame(elapsedTime, true);
            } else {
                frame = animacaoBalao2.getKeyFrame(elapsedTime, true);
            }

            float larguraBalao = 800f;
            float alturaBalao  = 400f;

            float x = camera.position.x - larguraBalao / 2f;
            float y = camera.position.y + (alturaJanela / 2f) - alturaBalao - 50f;

            batch.draw(frame, x, y, larguraBalao, alturaBalao);
        }

        // BALÃO DE FALA NPC3
        if (dialogoNPC3) {

            TextureRegion frame;

            if (!jogo.npc3Falou) {
                frame = animacaoBalaoNPC3.getKeyFrame(elapsedTime, true);
            } else {
                frame = animacaoBalaoNPC3_2.getKeyFrame(elapsedTime, true);
            }

            float larguraBalao = 800f;
            float alturaBalao = 400f;

            float x = camera.position.x - larguraBalao / 2f;
            float y = camera.position.y + (alturaJanela / 2f) - alturaBalao - 50f;

            batch.draw(frame, x, y, larguraBalao, alturaBalao);
        }

        if (dialogoNPC2) {
            TextureRegion frame = animacaoBalaoNPC2.getKeyFrame(elapsedTime, true);

            float larguraBalao = 800f;
            float alturaBalao = 400f;

            // Segue o mesmo padrão de centralizar na câmara que já usas
            float x = camera.position.x - larguraBalao / 2f;
            float y = camera.position.y + (alturaJanela / 2f) - alturaBalao - 50f;

            batch.draw(frame, x, y, larguraBalao, alturaBalao);
        }

        if (npc2LiberouArvore && pertoDoNPC(800f, -30f)) {
            TextureRegion frameBotao = animacaoBotao.getKeyFrame(elapsedTime, true);

            float escala = 0.45f;
            float largura = frameBotao.getRegionWidth() * escala;
            float altura  = frameBotao.getRegionHeight() * escala;

            // Posiciona o botão centralizado no início do mapa (perto da árvore) e flutuando
            float x = 940f - (largura / 2f);
            float y = 550f;

            batch.draw(frameBotao, x, y, largura, altura);
        }

        if (exibirProcurado) {
            float larguraProcurado = 800f;
            float alturaProcurado  = 800f;

            float xProcurado = camera.position.x - (larguraProcurado / 2f);
            float yProcurado = camera.position.y - (alturaProcurado / 2f) + 200;

            batch.draw(procuradoImg, xProcurado, yProcurado, larguraProcurado, alturaProcurado);
        }

        // BOTÃO DE INTERAÇÃO NPC3 — original 4780 + 800 = 5580
        if (pertoDoNPC(npc3X, 400f)) {

            // 🔘 BOTÃO (continua normal)
            TextureRegion frameBotao = animacaoBotao.getKeyFrame(elapsedTime, true);

            float escala = 0.6f;
            float largura = frameBotao.getRegionWidth() * escala;
            float altura  = frameBotao.getRegionHeight() * escala;

            float x = 5580 + (100f / 2f) - (largura / 2f);
            float y = 510;

            batch.draw(frameBotao, x, y, largura, altura);

        } else if (!jogo.npc3Falou || (jogo.puzzle2Completo && !jogo.npc3PosPuzzleFalou)) {
            // original 4755 + 800 = 5555
            batch.draw(animacaoPonto2.getKeyFrame(elapsedTime, true),
                5555, 470, 150, 150);
        }

        // Placas do tutorial (ficam fixas no chão)
        batch.draw(placaD, 100, 190, 120, 120);
        batch.draw(placaE, 320, 190, 120, 120);
        batch.draw(placaI, 540, 190, 120, 120);

        // --- POP-UPS DAS PLACAS DO TUTORIAL (IMAGENS MAIORES) ---
        float popupLargura = 470;
        float popupAltura = 380;
        float popupX = camera.position.x - (popupLargura / 2f);
        float popupY = camera.position.y + 300f; // Exibido no alto da tela

        // Se passar por cima da placa D (Direita)
        if (centroRobo >= 100f && centroRobo <= 220f) {
            batch.draw(placaDTutorial, popupX, popupY, popupLargura, popupAltura);
        }
        // Se passar por cima da placa E (Esquerda)
        else if (centroRobo >= 320f && centroRobo <= 440f) {
            batch.draw(placaETutorial, popupX, popupY, popupLargura, popupAltura);
        }

        // Se estiver bloqueado na placa I (Interação)
        if (mostrandoPlacaI) {
            batch.draw(placaITutorial, popupX, popupY, popupLargura, popupAltura);

            // Desenha o botão de interação por cima do Pop-up da Placa I para ensinar o jogador a apertar!
            TextureRegion frameBotao = animacaoBotao.getKeyFrame(elapsedTime, true);
            float btnLargura = frameBotao.getRegionWidth() * 0.7f;
            float btnAltura  = frameBotao.getRegionHeight() * 0.7f;
            batch.draw(frameBotao, camera.position.x - (btnLargura / 2f), popupY - btnAltura - 20f, btnLargura, btnAltura);
        }

        // Robô
        // ---- DESENHO DO ROBÔ COM EFEITO DE PERSPECTIVA ----
        TextureRegion frameRobo;
        float drawX = roboX;
        float drawY = roboY;
        float drawWidth = tamanhoRobo;
        float drawHeight = tamanhoRobo;

        if (roboEntrando) {
            // Pega o frame da animação de entrada
            frameRobo = animacaoEntrando.getKeyFrame(tempoEntrando, true);

            // --- CÁLCULO DA PERSPECTIVA (NOVO) ---
            float duracaoTotal = 1.3f; // Tempo definido na lógica de transição
            // Cria um fator de progresso de 0.0 (início) a 1.0 (fim)
            // Usamos Math.min para garantir que não passe de 1.0
            float progresso = Math.min(1.0f, tempoEntrando / duracaoTotal);

            // 1. Diminuir tamanho (Escala): Começa em 100% e termina em 40% do tamanho original
            float escalaInicial = 1.0f;
            float escalaFinal = 0.6f; // Ajuste aqui o quão pequeno ele fica no final
            // Interpolação linear: valorInicial + (valorFinal - valorInicial) * progresso
            float escalaAtual = escalaInicial + (escalaFinal - escalaInicial) * progresso;

            drawWidth = tamanhoRobo * escalaAtual;
            drawHeight = tamanhoRobo * escalaAtual;

            // 2. Aumentar a altura Y (Subir): Começa na base e sobe um pouco para "entrar"
            float sobePixelFinal = 180f; // Quantos pixels ele sobe para dentro da porta
            drawY = roboY + (sobePixelFinal * progresso);

            // 3. Centralizar X: Como diminuímos a largura, precisamos ajustar o X
            // para que ele continue centralizado no meio da porta.
            float centroOriginalX = roboX + tamanhoRobo / 2f;
            drawX = centroOriginalX - drawWidth / 2f;

        } else if (esq) {
            frameRobo = animacaoEsquerda.getKeyFrame(elapsedTime, true);
        } else if (dir) {
            frameRobo = animacaoDireita.getKeyFrame(elapsedTime, true);
        } else {
            frameRobo = animacaoParado.getKeyFrame(elapsedTime, true);
        }

        // Desenha o robô com os parâmetros (X, Y, Largura, Altura) calculados
        batch.draw(frameRobo, drawX, drawY, drawWidth, drawHeight);

        // Portinhas — original 0/5600 + 800 = 800/6400
        batch.draw(portinhaImg,  800,  0, 800, alturaJanela - 259);
        batch.draw(portinha2Img, 6400, 0, 800, alturaJanela - 259);

        // Aviso AFK
        if (tempoAFK <= 5f && tempoAFK > 0f) {
            int seg = (int) Math.ceil(tempoAFK);
            String mensagemAFK = textosAFK[jogo.idioma] + seg;

            fonte.getData().setScale(2.5f);
            fonte.setColor(Color.WHITE);

            GlyphLayout layoutAFK = new GlyphLayout(fonte, mensagemAFK);

            float textoX = camera.position.x - (layoutAFK.width / 2f);
            float textoY = camera.position.y + 800f;

            fonte.draw(batch, mensagemAFK, textoX, textoY, layoutAFK.width, Align.center, false);

            fonte.getData().setScale(1f);
        }

        if (tempoMensagemBarreira > 0 && !dialogoAtivo && !dialogoNPC3 && !mostrandoPlacaI) {

            fonte.setColor(Color.WHITE);
            fonte.getData().setScale(2f);

            GlyphLayout layout = new GlyphLayout(fonte, mensagemBarreira);

            float textoX = camera.position.x - (layout.width / 2f);
            float textoY = camera.position.y + 800f;

            // Desenhando a sombra preta centralizada
            fonte.setColor(Color.BLACK);
            fonte.draw(batch, mensagemBarreira, textoX + 2f, textoY - 2f, layout.width, Align.center, false);

            // Desenhando o texto amarelo centralizado
            fonte.setColor(Color.WHITE);
            fonte.draw(batch, mensagemBarreira, textoX, textoY, layout.width, Align.center, false);
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
