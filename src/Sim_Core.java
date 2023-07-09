import java.util.Random;

public abstract class Sim_Core {
    protected final Random seedGenerator;
    protected int completedReplications;
    protected int replications;
    boolean isRunning = false;

    public Sim_Core(Random seedGenerator) {
        this.seedGenerator = seedGenerator;
    }

    public void simulate(int replications) {
        beforeSimulation();
        isRunning = true;
        this.replications = replications;
        this.completedReplications = replications;
        for (int i = 0; i < replications; i++) {
            beforeReplication();
            oneReplication();
            if (!isRunning) {
                this.completedReplications = i+1;
                break;
            }
            afterReplication(i);
        }
        afterSimulation();
    }

    protected abstract void afterSimulation();

    protected abstract void afterReplication(int i);

    protected abstract void beforeSimulation();

    protected abstract void oneReplication();

    protected abstract void beforeReplication();

    public void setRunning(boolean running) {
        isRunning = running;
    }
}
