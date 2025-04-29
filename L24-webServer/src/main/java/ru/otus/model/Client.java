package ru.otus.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "client")
public class Client implements Cloneable {

    @Id
    @SequenceGenerator(name = "client_gen", sequenceName = "client_seq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_gen")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "login", unique = true, nullable = false)
    private String login;

    @Column(name = "password", nullable = false)
    private String password;

    @OneToOne(
            fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @OneToMany(
            mappedBy = "client",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.EAGER)
    private List<Phone> phones = Collections.emptyList();

    public Client(String login, String password) {
        this.id = null;
        this.login = login;
        this.password = password;
    }

    public Client(Long id, String login, String password) {
        this.id = id;
        this.login = login;
        this.password = password;
    }

    @SuppressWarnings("this-escape")
    public Client(Long id, String name, String login, String password, Address address, List<Phone> phones) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.password = password;
        setAddress(address);
        setPhones(phones);
    }

    public void setAddress(Address address) {
        this.address = address;
        if (this.address != null) {
            this.address.setClient(this);
        }
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones != null ? new ArrayList<>(phones) : new ArrayList<>();
        this.phones.forEach(phone -> phone.setClient(this));
    }

    @Override
    @SuppressWarnings({"java:S2975", "java:S1182"})
    public Client clone() {
        return new Client(this.id, this.name, this.login, this.password, this.address, this.phones);
    }

    @Override
    public String toString() {
        return "Client{" + "id=" + id + ", login ='" + login + '\'' + '}';
    }
}
