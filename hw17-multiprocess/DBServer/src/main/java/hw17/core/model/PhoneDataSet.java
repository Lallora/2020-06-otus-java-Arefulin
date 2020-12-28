package hw17.core.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "phones")
public class PhoneDataSet {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private String number;

    public PhoneDataSet() {
    }

    public PhoneDataSet(String number) {
        this.number = number;
    }

    public Long getId() {
        return id;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "PhoneDataSet{" + "id = " + id + ", number = " + getNumber() + "}";
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof PhoneDataSet))
            return false;
        return id != null && id.equals(((PhoneDataSet) o).getId());
    }
}
