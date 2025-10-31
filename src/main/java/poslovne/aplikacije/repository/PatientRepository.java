package poslovne.aplikacije.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import poslovne.aplikacije.appointments.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}
