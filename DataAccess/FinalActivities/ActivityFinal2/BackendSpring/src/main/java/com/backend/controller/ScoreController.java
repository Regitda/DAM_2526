package com.backend.controller;

import com.backend.dto.score.ScoreDto;
import com.backend.dto.score.UnmarkedScoreItem;
import com.backend.service.ScoreService;
import jakarta.validation.constraints.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@Validated
@RequestMapping("/scores")
public class ScoreController {

    private final ScoreService scoreService;

    public ScoreController(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    @GetMapping("/unmarked")
    public List<UnmarkedScoreItem> unmarked(
            @RequestParam @NotBlank @Size(max = 12) String studentId,
            @RequestParam @Min(1) int courseId
    ) {
        return scoreService.listUnmarkedScores(studentId.trim(), courseId);
    }

    @PostMapping("/{id}")
    public ScoreDto setScore(
            @PathVariable @Min(1) Integer id,
            @RequestParam @NotNull @Min(0) @Max(10) Integer value
    ) {
        return scoreService.setScore(id, value);
    }
}


