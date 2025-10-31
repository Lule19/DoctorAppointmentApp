package poslovne.aplikacije;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import poslovne.aplikacije.appointments.Doctor;
import poslovne.aplikacije.appointments.Patient;
import poslovne.aplikacije.repository.DoctorRepository;
import poslovne.aplikacije.repository.PatientRepository;

@Configuration
public class InitialLoad {
	private static final Logger log = LoggerFactory.getLogger(InitialLoad.class);

	@Bean
	public CommandLineRunner seedData(DoctorRepository doctors, PatientRepository patients) {
		return (args) -> {
			if (doctors.count() == 0) {
				doctors.save(new Doctor("Milan", "K.", "Opšta praksa"));
				doctors.save(new Doctor("Jelena", "S.", "Dermatolog"));
				log.info("Seeded Doctors");
			}
			if (patients.count() == 0) {
				patients.save(new Patient("Petar", "Petrović"));
				patients.save(new Patient("Ana", "Anić"));
				log.info("Seeded Patients");
			}
		};
	}
}
