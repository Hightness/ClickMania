import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;

public class Params {
    int DELAY, MAX_BULLETS, MAP_PADDING, num_enemy_archers, num_enemy_tanks, mainMenuCounter = 1;

    public Params(){
        getData();
    }

    public void getData() {
        try{
            Properties prop = new Properties();
            FileInputStream fis = new FileInputStream("conf/game_settings.properties");
            prop.load(fis);
            fis.close();
            this.DELAY = Integer.parseInt(prop.getProperty("DELAY"));
            this.MAX_BULLETS = Integer.parseInt(prop.getProperty("MAX_BULLETS"));
            this.MAP_PADDING = Integer.parseInt(prop.getProperty("MAP_PADDING"));
            this.num_enemy_archers = Integer.parseInt(prop.getProperty("num_enemy_archers"));
            this.num_enemy_tanks = Integer.parseInt(prop.getProperty("num_enemy_tanks"));
        }catch(IOException e){
            System.out.println("Error with loading properties.");
        }
    }
}
