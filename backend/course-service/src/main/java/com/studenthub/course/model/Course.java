package com.studenthub.course.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * Course (Ders) varlığını temsil eden bir model sınıfıdır.
 * Bu sınıf, derslerin veritabanında nasıl saklanacağını tanımlar.
 */
@Data // Getter, Setter, toString, equals ve hashCode metodlarını otomatik olarak
      // oluşturur.
@Entity // Bu sınıfın bir JPA (Java Persistence API) varlığı olduğunu belirtir.
@Table(name = "courses") // Bu varlık için veritabanında "courses" adında bir tablo oluşturur.
public class Course {
    @Id // Bu alanın birincil anahtar olduğunu belirtir.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Birincil anahtarın otomatik olarak artacağını belirtir.
    private Long id; // Dersin benzersiz kimliği.

    private String code; // Dersin kodu (ör. "CS101").
    private String name; // Dersin adı.
    private String description; // Dersin açıklaması.
    private Integer capacity; // Dersin kapasitesi (maksimum öğrenci sayısı).
    private Long teacherId; // Dersi veren öğretmenin kimliği.

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    // Bu dersle ilişkilendirilmiş kayıtlar (Enrollments) ile bire-çok ilişki
    // tanımlanır.
    // mappedBy: İlişkinin "course" alanı tarafından yönetildiğini belirtir.
    // cascade: Ders üzerinde yapılan işlemlerin (örneğin silme) ilişkili kayıtlar
    // üzerinde de uygulanmasını sağlar.
    private Set<Enrollment> enrollments = new HashSet<>(); // Bu dersin kayıtlı öğrencilerinin listesi.
}
