package home.yura.pivot_test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Spliterator;
import java.util.function.Consumer;

public class MyStreamSpliterator implements Spliterator<TestModel> {

    private InputStream inputStream = null;

    private InputStreamReader isReader = null;

    private BufferedReader reader = null;

    public MyStreamSpliterator (InputStream inputStream) {
        this.inputStream = inputStream;
        this.isReader = new InputStreamReader(this.inputStream);
        this.reader = new BufferedReader(this.isReader );
    }

    @Override
    public boolean tryAdvance(Consumer<? super TestModel> action) {
        String str = null;
        try {
            str = reader.readLine();
        } catch (IOException ex) {

        }
        if (str != null) {
            String[] parts = str.split(",");
            TestModel model = new TestModel(parts[0], 10);
            model.setData(parts);
            action.accept(model);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Spliterator<TestModel> trySplit() {
        return null;
    }

    @Override
    public long estimateSize() {
        return 0;
    }

    @Override
    public int characteristics() {
        return ORDERED;
    }
}
