package org.example.courier;

public class DeleteCourierResponse {
    private int id;

    public DeleteCourierResponse(int id) {
        this.id = id;
    }

    public DeleteCourierResponse() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
