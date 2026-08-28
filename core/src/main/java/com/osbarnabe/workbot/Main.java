package com.osbarnabe.workbot;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;

public class Main extends Game {
    public boolean npc1Completo = false;
    public boolean npc1Fase2Falou = false;
    public boolean npc3Fase2Falou = false;
    public boolean puzzle1Completo = false;
    public boolean npc1PosPuzzleFalou = false;
    public boolean npc3Liberado = false;
    public boolean puzzle2Completo = false;
    public boolean npc3Falou = false;
    public boolean npc3PosPuzzleFalou = false;

    public int idioma = 0;

    public AssetManager assets;

    @Override public void create() {
        assets = new AssetManager();
        setScreen(new MenuScreen(this));
    }
    @Override public void dispose() {
        super.dispose();
        assets.dispose();
    }
}
