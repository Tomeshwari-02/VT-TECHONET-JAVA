package com.meditrack;

import com.meditrack.service.AppointmentService;
import com.meditrack.service.BillingService;
import com.meditrack.service.InventoryService;
import com.meditrack.service.PatientService;
import com.meditrack.service.ReportService;
import com.meditrack.util.InputUtil;

public class Main {
    private final InputUtil input = new InputUtil();
    private final PatientService patientService = new PatientService();
    private final AppointmentService appointmentService = new AppointmentService(patientService);
    private final InventoryService inventoryService = new InventoryService();
    private final BillingService billingService = new BillingService(patientService, inventoryService);
    private final ReportService reportService = new ReportService(patientService, appointmentService, inventoryService, billingService);

    public static void main(String[] args) {
        new Main().run();
    }

    private void run() {
        System.out.println("====================================");
        System.out.println("       MediTrack Java System");
        System.out.println("====================================");

        boolean running = true;
        while (running) {
            printMenu();
            int choice = input.readInt("Choose an option: ");

            switch (choice) {
                case 1 -> patientService.registerPatient(input);
                case 2 -> patientService.showAllPatients();
                case 3 -> patientService.searchPatient(input);
                case 4 -> appointmentService.scheduleAppointment(input);
                case 5 -> appointmentService.showAppointments();
                case 6 -> inventoryService.addMedicine(input);
                case 7 -> inventoryService.showMedicines();
                case 8 -> billingService.createBill(input);
                case 9 -> billingService.showBills();
                case 10 -> reportService.showDashboard();
                case 0 -> running = false;
                default -> System.out.println("Invalid option. Please try again.");
            }
        }

        System.out.println("Thank you for using MediTrack Java.");
        input.close();
    }

    private void printMenu() {
        System.out.println();
        System.out.println("1. Register patient");
        System.out.println("2. View all patients");
        System.out.println("3. Search patient");
        System.out.println("4. Schedule appointment");
        System.out.println("5. View appointments");
        System.out.println("6. Add medicine stock");
        System.out.println("7. View medicines");
        System.out.println("8. Create bill");
        System.out.println("9. View bills");
        System.out.println("10. Clinic report");
        System.out.println("0. Exit");
    }
}
