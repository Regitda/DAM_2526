package ejercicios.three.ArtworkType;

import ejercicios.three.Art;
import ejercicios.three.Author;
import ejercicios.three.Room;

public class Painting extends Art {


    public Painting(String title, Author author, Room room,  PaintingType paintingType, String format) {
        super(title, author, room);
        _paintingType = paintingType;
        _format = format;
    }

    private PaintingType _paintingType;

    public PaintingType getPaintingType() {
        return _paintingType;
    }

    public void setPaintingType(PaintingType paintingType) {
        _paintingType = paintingType;
    }

    private String _format;

    public String getFormat() {
        return _format;
    }

    public void setFormat(String style) {
        _format = style;
    }
}
