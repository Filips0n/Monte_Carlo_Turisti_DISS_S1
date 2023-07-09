import java.io.FileWriter;
import java.io.IOException;

public class WriteToTxt {

    public WriteToTxt(EmpiricGenerator gen) {
        try {
            FileWriter writer = new FileWriter("random_numbers.txt");
            for (int i = 0; i < 100000; i++) {
                double randomValue = gen.getSample();
                writer.write(String.valueOf(randomValue));
                writer.write("\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
