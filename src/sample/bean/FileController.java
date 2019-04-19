package sample.bean;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.LineIterator;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileController {

    /**
     * ------- CONSTANTS ------
     **/
    Integer LINE_TEMPLATE_IN = 4227;

    /**
     * VARIABLES
     **/
    private StringBuilder log = new StringBuilder();
    private Integer indexLine = 1;
    private Integer indexLevel = 0;


    /**
     * @param ficheroOriginal
     * @return
     */
    public long fileNumbers(File ficheroOriginal) {
        long countLines = 0;
        FileReader fr = null;
        BufferedReader bf = null;

        try {
            fr = new FileReader(ficheroOriginal);
            bf = new BufferedReader(fr);


            while ((bf.readLine()) != null) {
                countLines++;
            }

        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (bf != null) bf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (bf != null) fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return countLines;
    }

    /**
     * @param line
     * @param lenghtLine
     * @param index
     */
    public void validateLenght(String line, Integer lenghtLine, Integer index) {
        Integer intLength = line.length();
        String lenghtLineStr = lenghtLine.toString();

        if (lenghtLineStr.equals(intLength.toString())) {
            //debug("Line "+index+": correct length, "+intLength+" characters, template "+lenghtLine+" characters.");
        } else {
            debug("Line " + index + ": INCORRECT length, " + intLength + " characters, template " + lenghtLine + " characters.");
            this.indexLevel++;
        }

    }


    /**
     * Split file.
     *
     * @return the number of generated files
     * @throws IOException
     */
    public int splitFile(String fileLocation, String outputFolder,
                         int linesPerFile, int templateChar) throws IOException {
        File fileToRead = new File(fileLocation);
        String filename = FilenameUtils.getBaseName(fileLocation);
        String extension = FilenameUtils.getExtension(fileLocation);

        int fileCounter = 0;
        int counter = 1;
        List<String> newLines = new ArrayList<String>();
        LineIterator it = FileUtils.lineIterator(fileToRead);

        debug("Reading file...");
        try {
            while (it.hasNext()) {
                String line = it.nextLine();

                //Validate the lenght
                validateLenght(line, templateChar, this.indexLine);

                newLines.add(line);
                counter++;
                if (counter == linesPerFile
                        || (counter < linesPerFile && !it.hasNext())) {
                    File outputFolderFile = new File(outputFolder);
                    outputFolderFile.mkdirs();

                    String newFileLocation = outputFolder + "/" + filename
                            + "-" + (++fileCounter) + "." + extension;

                    File outputFile = new File(newFileLocation);
                    FileUtils.writeLines(outputFile, newLines);
                    newLines.clear();
                    counter = 1;
                    System.out.println("Line " + this.indexLine + " comprobed.");
                }
                this.indexLine++;
            }
        } catch (Exception e) {
            debug("Line " + this.indexLine + ": INCORRECT length, impossible read to line, template " + LINE_TEMPLATE_IN + " characters.");
            LineIterator.closeQuietly(it);

        } finally {
            LineIterator.closeQuietly(it);
        }

        return fileCounter;
    }


    /**
     * @param entry
     * @return
     */
    public String debug(String entry) {
        log.append(entry);
        log.append("\n");
        return log.toString();
    }


    /**
     * @param ficheroOriginal
     * @param ficheroCopia
     */
    public void createFile(File ficheroOriginal, String ficheroCopia, Integer countLines) {
        FileReader fr = null;
        BufferedReader br = null;
        FileWriter fichero = null;
        PrintWriter pw = null;

        // Lectura del fichero
        String linea;
        Integer count = 0;

        try {
            // Se abre el fichero original para lectura
            fr = new FileReader(ficheroOriginal);
            br = new BufferedReader(fr);

            // Se abre el fichero original para escritura
            fichero = new FileWriter(ficheroCopia);
            pw = new PrintWriter(fichero);


            while ((linea = br.readLine()) != null && count < countLines) {
                System.out.println(linea);
                pw.println(linea);
                count++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fr) {
                    fr.close();
                }
                if (null != fichero) {
                    fichero.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public StringBuilder getLog() {
        return log;
    }

    public void setLog(StringBuilder log) {
        this.log = log;
    }

    public Integer getIndexLine() {
        return indexLine;
    }

    public void setIndexLine(Integer indexLine) {
        this.indexLine = indexLine;
    }

    public Integer getIndexLevel() {
        return indexLevel;
    }

    public void setIndexLevel(Integer indexLevel) {
        this.indexLevel = indexLevel;
    }
}
