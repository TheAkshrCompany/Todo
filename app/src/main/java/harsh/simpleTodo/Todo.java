package harsh.simpleTodo;
public class Todo {
    private String title;
    private String date;
    public Todo(String date, String title) {
        this.title = title;
        this.date=date;
    }public String date() {
        return date;
    }
    public String getTitle() {
        return title;
    }

}
