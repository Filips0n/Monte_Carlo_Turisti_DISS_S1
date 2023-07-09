import java.util.Random;

public class Monte_Carlo extends Sim_Core{

    private static double[][] routeCM = {
            {3,10,0.2},
            {11,20,0.2},
            {21,34,0.3},
            {35,52,0.1},
            {53,59,0.15},
            {60,95,0.03},
            {96,110,0.02},
    };
    private static double[][] routeEC = {
            {230,243,0.3},
            {244,280,0.5},
//            {281,350,0.2},
            {281,490,0.2},
    };

    private Random routeABGen;
    private int routeBCGen = 57;
    private EmpiricGenerator routeCMGen;
    private Random routeDEGen;
    private EmpiricGenerator routeECGen;

    private int success = 0;
    long waiting = 0;
    private GUI gui;
    private double probability;
    private double averageWaitingTime;
    public Monte_Carlo(Random seedGenerator) {
        super(seedGenerator);

    }

    public double getProbability() {
        return probability;
    }

    public double getAverageWaitingTime() {
        return averageWaitingTime;
    }

    @Override
    protected void afterSimulation() {
        this.probability = ((double)this.success/this.completedReplications)*100;
        this.averageWaitingTime = ((double)this.waiting/this.completedReplications);
        System.out.println(probability + "%");
        System.out.println(averageWaitingTime);
        this.gui.setLblProbability(probability);
        this.gui.setLblAvgWaitTime(averageWaitingTime);

        this.gui.numberOfPoints();
    }

    @Override
    protected void afterReplication(int i) {
        if (i % ((this.replications - this.replications*0.3)/1000) == 0 &&
                i>this.replications*0.3) {
            this.gui.addPoint((double)this.success/i,i);
        }
    }

    @Override
    protected void beforeSimulation() {
        success = 0;
        waiting = 0;
        //gen
        this.routeABGen = new Random(seedGenerator.nextInt());
        this.routeCMGen = new EmpiricGenerator(seedGenerator, routeCM);
        this.routeDEGen = new Random(seedGenerator.nextInt());
        this.routeECGen = new EmpiricGenerator(seedGenerator, routeEC);
    }

    @Override
    protected void beforeReplication() {}

    @Override
    protected void oneReplication() {
        //A -> AB-BC-CM
        //B -> DE-EC-CM
        //Strtnutie 13:00 -> 780
        double arrivalA;
        double arrivalB;

        //1
        int startA = 655;
        arrivalA = startA + (routeABGen.nextInt(64-39+1)+39) + routeBCGen + routeCMGen.getSample();
        if (arrivalA > 780) {
            waiting += arrivalA - 780;
        }

        //2
        int startB = 460;
        arrivalB = startB + (19 + routeDEGen.nextDouble() * (36 - 19)) + routeECGen.getSample() + routeCMGen.getSample();
        if (arrivalB < 780) {
            success++;
        }
    }

    public void setGUI(GUI gui) {
        this.gui = gui;
    }
}
