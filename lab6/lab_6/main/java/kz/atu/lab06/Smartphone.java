package kz.atu.lab06;

public class Smartphone {
    private String model;
    private String brand;
    private int memory;
    private double price;

    public Smartphone(String model, String brand, int memory, double price) {
        this.model = model;
        this.brand = brand;
        this.memory = memory;
        this.price = price;
    }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public int getMemory() { return memory; }
    public void setMemory(int memory) { this.memory = memory; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}