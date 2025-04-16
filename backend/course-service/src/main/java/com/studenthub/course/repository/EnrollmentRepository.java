package com.studenthub.course.repository;

import com.studenthub.course.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * EnrollmentRepository, Enrollment (Kayıt) varlığı için veri erişim işlemlerini
 * yönetir.
 * JpaRepository arayüzünü genişleterek temel CRUD işlemlerini sağlar
 * ve özel sorgular için metotlar tanımlar.
 */
@Repository // Bu sınıfın bir Spring veri erişim bileşeni olduğunu belirtir.
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    /**
     * Belirli bir öğrenciye ait tüm kayıtların listesini döner.
     *
     * @param studentId Öğrencinin benzersiz kimliği.
     * @return Öğrencinin yaptığı tüm kayıtların listesi.
     */
    List<Enrollment> findByStudentId(Long studentId);

    /**
     * Belirli bir derse ait tüm kayıtların listesini döner.
     *
     * @param courseId Dersin benzersiz kimliği.
     * @return Derse ait tüm kayıtların listesi.
     */
    List<Enrollment> findByCourseId(Long courseId);
}
