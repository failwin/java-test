package home.yura.pivot_test;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class TestModel {

    private String name;

    private int age;

    private String[] data;

    TestModel(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public void setData(String[] data) {
        this.data = data;
    }

    public String getName() {
        return this.name;
    }

    public int getAge() {
        return this.age;
    }

    @JsonIgnore
    public String getFullName() {
        return this.name + " " + this.age;
    }
}
