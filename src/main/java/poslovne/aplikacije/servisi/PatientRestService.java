package poslovne.aplikacije.servisi;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import poslovne.aplikacije.appointments.Patient;
import poslovne.aplikacije.repository.PatientRepository;

import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientRestService {

    private final PatientRepository repo;

    public PatientRestService(PatientRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Patient> list() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public Patient get(@PathVariable Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Patient not found: " + id));
    }
}
