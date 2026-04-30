package ejercicios.two;

import java.util.HashSet;
import java.util.Set;

class Company {
    private String _name;

    public String getName() {
        return _name;
    }

    public void setName(String _name) {
        this._name = _name;
    }

    private final Set<Worker> _workforce = new HashSet<>();

    public void addWorkforce(Worker worker) {
        if (worker == null) {
            throw new IllegalArgumentException("Worker value cannot be null");
        }
        this._workforce.add(worker);
    }
    public void addWorkforce(Set<Worker> workers) {
        if (workers.isEmpty()) {
            throw new IllegalArgumentException("Worker list cannot be empty");
        }
        this._workforce.addAll(workers);
    }

    public Set<Worker> getWorkforce() {
        return this._workforce;
    }

    public void removeWorkforce(Worker worker) {
        if (this._workforce.isEmpty()) {
            throw new IllegalArgumentException("No workers in workforce");
        }
        if (this._workforce.contains(worker)) {
            this._workforce.remove(worker);
        } else {
            throw new IllegalArgumentException("Worker not found");
        }
    }
    public int getWorkforceAmount() {
        return this._workforce.size();
    }



    private final Set<Client> _clientList = new HashSet<>();

    public Set<Client> getClients(Set<Client> clients) {
        return _clientList;
    }
    public void addClient(Client client) {
        if (client == null) {
            throw new IllegalArgumentException("Client value cannot be null");
        }
        this._clientList.add(client);
    }
    public void addClient(Set<Client> client) {
        if (client.isEmpty()) {
            throw new IllegalArgumentException("Client list cannot be empty");
        }
        this._clientList.addAll(client);
    }
    public void removeClient(Client client) {
        if (this._clientList.isEmpty()) {
            throw new IllegalArgumentException("No clients in client list");
        }
        if (this._clientList.contains(client)) {
            this._clientList.remove(client);
        } else {
            throw new IllegalArgumentException("Client not found");
        }
    }

    public int getClientAmount() {
        return this._clientList.size();
    }

}
