package com.mygdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;


public class game extends Game {
	//variables
	public Skin skin;


	@Override
	public void create() {

		//skins
		skin = new Skin();
		skin.addRegions(new TextureAtlas(Gdx.files.internal("metal/skin/metal-ui.atlas")));
		skin.add("font", new BitmapFont(Gdx.files.internal("metal/raw/font-export.fnt")));
		skin.load(Gdx.files.internal("metal/skin/metal-ui.json"));
		//setting screen to menu initially
		setScreen(new MenuScreen(this, skin));


	}
}
