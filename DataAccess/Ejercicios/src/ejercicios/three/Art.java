package ejercicios.three;

public class Art {

    public Art(String title, Author author, Room room) {
        _title = title;
        _author = author;
        _room = room;
    }

    private String _title;

    public void setTitle(String title){
        _title = title;
    }
    public String getTitle(){
        return _title;
    }

    private Author _author;

    public void setAuthor(Author author){
        _author = author;
    }
    public Author getAuthor(){
        return _author;
    }

    private Room _room;
    public Room getRoom(){
        return _room;
    }
    public void setRoom(Room room){
        _room = room;
    }
}

