package ejercicios.two;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

class Client extends Person {
    private String _phone;
    private static final Pattern PHONE_PATTERN = Pattern.compile("^((\\+|00)\\d{2,3})?\\d{9}$");

    private Set<Company> _clientOfCompany = new HashSet<>();
    //Not used? Not sure.


    public void setPhone(String phone) {
        if (phone != null && PHONE_PATTERN.matcher(phone).matches()) {
            this._phone = phone;
        } else {
            throw new IllegalArgumentException("Invalid phone number");
        }
    }

    public String getPhone() {
        return Objects.requireNonNullElse(this._phone, "None saved");
    }
}
