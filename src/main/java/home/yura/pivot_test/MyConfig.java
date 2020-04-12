package home.yura.pivot_test;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "yura")
public class MyConfig {

    public int number;

    public String string;

    public void setNumber(int number) {
        this.number = number;
    }

    public void setString(String string) {
        this.string = string;
    }
}
