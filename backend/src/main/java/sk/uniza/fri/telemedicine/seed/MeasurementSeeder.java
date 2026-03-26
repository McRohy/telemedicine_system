package sk.uniza.fri.telemedicine.seed;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import sk.uniza.fri.telemedicine.entity.*;
import sk.uniza.fri.telemedicine.enumeration.Frequency;
import sk.uniza.fri.telemedicine.enumeration.MeasurementStatus;
import sk.uniza.fri.telemedicine.repository.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Inserts default data when the application starts.
 * Runs after DataSeeder (Order 3) and only adds data if the tables are empty.
 * Seeds measurement  plan and records for the demo patient.
 */
@Component
@Order(3)
public class MeasurementSeeder  implements CommandLineRunner {

    private final PatientRepository patientRepository;
    private final MeasurementPlanRepository measurementPlanRepository;
    private final TypeOfMeasurementRepository typeOfMeasurementRepository;
    private final MeasurementTimePlanRepository measurementTimePlanRepository;
    private final MeasurementTypePlanRepository measurementTypePlanRepository;
    private final MeasurementRecordRepository measurementRecordRepository;

    public MeasurementSeeder(PatientRepository patientRepository, MeasurementPlanRepository measurementPlanRepository,
                             MeasurementTimePlanRepository measurementTimePlan, MeasurementTypePlanRepository measurementTypePlan,
                             MeasurementRecordRepository measurementRecordRepository, TypeOfMeasurementRepository typeOfMeasurementRepository) {
        this.patientRepository = patientRepository;
        this.measurementPlanRepository = measurementPlanRepository;
        this.measurementTimePlanRepository = measurementTimePlan;
        this.measurementTypePlanRepository = measurementTypePlan;
        this.measurementRecordRepository = measurementRecordRepository;
        this.typeOfMeasurementRepository = typeOfMeasurementRepository;
    }

    /**
     * Runs data seeding at application startup.
     */
    @Override
    public void run(String... args) {
        seedMeasurementPlan();
        seedMeasurementRecords();
    }

    private void seedMeasurementPlan() {
        if (measurementPlanRepository.count() > 0) return;

       Patient patient = patientRepository.findById("0404311234")
               .orElseThrow(() -> new RuntimeException("Patient not found"));

        MeasurementPlan plan = new MeasurementPlan();
        plan.setPatient(patient);
        plan.setFrequency(Frequency.ONE_TIME_DAILY);
        plan.setValidFrom(LocalDateTime.now());
        measurementPlanRepository.save(plan);

        MeasurementTimePlan timePlan = new MeasurementTimePlan();
        timePlan.setMeasurementPlan(plan);
        timePlan.setTime(LocalTime.now().withHour(7).withMinute(30).withSecond(0).withNano(0));
        measurementTimePlanRepository.save(timePlan);

        TypeOfMeasurement typeOfMeasurement = typeOfMeasurementRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Type of measurement not found"));

        MeasurementTypePlan typePlan = new MeasurementTypePlan();
        typePlan.setMeasurementPlan(plan);
        typePlan.setTypeOfMeasurement(typeOfMeasurement);
        measurementTypePlanRepository.save(typePlan);
    }

    private void seedMeasurementRecords() {
        if (measurementRecordRepository.count() > 0) return;

        Patient patient = patientRepository.findById("0404311234")
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        TypeOfMeasurement typeOfMeasurement = typeOfMeasurementRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Type of measurement not found"));

        for (int i = 0; i < 20; i++) {
            MeasurementRecord record = new MeasurementRecord();
            record.setPatient(patient);
            record.setTypeOfMeasurement(typeOfMeasurement);
            record.setTimeOfMeasurement(LocalDateTime.now().minusDays(20 - i)
                    .withHour(7).withMinute((int)(Math.random() * 60)));

            if(i % 5 == 0) {
                record.setValue(Math.round((37.5 + Math.random() * 1.5) * 10.0) / 10.0);
                record.setMeasurementStatus(MeasurementStatus.ABNORMAL);
                record.setNote("Zle mi bolo celú noc");
            } else {
                record.setValue(Math.round((36.0 + Math.random() * 0.9) * 10.0) / 10.0);
                record.setMeasurementStatus(MeasurementStatus.NORMAL);
                if (i % 4 == 0) {
                    record.setNote("všetko v poriadku");
                }
            }
            measurementRecordRepository.save(record);
        }
    }
}
