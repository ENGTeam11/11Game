package com.mygdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;


public class game extends Game {
	public Music backgroundMusic; // Background music
	public Skin skin; // UI Skin

	@Override
	public void create() {

		// Background Music
		backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("music/background_music.mp3"));
		backgroundMusic.setLooping(true);
		backgroundMusic.setVolume(0.4f);
		backgroundMusic.play();

		//skins
		skin = new Skin();
		skin.addRegions(new TextureAtlas(Gdx.files.internal("metal/skin/metal-ui.atlas")));
		skin.add("font", new BitmapFont(Gdx.files.internal("metal/raw/font-export.fnt")));
		skin.load(Gdx.files.internal("metal/skin/metal-ui.json"));
		//setting screen to menu initially
		setScreen(new MenuScreen(this, skin));


	}
}
