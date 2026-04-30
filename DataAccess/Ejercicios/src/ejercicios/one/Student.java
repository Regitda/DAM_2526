package ejercicios.one;

class Student {

    Mark mark = new Mark();

    static class Mark {
        private int value;

        public void setValue(int mark) {
            this.value = mark;
        }

        public int getValue() {
            return value;
        }
    }

    Name name = new Name();

    static class Name {
        private String name;

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    boolean PassedClass(int mark) {
        return (mark >= 5);
    }
}
