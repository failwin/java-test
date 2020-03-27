package home.yura.pivot_test;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class TestModel {

    private String name;

    private int age;

    TestModel(String name, int age) {
        this.name = name;
        this.age = age;
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
