package org.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "scores", schema = "vtschool", uniqueConstraints = {
        @UniqueConstraint(name = "ur_scores", columnNames = {"enrollment_id", "subject_id"})
})
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "scores_seq_gen")
    @SequenceGenerator(
            name = "scores_seq_gen",
            sequenceName = "vtschool.scores_code_seq",
            allocationSize = 1
    )
    @Column(name = "code", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "enrollment_id", nullable = false)
    private Enrollment enrollment;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @Column(name = "score")
    private Integer score;

    public Score() {}

    public Score(Enrollment enrollment, Subject subject) {
        this.enrollment = enrollment;
        this.subject = subject;
        this.score = null;
    }

    public Score(Enrollment enrollment, Subject subject, Integer score) {
        this.enrollment = enrollment;
        this.subject = subject;
        this.score = score;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Enrollment getEnrollment() {
        return enrollment;
    }

    public void setEnrollment(Enrollment enrollment) {
        this.enrollment = enrollment;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

}