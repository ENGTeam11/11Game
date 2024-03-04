package com.mygdx;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class Map {
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer renderer;

    public Map(String path) {
        tiledMap = new TmxMapLoader().load(path);
        renderer = new OrthogonalTiledMapRenderer(tiledMap);
    }

    public void render(OrthographicCamera camera) {
        renderer.setView(camera);
        renderer.render();
    }

    public void update(float delta) {
        // Update logic for the map, if any (e.g., animated tiles)
    }

    public TiledMap getTiledMap() {
        return tiledMap;
    }

    // Add methods for collision detection, getting specific layers, etc.

    public void dispose() {
        tiledMap.dispose();
        renderer.dispose();
    }
}
