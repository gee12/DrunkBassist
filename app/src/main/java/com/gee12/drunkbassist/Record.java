package com.gee12.drunkbassist;

/**
 * Created by Иван on 10.03.2015.
 */
public class Record implements Comparable<Record> {

    protected String name;
    protected int points;
    protected int degree;

    public Record() {}

    public Record(String name, int points, int degree) {
        this.name = name;
        this.points = points;
        this.degree = degree;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }

    public int getDegree() {
        return degree;
    }

    public static Record parse(String s) {
        if (s == null) return null;

        String[] parts = s.split(" ");
        if (parts.length < 3) return null;
        return new Record(parts[0], Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
    }

    public String toString() {
        return String.format("%s %d %d", name, points, degree);
    }

    @Override
    public int compareTo(Record another) {
        return another.getPoints() - points;
    }
}
