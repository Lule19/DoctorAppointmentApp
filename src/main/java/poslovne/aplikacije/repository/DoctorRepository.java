package poslovne.aplikacije.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import poslovne.aplikacije.appointments.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}
