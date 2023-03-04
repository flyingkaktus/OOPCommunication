@echo off

REM Pfade einstellen
set "JAR_FILE=%~dp0\simple-player\target\simple-player-1.0-SNAPSHOT.jar"

REM Prüfen, ob die JAR-Datei existiert
if not exist "%JAR_FILE%" (
    echo Die JAR-Datei wurde nicht gefunden.
    pause
    exit /b
)

REM JAR-Datei ausführen
java -jar "%JAR_FILE%"

REM Warte auf Benutzereingabe
pause
