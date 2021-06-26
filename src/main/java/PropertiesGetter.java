import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesGetter {

    private final String tokenTelegram;
    private final String jsonUrlVk;
    private final String jsonUrlVkWithOffset;

    public static final String PATH_TO_PROPERTIES = "src/main/resources/config.properties";
    public PropertiesGetter() {
        Properties prop = getProperties();
        this.tokenTelegram = prop.getProperty("tokenTelegram");
        this.jsonUrlVk = prop.getProperty("jsonUrlVk");
        this.jsonUrlVkWithOffset = prop.getProperty("jsonUrlVkWithOffset");

    }

    public String getJsonUrlVkWithOffset() {
        return jsonUrlVkWithOffset;
    }

    public String getTokenTelegram() {
        return tokenTelegram;
    }

    public String getJsonUrlVk() {
        return jsonUrlVk;
    }

    private Properties getProperties() {
        FileInputStream fileInputStream;
        Properties prop = new Properties();

        try {
            fileInputStream = new FileInputStream(PATH_TO_PROPERTIES);
            prop.load(fileInputStream);
        } catch (IOException e) {
            System.out.println("Property file not found or access denied");
            e.printStackTrace();
        }
        return prop;
    }
}
