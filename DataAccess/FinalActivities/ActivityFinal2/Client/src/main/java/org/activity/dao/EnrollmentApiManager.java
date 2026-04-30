package org.activity.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.activity.dto.enrollment.ReturnEnrollmentDto;
import org.activity.restapi.HttpResponse;
import org.activity.restapi.RestApiConnection;
import org.entities.Enrollment;
import org.hibernate.Session;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class EnrollmentApiManager {

    private final RestApiConnection conn;
    private final ObjectMapper mapper = new ObjectMapper();

    public EnrollmentApiManager(RestApiConnection conn) {
        this.conn = conn;
    }


    private static String enc(String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8);
    }

    public ReturnEnrollmentDto enrollStudent(String studentId, int courseId) throws IOException {
        String endpoint = "/enrollments?studentId=" + enc(studentId.trim()) + "&courseId=" + courseId;

        HttpResponse resp = conn.post(endpoint, "{}");

        if (resp.getStatusCode() < 200 || resp.getStatusCode() >= 300) {
            throw new IOException("Enroll failed: HTTP " + resp.getStatusCode() + " " + resp.getStatusMessage() + " -> " + resp.getBody());
        }

        return mapper.readValue(resp.getBody(), ReturnEnrollmentDto.class);
    }
}
