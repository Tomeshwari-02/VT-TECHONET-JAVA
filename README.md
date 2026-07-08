# VT TECHONET JAVA

VT TECHONET JAVA is a clinic management project for Java vocational training. It includes both a desktop interface and a console version, so it is small enough to understand but complete enough to demonstrate real Java concepts: classes, objects, collections, file handling, validation, GUI screens, searching, reporting, and simple billing.

## Features

- Register and view patients
- Search patients by name, phone number, or ID
- Schedule appointments with department, doctor, date, and time
- Manage medicine inventory
- Create bills for consultations and medicines
- Automatically reduce medicine stock after billing
- Save all records in CSV files
- View simple clinic reports
- Desktop interface using Java Swing

## Concepts Covered

- Object-oriented programming
- Encapsulation with model classes
- Service layer separation
- ArrayList and HashMap usage
- File I/O with CSV persistence
- Exception handling
- Date and time handling with `java.time`
- Java Swing GUI development
- Menu-driven console application

## Project Structure

```text
VT-TECHONET-JAVA/
  data/
    appointments.csv
    bills.csv
    medicines.csv
    patients.csv
  src/
    com/meditrack/
      Main.java
      gui/
      model/
      service/
      util/
```

## How To Run

Open PowerShell inside `MediTrack-Java`, then run:

### Desktop Interface

```powershell
javac -d out (Get-ChildItem -Recurse src -Filter *.java).FullName
java -cp out com.meditrack.gui.MediTrackGui
```

You can also double-click `run-gui.bat` on Windows.

### Console Version

```powershell
javac -d out (Get-ChildItem -Recurse src -Filter *.java).FullName
java -cp out com.meditrack.Main
```

## Demo Login

No login is required. The app starts directly with the main menu.

## Suggested Viva Explanation

You can explain it like this:

> My project is a clinic management system called MediTrack Java. It helps a small clinic store patient records, schedule appointments, manage medicine stock, generate bills, and view reports. I used object-oriented programming to divide the project into model classes, service classes, utility classes, and a Swing GUI class. Data is stored permanently in CSV files, so records are available even after closing the program.
