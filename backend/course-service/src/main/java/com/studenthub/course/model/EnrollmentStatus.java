package com.studenthub.course.model;

/**
 * EnrollmentStatus (Kayıt Durumu) enum sınıfı,
 * bir kaydın durumunu belirtmek için kullanılan sabit değerleri tanımlar.
 */
public enum EnrollmentStatus {
    /**
     * Kayıt işlemi beklemede.
     */
    PENDING,

    /**
     * Kayıt onaylanmış.
     */
    APPROVED,

    /**
     * Kayıt reddedilmiş.
     */
    REJECTED,

    /**
     * Kayıt tamamlanmış.
     */
    COMPLETED
}
