package com.osbarnabe.workbot;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;

public class Main extends Game {

    // Gerenciador de assets compartilhado por todo o jogo
    public AssetManager assets;

    @Override
    public void create() {
        assets = new AssetManager();
        setScreen(new MenuScreen(this));
    }

    @Override
    public void dispose() {
        super.dispose();
        assets.dispose();
    }
}
