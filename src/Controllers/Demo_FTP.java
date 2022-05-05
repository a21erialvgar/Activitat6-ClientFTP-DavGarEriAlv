package Controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import org.apache.commons.net.ftp.FTPClient;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import org.apache.commons.net.ftp.FTPFile;

/**
 * Codi original:
 * https://www.tabnine.com/code/java/packages/org.apache.commons.net.ftp
 * Descarga org.apache.commons.net.ftp des
 * de:https://commons.apache.org/proper/commons-net/download_net.cgi Site FTP
 * per fer proves: demo.wftpserver.com demo / demo
 *
 * @author santi
 */
public class Demo_FTP {

    static final String SITE = "demo.wftpserver.com";
    static final String USER = "demo";
    static final String PWD = "demo";

    static FTPClient client = new FTPClient();

    public static void main(String[] args) throws UnknownHostException, IOException {

        client = new FTPClient();

        System.out.println("Vaig a connectar...");
        Connectar();

        System.out.println("Mostant el directori actiu...");
        MostrarDirectoriActiu();

        System.out.println("Llistant el directori arrel...");
        LlistarDirectori("/");

        System.out.println("Canviant al directori /download");
        CanviarDirectori("/download");
        MostrarDirectoriActiu();

        System.out.println("Llistant el directori actual");
        LlistarDirectori(".");

        System.out.println("Baixant un fitxer de text");
        Download("version.txt", "C:\\Borrar\\down1.txt");

        System.out.println("Baixant un fitxer PDF");
        Download("manual_en.pdf", "C:\\Borrar\\down2.pdf");

        System.out.println("Canviant al directori anterior");
        CanviarDirectori("..");
        MostrarDirectoriActiu();

        System.out.println("Canviant al directori upload");
        CanviarDirectori("/upload");
        MostrarDirectoriActiu();

        System.out.println("Pujant un fitxer");
        Upload("C:\\Borrar\\demofile.txt");
        LlistarDirectori(".");

        System.out.println("Desconnectant");
        Desconnectar();

        System.out.println("Surto de l'aplicació");
    }

    private static void Connectar() throws IOException {
        client.connect(SITE);
        client.enterLocalPassiveMode();
        client.login(USER, PWD);
    }

    private static void Desconnectar() throws IOException {
        client.logout();
        if (client.isConnected()) {
            client.disconnect();
        }
    }

    private static void LlistarDirectori(String directori) throws IOException {
        FTPFile[] files = client.listFiles(directori);
        for (FTPFile file : files) {
            SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy'T'HH:mmZ");
            String dataFormatada = format1.format(file.getTimestamp().getTime());
            System.out.printf("%-40s --- %10d --- %s\n", file.getName(), file.getSize(), dataFormatada);
        }
    }

    private static void MostrarDirectoriActiu() throws IOException {
        System.out.println("Directori Actiu: " + client.printWorkingDirectory());
    }

    private static void CanviarDirectori(String directori) throws IOException {
        client.changeWorkingDirectory(directori);
    }

    private static void Upload(String fileName) throws FileNotFoundException, IOException {

        File file = new File(fileName);
        FileInputStream fis = new FileInputStream(file);

        client.storeFile("./" + file.getName(), fis);

        fis.close();
    }

    private static void Download(String remoteFileName, String localFileName) throws FileNotFoundException, IOException {

        File file = new File(localFileName);
        FileOutputStream fos = new FileOutputStream(file);
        client.retrieveFile(remoteFileName, fos);
        fos.flush();
        fos.close();
    }

}