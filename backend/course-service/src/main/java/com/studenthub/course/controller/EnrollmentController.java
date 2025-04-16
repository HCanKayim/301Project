package com.studenthub.course.controller;

import com.studenthub.course.model.Enrollment;
import com.studenthub.course.model.EnrollmentStatus;
import com.studenthub.course.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Bu controller, Ders Kayıt (Enrollment) varlığı ile ilgili tüm HTTP
 * isteklerini yönetir.
 * Öğrencilerin derslere kaydolması, kayıt bilgilerini görüntüleme ve güncelleme
 * gibi işlemleri sağlar.
 */
@RestController
@RequestMapping("/api/enrollments") // Tüm isteklerin "/api/enrollments" ile başlamasını sağlar.
@RequiredArgsConstructor // final değişkenler için constructor otomatik olarak oluşturulur.
@CrossOrigin(origins = "*") // Farklı kaynaklardan gelen isteklere izin verir.
public class EnrollmentController {

    // EnrollmentService, iş mantığı katmanını temsil eder ve bu controller'da
    // kullanılır.
    private final EnrollmentService enrollmentService;

    /**
     * Bir öğrenciyi bir derse kaydeder.
     *
     * @param courseId  Kaydolunacak dersin kimliği.
     * @param studentId Kaydolacak öğrencinin kimliği.
     * @return Oluşturulan kayıt bilgileri ile HTTP 200 (OK) yanıtı.
     */
    @PostMapping("/{courseId}/student/{studentId}")
    public ResponseEntity<Enrollment> enrollInCourse(
            @PathVariable Long courseId,
            @PathVariable Long studentId) {
        return ResponseEntity.ok(enrollmentService.enrollInCourse(courseId, studentId));
    }

    /**
     * Belirli bir öğrencinin tüm kayıtlarını döner.
     *
     * @param studentId Öğrencinin benzersiz kimliği.
     * @return Öğrencinin kayıtları listesi ile HTTP 200 (OK) yanıtı.
     */
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Enrollment>> getStudentEnrollments(@PathVariable Long studentId) {
        return ResponseEntity.ok(enrollmentService.getStudentEnrollments(studentId));
    }

    /**
     * Belirli bir dersin tüm kayıtlarını döner.
     *
     * @param courseId Dersin benzersiz kimliği.
     * @return Derse ait kayıtlar listesi ile HTTP 200 (OK) yanıtı.
     */
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Enrollment>> getCourseEnrollments(@PathVariable Long courseId) {
        return ResponseEntity.ok(enrollmentService.getCourseEnrollments(courseId));
    }

    /**
     * Bir kaydın durumunu günceller.
     *
     * @param enrollmentId Güncellenecek kaydın kimliği.
     * @param status       Yeni kayıt durumu (ör. ACTIVE, CANCELLED).
     * @return Güncellenmiş kayıt bilgileri ile HTTP 200 (OK) yanıtı.
     */
    @PutMapping("/{enrollmentId}/status")
    public ResponseEntity<Enrollment> updateEnrollmentStatus(
            @PathVariable Long enrollmentId,
            @RequestParam EnrollmentStatus status) {
        return ResponseEntity.ok(enrollmentService.updateEnrollmentStatus(enrollmentId, status));
    }

    /**
     * Belirli bir kaydı iptal eder.
     *
     * @param enrollmentId İptal edilecek kaydın kimliği.
     * @return Boş bir HTTP 200 (OK) yanıtı.
     */
    @DeleteMapping("/{enrollmentId}")
    public ResponseEntity<Void> cancelEnrollment(@PathVariable Long enrollmentId) {
        enrollmentService.cancelEnrollment(enrollmentId);
        return ResponseEntity.ok().build();
    }
}
