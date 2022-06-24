import com.opencsv.CSVWriter;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class RandomCsv {
    public static void main(String[] args) {
        //ArrayList<Car> cars = new ArrayList<>();

        int n = 20000000;



        try {
//            PrintWriter writer = new PrintWriter("C:\\random.csv");
//            writer.println("Id,Marca,Anul de productie,Capacitatea,Kilometraj,Cutia de viteza,Pret");
//
//            for (Car i : cars) {
//                writer.println(i.toString());
//            }
//
//            writer.close();

            CSVWriter writer = new CSVWriter( new FileWriter("C:\\random.csv") );
            writer.writeNext(new String[] {"Id", "Marca", "Anul de productie", "Capacitatea", "Kilometraj", "Cutia de viteza", "Pret"});

            for (int i = 1; i < n; i++) {
                String name = "name";
                String year = Integer.toString(RandomUtils.nextInt(1990, 2020));
                String capacity = "capacity";
                String km = RandomUtils.nextInt(1, 200000) + " km";
                String gearbox = "aut.";
                String price = Integer.toString(RandomUtils.nextInt(500, 20200));

                writer.writeNext(new String[] { Integer.toString(i), name, year, capacity, km, gearbox, price });
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
