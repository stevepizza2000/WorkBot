package com.osbarnabe.workbot;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;

public class Main extends Game {
    public boolean npc1Completo = false;
    public boolean puzzle1Completo = false;
    public boolean npc1PosPuzzleFalou = false;
    public boolean npc3Liberado = false;
    public boolean puzzle2Completo = false;    // ← vem do main
    public boolean npc3Falou = false;          // ← vem do main
    public boolean npc3PosPuzzleFalou = false; // ← vem do main

    public int idioma = 0; // ← vem de linguas (0=PT, 1=ES, 2=EN)

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
