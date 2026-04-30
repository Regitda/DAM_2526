package ejercicios.three.ArtworkType;

import ejercicios.three.Art;
import ejercicios.three.Author;
import ejercicios.three.Room;

public class Writing extends Art {

        private String _material;

        public Writing(String title, Author author, Room room, String material, String style) {
            super(title, author, room);
            _material = material;
            _style = style;
        }

        public String getMaterial() {
            return _material;
        }

        public void setMaterial(String material) {
            _material = material;
        }

        private String _style;

        public String getStyle() {
            return _style;
        }

        public void setStyle(String style) {
            _style = style;
        }

}
