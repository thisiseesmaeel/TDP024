package se.liu.ida.hello;

import jdk.jpackage.internal.Log;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files

@Mojo(name="file-reader", defaultPhase = LifecyclePhase.COMPILE)
public class FilePrinterMojo extends AbstractMojo {

    @Parameter(property = "name", defaultValue = "test.txt")
    private String filename = "";

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("File to be opened: " + filename);
        String fileContent = "";
        try {
            File myObj = new File(filename);
            if (myObj.exists()) {
                Scanner myReader = new Scanner(myObj);
                while (myReader.hasNextLine()) {
                    fileContent += myReader.nextLine();
                }
                myReader.close();
                getLog().info("Content:\n" + fileContent);
            }else {
                getLog().info("File does not exist!");
            }
        } catch (FileNotFoundException e) {
            getLog().info("An error occurred.");
            e.printStackTrace();
        }

    }
}
