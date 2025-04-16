package com.studenthub.course.repository;

import com.studenthub.course.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * CourseRepository, Course (Ders) varlığı için veri erişim işlemlerini yönetir.
 * JpaRepository arayüzünü genişleterek temel CRUD işlemlerini sağlar
 * ve ayrıca özel sorgular için metotlar tanımlar.
 */
@Repository // Bu sınıfın bir Spring veri erişim bileşeni olduğunu belirtir.
public interface CourseRepository extends JpaRepository<Course, Long> {
    /**
     * Belirli bir öğretmenin verdiği derslerin listesini döner.
     *
     * @param teacherId Öğretmenin benzersiz kimliği.
     * @return Öğretmenin verdiği derslerin listesi.
     */
    List<Course> findByTeacherId(Long teacherId);

    /**
     * Belirli bir ders kodunun veritabanında zaten var olup olmadığını kontrol
     * eder.
     *
     * @param code Kontrol edilecek ders kodu.
     * @return Ders kodu mevcutsa true, değilse false.
     */
    boolean existsByCode(String code);
}
