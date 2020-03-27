package home.yura.pivot_test;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class MainController {

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public TestModel greeting(@RequestParam(name="name", required=false, defaultValue="Yura") String name) {
        TestModel model = new TestModel(name, 28);
        return model;
    }
}