package home.yura.pivot_test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class MainController {

    Logger logger = LoggerFactory.getLogger(MainController.class);

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public TestModel greeting(@RequestParam(name="name", required=false, defaultValue="Yura") String name) {
        TestModel model = new TestModel(name, 28);
        logger.info("info string");
        logger.debug("debug string");
        return model;
    }
}