package templayter;

import freemarker.template.Configuration;

import java.io.StringWriter;
import java.io.Writer;

public class PageGenerator {
    private static final String HTML_DIR = "templates";
    private static PageGenerator pageGenerator;
    private final Configuration cfg;

    public PageGenerator() {
        cfg =  new Configuration(Configuration.VERSION_2_3_23);
    }

    public static PageGenerator instance(){
        if (pageGenerator==null)
            pageGenerator = new PageGenerator();
        return pageGenerator;
    }

    public String getStaticPage(String fileName){
        Writer writer = new StringWriter();
        return null;

    }
}
