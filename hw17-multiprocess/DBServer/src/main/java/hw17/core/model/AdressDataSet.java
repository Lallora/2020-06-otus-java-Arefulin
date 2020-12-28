package hw17.core.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "adress")
public class AdressDataSet {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", unique = true, nullable = false)
    private long id;

    @Column(name = "street")
    private String street;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public AdressDataSet() {
    }

    public AdressDataSet(String street) {
        this.street = street;
    }

    public long getId() {
        return id;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreet() {
        return street;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (int) (id ^ (id >>> 32));
        result = 31 * result + (street != null ? street.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        AdressDataSet object = (AdressDataSet) o;

        if (id != object.id)
            return false;
        return Objects.equals(street, object.street);
    }

    @Override
    public String toString() {
        return "AdressDataSet{" +
            "id = " + getId() +
            ", street = " + getStreet() +
            "}";
    }

}
