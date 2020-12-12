package ru.otus.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import ru.otus.codec.PhoneDeserializer;
import ru.otus.codec.PhoneSerializer;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@JsonDeserialize(using = PhoneDeserializer.class)
@JsonSerialize(using = PhoneSerializer.class)
public class Phone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String number;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
    @JoinColumn(name = "user_id")
    private User user;

    public Phone(String number) {
        this.id = 0;
        this.number = number;
    }

    public static List<Phone> single(String phone) {
        return new ArrayList<>(Collections.singletonList(new Phone(phone)));
    }
}
