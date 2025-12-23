# Vehicule Showroom Project
## Requirements
- Java 17+

## How to Run

### Build locally
after exctracting the `VRSM_Demo.zip` do the following:
```cmd
cd VRSM
mvn clean compile
mvn exec:java
```

### From Github
The project ships with a precompiled demo (in the preview directory) to avoid complications.
```cmd
git clone https://github.com/Pastoray/VRSM
cd VRSM/preview
java -jar VRSM_Demo.jar
```

### Notes
- Data is saved automatically in a database in the same folder
- No installation or setup required
- You can either try the console version or the GUI version
- The way this codebase was handled will make it so that you don't need any external dependencies
