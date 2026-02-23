package org.example.service;

public class VoiceService {

    public void generateVoice(String message) {

        try {

            String command =
                    "powershell -Command \"Add-Type -AssemblyName System.Speech; " +
                            "$speak = New-Object System.Speech.Synthesis.SpeechSynthesizer; " +
                            "$speak.Speak('" + message + "');\"";

            ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", command);
            builder.inheritIO();
            builder.start();

            System.out.println("🔊 Offline voice alert spoken.");

        } catch (Exception e) {
            System.out.println("Voice failed: " + e.getMessage());
        }
    }
}