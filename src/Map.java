import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class Map{
	Image background;
    int padding;
	Entity[][] map; 
    public int height, width;

    Map(Image background, int padding){
        this.padding = padding;
        this.background = background;
        height = background.getHeight(null)*10;
        width = background.getWidth(null)*10;
        map = new Entity[height + 2*padding][width + 2*padding];
    }

    public ArrayList checkCollisions(Entity entity, String type){
        ArrayList<Entity> entities = new ArrayList<>();
        int max_collisions = 4;
        for (int i = -entity.repulsion_radius; i <= entity.size + entity.repulsion_radius; i++){
            for (int j = -entity.repulsion_radius; j <= entity.size + entity.repulsion_radius; j++){
                if(entities.size() > max_collisions)return entities;
                Entity e = map[(int)entity.pos.y + i + padding][(int)entity.pos.x + j + padding];
                if (e != entity && e != null && type == entity.type)entities.add(e);
            }
        }
        return entities;
    }

    public ArrayList checkCollisions(Entity entity){
        return checkCollisions(entity, entity.type);
    }

    public void update(Entity entity){
        for (int i = 0; i <= entity.size; i++){
            for (int j = 0; j <= entity.size; j++)
                map[(int)entity.pos.y + i + padding][(int)entity.pos.x + j + padding] = entity;
        }
    }
    public void delete(Entity entity){
        for (int i = 0; i <= entity.size; i++){
            for (int j = 0; j <= entity.size; j++){
                Entity e = map[(int)entity.pos.y + i + padding][(int)entity.pos.x + j + padding];
                if (e == entity)map[(int)entity.pos.y + i + padding][(int)entity.pos.x + j + padding] = null;
            }
        }
    }
}