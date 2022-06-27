import com.opencsv.CSVWriter;
import org.apache.commons.lang3.RandomUtils;

import java.io.FileWriter;
import java.io.IOException;


public class RandomCsv {
    public static void main(String[] args) {
        int n = 20000000;
        try {
            CSVWriter writer = new CSVWriter( new FileWriter("C:\\random.csv") );
            writer.writeNext(new String[] {"Id", "Marca", "Anul de productie", "Capacitatea", "Kilometraj", "Cutia de viteza", "Pret"});

            for (int i = 1; i < n; i++) {
                String year = Integer.toString(RandomUtils.nextInt(1990, 2020));
                String km = RandomUtils.nextInt(1, 200000) + " km";
                String price = Integer.toString(RandomUtils.nextInt(500, 20200));

                writer.writeNext(new String[] { Integer.toString(i), "name", year, "capacity", km, "aut.", price });
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
