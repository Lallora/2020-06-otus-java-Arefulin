package ru.otus.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.Iterables;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.dto.UserDto;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(value = { "id" })
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private int age;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
    private List<Phone> phones = new ArrayList<>();

    public User(String name, int age, Address address, List<Phone> phones) {
        this(0L, name, age, address, new ArrayList<>());
        setPhones(phones);
    }

    public void setPhones(Collection<Phone> phones) {
        this.phones.clear();
        phones.forEach(this::addPhone);
    }

    public void addPhone(Phone phone) {
        phone.setUser(this);
        phones.add(phone);
    }

    public UserDto toDto() {
        return new UserDto(id, name, age, address.getStreet(),
                phones.stream().map(Phone::getNumber).collect(Collectors.toList()));
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (object == null || getClass() != object.getClass())
            return false;

        User user = (User) object;

        if (id != user.id)
            return false;
        if (age != user.age)
            return false;
        if (!name.equals(user.name))
            return false;
        if (!address.equals(user.address))
            return false;
        return Iterables.elementsEqual(phones, user.phones);
    }

}

