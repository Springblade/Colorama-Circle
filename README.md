# Colorama-Circle

A JavaFX game project.

## Prerequisites
- Java 17 or higher
- JavaFX SDK 17.0.13

## Setup Instructions

1. Download JavaFX SDK 17.0.13 from [OpenJFX](https://openjfx.io/)

2. Configure your IDE (VS Code):
   - Install "Extension Pack for Java" if not already installed
   - Create a `lib` folder in project root
   - Copy all .jar files from JavaFX SDK's lib folder to your project's lib folder

3. Update launch.json:
```json
{
    "version": "0.2.0",
    "configurations": [
        {
            "type": "java",
            "name": "App",
            "request": "launch",
            "mainClass": "main.App",
            "vmArgs": "--module-path \"path/to/your/javafx-sdk-17.0.13/lib\" --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.base,javafx.media,javafx.web",
            "projectName": "Colorama-Circle_3d28692"
        }
    ]
}
```
Replace `path/to/your/javafx-sdk-17.0.13/lib` with your JavaFX lib directory path.

## Running the Application
1. Open project in VS Code
2. Navigate to App.java
3. Click Run button or press F5

## Troubleshooting
If you encounter JavaFX runtime errors:
- Verify JavaFX SDK version matches your Java version
- Confirm all required .jar files are in lib folder
- Check module path in launch.json points to correct directory