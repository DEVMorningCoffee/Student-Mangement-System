package org.example.Enrollment;

import java.sql.Connection;
import java.util.UUID;

/*
 * Primary function of this class is to create a many-to-many relationship with student and course to easily manage.
 */

public class Enrollment {
    String enrollmentId;

    public Enrollment(Connection connection) {
        this.enrollmentId = UUID.randomUUID().toString();
    }

}
