package org.apache.tika.parser.ocr;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.apache.tika.parser.ocr.TesseractOCRConfig;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.tika.exception.TikaConfigException;
import org.apache.tika.utils.StringUtils;

public class TesseractOCRConfig_setOtherTesseractConfig_40_0_Test {

    @Test
    public void testSetOtherTesseractConfig_NullMap() throws Exception {
        TesseractOCRConfig config = new TesseractOCRConfig();
        config.setOtherTesseractConfig(null);
        assertEquals(0, config.getOtherTesseractConfig().size());
    }

    @Test
    public void testSetOtherTesseractConfig_EmptyMap() throws Exception {
        TesseractOCRConfig config = new TesseractOCRConfig();
        config.setOtherTesseractConfig(new HashMap<>());
        assertEquals(0, config.getOtherTesseractConfig().size());
    }

    @Test
    public void testSetOtherTesseractConfig_SingleEntry() throws Exception {
        TesseractOCRConfig config = new TesseractOCRConfig();
        config.setOtherTesseractConfig(Map.of("key", "value"));
        assertEquals(1, config.getOtherTesseractConfig().size());
        assertEquals("value", config.getOtherTesseractConfig().get("key"));
    }

    @Test
    public void testSetOtherTesseractConfig_MultipleEntries() throws Exception {
        TesseractOCRConfig config = new TesseractOCRConfig();
        config.setOtherTesseractConfig(Map.of("key1", "value1", "key2", "value2"));
        assertEquals(2, config.getOtherTesseractConfig().size());
        assertEquals("value1", config.getOtherTesseractConfig().get("key1"));
        assertEquals("value2", config.getOtherTesseractConfig().get("key2"));
    }

    @Test
    public void testSetOtherTesseractConfig_InvalidKey() throws Exception {
        TesseractOCRConfig config = new TesseractOCRConfig();
        try {
            config.setOtherTesseractConfig(Map.of("key!", "value"));
            fail("Expected IllegalArgumentException for invalid key");
        } catch (IllegalArgumentException e) {
            assertEquals("Key contains illegal characters: key!", e.getMessage());
        }
    }

    @Test
    public void testSetOtherTesseractConfig_InvalidValue() throws Exception {
        TesseractOCRConfig config = new TesseractOCRConfig();
        try {
            config.setOtherTesseractConfig(Map.of("key", "value!"));
            fail("Expected IllegalArgumentException for invalid value");
        } catch (IllegalArgumentException e) {
            assertEquals("Value contains illegal characters: value!", e.getMessage());
        }
    }

    @Test
    public void testSetOtherTesseractConfig_ExistingKey() throws Exception {
        TesseractOCRConfig config = new TesseractOCRConfig();
    }
}
