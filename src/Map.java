import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class Map{
	Image background;
    int padding = 100;
	int[][] map; 

    Map(Image background){
        this.background = background;
        this.map = new int[background.getHeight(null)*10 + 2*padding][background.getWidth(null)*10 + 2*padding];
    }

    public Set checkCollisions(Entity entity, int tag){
        Set<Integer> tags = new HashSet<Integer>();
        for (int i = 0; i <= entity.size; i++){
            for (int j = 0; j <= entity.size; j++){
                if (map[(int)entity.pos.y + i + padding][(int)entity.pos.x + j + padding] != 0 && map[(int)entity.pos.y + i + padding][(int)entity.pos.x + j + padding] != tag){
                    tags.add(map[(int)entity.pos.y + i + padding][(int)entity.pos.x + j + padding]);
                }
            }
        }
        return tags;
    }

    public void update(Entity entity, int tag){
        for (int i = 0; i <= entity.size; i++){
            for (int j = 0; j <= entity.size; j++)
                map[(int)entity.pos.y + i + padding][(int)entity.pos.x + j + padding] = tag;
        }
    }
    public void delete(Entity entity, int tag){
        for (int i = 0; i <= entity.size; i++){
            for (int j = 0; j <= entity.size; j++){
                if (map[(int)entity.pos.y + i + padding][(int)entity.pos.x + j + padding] == tag)
                    map[(int)entity.pos.y + i + padding][(int)entity.pos.x + j + padding] = 0;
            }
        }
    }
}