package poslovne.aplikacije.servisi;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import poslovne.aplikacije.appointments.Doctor;
import poslovne.aplikacije.repository.DoctorRepository;

import java.util.List;

@RestController
@RequestMapping("/doctors")
public class DoctorRestService {

    private final DoctorRepository repo;

    public DoctorRestService(DoctorRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Doctor> list() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public Doctor get(@PathVariable Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Doctor not found: " + id));
    }
}
