import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.postgresql.copy.CopyIn;
import org.postgresql.copy.CopyManager;
import org.postgresql.copy.PGCopyOutputStream;
import org.postgresql.core.BaseConnection;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args){

        Main main = new Main();
//        ArrayList<Car> cars = new ArrayList<>();

//        cars = main.readCars(1, 0, cars, "cars.csv");
//        main.csvWrite(cars, "cars.csv");
//        main.csvToSql("carsV2", "cars.csv");
//        main.maxMinAvgCar(cars);
    }

    private ArrayList<Car> readCars(Integer i, int id, ArrayList<Car> cars, String fileName) throws IOException {
        int count = 0;

        Document doc = getPageData(i);
        Elements rows = getTableData(doc);

        CSVWriter writer = new CSVWriter(new FileWriter("C:\\" + fileName));
        writer.writeNext(new String[]{"Id", "Marca", "Anul de productie", "Capacitatea", "Kilometraj", "Cutia de viteza", "Pret"});

        for (Element e : rows) {
            String name = e.select("a").text().replace("\"", "").replace("'", "`");
            String year = e.getElementsByClass("ads-list-table-col-3 feature-19").text();
            String capacity = e.getElementsByClass("ads-list-table-col-4 feature-103").text();
            String km = e.getElementsByClass("ads-list-table-col-4 feature-104").text();
            if (capacity.isEmpty() || km.isEmpty()) continue;
            km = km.replace("mi", "km");

            Elements columns = e.select("td");
            String price = "negociabil";
            int intPrice = 0;
            for (Element child : columns) {
                if (child.hasClass(" ads-list-table-price feature-2  "))
                    price = child.getElementsByClass(" ads-list-table-price feature-2  ").text().replaceAll("[^0-9]", "");
                intPrice = Integer.parseInt(price);
            }

            if (price.equals("negociabil")) continue;

            String gearbox = e.getElementsByClass("ads-list-table-col-2 feature-101").text();

            count++;
            id++;
            cars.add(new Car(id, name, year, capacity, km, gearbox, intPrice));
        }

        if (count != 0) readCars(i + 1, id, cars, fileName);
        return cars;
    }

    private void csvWrite(ArrayList<Car> cars, String path) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter("C:\\" + path);
        writeDataToCsv(cars, writer);
    }

    private void csvToSql(String tableName, String path) {
        try {
            Connection connection = createConnection();
            Writer writer = defineWriter(connection, tableName);

            CsvToBean<Car> bean = new CsvToBeanBuilder<Car>(new FileReader("C:\\" + path)).withType(Car.class).build();

            writeDataToSql(writer, bean, connection);

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    private void maxMinAvgCar(ArrayList<Car> cars) {
        String manufacturer = readManufacturer();
        String year = readYear();

        try {
            displayMaxPrice(cars.stream().filter(x -> x.getName().contains(manufacturer) && x.getYear().equals(year)).max(Comparator.comparing(Car::getPrice)).get());
            displayMinPrice(cars.stream().filter(x -> x.getName().contains(manufacturer) && x.getYear().equals(year)).min(Comparator.comparing(Car::getPrice)).get());
            displayAvgPrice(cars.stream().filter(x -> x.getName().contains(manufacturer) && x.getYear().equals(year)).mapToInt(Car::getPrice).average().getAsDouble());
        } catch (NoSuchElementException e) {
            System.out.println("Acest model nu a putut fi gasit!");
        }
    }


    private void displayMaxPrice(Car max) {
        System.out.println("Automobilul cu pret maxim:");
        max.display();
    }

    private void displayMinPrice(Car min) {
        System.out.println("Automobilul cu pret minim:");
        min.display();
    }

    private void displayAvgPrice(double avg) {
        System.out.printf("Pretul mediu al automobilelor:\n %.2f", avg);
    }

    private String readManufacturer() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Marca automobilului: ");
        return scanner.nextLine();
    }

    private String readYear() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Anul de productie a automobilului: ");
        return scanner.nextLine();
    }

    private Connection createConnection() throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "olanesti905");
        connection.setAutoCommit(false);
        return connection;
    }

    private Writer defineWriter(Connection connection, String tableName) throws SQLException {
        CopyManager copyManager = new CopyManager((BaseConnection) connection);
        CopyIn copyIn = copyManager.copyIn("COPY " + tableName + " FROM STDIN WITH CSV");
        return new OutputStreamWriter(new PGCopyOutputStream(copyIn), StandardCharsets.UTF_8);
    }

    private void writeDataToSql(Writer writer, CsvToBean<Car> bean, Connection connection) throws IOException, SQLException {
        for (Car car : bean) {
            String rawKm = car.getKm().replaceAll("[^0-9]", "");
            int km = Integer.parseInt(rawKm);
            writer.write(car + "," + Grade.compare(km) + "\n");
        }

        writer.close();
        connection.commit();
        connection.close();
    }

    private void writeDataToCsv(ArrayList<Car> cars, PrintWriter writer) {
        writer.println("Id,Marca,Anul de productie,Capacitatea,Kilometraj,Cutia de viteza,Pret");

        for (Car i : cars) {
            writer.println(i.toString());
        }
        writer.close();
    }

    private Document getPageData(Integer i) throws IOException {
        String page = i.toString();
        return Jsoup.connect("https://999.md/ro/list/transport/cars").data("view_type", "short").data("o_260_1", "776").data("hide_duplicates", "yes").data("r_6_2_unit", "eur").data("page", page).get();
    }

    private Elements getTableData(Document doc) {
        Element table = doc.select(".ads-list-table").first();
        assert table != null;
        return table.select("tr");
    }

}
