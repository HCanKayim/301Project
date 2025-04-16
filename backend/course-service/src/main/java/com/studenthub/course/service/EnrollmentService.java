package com.studenthub.course.service;

import com.studenthub.course.model.Course;
import com.studenthub.course.model.Enrollment;
import com.studenthub.course.model.EnrollmentStatus;
import com.studenthub.course.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * EnrollmentService, öğrenci kayıtları ile ilgili iş mantığını yönetir.
 * Bu sınıf, öğrenci kayıtlarını oluşturma, güncelleme, sorgulama ve iptal etme
 * işlemlerini gerçekleştirir.
 */
@Service // Bu sınıfın bir Spring servis bileşeni olduğunu belirtir.
@RequiredArgsConstructor // Final alanları otomatik olarak constructor ile enjekte eder.
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository; // EnrollmentRepository'yi enjekte eder.
    private final CourseService courseService; // CourseService'yi enjekte eder.

    /**
     * Bir öğrenciyi belirli bir derse kaydeder. Eğer ders doluysa hata fırlatır.
     * 
     * @param courseId  Kaydolunacak dersin kimliği.
     * @param studentId Kaydedecek öğrencinin kimliği.
     * @return Öğrencinin derse kaydı.
     * @throws RuntimeException Eğer ders doluysa bir hata fırlatır.
     */
    public Enrollment enrollInCourse(Long courseId, Long studentId) {
        // Derse ait bilgileri alır
        Course course = courseService.getCourse(courseId);

        // Eğer dersin kapasitesi dolmuşsa hata fırlatır
        if (course.getEnrollments().size() >= course.getCapacity()) {
            throw new RuntimeException("Course is full");
        }

        // Yeni bir kayıt nesnesi oluşturur
        Enrollment enrollment = new Enrollment();
        enrollment.setCourse(course); // Kayıt için dersi belirler
        enrollment.setStudentId(studentId); // Öğrencinin kimliğini belirler
        enrollment.setEnrollmentDate(LocalDateTime.now()); // Kayıt tarihini alır
        enrollment.setStatus(EnrollmentStatus.PENDING); // Kayıt durumu başlangıçta beklemede olur

        // Kayıt işlemini veritabanına kaydeder
        return enrollmentRepository.save(enrollment);
    }

    /**
     * Belirli bir öğrencinin tüm kayıtlarını döner.
     * 
     * @param studentId Öğrencinin kimliği.
     * @return Öğrencinin tüm kayıtlarının listesi.
     */
    public List<Enrollment> getStudentEnrollments(Long studentId) {
        return enrollmentRepository.findByStudentId(studentId);
    }

    /**
     * Belirli bir dersin tüm kayıtlarını döner.
     * 
     * @param courseId Dersin kimliği.
     * @return Derse ait tüm kayıtların listesi.
     */
    public List<Enrollment> getCourseEnrollments(Long courseId) {
        return enrollmentRepository.findByCourseId(courseId);
    }

    /**
     * Bir kaydın durumunu günceller.
     * 
     * @param enrollmentId Güncellenmek istenen kaydın kimliği.
     * @param status       Yeni durum değeri.
     * @return Güncellenmiş kayıt.
     */
    public Enrollment updateEnrollmentStatus(Long enrollmentId, EnrollmentStatus status) {
        // Verilen ID'ye sahip kaydı bulur
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        // Kaydın durumunu günceller
        enrollment.setStatus(status);

        // Güncellenmiş kaydı kaydeder
        return enrollmentRepository.save(enrollment);
    }

    /**
     * Belirli bir kaydı iptal eder.
     * 
     * @param enrollmentId İptal edilecek kaydın kimliği.
     */
    public void cancelEnrollment(Long enrollmentId) {
        // Kaydı veritabanından siler
        enrollmentRepository.deleteById(enrollmentId);
    }
}
