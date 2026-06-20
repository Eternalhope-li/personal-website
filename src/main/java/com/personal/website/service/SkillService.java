package com.personal.website.service;

import com.personal.website.entity.Skill;
import com.personal.website.repository.SkillRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SkillService {
    private final SkillRepository repo;
    public SkillService(SkillRepository repo) { this.repo = repo; }

    public List<Skill> findAll() { return repo.findAllByOrderBySortOrderAsc(); }
    public Skill save(Skill s) { return repo.save(s); }
    public void delete(Long id) { repo.deleteById(id); }
}