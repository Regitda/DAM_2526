package ejercicios.two;

class Worker extends Person {
    private double _grossSalary;

    public void setGrossSalary(double grossSalary) {
        if (grossSalary <= 0) {
            throw new IllegalArgumentException("Gross salary can't be negative");
        }

        this._grossSalary = grossSalary;
    }

    public double getGrossSalary() {
        return this._grossSalary;
    }
}
