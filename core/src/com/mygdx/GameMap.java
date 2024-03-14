package com.mygdx;

import com.badlogic.gdx.maps.MapGroupLayer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import java.util.HashMap;
import java.util.Map;

public class GameMap {
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer renderer;
    private float timeSinceLastToggle = 0;
    private Map<String, String[]> doorToRoofMap = new HashMap<>();
    private final float proximityThreshold = 5f; // Define a threshold for how close the player needs to be to interact

    public GameMap(String mapPath) {
        tiledMap = new TmxMapLoader().load(mapPath);
        renderer = new OrthogonalTiledMapRenderer(tiledMap, 1 / 0.18f); // Adjust the unit scale to match your game

        // Define the associations between door layers and roof layers
        doorToRoofMap.put("house_door", new String[]{"roof_house_1", "roof_house_2"});
        doorToRoofMap.put("res_door", new String[]{"roof_restaurant1", "roof_restaurant2"});
        doorToRoofMap.put("cs_door", new String[]{"roof_cs_1", "roof_cs_2"});
    }

    public void render(OrthographicCamera camera) {
        renderer.setView(camera);
        renderer.render();
    }

    public void update(float delta) {
        timeSinceLastToggle += delta;
        if (timeSinceLastToggle >= .75f) {
            toggleLayerVisibility("terrain", "water2");
            timeSinceLastToggle -= 0.75f; // Reset the timer, subtracting instead of setting to 0 to avoid any overflow
        }
    }

    public void toggleLayerVisibility(String groupName, String layerName) {
        MapLayer groupLayer = tiledMap.getLayers().get(groupName);

        if (groupLayer instanceof MapGroupLayer) {
            MapLayer layer = ((MapGroupLayer) groupLayer).getLayers().get(layerName);
            if (layer != null) {
                layer.setVisible(!layer.isVisible());
            }
        }
    }
    
    public void dispose() {
        tiledMap.dispose();
        renderer.dispose();
    }
}