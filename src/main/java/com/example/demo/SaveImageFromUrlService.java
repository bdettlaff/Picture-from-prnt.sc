package com.example.demo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class SaveImageFromUrlService {

    private static final String ALPHA_NUMERIC_PATTERN = "0123456789"
            + "abcdefghijklmnopqrstuvxyz";
    private static final String PRNT_SC_ADDRESS = "https://prnt.sc/";
    private static final String FIRST_CSS_ELEMENT = "span.image-info-item";
    private static final String SECOND_CSS_ELEMENT = "a";
    public static final String LOCAL_PATH = "U:\\pictures\\";
    public static final String EXTENSION = ".jpg";

    public List<String> downloadPicture() throws IOException {
        List<String> linksToDownloadedPictures = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            StringBuilder generatedLink = generateSixCharactersString();
            linksToDownloadedPictures.add(generatedLink.toString());
            String correctLink = getLinkToImg(generatedLink);
            saveImage(correctLink, LOCAL_PATH + generatedLink.toString() + EXTENSION);
        }

        return linksToDownloadedPictures;
    }

    public void saveImage(String imageUrl, String destinationFile) throws IOException {
        URL url;
        try {
            url = new URL(imageUrl);
            InputStream is = url.openStream();
            OutputStream os = new FileOutputStream(destinationFile);

            byte[] b = new byte[2048];
            int length;

            while ((length = is.read(b)) != -1) {
                os.write(b, 0, length);
            }

            is.close();
            os.close();
        } catch (MalformedURLException e) {
            System.out.println("ERROR - Problem z linkiem pomijam");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("ERROR - Nie znaleziono linku");
        }
    }

    public String getLinkToImg(StringBuilder sb) throws IOException {
        Document pageInHtml = Jsoup.connect(PRNT_SC_ADDRESS + sb.toString()).get();
        Elements firstFilter = pageInHtml.select(FIRST_CSS_ELEMENT);
        Elements secondFilter = firstFilter.select(SECOND_CSS_ELEMENT);

        String link = receiveLinkFromHtml(secondFilter);
        int indexOfLink = link.indexOf('=');

        return link.substring(indexOfLink + 1);
    }

    public StringBuilder generateSixCharactersString() {
        StringBuilder generatedCombination = new StringBuilder();

        for (int j = 0; j < 6; j++) {
            int index = (int) (ALPHA_NUMERIC_PATTERN.length() * Math.random());
            generatedCombination.append(ALPHA_NUMERIC_PATTERN.charAt(index));
        }

        return generatedCombination;
    }

    private String receiveLinkFromHtml(Elements secondFilter) {
        String imageURL = " ";

        try {
            imageURL = secondFilter.get(1).attr("href");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("ERROR - Brak href w drugim filtrze");
        }
        return imageURL;
    }
}
