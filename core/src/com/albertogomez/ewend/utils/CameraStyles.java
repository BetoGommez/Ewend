package com.albertogomez.ewend.utils;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Class for fixing the player camera position on the map bounds
 * @author Alberto Gómez
 */
public class CameraStyles {

    /**
     * Sets the camera to player position
     * @param camera Game Camera
     * @param target Player position
     */
    public static void lerpToTarget(Camera camera,Vector2 target){
        Vector3 position = camera.position;
        position.x = camera.position.x +(target.x-camera.position.x)*.1f;
        position.y = camera.position.y +(target.y-camera.position.y)*.1f;
        camera.position.set(position);
        camera.update();
    }

    /**
     * Sets the camera position to the bounds, if they are exceded it doesn't move
     * @param @Camera Game Camera
     * @param startX Minimum x position
     * @param startY Minimum y position
     * @param width Max width
     * @param height Max height
     */
    public static void boundary(Camera camera,float startX,float startY, float width, float height){
        Vector3 position = camera.position;


        if(position.x<startX){
            position.x = startX;
        }

        if(position.y<startY){
            position.y = startY;
        }

        if(position.x>startX+width){
            position.x = startX+width;
        }

        if(position.y>startY+height){
            position.y = startY+height;
        }
        camera.position.set(position);
        camera.update();
    }
}
