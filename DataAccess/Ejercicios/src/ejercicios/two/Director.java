package ejercicios.two;

import java.util.HashSet;
import java.util.Set;

class Director extends Worker {
    //
    private String _category;

    public void setCategory(String category) {
        this._category = category;
    }

    public String getCategory() {
        return this._category;
    }

    private final Set<Worker> _subordinates = new HashSet<Worker>();

    public void setSupervises(Worker supervise) {
        this._subordinates.add(supervise);
    }

    public Set<Worker> getSubordinates() {
        if (this._subordinates.isEmpty()) {
            throw new IllegalArgumentException("No supervised workers found");
        }
        return this._subordinates;
    }

    public int getSubordinatesAmount() {
        return this._subordinates.size();
    }

    public void removeWorker(Worker worker) {
        if (this._subordinates.isEmpty()) {
            throw new IllegalArgumentException("No subordinates found");
        }
        if (this._subordinates.contains(worker)) {
            this._subordinates.remove(worker);
        } else {
            throw new IllegalArgumentException("Worker not found");
        }
    }
}
