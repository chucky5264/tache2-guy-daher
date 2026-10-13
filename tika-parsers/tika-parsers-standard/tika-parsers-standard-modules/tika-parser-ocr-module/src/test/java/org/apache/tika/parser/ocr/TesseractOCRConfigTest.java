/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.tika.parser.ocr;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import org.apache.tika.TikaTest;
import org.apache.tika.config.loader.TikaLoader;
import org.apache.tika.exception.TikaConfigException;
import org.apache.tika.parser.CompositeParser;

public class TesseractOCRConfigTest extends TikaTest {

    @Test
    public void testNoConfig() throws Exception {
        TesseractOCRConfig config = new TesseractOCRConfig();
        assertEquals("eng", config.getLanguage(), "Invalid default language value");
        assertEquals("1", config.getPageSegMode(), "Invalid default pageSegMode value");
        assertEquals(0, config.getMinFileSizeToOcr(), "Invalid default minFileSizeToOcr value");
        assertEquals(Integer.MAX_VALUE, config.getMaxFileSizeToOcr(), "Invalid default maxFileSizeToOcr value");
        assertEquals(120, config.getTimeoutSeconds(), "Invalid default timeout value");
        assertEquals(300, config.getDensity(), "Invalid default density value");
        assertEquals(4, config.getDepth(), "Invalid default depth value");
        assertEquals("gray", config.getColorspace(), "Invalid default colorpsace value");
        assertEquals("triangle", config.getFilter(), "Invalid default filter value");
        assertEquals(200, config.getResize(), "Invalid default resize value");
        assertEquals(false, config.isApplyRotation(), "Invalid default applyRotation value");
    }

    @Test
    public void testPartialConfig() throws Exception {
        TikaLoader loader = TikaLoader.load(
                getConfigPath(TesseractOCRConfigTest.class, "tika-config-tesseract-partial.json"));
        TesseractOCRParser parser =
                (TesseractOCRParser) ((CompositeParser) loader.loadParsers())
                        .getAllComponentParsers().get(0);
        TesseractOCRConfig config = parser.getDefaultConfig();
        assertEquals("fra+deu", config.getLanguage(), "Invalid overridden language value");
        assertEquals("1", config.getPageSegMode(), "Invalid default pageSegMode value");
        assertEquals(1, config.getMinFileSizeToOcr(), "Invalid overridden minFileSizeToOcr value");
        assertEquals(Integer.MAX_VALUE, config.getMaxFileSizeToOcr(), "Invalid default maxFileSizeToOcr value");
        assertEquals(240, config.getTimeoutSeconds(), "Invalid overridden timeout value");
        assertEquals(200, config.getDensity(), "Invalid overridden density value");
        assertEquals(8, config.getDepth(), "Invalid overridden depth value");
        assertEquals("box", config.getFilter(), "Invalid overridden filter value");
        assertEquals(300, config.getResize(), "Invalid overridden resize value");
        assertEquals(false, config.isApplyRotation(), "Invalid default applyRotation value");
    }

    @Test
    public void testFullConfig() throws Exception {
        TikaLoader loader = TikaLoader.load(
                getConfigPath(TesseractOCRConfigTest.class, "tika-config-tesseract-full.json"));
        TesseractOCRParser parser =
                (TesseractOCRParser) ((CompositeParser) loader.loadParsers())
                        .getAllComponentParsers().get(0);
        TesseractOCRConfig config = parser.getDefaultConfig();
        assertEquals("ceb", config.getLanguage(), "Invalid overridden language value");
        assertEquals("2", config.getPageSegMode(), "Invalid default pageSegMode value");
        assertEquals(1, config.getMinFileSizeToOcr(), "Invalid overridden minFileSizeToOcr value");
        assertEquals(2000000, config.getMaxFileSizeToOcr(), "Invalid default maxFileSizeToOcr " +
                "value");
        assertEquals(240, config.getTimeoutSeconds(), "Invalid overridden timeout value");
        assertEquals(200, config.getDensity(), "Invalid overridden density value");
        assertEquals(8, config.getDepth(), "Invalid overridden depth value");
        assertEquals("box", config.getFilter(), "Invalid overridden filter value");
        assertEquals(300, config.getResize(), "Invalid overridden resize value");
        assertEquals(true, config.isApplyRotation(), "Invalid default applyRotation value");
    }

    @Test
    public void testValidateValidLanguage() {
        List<String> validLanguages =
                Arrays.asList("eng", "slk_frak", "chi_tra", "eng+fra", "tgk+chi_tra+slk_frak",
                        "chi_tra_vert", "tgk+chi_tra_vert+slk_frak", "eng+script/Arabic",
                        "script/HanT_vert");

        TesseractOCRConfig config = new TesseractOCRConfig();

        for (String language : validLanguages) {
            config.setLanguage(language);
            assertEquals(language, config.getLanguage(), "Valid language not set");
        }
    }

    @Test
    public void testValidateInvalidLanguage() {
        List<String> invalidLanguages = Arrays.asList(
                //"", allow empty string
                "+", "en", "en+", "eng+fra+", "Arabic", "/script/Arabic", "rm -rf *");

        TesseractOCRConfig config = new TesseractOCRConfig();

        for (String language : invalidLanguages) {
            try {
                config.setLanguage(language);
                fail("Invalid language set: " + language);
            } catch (IllegalArgumentException e) {
                // expected exception thrown
            }
        }
    }

    @Test
    public void testValidatePageSegMode() {
        TesseractOCRConfig config = new TesseractOCRConfig();
        config.setPageSegMode("0");
        config.setPageSegMode("10");
        assertTrue(true, "Couldn't set valid values");
        assertThrows(IllegalArgumentException.class, () -> {
            config.setPageSegMode("14");
        });
    }

    @Test
    public void testValidateDensity() {
        TesseractOCRConfig config = new TesseractOCRConfig();
        config.setDensity(300);
        config.setDensity(400);
        assertTrue(true, "Couldn't set valid values");
        assertThrows(IllegalArgumentException.class, () -> {
            config.setDensity(1);
        });
    }

    @Test
    public void testValidateDepth() {
        TesseractOCRConfig config = new TesseractOCRConfig();
        config.setDepth(4);
        config.setDepth(8);
        assertTrue(true, "Couldn't set valid values");
        assertThrows(IllegalArgumentException.class, () -> {
            config.setDepth(6);
        });
    }

    @Test
    public void testValidateFilter() {
        TesseractOCRConfig config = new TesseractOCRConfig();
        config.setFilter("Triangle");
        config.setFilter("box");
        assertTrue(true, "Couldn't set valid values");
        assertThrows(IllegalArgumentException.class, () -> {
            config.setFilter("abc");
        });
    }

    @Test
    public void testValidateResize() {
        TesseractOCRConfig config = new TesseractOCRConfig();
        config.setResize(200);
        config.setResize(400);
        assertTrue(true, "Couldn't set valid values");
        assertThrows(IllegalArgumentException.class, () -> {
            config.setResize(1000);
        });
    }

    @Test
    public void testDataPathCheck() {
        TesseractOCRConfig config = new TesseractOCRConfig();
        assertThrows(IllegalArgumentException.class, () -> {
            config.setTessdataPath("blah\u0000deblah");
        });
    }

    @Test
    public void testPathCheck() {
        TesseractOCRConfig config = new TesseractOCRConfig();
        assertThrows(IllegalArgumentException.class, () -> {
            config.setTesseractPath("blah\u0000deblah");
        });
    }

    @Test
    public void testBadOtherKey() {
        TesseractOCRConfig config = new TesseractOCRConfig();
        assertThrows(IllegalArgumentException.class, () -> {
            config.addOtherTesseractConfig("bad bad", "bad");
        });
    }

    @Test
    public void testBadOtherValue() {
        TesseractOCRConfig config = new TesseractOCRConfig();
        assertThrows(IllegalArgumentException.class, () -> {
            config.addOtherTesseractConfig("bad", "bad bad");
        });
    }

    @Test
    public void testBadOtherValueSlash() {
        TesseractOCRConfig config = new TesseractOCRConfig();
        assertThrows(IllegalArgumentException.class, () -> {
            config.addOtherTesseractConfig("bad", "bad\\bad");
        });
    }

    @Test
    public void testBadOtherValueControl() {
        TesseractOCRConfig config = new TesseractOCRConfig();
        assertThrows(IllegalArgumentException.class, () -> {
            config.addOtherTesseractConfig("bad", "bad\u0001bad");
        });
    }

    @Test
    public void testGoodOtherParameters() {
        TesseractOCRConfig config = new TesseractOCRConfig();
        config.addOtherTesseractConfig("good", "good");
    }

    @Test
    public void testBadLanguageCode() throws Exception {
        TesseractOCRConfig tesseractOCRConfig = new TesseractOCRConfig();
        assertThrows(IllegalArgumentException.class, () -> {
            tesseractOCRConfig.setLanguage("kerplekistani");
        });
    }

    @Test
    public void testBadColorSpace() {
        TesseractOCRConfig config = new TesseractOCRConfig();
        assertThrows(IllegalArgumentException.class, () -> {
            config.setColorspace("someth!ng");
        });
    }

    // Tests ajoutés pour couvrir les mutants pitest qui survivaient encore.

    // Les bornes 150 et 1200 n'étaient pas testées précisément: un mutant
    // changeant "<" en "<=" passait inaperçu.
    @Test
    public void testValidateDensityBoundaries() {
        TesseractOCRConfig config = new TesseractOCRConfig();
        config.setDensity(150);
        assertEquals(150, config.getDensity(), "Lower boundary density value should be accepted");
        config.setDensity(1200);
        assertEquals(1200, config.getDensity(), "Upper boundary density value should be accepted");
        assertThrows(IllegalArgumentException.class, () -> config.setDensity(149),
                "Value just below the lower boundary must be rejected");
        assertThrows(IllegalArgumentException.class, () -> config.setDensity(1201),
                "Value just above the upper boundary must be rejected");
    }

    // Aucun test n'appelait setColorspace(null); le contrôle de nullité
    // aurait pu être inversé sans rien casser.
    @Test
    public void testColorspaceNullThrows() {
        TesseractOCRConfig config = new TesseractOCRConfig();
        assertThrows(IllegalArgumentException.class, () -> config.setColorspace(null));
    }

    // Le retour de getOutputType() n'était jamais vérifié après un appel
    // à setOutputType("txt"/"hocr").
    @Test
    public void testOutputTypeStringRoundTrip() {
        TesseractOCRConfig config = new TesseractOCRConfig();
        assertEquals(TesseractOCRConfig.OUTPUT_TYPE.TXT, config.getOutputType(),
                "Default output type should be TXT");
        config.setOutputType("hocr");
        assertEquals(TesseractOCRConfig.OUTPUT_TYPE.HOCR, config.getOutputType(),
                "Output type should be updated to HOCR");
        config.setOutputType("txt");
        assertEquals(TesseractOCRConfig.OUTPUT_TYPE.TXT, config.getOutputType(),
                "Output type should be updated back to TXT");
    }

    // Le setter est appelé ailleurs dans la suite, mais isEnableImagePreprocessing()
    // n'était jamais relu après coup.
    @Test
    public void testEnableImagePreprocessingRoundTrip() {
        TesseractOCRConfig config = new TesseractOCRConfig();
        assertFalse(config.isEnableImagePreprocessing(), "Default should be disabled");
        config.setEnableImagePreprocessing(true);
        assertTrue(config.isEnableImagePreprocessing(), "Should reflect value set to true");
    }

    // Même situation pour isPreloadLangs().
    @Test
    public void testPreloadLangsRoundTrip() {
        TesseractOCRConfig config = new TesseractOCRConfig();
        assertFalse(config.isPreloadLangs(), "Default should be disabled");
        config.setPreloadLangs(true);
        assertTrue(config.isPreloadLangs(), "Should reflect value set to true");
    }

    // getTesseractPath/getImageMagickPath/getTessdataPath n'étaient testés
    // qu'avec un chemin invalide (caractère de contrôle). Avec un chemin
    // correct, le setter doit ajouter le séparateur final s'il manque;
    // ça n'avait jamais été vérifié.
    @Test
    public void testTesseractPathRoundTrip() throws Exception {
        TesseractOCRConfig config = new TesseractOCRConfig();
        config.setTesseractPath("tesseract-bin");
        assertEquals("tesseract-bin" + File.separator, config.getTesseractPath(),
                "Path should be normalized with a trailing separator");
    }

    @Test
    public void testImageMagickPathRoundTrip() throws Exception {
        TesseractOCRConfig config = new TesseractOCRConfig();
        config.setImageMagickPath("imagemagick-bin");
        assertEquals("imagemagick-bin" + File.separator, config.getImageMagickPath(),
                "Path should be normalized with a trailing separator");
    }

    @Test
    public void testTessdataPathRoundTrip() throws Exception {
        TesseractOCRConfig config = new TesseractOCRConfig();
        config.setTessdataPath("tessdata-dir");
        assertEquals("tessdata-dir" + File.separator, config.getTessdataPath(),
                "Path should be normalized with a trailing separator");
    }

    // setOtherTesseractConfig(Map) et getOtherTesseractConfig() n'étaient
    // jamais appelés directement; seule la variante clé/valeur
    // (addOtherTesseractConfig) était couverte.
    @Test
    public void testSetOtherTesseractConfigMap() {
        TesseractOCRConfig config = new TesseractOCRConfig();
        config.setOtherTesseractConfig(Map.of("good", "good"));
        assertEquals("good", config.getOtherTesseractConfig().get("good"),
                "Map entries passed to setOtherTesseractConfig should be applied");
    }

    // Aucun test n'existait pour isInlineContent/setInlineContent.
    @Test
    public void testInlineContentRoundTrip() {
        TesseractOCRConfig config = new TesseractOCRConfig();
        assertFalse(config.isInlineContent(), "Default should be disabled");
        config.setInlineContent(true);
        assertTrue(config.isInlineContent(), "Should reflect value set to true");
    }

    // RuntimeConfig était à 0% de couverture avant ces tests (voir JaCoCo).
    // Elle verrouille les chemins tesseract/tessdata/imageMagick une fois
    // le parseur initialisé, pour empêcher qu'une config lue à l'exécution
    // les modifie.
    @Test
    public void testRuntimeConfigRejectsPathChanges() {
        TesseractOCRConfig.RuntimeConfig runtimeConfig = new TesseractOCRConfig.RuntimeConfig();
        assertThrows(TikaConfigException.class, () -> runtimeConfig.setTesseractPath("/some/path"),
                "RuntimeConfig must reject changing tesseractPath at runtime");
        assertThrows(TikaConfigException.class, () -> runtimeConfig.setTessdataPath("/some/path"),
                "RuntimeConfig must reject changing tessdataPath at runtime");
        assertThrows(TikaConfigException.class, () -> runtimeConfig.setImageMagickPath("/some/path"),
                "RuntimeConfig must reject changing imageMagickPath at runtime");
    }

    // Une valeur vide ou nulle doit en revanche passer sans problème
    // (branche StringUtils.isBlank), sinon on bloquerait aussi les configs
    // qui ne touchent pas aux chemins.
    @Test
    public void testRuntimeConfigAllowsBlankPaths() {
        TesseractOCRConfig.RuntimeConfig runtimeConfig = new TesseractOCRConfig.RuntimeConfig();
        assertDoesNotThrow(() -> runtimeConfig.setTesseractPath(""));
        assertDoesNotThrow(() -> runtimeConfig.setTessdataPath(null));
        assertDoesNotThrow(() -> runtimeConfig.setImageMagickPath(""));
    }

    // Trois cas de setPageSeparator n'étaient jamais couverts ensemble:
    // entrée vide (no-op), séparateur valide ("end-of-page", conforme à
    // ALLOWABLE_PAGE_SEPARATORS_PATTERN) et séparateur contenant un
    // caractère interdit comme "<".
    @Test
    public void testPageSeparatorRoundTrip() {
        TesseractOCRConfig config = new TesseractOCRConfig();
        assertEquals("", config.getPageSeparator(), "Default page separator should be empty");
        config.setPageSeparator("");
        assertEquals("", config.getPageSeparator(), "Blank input should be a no-op");
        config.setPageSeparator("end-of-page");
        assertEquals("end-of-page", config.getPageSeparator(), "Valid separator should be applied");
        assertThrows(IllegalArgumentException.class, () -> config.setPageSeparator("<script>"),
                "Separator with illegal characters must be rejected");
    }

    // isPreserveInterwordSpacing() et isSkipOcr() (test suivant) n'avaient
    // jamais été relus après leur setter respectif.
    @Test
    public void testPreserveInterwordSpacingRoundTrip() {
        TesseractOCRConfig config = new TesseractOCRConfig();
        assertFalse(config.isPreserveInterwordSpacing(), "Default should be disabled");
        config.setPreserveInterwordSpacing(true);
        assertTrue(config.isPreserveInterwordSpacing(), "Should reflect value set to true");
    }

    @Test
    public void testSkipOcrRoundTrip() {
        TesseractOCRConfig config = new TesseractOCRConfig();
        assertFalse(config.isSkipOcr(), "Default should be disabled");
        config.setSkipOcr(true);
        assertTrue(config.isSkipOcr(), "Should reflect value set to true");
    }

    // setTrustedPageSeparator n'était exercé par aucun test existant.
    @Test
    public void testRuntimeConfigRejectsTrustedPageSeparator() {
        TesseractOCRConfig.RuntimeConfig runtimeConfig = new TesseractOCRConfig.RuntimeConfig();
        assertThrows(IllegalArgumentException.class,
                () -> runtimeConfig.setTrustedPageSeparator("|"));
    }
}
