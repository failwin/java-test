package home.yura.pivot_test;

public class TestRequest {
    private String id;

    private Object data;

    TestRequest() {
    }

    TestRequest(Object data) {
        this.data = data;
    }

    public Object getData() {
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
