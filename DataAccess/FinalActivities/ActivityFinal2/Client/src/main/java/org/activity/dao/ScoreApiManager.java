package org.activity.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.activity.dto.enrollment.ReturnEnrollmentDto;
import org.activity.dto.score.UnmarkedScoreItem;
import org.activity.restapi.HttpResponse;
import org.activity.restapi.RestApiConnection;
import org.entities.Enrollment;
import org.entities.Score;
import org.entities.Subject;
import org.hibernate.Session;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ScoreApiManager {

    private final RestApiConnection conn;
    private final ObjectMapper mapper = new ObjectMapper();


    public ScoreApiManager(RestApiConnection connection) {
        conn = connection;
    }

    private static String enc(String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8);
    }

    public List<UnmarkedScoreItem> getUnmarkedScores(String studentId, int courseId) throws IOException {
        String endpoint = "/scores/unmarked?studentId=" + enc(studentId.trim()) + "&courseId=" + courseId;

        HttpResponse resp = conn.getRequest(endpoint);

        if (resp.getStatusCode() < 200 || resp.getStatusCode() >= 300) {
            throw new IOException("Get unmarked scores failed: HTTP " + resp.getStatusCode()
                    + " " + resp.getStatusMessage() + " -> " + resp.getBody());
        }

        return mapper.readValue(resp.getBody(),
                new TypeReference<List<UnmarkedScoreItem>>() {});
    }

    public void saveScore(Score score) {
        session.persist(score);
    }

}
