package hello.logging;

import com.fasterxml.jackson.core.JsonStreamContext;
import net.logstash.logback.mask.ValueMasker;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class RegexFindValueMasker implements ValueMasker {
    private static final String REGEX_PATTERN_FILE_LOCATION = "mask.patterns";
    private Pattern multiLinePattern;

    /**
     *
     */
    public RegexFindValueMasker() {
        try {
            Path path = Paths.get(getClass().getClassLoader()
                    .getResource(REGEX_PATTERN_FILE_LOCATION).toURI());

            Stream<String> lines = Files.lines(path);
            multiLinePattern = Pattern.compile(
                    lines.collect(Collectors.joining("|")), Pattern.MULTILINE);
            lines.close();
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object mask(JsonStreamContext jsonStreamContext, Object value) {
        if (value instanceof CharSequence) {
            StringBuilder valueToBeMasked = new StringBuilder((CharSequence) value);
            Matcher matcher = multiLinePattern.matcher(valueToBeMasked);
            while (matcher.find()) {
                IntStream.rangeClosed(1, matcher.groupCount())
                    .forEach(group -> {
                            if (matcher.group(group) != null) {
                                IntStream.range(matcher.start(group), matcher.end(group))
                                    .forEach(i -> valueToBeMasked.setCharAt(i, '*'));
                            }
                    });
            }
            return valueToBeMasked.toString();
        }
        return value;
    }
}
