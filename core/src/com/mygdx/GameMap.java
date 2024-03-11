package com.mygdx;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class GameMap {
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer renderer;
    private float timeSinceLastToggle = 0;
    private boolean layerVisible = true; // Initial visibility of water2 layer

    public GameMap(String mapPath) {
        tiledMap = new TmxMapLoader().load(mapPath);
        renderer = new OrthogonalTiledMapRenderer(tiledMap);
    }

    public void render(OrthographicCamera camera) {
        renderer.setView(camera);
        renderer.render();
    }

    public void update(float delta) {
        timeSinceLastToggle += delta;
        if (timeSinceLastToggle >= 0.5f) {
            toggleLayerVisibility("water2");
            timeSinceLastToggle = 0;
        }
    }
    private void toggleLayerVisibility(String layerName) {
        TiledMapTileLayer layer = (TiledMapTileLayer)tiledMap.getLayers().get(layerName);
        if (layer != null) {
            layer.setVisible(layerVisible = !layerVisible);
        }
    }

    public void dispose() {
        tiledMap.dispose();
        renderer.dispose();
    }
}
