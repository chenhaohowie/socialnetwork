package com.sp.sn.fm.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Self-contained entity
 * @author chenhao
 */
@Entity
@Table(name = "PERSON")
public class Person {

    @Id
    @Column(name = "PERSON_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long personId;

    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;

    /**
     * friends that are initiated by this person
     */
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "FRIENDS",
            joinColumns = {@JoinColumn(name = "PERSON_ID")},
            inverseJoinColumns = {@JoinColumn(name = "FRIEND_ID")})
    private Set<Person> friends = new HashSet<>();

    /**
     * friends that are initiated by other persons
     */
    @ManyToMany(mappedBy = "friends")
    private Set<Person> inverseFriends = new HashSet<>();

    /**
     * subscriptors who subscribe to this person to receive updates
     */
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "SUBSCRIPTION",
            joinColumns = {@JoinColumn(name = "TARGET_ID")},
            inverseJoinColumns = {@JoinColumn(name = "REQUESTOR_ID")})
    private Set<Person> subscriptors = new HashSet<>();

    /**
     * notifiers from which this person is able to receive updates
     */
    @ManyToMany(mappedBy = "subscriptors")
    private Set<Person> notifiers = new HashSet<>();

    /**
     * persons being blocked by this person
     */
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "BLOCK",
            joinColumns = {@JoinColumn(name = "REQUESTOR_ID")},
            inverseJoinColumns = {@JoinColumn(name = "TARGET_ID")})
    private Set<Person> blockedPersons = new HashSet<>();

    /**
     * persons that are blocking this person
     */
    @ManyToMany(mappedBy = "blockedPersons")
    private Set<Person> blockingPersons = new HashSet<>();

    public long getPersonId() {
        return personId;
    }

    public void setPersonId(long personId) {
        this.personId = personId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Person> getFriends() {
        return friends;
    }

    public void setFriends(Set<Person> friends) {
        this.friends = friends;
    }

    public Set<Person> getInverseFriends() {
        return inverseFriends;
    }

    public void setInverseFriends(Set<Person> inverseFriends) {
        this.inverseFriends = inverseFriends;
    }

    public Set<Person> getSubscriptors() {
        return subscriptors;
    }

    public void setSubscriptors(Set<Person> subscriptors) {
        this.subscriptors = subscriptors;
    }

    public Set<Person> getNotifiers() {
        return notifiers;
    }

    public void setNotifiers(Set<Person> notifiers) {
        this.notifiers = notifiers;
    }

    public Set<Person> getBlockedPersons() {
        return blockedPersons;
    }

    public void setBlockedPersons(Set<Person> blockedPersons) {
        this.blockedPersons = blockedPersons;
    }

    public Set<Person> getBlockingPersons() {
        return blockingPersons;
    }

    public void setBlockingPersons(Set<Person> blockingPersons) {
        this.blockingPersons = blockingPersons;
    }

    /**
     * Implement hashCode against email field
     * @return int the computed hash
     */
    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    /**
     * Implement equals against email field, especially important if used in Set
     * @param object
     * @return boolean true if equal, false otherwise
     */

    @Override
    public boolean equals(final Object object) {
        if (this == object) return true;
        if (object instanceof Person) {
            return Objects.equals(this.email, ((Person)object).email);
        }
        return false;
    }
}
