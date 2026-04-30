package ejercicios.one;


class StudentFTC extends Student {

    Company company = new Company();

    static class Company {
        private String name;

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    Tutor tutor = new Tutor();

    static class Tutor {
        private String name;

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    Instructor instructor = new Instructor();

    static class Instructor {
        private String name;

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
