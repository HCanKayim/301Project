package com.studenthub.course.service;

import com.studenthub.course.model.Course;
import com.studenthub.course.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * CourseService, dersler ile ilgili iş mantığını yönetir.
 * Bu sınıf, dersleri oluşturma, güncelleme, silme ve sorgulama işlemlerini
 * gerçekleştirir.
 */
@Service // Bu sınıfın bir Spring servis bileşeni olduğunu belirtir.
@RequiredArgsConstructor // Final alanları otomatik olarak constructor ile enjekte eder.
public class CourseService {

    private final CourseRepository courseRepository; // CourseRepository'yi enjekte eder.

    /**
     * Yeni bir ders oluşturur. Eğer ders kodu zaten varsa hata fırlatır.
     * 
     * @param course Oluşturulacak ders bilgilerini içeren Course nesnesi.
     * @return Oluşturulan ders.
     * @throws RuntimeException Eğer ders kodu zaten varsa bir hata fırlatır.
     */
    public Course createCourse(Course course) {
        // Ders kodunun veritabanında olup olmadığını kontrol eder
        if (courseRepository.existsByCode(course.getCode())) {
            throw new RuntimeException("Course with code " + course.getCode() + " already exists");
        }
        // Ders kodu benzersizse, yeni dersi kaydeder
        return courseRepository.save(course);
    }

    /**
     * Verilen ID'ye sahip dersi döner. Ders bulunamazsa hata fırlatır.
     * 
     * @param id Dersin benzersiz kimliği.
     * @return İlgili ders.
     * @throws RuntimeException Eğer ders bulunamazsa hata fırlatır.
     */
    public Course getCourse(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
    }

    /**
     * Tüm derslerin listesini döner.
     * 
     * @return Tüm derslerin listesi.
     */
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    /**
     * Belirli bir öğretmene ait derslerin listesini döner.
     * 
     * @param teacherId Öğretmenin kimliği.
     * @return Öğretmenin verdiği derslerin listesi.
     */
    public List<Course> getCoursesByTeacher(Long teacherId) {
        return courseRepository.findByTeacherId(teacherId);
    }

    /**
     * Var olan bir dersi günceller.
     * 
     * @param id     Güncellenecek dersin kimliği.
     * @param course Güncellenmiş ders bilgilerini içeren Course nesnesi.
     * @return Güncellenmiş ders.
     */
    public Course updateCourse(Long id, Course course) {
        // Güncellenmek istenen dersi alır
        Course existingCourse = getCourse(id);

        // Var olan dersin bilgilerini günceller
        existingCourse.setName(course.getName());
        existingCourse.setDescription(course.getDescription());
        existingCourse.setCapacity(course.getCapacity());

        // Güncellenmiş dersi kaydeder
        return courseRepository.save(existingCourse);
    }

    /**
     * Verilen ID'ye sahip dersi siler.
     * 
     * @param id Silinecek dersin kimliği.
     */
    public void deleteCourse(Long id) {
        // Dersin var olup olmadığını kontrol ettikten sonra siler
        courseRepository.deleteById(id);
    }
}
