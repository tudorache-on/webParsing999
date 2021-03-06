import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.sql.Connection;
import java.sql.DriverManager;

import java.io.*;
import java.sql.Statement;
import java.util.*;


public class Main {

    public ArrayList<Car> readCars(Integer i, ArrayList<Car> cars, int id) throws IOException{

        String page = i.toString();
        Document doc = Jsoup.connect("https://999.md/ro/list/transport/cars")
                .data("view_type", "short")
                .data("o_260_1", "776")
                .data("hide_duplicates", "yes")
                .data("r_6_2_unit", "eur")
                .data("page", page)
                .get();

        int count = 0;

        Element table = doc.select(".ads-list-table").first();
        Elements rows = table.select("tr");

        for (Element e : rows) {
            String name = e.select("a").text();
            name = name.replace("\"", "").replace("'", "`");
            String year = e.getElementsByClass("ads-list-table-col-3 feature-19").text();
            String price = "negociabil";
            int intPrice;
            String capacity = e.getElementsByClass("ads-list-table-col-4 feature-103").text();
            String km = e.getElementsByClass("ads-list-table-col-4 feature-104").text();
            if (capacity.isEmpty() || km.isEmpty()) continue;

            Elements columns = e.select("td");
            for (Element child : columns) {
                if (child.hasClass(" ads-list-table-price feature-2  "))
                    price = child.getElementsByClass(" ads-list-table-price feature-2  ").text();
            }
            if (price.equals("negociabil")) continue;
            else {
                price = price.replaceAll("[^0-9]", "");
                intPrice = Integer.parseInt(price);
            }
            String gearbox = e.getElementsByClass("ads-list-table-col-2 feature-101").text();

            count++;
            id++;
            cars.add( new Car(id, name, year, capacity, km, gearbox, intPrice));

        }

        if (count != 0)
            readCars(i + 1, cars, id);

        return cars;
    }

    public void CsvWrite(ArrayList<Car> cars, String path) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter("C:\\" + path);
        writer.println("Id,Marca,Anul de productie,Capacitatea,Kilometraj,Cutia de viteza,Pret");

        for (Car i : cars) {
            writer.println(i.toString());
        }
        writer.close();
    }

    public void CsvToSql(String tableName, String path) throws IOException {
        Connection connection;
        Statement stmt;

        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/postgres",
                            "postgres", "olanesti905");
            connection.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = connection.createStatement();

            String sql = "COPY " + tableName + " FROM 'C:\\" + path + "' " + " DELIMITER ',' CSV HEADER;";
            stmt.executeUpdate(sql);
            stmt.close();
            connection.commit();
            connection.close();

        } catch (Exception e) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
    }

    public void maxMinAvgCar(ArrayList<Car> cars) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Marca automobilului: ");
        String manufacturer = scanner.nextLine();
        System.out.print("Anul de productie a automobilului: ");
        String year = scanner.nextLine();

        try {
            Car max = cars.stream().filter(x -> x.getName().contains(manufacturer) && x.getYear().equals(year)).max(Comparator.comparing(Car::getPrice)).get();
            Car min = cars.stream().filter(x -> x.getName().contains(manufacturer) && x.getYear().equals(year)).min(Comparator.comparing(Car::getPrice)).get();
            Double avg = cars.stream().filter(x -> x.getName().contains(manufacturer) && x.getYear().equals(year)).mapToInt(Car::getPrice).average().getAsDouble();
            System.out.println("Automobilul cu pret maxim:");
            max.display();
            System.out.println("Automobilul cu pret minim:");
            min.display();
            System.out.printf("Pretul mediu al automobilelor:\n %.2f", avg);
        } catch (NoSuchElementException e) {
            System.out.println("Acest model nu a putut fi gasit!");
        }
    }


    public static void main(String[] args) throws IOException {

        Main main = new Main();
        ArrayList<Car> cars = new ArrayList<>();
        List<String[]> allCars;


//        cars = main.readCars(1, cars, 0);
//        main.CsvWrite(cars, "cars.csv");
        main.CsvToSql("carsV2" ,"carsV2.csv");

//        main.maxMinAvgCar(cars);
    }
}
