package com.sp.sn.fm.repository;

import com.sp.sn.fm.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    Person findByPersonId(long id);

    Person findByEmail(String email);
}
