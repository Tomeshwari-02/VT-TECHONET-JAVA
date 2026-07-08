package com.meditrack.service;

public class ReportService {
    private final PatientService patientService;
    private final AppointmentService appointmentService;
    private final InventoryService inventoryService;
    private final BillingService billingService;

    public ReportService(PatientService patientService, AppointmentService appointmentService,
                         InventoryService inventoryService, BillingService billingService) {
        this.patientService = patientService;
        this.appointmentService = appointmentService;
        this.inventoryService = inventoryService;
        this.billingService = billingService;
    }

    public void showDashboard() {
        System.out.println();
        System.out.println("========== Clinic Report ==========");
        System.out.println("Total patients      : " + patientService.count());
        System.out.println("Total appointments : " + appointmentService.count());
        System.out.println("Total bills        : " + billingService.count());
        System.out.printf("Total revenue      : Rs.%.2f%n", billingService.totalRevenue());
        System.out.println("Low-stock medicines: " + inventoryService.countLowStock());
        System.out.println("===================================");
    }
}
