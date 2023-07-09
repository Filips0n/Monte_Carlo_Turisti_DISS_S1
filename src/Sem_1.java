import java.util.Random;

public class Sem_1 {
    private static double[][] routeEC = {
            {230,243,0.3},
            {244,280,0.5},
//            {281,350,0.2},
            {281,490,0.2},
    };
    public static void main(String[] args) {
        Random seedGenerator = new Random();
        Monte_Carlo myMonteCarlo = new Monte_Carlo(seedGenerator);
        GUI gui = new GUI(myMonteCarlo);
//        myMonteCarlo.simulate(100000);

//        WriteToTxt wrt = new WriteToTxt(new EmpiricGenerator(seedGenerator, routeEC));
    }
}