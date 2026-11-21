package com.skillswap.controller;

import com.skillswap.entity.Skill;
import com.skillswap.service.SkillService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/skills")
public class SkillController {

    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @GetMapping
    public ResponseEntity<List<Skill>> getAllSkills() {
        return ResponseEntity.ok(skillService.getAllSkills());
    }

    @PostMapping
    public ResponseEntity<Skill> createSkill(@RequestBody Map<String, String> body) {
        String name = body.get("name");
        String category = body.get("category");

        if (name == null || name.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        Skill created = skillService.createSkill(name, category);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
