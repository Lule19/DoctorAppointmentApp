package poslovne.aplikacije.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import poslovne.aplikacije.appointments.Appointment;
import poslovne.aplikacije.appointments.AppointmentStatus;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
	@Query("select count(a) from Appointment a where a.doctor.id = :doctorId and a.status in :statuses and a.startTime < :end and a.endTime > :start")
	long countOverlaps(@Param("doctorId") Long doctorId,
					 @Param("start") LocalDateTime start,
					 @Param("end") LocalDateTime end,
					 @Param("statuses") Collection<AppointmentStatus> statuses);

	@Query("select a from Appointment a where (:doctorId is null or a.doctor.id = :doctorId) and (:status is null or a.status = :status) and (:fromTs is null or a.startTime >= :fromTs) and (:toTs is null or a.endTime <= :toTs)")
	List<Appointment> findFiltered(@Param("doctorId") Long doctorId,
								   @Param("status") AppointmentStatus status,
								   @Param("fromTs") LocalDateTime fromTs,
								   @Param("toTs") LocalDateTime toTs);
}
