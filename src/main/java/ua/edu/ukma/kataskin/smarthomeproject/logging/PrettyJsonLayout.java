package ua.edu.ukma.kataskin.smarthomeproject.logging;

import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.layout.AbstractStringLayout;
import org.apache.logging.log4j.core.config.plugins.*;
import org.apache.logging.log4j.util.Strings;

import java.nio.charset.Charset;
import java.time.Instant;
import java.util.Map;
import java.util.StringJoiner;

@Plugin(name = "PrettyJsonLayout", category = "Core", elementType = Layout.ELEMENT_TYPE, printObject = true)
public class PrettyJsonLayout extends AbstractStringLayout {

    protected PrettyJsonLayout(Charset charset) {
        super(charset);
    }

    @PluginFactory
    public static PrettyJsonLayout createLayout(
            @PluginAttribute(value = "charset", defaultString = "UTF-8") final String charset
    ) {
        return new PrettyJsonLayout(Charset.forName(charset));
    }

    @Override
    public String toSerializable(LogEvent event) {
        String ts = Instant.ofEpochMilli(event.getTimeMillis()).toString();
        String marker = event.getMarker() != null ? event.getMarker().getName() : null;

        // ThreadContext (MDC) як JSON
        StringJoiner mdcPairs = new StringJoiner(",", "{", "}");
        for (Map.Entry<String, String> e : event.getContextData().toMap().entrySet()) {
            mdcPairs.add(jsonKV(e.getKey(), e.getValue()));
        }
        if (mdcPairs.length() == 2) mdcPairs = new StringJoiner("", "", ""); // порожній {}

        StringBuilder sb = new StringBuilder(256);
        sb.append("{")
                .append(jsonKV("ts", ts)).append(",")
                .append(jsonKV("level", event.getLevel().name())).append(",")
                .append(jsonKV("thread", event.getThreadName())).append(",")
                .append(jsonKV("logger", event.getLoggerName())).append(",");

        if (!Strings.isEmpty(marker)) {
            sb.append(jsonKV("marker", marker)).append(",");
        }

        sb.append("\"mdc\":").append(mdcPairs.length() > 0 ? mdcPairs.toString() : "{}").append(",")

                .append(jsonKV("message", event.getMessage().getFormattedMessage()));

        if (event.getThrown() != null) {
            sb.append(",").append(jsonKV("exception", event.getThrown().toString()));
        }

        sb.append("}\n");
        return sb.toString();
    }

    private static String jsonKV(String k, String v) {
        return "\"" + escape(k) + "\":\"" + escape(v) + "\"";
    }

    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");
    }

    @Override
    public String getContentType() {
        return "application/json; charset=" + getCharset();
    }
}
