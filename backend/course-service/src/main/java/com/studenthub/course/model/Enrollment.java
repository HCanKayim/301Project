package com.studenthub.course.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Enrollment (Kayıt) varlığını temsil eden bir model sınıfıdır.
 * Bu sınıf, öğrencilerin derslere kayıtlarını ve kayıt durumlarını tanımlar.
 */
@Data // Getter, Setter, toString, equals ve hashCode metodlarını otomatik olarak
      // oluşturur.
@Entity // Bu sınıfın bir JPA (Java Persistence API) varlığı olduğunu belirtir.
@Table(name = "enrollments") // Bu varlık için veritabanında "enrollments" adında bir tablo oluşturur.
public class Enrollment {
    @Id // Bu alanın birincil anahtar olduğunu belirtir.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Birincil anahtarın otomatik olarak artacağını belirtir.
    private Long id; // Kaydın benzersiz kimliği.

    @ManyToOne // Kayıt ile ders arasında çoktan-bire ilişki tanımlar.
    @JoinColumn(name = "course_id") // Bu alan, ilgili dersin kimliğini saklayan bir yabancı anahtar sütununu
                                    // belirtir.
    private Course course; // Bu kayıtla ilişkili ders.

    private Long studentId; // Kayıtlı öğrencinin kimliği.
    private LocalDateTime enrollmentDate; // Kayıt tarihi ve saati.

    @Enumerated(EnumType.STRING) // Bu alanın Enum değerlerinin adlarını (STRING) veritabanında saklayacağını
                                 // belirtir.
    private EnrollmentStatus status; // Kaydın durumu (ör. ACTIVE, CANCELLED).
}
