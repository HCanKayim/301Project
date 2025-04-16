package com.studenthub.course.controller;

import com.studenthub.course.model.Course;
import com.studenthub.course.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Bu controller, Course (Ders) varlığı ile ilgili tüm HTTP isteklerini yönetir.
 * Ders oluşturma, okuma, güncelleme ve silme gibi işlemler için uç noktalar
 * sağlar.
 */
@RestController
@RequestMapping("/api/courses") // Tüm isteklerin "/api/courses" ile başlamasını sağlar.
@RequiredArgsConstructor // final değişkenler için constructor otomatik olarak oluşturulur.
@CrossOrigin(origins = "*") // Farklı kaynaklardan gelen isteklere izin verir.
public class CourseController {

    // CourseService, iş mantığı katmanını temsil eder ve bu controller'da
    // kullanılır.
    private final CourseService courseService;

    /**
     * Yeni bir ders oluşturur.
     *
     * @param course Oluşturulacak ders bilgileri.
     * @return Oluşturulan ders bilgileri ile HTTP 200 (OK) yanıtı.
     */
    @PostMapping
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        return ResponseEntity.ok(courseService.createCourse(course));
    }

    /**
     * Belirli bir dersin bilgilerini döner.
     *
     * @param id Dersin benzersiz kimliği.
     * @return İstenen ders bilgileri ile HTTP 200 (OK) yanıtı.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourse(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.getCourse(id));
    }

    /**
     * Tüm derslerin listesini döner.
     *
     * @return Dersler listesi ile HTTP 200 (OK) yanıtı.
     */
    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    /**
     * Belirli bir öğretmene ait derslerin listesini döner.
     *
     * @param teacherId Öğretmenin benzersiz kimliği.
     * @return Öğretmene ait dersler listesi ile HTTP 200 (OK) yanıtı.
     */
    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<Course>> getCoursesByTeacher(@PathVariable Long teacherId) {
        return ResponseEntity.ok(courseService.getCoursesByTeacher(teacherId));
    }

    /**
     * Belirli bir dersi günceller.
     *
     * @param id     Güncellenecek dersin kimliği.
     * @param course Yeni ders bilgileri.
     * @return Güncellenmiş ders bilgileri ile HTTP 200 (OK) yanıtı.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable Long id, @RequestBody Course course) {
        return ResponseEntity.ok(courseService.updateCourse(id, course));
    }

    /**
     * Belirli bir dersi siler.
     *
     * @param id Silinecek dersin kimliği.
     * @return Boş bir HTTP 200 (OK) yanıtı.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.ok().build();
    }
}
