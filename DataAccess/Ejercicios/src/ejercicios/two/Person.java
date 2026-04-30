package ejercicios.two;

import java.time.LocalDate;
import java.time.Period;

class Person {

    private String _name;

    public void setName(String name) {
        this._name = name;
    }

    public String getName() {
        return this._name;
    }

    private LocalDate _birthDate;

    public void setBirthDate(LocalDate birthDate) {
        if (birthDate == null) {
            throw new IllegalArgumentException("Birth date cannot be null");
        }
        LocalDate today = LocalDate.now();

        if (birthDate.isAfter(today)) {
            throw new IllegalArgumentException("Date of birth cannot be in the future");
        }
        this._birthDate = birthDate;
    }

    public LocalDate getBirthDate() {
        return this._birthDate;
    }

    public int getAge() {
        if (this._birthDate == null) {
            return 0;
        }
        ;
        return Period.between(this._birthDate, LocalDate.now()).getYears();
    }
}
