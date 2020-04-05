package home.yura.pivot_test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import com.conductor.stream.utils.OrderedStreamUtils;
import org.springframework.web.socket.WebSocketSession;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@RestController
public class MainController {
//
//    @Autowired
//    private RabbitTemplate template;

    @Autowired
    private RestTemplate restTemplate;

    Logger logger = LoggerFactory.getLogger(MainController.class);

    @RequestMapping("/test")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public TestModel greeting(@RequestParam(name="name", required=false, defaultValue="Yura") String name) {
        TestModel model = new TestModel(name, 28);
        logger.info("info string");

        //template.convertAndSend("hello-3", "Test");


        String res = restTemplate.execute("http://www.google.com", HttpMethod.GET, new RequestCallback() {
            @Override
            public void doWithRequest(ClientHttpRequest clientHttpRequest) throws IOException {
                logger.info("doWithRequest");
            }
        }, new ResponseExtractor<String>() {
            @Override
            public String extractData(ClientHttpResponse clientHttpResponse) throws IOException {
                logger.info("extractData");
                InputStream responseInputStream = clientHttpResponse.getBody();
                InputStreamReader isReader = new InputStreamReader(responseInputStream);
                //Creating a BufferedReader object
                BufferedReader reader = new BufferedReader(isReader);
                String str;
                try {
                    while((str = reader.readLine()) != null) {
                        logger.info("stream - " + str);
                    }
                } catch (IOException ex) {

                }
                return "some data";
            }
        });
        logger.info("res - " + res);


        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

//        ResponseEntity<Resource> responseEntity = restTemplate.exchange( "http://www.google.com", HttpMethod.GET, entity, Resource.class );
//
//        InputStream responseInputStream;
//        try {
//            responseInputStream = responseEntity.getBody().getInputStream();
//        }
//        catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        if (responseInputStream != null) {
//            InputStreamReader isReader = new InputStreamReader(responseInputStream);
//            //Creating a BufferedReader object
//            BufferedReader reader = new BufferedReader(isReader);
//            String str;
//            try {
//                while((str = reader.readLine()) != null) {
//                    logger.info("stream - " + str);
//                }
//            } catch (IOException ex) {
//
//            }
//        }


        logger.debug("debug string");
        return model;
    }

    @RequestMapping("/test2")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public StreamingResponseBody getCsv(HttpServletRequest r) {
        ClassLoader classLoader = new MainController().getClass().getClassLoader();

        InputStream readStrem;
        try {
            readStrem = classLoader.getResource("Test.csv").openStream();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Stream<Integer> stream = Stream.generate(() -> n.incrementAndGet()).limit(100);

        logger.debug("debug string");
        return (outputStream) -> {
            if (readStrem != null) {
                // StreamUtils.copy(readStrem, outputStream);

                Stream<String> lines = new BufferedReader(new InputStreamReader(readStrem)).lines();

                Stream<TestModel> models = lines.map(str -> {
                    String[] parts = str.split(",");
                    TestModel model = new TestModel(parts[0], 10);
                    model.setData(parts);
                    return model;
                });

//                lines.forEach(s -> {
//                    try {
//                        outputStream.write((s + "\n").getBytes());
//                    }
//                    catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                    //logger.info(Thread.currentThread().getId() + " string " + s.getName());
//                });

                //MyStreamSpliterator testStreamSpliterator = new MyStreamSpliterator(readStrem);
                //Stream<TestModel> myStream = StreamSupport.stream(testStreamSpliterator, false);

                //Stream<TestModel> myStream2 = myStream.collect(Collectors.groupingBy(TestModel::getItemName));

//                final Stream<List<TestModel>> contentGroupedByCategory =
//                        OrderedStreamUtils.groupBy(myStream, item -> item.getName());

                //StreamUtils.copy(myStream, outputStream);

//                contentGroupedByCategory.forEach(s -> {
//                    String res = s.get(0).getName();
//                    try {
//                        outputStream.write((res + " " + s.size() + "\n").getBytes());
//                    }
//                    catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                });

                models.forEach(s -> {
                    String res = s.getName();
                    try {
                        outputStream.write((res + "\n").getBytes());
                    }
                    catch (IOException e) {
                        throw new RuntimeException(e);
                    } finally {
                        s.dispose();
                    }
                    //logger.info(Thread.currentThread().getId() + " string " + s.getName());
                });
//                myStream.close();
//                ArrayList<TestModel> aaa = myStream.collect(Collectors.toCollection(ArrayList::new));
//
//                logger.info(Thread.currentThread().getId() + " done " + aaa.size());
//
//                aaa = null;


//                int byteCount = 0;
//                byte[] buffer = new byte[4096];
//
//                int bytesRead;
//                try {
//                    for(boolean var4 = true; (bytesRead = readStrem.read(buffer)) != -1; byteCount += bytesRead) {
//
//                        outputStream.write(buffer, 0, bytesRead);
//                        // logger.info(Thread.currentThread().getId() + " bytesRead " + bytesRead);
//                        Thread.sleep(10);
//                    }
//                }
//                catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }


                // outputStream.write("Done".getBytes());
                outputStream.flush();

                readStrem.close();
                lines.close();
                models.close();
            }
            outputStream.close();
        };
    }
}