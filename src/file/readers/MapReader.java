package file.readers;

import constants.Constants;
import file.helpers.FastRGB;
import world.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapReader {

    public List<State> getStates(File imageFile, World world){

        int[][] matrix = FastRGB.getMatrix(imageFile);
        BufferedImage image = null;
        try {
            image = ImageIO.read(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int width = image.getWidth();
        int height = image.getHeight();

        //hashmap of colours
        HashMap<Integer, List<Integer>> neigbours = new HashMap<>();
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                neigbours.put(matrix[i][j], List.of());
            }
        }

        //border for colours
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                handleNeigbours(matrix[i][j], i, j, neigbours, matrix);
            }
        }

        //make into states
        List<State> states = new ArrayList<>();

        //temp
        Nation nation = new Nation("Fakistan", "Fakistani", world);
        RaceParameters germanRace = new RaceParameters(Constants.PROTESTANT, Constants.GERMANIC);
        JobParameters germanJob = new JobParameters(0, 0, 30, 10, 5, 15, 1, 2, 2, 2, 3, 1);
        int population = 1000;

        for(Integer stateColor : neigbours.keySet()){

            State state = new State(stateColor.toString(), nation, germanRace, germanJob, 1000);
            states.add(state);

            //DO LATER: read txt file that corelates to state colours so we can add information to the state
        }
        //DO LATER: addNeigbours

        //DO LATER: (distance calculation for colours?)

        return states;
    }

    private void handleNeigbours(int color, int i, int j, HashMap<Integer, List<Integer>> neigbours, int[][] matrix) {
        final int[] neigbourI = {-1, 0, 1,
                                 -1,    1,
                                 -1, 0, 1};
        final int[] neigbourJ = {-1, -1, -1,
                                  0,      0,
                                  1,  1,  1};
        List<Integer> curList = new ArrayList<>();

        for(int n = 0; n < 8; n++){
            try{
                if(color != matrix[neigbourI[n]+i][neigbourJ[n]+j] && !curList.contains(matrix[neigbourI[n]+i][neigbourJ[n]+j])){
                    curList.add(matrix[neigbourI[n]+i][neigbourJ[n]+j]);
                }
            }
            catch(NullPointerException|ArrayIndexOutOfBoundsException e){
                //DO LATER: map wrapping
            }
        }
        neigbours.put(color, curList);
    }
}
