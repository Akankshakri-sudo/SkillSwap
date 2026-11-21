package com.skillswap.config;

import com.skillswap.entity.Skill;
import com.skillswap.repository.SkillRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("!test")
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);
    private final SkillRepository skillRepository;

    public DataSeeder(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    @Override
    public void run(String... args) {
        log.info("Seeding skill master list...");

        List<Object[]> skills = List.of(
            new Object[]{"Java",                    "Programming"},
            new Object[]{"Python",                  "Programming"},
            new Object[]{"C++",                     "Programming"},
            new Object[]{"JavaScript",              "Programming"},
            new Object[]{"HTML",                    "Web Development"},
            new Object[]{"CSS",                     "Web Development"},
            new Object[]{"SQL",                     "Database"},
            new Object[]{"Data Structures and Algorithms", "Computer Science"},
            new Object[]{"Machine Learning",        "AI/ML"},
            new Object[]{"Artificial Intelligence", "AI/ML"},
            new Object[]{"Data Analytics",          "Data Science"},
            new Object[]{"Graphic Design",          "Design"},
            new Object[]{"UI/UX Design",            "Design"},
            new Object[]{"Communication Skills",    "Soft Skills"},
            new Object[]{"React",                   "Web Development"},
            new Object[]{"Spring Boot",             "Programming"},
            new Object[]{"Git & GitHub",            "Tools"},
            new Object[]{"Docker",                  "DevOps"},
            new Object[]{"Cloud Computing",         "DevOps"},
            new Object[]{"Cyber Security",          "Computer Science"},
            new Object[]{"Android Development",     "Mobile"},
            new Object[]{"iOS Development",         "Mobile"},
            new Object[]{"Public Speaking",         "Soft Skills"},
            new Object[]{"Technical Writing",       "Soft Skills"}
        );

        for (Object[] skillData : skills) {
            String name = (String) skillData[0];
            String category = (String) skillData[1];

            if (!skillRepository.existsByNameIgnoreCase(name)) {
                skillRepository.save(
                    Skill.builder()
                        .name(name)
                        .category(category)
                        .build()
                );
                log.debug("Seeded skill: {}", name);
            }
        }

        log.info("Skill seeding complete. Total skills: {}", skillRepository.count());
    }
}
