package ru.otus.core.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "age")
    private int age;

    @OneToOne(targetEntity = Address.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_address")
    private Address address;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Phone> phones;

    public User() {
    }

    public User(String name, int age, Address address, List<Phone> phones) {
        this.name = name;
        this.age = age;
        this.address = address;
        this.phones = phones;
        phones.stream().forEach(p -> p.setUser(this));
    }

    public User(String name, Address address) {
        this.name = name;
        this.address = address;
        this.phones = null;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Address getAddress() {
        return this.address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Phone> getPhones() {
        return this.phones;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
        for(Phone p : this.phones) {
            p.setUser(this);
        }
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for(Phone phone : this.phones) {
            s.append(phone.getNumber() + " ");
        }

        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age + '\'' +
                ", street' " + this.address.getStreet()  + '\'' +
                ", phones' " + s.toString() +
                '}';
    }

}

