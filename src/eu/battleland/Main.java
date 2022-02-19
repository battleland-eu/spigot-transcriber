/**
 *     Spigot legacy transcriber - tool for transcribe hex color from
 *     MiniMessage format to Spigot legacy format
 *     Copyright (C) 2022 ShawChoo
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package eu.battleland;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;

public class Main {

    // Method for checking files
    private static boolean checkFile(String testedFile, boolean isInput) {
        File file = new File((testedFile));

        // Original file
        if (isInput) {
            if (!file.exists()) {
                System.out.println("Input file with given name not found!");
                return false;
            } else {
                System.out.println("Found input file " + testedFile);
            }

        // Transcribed file
        } else {
            if (file.exists()) {
                System.out.println("Output file with given name already exist!");
                return false;
            } else {
                System.out.println("Output file name will be " + testedFile);
            }
        }
        return true;
    }

    // Transcribe method
    private static void transcribe(String inputFile, String outputFile) {

        StringBuilder fileContent = new StringBuilder();
        String searchFor = "<#";                            // Search pattern for the beginning of hex code
        BufferedReader bfr;
        BufferedWriter bfw;

        try {
            String row;
            char[] replaced = new char[18];
            File file1 = new File(inputFile);
            File file2 = new File(outputFile);

            FileWriter fw = new FileWriter(file2);

            bfr = new BufferedReader(new FileReader(file1));
            bfw = new BufferedWriter(fw);

            while ((row = bfr.readLine()) != null) {
                int num = 0;

                if (row.contains(searchFor)) {
                    while ((num = row.indexOf(searchFor, num)) != 0 && num != -1) {
                        char[] arr = new char[9];       // original 9

                        for (int i = 0; i <= 8; i++) {
                            arr[i] = row.charAt(num + i);
                        }

                        for (int i = 0; i <= 8; i++) {
                            if (arr[i] == '#') {
                                replaced[2 * i + 1] = 'x';
                            } else {
                                replaced[2 * i + 1] = arr[i];
                            }
                            replaced[(2 * i)] = '&';

                        }

                        StringBuilder replacesWith = new StringBuilder();
                        for (int i = 2; i <= 15; i++) {
                            replacesWith.append(replaced[i]);
                        }

                        String toBeReplaced = row.substring(num, num + 9);
                        System.out.println("Transcribed:" + toBeReplaced);
                        row = row.replace(toBeReplaced, replacesWith.toString());

                        num++;
                    }

                    // write transcribed color to row
                    fileContent.append(row);
                    fileContent.append('\n');

                    // no change in row
                } else {
                    fileContent.append(row);
                    fileContent.append('\n');
                }
            }
            bfw.write(fileContent.toString());
            bfw.close();

        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("\n\nCopyright (C) 2022 ShawChoo");
        System.out.println("This program comes with ABSOLUTELY NO WARRANTY.");
        System.out.println("This is free software, and you are welcome to redistribute it");
        System.out.println("under certain conditions.\n");

        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("~~  SPIGOT HEX COLOR TRANSCRIBER v1.1 ~~");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");


        System.out.println("Warning: enter file name with extension!\n");

        do {

            System.out.print("Name of the file which will be transcribed: ");
            String inputFile = sc.nextLine();

            if (checkFile(inputFile, true)) {
                System.out.print("\nName of the new file: ");
                String outputFile = sc.nextLine();
                System.out.print("\n");

                if (checkFile(outputFile, false)) {
                    System.out.println("Transcription started");
                    transcribe(inputFile, outputFile);

                } else {
                    System.out.print("Overwrite? (Y/N)");
                    if (Character.toUpperCase(sc.nextLine().charAt(0)) == 'Y') {
                        System.out.println("Transcription started");
                        transcribe(inputFile, outputFile);
                        System.out.println("Transcription ended");
                    }
                }
            }

            System.out.print("Exit? (Y/N): ");

        } while (Character.toUpperCase(sc.nextLine().charAt(0)) == 'N');
        System.exit(0);
    }
}

