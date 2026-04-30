package ejercicios.one;

import java.util.ArrayList;
import java.util.List;

class Students {
    private final List<Student> students = new ArrayList<>();

    public void Add(Student alu) {
        students.add(alu);
    }

    public Student Get(int num) {
        if (num >= 0 && num <= students.size()) {
            return (students.get(num));
        }
        return null;
    }

    public float Average() {

        if (students.isEmpty()) return 0;

        float media = 0;
        for (Student student : students) {
            media += (student.mark.getValue());
        }
        return (media / students.size());

    }
}