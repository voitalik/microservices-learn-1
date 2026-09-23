package com.epam.resource.service.impl;

import com.epam.resource.dto.SongMetadata;
import com.epam.resource.exception.InvalidResourceException;
import com.epam.resource.service.Mp3MetadataExtractor;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaCoreProperties;
import org.apache.tika.metadata.XMPDM;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

@Component
public class Mp3MetadataExtractorImpl implements Mp3MetadataExtractor {

    @Override
    public SongMetadata extract(byte[] data) {
        if (data == null || data.length == 0) {
            throw invalidMp3();
        }

        Metadata metadata = new Metadata();
        try (var input = new ByteArrayInputStream(data)) {
            new Mp3Parser().parse(input, new DefaultHandler(), metadata, new ParseContext());
            String duration = metadata.get(XMPDM.DURATION);
            if (!"audio/mpeg".equals(metadata.get(Metadata.CONTENT_TYPE)) || duration == null) {
                throw invalidMp3();
            }
            BigDecimal seconds = new BigDecimal(duration);
            if (seconds.signum() <= 0) {
                throw invalidMp3();
            }
            long totalSeconds = seconds.setScale(0, RoundingMode.DOWN).longValueExact();
            return new SongMetadata(null, metadata.get(TikaCoreProperties.TITLE),
                                    metadata.get(XMPDM.ARTIST), metadata.get(XMPDM.ALBUM),
                                    String.format(Locale.ROOT, "%02d:%02d",
                                                  totalSeconds / 60,
                                                  totalSeconds % 60),
                                    metadata.get(XMPDM.RELEASE_DATE));
        } catch (IOException | SAXException | TikaException | ArithmeticException |
                 NumberFormatException exception) {
            throw invalidMp3();
        }
    }

    private InvalidResourceException invalidMp3() {
        return new InvalidResourceException("Invalid MP3 file");
    }
}
