package sample.classes;

public class Book {

    private final String title;
    private final String teacher;
    private final String time;
    private final String rating;


    public Book(String title, String teacher, String time, String rating) {
        this.title = title;
        this.teacher = teacher;
        this.time = time;
        this.rating = rating;
    }

    public String getRating() {
        return rating;
    }

    public String getTitle() {
        return title;
    }

    public String getTeacher() {
        return teacher;
    }

    public String getTime() {
        return time;
    }
}