package ua.edu.ukma.kataskin.smarthomeproject.logging;

import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.*;
import org.apache.logging.log4j.core.util.CloseShieldOutputStream;

import java.io.*;
import java.nio.charset.Charset;

@Plugin(name = "CustomFileAppender", category = "Core", elementType = Appender.ELEMENT_TYPE, printObject = true)
public class CustomFileAppender extends AbstractAppender {

    private final File file;
    private OutputStream out;

    protected CustomFileAppender(String name, Filter filter, Layout<? extends Serializable> layout,
                                 boolean ignoreExceptions, Property[] properties, File file) {
        super(name, filter, layout, ignoreExceptions, properties);
        this.file = file;
    }

    @PluginFactory
    public static CustomFileAppender createAppender(
            @PluginAttribute("name") String name,
            @PluginAttribute("fileName") String fileName,
            @PluginAttribute(value = "append", defaultBoolean = true) boolean append,
            @PluginElement("Filter") Filter filter,
            @PluginElement("Layout") Layout<? extends Serializable> layout
    ) {
        if (name == null) name = "CustomFile";
        if (layout == null) layout = new PrettyJsonLayout(Charset.forName("UTF-8"));

        File f = new File(fileName != null ? fileName : "logs/custom-app.log");
        CustomFileAppender appender = new CustomFileAppender(name, filter, layout, true, Property.EMPTY_ARRAY, f);
        try {
            f.getParentFile().mkdirs();
            OutputStream os = new FileOutputStream(f, append);
            appender.out = new CloseShieldOutputStream(os);
            appender.start();
        } catch (IOException e) {
            throw new AppenderLoggingException("Failed to open " + f.getAbsolutePath(), e);
        }
        return appender;
    }

    @Override
    public void append(LogEvent event) {
        try {
            byte[] bytes = getLayout().toByteArray(event);
            out.write(bytes);
            out.flush();
        } catch (IOException e) {
            if (!ignoreExceptions()) {
                throw new AppenderLoggingException(e);
            }
        }
    }

    @Override
    public void stop() {
        super.stop();
        if (out != null) {
            try {
                out.close();
            } catch (IOException ignored) {
            }
        }
    }
}
