import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class Map{
	Image background;
    int padding = 200;
	int[][] map; 

    Map(Image background){
        this.background = background;
        this.map = new int[background.getHeight(null)*10 + 2*padding][background.getWidth(null)*10 + 2*padding];
    }

    public Set checkCollisions(Entity entity, int tag){
        Set<Integer> tags = new HashSet<Integer>();
        ArrayList<Entity> entities = new ArrayList<>();

        for (int i = -(int)entity.repulsion_radius; i <= entity.size + (int)entity.repulsion_radius; i++){
            for (int j = -(int)entity.repulsion_radius; j <= entity.size + (int)entity.repulsion_radius; j++){
                int e = map[(int)entity.pos.y + i + padding][(int)entity.pos.x + j + padding];
                if (e != 0 && e != tag){
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