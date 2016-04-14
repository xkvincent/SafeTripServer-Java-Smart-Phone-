package com.crimekiller.safetrip.model;

import java.io.Serializable;

public class Post implements Serializable {

	private static final long serialVersionUID = 4540232621584474590L;

	public String date, licensePlate, destination, model, color, departure, owner;

    public Post () {};


    public Post(String date, String plate, String destination, String model, String color, String departure, String owner) {
        this.date = date;
        this.licensePlate = plate;
        this.destination = destination;
        this.model = model;
        this.color = color;
        this.departure = departure;
        this.owner = owner;
    }

    public String getDate() {
        return date;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public String getDestination() {
        return destination;
    }

    public String getModel() {
        return model;
    }

    public String getColor() {
        return color;
    }

    public String getDeparture() {
        return departure;
    }

    public String getOwner() {
        return owner;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setLicensePlate(String plate) {
        this.licensePlate = plate;
    }

    public void setDestination(String destination) {

        this.destination = destination;
    }

    public void setModel(String model) {

        this.model = model;
    }

    public void setColor(String color) {

        this.color = color;
    }

    public void setDeparture(String departure) {

        this.departure = departure;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

}
