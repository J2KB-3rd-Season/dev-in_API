package com.devin.dev.repository.subject;

import com.devin.dev.entity.post.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Long> {

    List<Subject> findByNameIn(List<String> name);
}
