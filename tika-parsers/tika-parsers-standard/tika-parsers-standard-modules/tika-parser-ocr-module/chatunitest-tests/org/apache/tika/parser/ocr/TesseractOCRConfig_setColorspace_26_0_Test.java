package org.apache.tika.parser.ocr;

import org.apache.tika.parser.ocr.TesseractOCRConfig;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
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

@ExtendWith(MockitoExtension.class)
public class TesseractOCRConfig_setColorspace_26_0_Test {

    private TesseractOCRConfig config;

    @Mock
    private Serializable mockSerializable;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        config = new TesseractOCRConfig();
    }

    @Test
    public void testSetColorspaceValid() throws Exception {
        String validColorspace = "rgb";
        config.setColorspace(validColorspace);
        assertEquals(validColorspace, config.getColorspace());
    }

    @Test
    public void testSetColorspaceInvalid() throws Exception {
        String invalidColorspace = "invalid-colorspace";
        assertThrows(IllegalArgumentException.class, () -> config.setColorspace(invalidColorspace));
    }

    @Test
    public void testSetColorspaceNull() throws Exception {
        assertThrows(IllegalArgumentException.class, () -> config.setColorspace(null));
    }

    @Test
    public void testSetColorspaceEmpty() throws Exception {
        assertThrows(IllegalArgumentException.class, () -> config.setColorspace(""));
    }

    @Test
    public void testSetColorspaceWithSpaces() throws Exception {
        String colorspaceWithSpaces = "rgb with spaces";
        assertThrows(IllegalArgumentException.class, () -> config.setColorspace(colorspaceWithSpaces));
    }

    @Test
    public void testSetColorspaceWithSpecialChars() throws Exception {
        String colorspaceWithSpecialChars = "rgb!@#";
        assertThrows(IllegalArgumentException.class, () -> config.setColorspace(colorspaceWithSpecialChars));
    }

    @Test
    public void testSetColorspaceWithNumbers() throws Exception {
        String colorspaceWithNumbers = "rgb123";
        assertThrows(IllegalArgumentException.class, () -> config.setColorspace(colorspaceWithNumbers));
    }

    @Test
    public void testSetColorspaceWithMixedCase() throws Exception {
        String colorspaceWithMixedCase = "RGB";
        assertThrows(IllegalArgumentException.class, () -> config.setColorspace(colorspaceWithMixedCase));
    }

    @Test
    public void testSetColorspaceWithDash() throws Exception {
        String colorspaceWithDash = "rgb-";
        assertThrows(IllegalArgumentException.class, () -> config.setColorspace(colorspaceWithDash));
    }

    @Test
    public void testSetColorspaceWithPeriod() throws Exception {
        String colorspaceWithPeriod = "rgb.";
        assertThrows(IllegalArgumentException.class, () -> config.setColorspace(colorspaceWithPeriod));
    }

    @Test
    public void testSetColorspaceWithUnderscore() throws Exception {
        String colorspaceWithUnderscore = "rgb_";
        assertThrows(IllegalArgumentException.class, () -> config.setColorspace(colorspaceWithUnderscore));
    }

    @Test
    public void testSetColorspaceWithUpperCase() throws Exception {
        String colorspaceWithUpperCase = "RGB";
        assertThrows(IllegalArgumentException.class, () -> config.setColorspace(colorspaceWithUpperCase));
    }
}
