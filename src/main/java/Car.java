import com.opencsv.bean.CsvBindByName;

public class Car {

    @CsvBindByName(column = "Id")
    private int id;

    @CsvBindByName(column = "Marca")
    private String name;

    @CsvBindByName(column = "Anul de productie")
    private String year;

    @CsvBindByName(column = "Capacitatea")
    private String capacity;

    @CsvBindByName(column = "Kilometraj")
    private String km;

    @CsvBindByName(column = "Cutia de viteza")
    private String gearbox;

    @CsvBindByName(column = "Pret")
    private int price;

    public Car(int id, String name, String year, String capacity, String km, String gearbox, int price) {
        this.id = id;
        this.name = name;
        this.year = year;
        this.capacity = capacity;
        this.km = km;
        this.gearbox = gearbox;
        this.price = price;
    }

    public Car() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getKm() {
        return km;
    }

    public void setKm(String km) {
        this.km = km;
    }

    public String getGearbox() {
        return gearbox;
    }

    public void setGearbox(String gearbox) {
        this.gearbox = gearbox;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void display() {
        System.out.println(name + " " + year + " " + capacity + " " + km + " " + gearbox + " " + price);
    }

    @Override
    public String toString(){
        return id + "," + name + "," + year + "," + capacity + ","
                + km + "," + gearbox + "," + price;
    }

    public String toSql(){
        return id + ", " + "'" + name + "'" + ", " + "'" + year + "'" + ", " + "'" + capacity + "'" + ", "
                + "'" + km + "'" + ", " + "'" + gearbox + "'" + ", " + price;
    }

}
