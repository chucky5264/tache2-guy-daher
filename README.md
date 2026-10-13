# IFT3913 — Tâche 2 : augmentation d'une suite de tests automatique

| Nom complet | Identifiant GitHub |
|---|---|
| Guy Simon | chucky5264 |
| Poliquin Daher Étienne | Mad-Barry |

- Lien vers le répertoire GitHub : https://github.com/chucky5264/tache2-guy-daher
- Lien vers ce README : https://github.com/chucky5264/tache2-guy-daher/blob/main/README.md

Ce dépôt est un sous-ensemble réduit du [cas d'étude Tika](https://github.com/umontreal-diro/tika),
limité aux modules nécessaires pour construire et tester la classe choisie
(`tika-core`, `tika-annotation-processor`, `tika-serialization`,
`tika-parser-xmp-commons`, `tika-parser-image-module`, `tika-parser-ocr-module`).

## Classe sélectionnée et justification

Module : `tika-parser-ocr-module`
Classe : `org.apache.tika.parser.ocr.TesseractOCRConfig` (et sa classe imbriquée
`TesseractOCRConfig.RuntimeConfig`)

`TesseractOCRConfig` possède déjà une suite de tests (`TesseractOCRConfigTest`,
~20 tests), mais une analyse JaCoCo avant toute modification montre qu'elle
n'est pas couverte à 100% :

| Métrique | Avant | Couvert / Total |
|---|---|---|
| Instructions | 77% | 511 / 660 |
| Branches | 60% | 41 / 68 |
| Lignes | 72% | 124 / 171 |
| Méthodes | 72% | 36 / 50 |

La classe imbriquée `TesseractOCRConfig.RuntimeConfig` était à **0% de
couverture** (0/35 instructions, 0/5 méthodes) — aucune ligne de cette classe
n'était exercée par un test. La classe principale contient par ailleurs de
nombreuses méthodes de validation (regex, bornes numériques, listes de
valeurs autorisées) qui en font un bon candidat pour l'analyse de mutation.

---

## Partie 1 — Génération de tests par IA (ChatUniTest)

### Configuration

[ChatUniTest](https://github.com/ZJU-ACES-ISE/ChatUniTest) (plugin Maven `chatunitest-maven-plugin` 2.1.1) a été configuré sur `tika-parser-ocr-module`, pointé vers un modèle local servi par [Ollama](https://ollama.com) (`qwen2.5-coder:1.5b`, exposé sous le nom `code-llama` via l'API compatible OpenAI d'Ollama, `http://localhost:11434/v1/chat/completions`) :

```xml
<plugin>
  <groupId>io.github.zju-aces-ise</groupId>
  <artifactId>chatunitest-maven-plugin</artifactId>
  <version>2.1.1</version>
  <configuration>
    <apiKeys>ollama-local</apiKeys>
    <model>code-llama</model>
    <url>http://localhost:11434/v1/chat/completions</url>
    <testNumber>2</testNumber>
    <maxRounds>2</maxRounds>
  </configuration>
</plugin>
```

```bash
cd tika-parsers/tika-parsers-standard/tika-parsers-standard-modules/tika-parser-ocr-module
mvn chatunitest:class -DselectClass=org.apache.tika.parser.ocr.TesseractOCRConfig
```

`testNumber` et `maxRounds` ont été réduits de 5 à 2 par rapport aux valeurs par défaut : avec un modèle local de 1,5 milliard de paramètres sur CPU, chaque appel prend plusieurs dizaines de secondes, et un modèle de cette taille ne gagne pas vraiment en qualité en multipliant les tentatives (voir ci-dessous).

### Sélection des méthodes : ChatUniTest filtre lui-même

`TesseractOCRConfig` expose 48 méthodes publiques. ChatUniTest n'a pourtant tenté d'en tester que **2** : `getLangs(String, Set, Set)` et `addOtherTesseractConfig(String, String)`. Ce n'est pas un choix que nous avons fait — c'est le comportement par défaut de l'outil : la classe `Counter` de `chatunitest-core` (méthode `filter(MethodInfo)`) exclut automatiquement les constructeurs, les accesseurs (getters/setters) et les méthodes booléennes (`isXxx`) de la génération. Dans les logs :

```
Skip method: getPageSegMode() in class: org.apache.tika.parser.ocr.TesseractOCRConfig
Skip method: isPreserveInterwordSpacing() in class: org.apache.tika.parser.ocr.TesseractOCRConfig
Skip method: setDensity(int) in class: org.apache.tika.parser.ocr.TesseractOCRConfig
...
Processed getLangs(String, Set, Set)
Processed addOtherTesseractConfig(String, String)
```

C'est un point de comparaison important pour la Partie 3 : les 46 méthodes écartées par ChatUniTest sont précisément le type de méthodes (accesseurs, booléens) où nos tests manuels de la Partie 2 ont trouvé le plus de mutants survivants.

### Les tests générés compilent-ils sans intervention manuelle ? Non — une correction a été nécessaire

**Première exécution : 0/2 méthodes ont produit un test qui compile.** Le gabarit de test de ChatUniTest ajoute systématiquement `@ExtendWith(MockitoExtension.class)`, `@Mock`, `@Spy`, que la classe sous test en ait besoin ou non. Or `tika-parser-ocr-module/pom.xml` ne déclarait aucune dépendance Mockito (seul `tika-parent` gère la version `mockito.version` dans son `dependencyManagement`, sans qu'aucun module ne la déclenche). Erreur typique, inchangée après les 2 tours de réparation automatique :

```
Error in TesseractOCRConfig_getLangs_0_1_Test: line 6 : package org.mockito.junit.jupiter does not exist
Error in TesseractOCRConfig_getLangs_0_1_Test: line 25 : cannot find symbol
  symbol: class MockitoExtension
```

C'est une limite structurelle du mécanisme « Generation-Validation-Repair » : il corrige le *code* du test à partir des messages d'erreur, mais ne peut pas modifier le `pom.xml`. **Correction manuelle apportée** : ajout de `mockito-core` et `mockito-junit-jupiter` comme dépendances `test` dans `tika-parser-ocr-module/pom.xml`.

**Deuxième exécution, après correction :**

| Méthode | Tentatives (testNumber × maxRounds) | Résultat |
|---|---|---|
| `addOtherTesseractConfig(String, String)` | 2 variantes × 2 tours | Variante 0 : échec de compilation (2 tours épuisés). Variante 1 : **compile** et s'exécute → fusionnée dans `chatunitest-tests/.../TesseractOCRConfig_Suite.java` |
| `getLangs(String, Set, Set)` | 2 variantes × 2 tours | Échec de compilation sur les 2 variantes, malgré Mockito corrigé |

Pour `getLangs`, la cause n'est plus une dépendance manquante mais une **hallucination d'API** : le modèle génère des appels à `tesseractOCRConfig.getTrustedPageSeparator()` et `tesseractOCRConfig.getPreserveInterwordSpacing()`, qui n'existent pas (le vrai getter est `isPreserveInterwordSpacing()`, et `setTrustedPageSeparator` n'a pas de getter du tout). Il génère aussi, sans qu'on le lui demande, des tests pour des méthodes totalement différentes (`testGetMaxFileSizeToOcr`, `testGetTimeoutSeconds`) qui n'exercent jamais `getLangs`. Les 2 tours de réparation automatique n'ont pas suffi : corriger un nom de méthode inventé demande de comprendre l'API réelle, pas seulement de relire un message du compilateur.

**Bilan** : 1 correction manuelle (dépendance) a été nécessaire avant que quoi que ce soit compile ; après cette correction, 1 méthode sur 2 a produit un test exécutable sans autre intervention (`addOtherTesseractConfig`), l'autre (`getLangs`) n'a jamais abouti malgré 4 tentatives de génération au total.

Les tests générés se trouvent dans `tika-parsers/tika-parsers-standard/tika-parsers-standard-modules/tika-parser-ocr-module/chatunitest-tests/org/apache/tika/parser/ocr/` (emplacement par défaut du plugin, non fusionné avec `src/test/java` pour ne pas mélanger la suite générée et la suite manuelle de la Partie 2).

Enfin, le fichier généré ne respecte pas le style du projet : en l'exécutant avec checkstyle activé, 16 violations sont rapportées (imports inutilisés — `Logger`, `Pattern`, `FilenameUtils`, `TikaConfigException`... — et un champ `serialVersionUID` ajouté sur une classe de test qui n'implémente pas `Serializable`).

### Deuxième essai : un modèle local plus gros, sans le filtre de méthodes

En parallèle, une deuxième configuration a été testée avec un modèle local plus gros, construit avec Ollama à partir d'un `Modelfile` :

```
FROM qwen2.5-coder:3b
PARAMETER num_ctx 8192
```

(`codeqwen:v1.5-chat`, 3 milliards de paramètres contre 1,5 pour `code-llama` ci-dessus, avec une fenêtre de contexte doublée à 8192 tokens). Avec cette configuration, ChatUniTest a produit des tests compilables pour **12 méthodes supplémentaires** de `TesseractOCRConfig`, dont plusieurs accesseurs/booléens (`getColorspace`, `setColorspace`, `getDepth`, `isSkipOcr`, `isPreserveInterwordSpacing`, `setFilter`, `setPageSeparator`, `setTrustedPageSeparator`, `getPageSeparator`, `setMaxFileSizeToOcr`, `setOtherTesseractConfig`) ainsi qu'une seconde variante de `addOtherTesseractConfig`. Fait notable : plusieurs de ces méthodes (`getColorspace`, `isSkipOcr`, `setColorspace`...) sont précisément le type d'accesseur/booléen que le filtre `Counter.filter` de `chatunitest-core` exclut d'ordinaire (voir plus haut). Nous n'avons pas pu confirmer avec certitude pourquoi ce filtre ne s'est pas appliqué ici de la même façon — une piste, visible dans le bytecode de `ClassRunner.start()`, est que l'exécution multithread du plugin (`thread=true`, valeur par défaut) emprunte un chemin de code (`methodJob()`) distinct de la boucle filtrée que nous avions observée ; cela reste une hypothèse non vérifiée, pas une conclusion établie.

Les 12 fichiers compilent tous. En les exécutant (`mvn test -Dtest=...`) : 64 tests au total, **45 passent, 18 échouent, 1 erreur**. Détail et analyse des échecs dans la Partie 3 ci-dessous.

---

## Partie 2 — Analyse de mutation (pitest) et tests manuels

### Configuration

Le plugin `pitest-maven` (1.17.0) a été ajouté au `pom.xml` de
`tika-parser-ocr-module`, avec `pitest-junit5-plugin` (1.2.1) pour la
compatibilité JUnit 5 (les versions transitives de `junit-platform-engine` /
`junit-platform-launcher` apportées par ce plugin ont dû être exclues pour
éviter un conflit avec la version utilisée par le projet).

```bash
cd tika-parsers/tika-parsers-standard/tika-parsers-standard-modules/tika-parser-ocr-module
mvn org.pitest:pitest-maven:mutationCoverage
```

Rapport HTML généré dans `target/pit-reports/index.html`.

### Score de mutation avec les tests originaux

| Métrique | Valeur |
|---|---|
| Mutants générés | 70 |
| Mutants tués | 34 (**49%**) |
| Mutants non couverts (aucun test ne passe sur ce code) | 25 |
| Force des tests (tués / couverts) | 76% |
| Couverture de lignes (classes mutées) | 72% |

Détail par mutateur (avant) :

| Mutateur | Générés | Tués | % |
|---|---|---|---|
| PrimitiveReturnsMutator | 6 | 6 | 100% |
| MathMutator | 1 | 1 | 100% |
| EmptyObjectReturnValsMutator | 9 | 4 | 44% |
| NegateConditionalsMutator | 33 | 19 | 58% |
| ConditionalsBoundaryMutator | 3 | 1 | 33% |
| VoidMethodCallMutator | 5 | 1 | 20% |
| BooleanTrueReturnValsMutator | 6 | 1 | 17% |
| BooleanFalseReturnValsMutator | 6 | 1 | 17% |
| NullReturnValsMutator | 1 | 0 | 0% |

Ce résultat démontre concrètement ce que la littérature du cours souligne
(cf. lecture Google "Code Coverage Best Practices", semaine 4) : une
couverture de lignes de 72% ne garantit absolument pas un score de mutation
équivalent (49% ici) — plusieurs lignes exécutées par les tests n'avaient
pas d'assertion assez précise pour détecter une erreur injectée.

### Score de mutation avec les tests générés par ChatUniTest (avant tests manuels)

Sur les 13 fichiers générés par ChatUniTest (Partie 1), 7 sont entièrement verts (`getColorspace`, `getDepth`, `getPageSeparator`, `isSkipOcr`, `setMaxFileSizeToOcr`, `setOtherTesseractConfig`, `setPageSeparator` — 25 tests). Les 6 autres contiennent au moins un test qui échoue (oracles incorrects, détaillés en Partie 3) ; pitest exige une suite entièrement verte ("Mutation testing requires a green suite"), donc ils sont exclus de cette mesure précise.

En ajoutant ces 7 fichiers verts aux tests originaux (sans les tests manuels de la section suivante) :

| Métrique | Tests originaux seuls | + tests générés (verts) |
|---|---|---|
| Mutants tués | 34 / 70 (49%) | **40 / 70 (57%)** |
| Couverture de lignes | 72% | 71% |

Les tests générés tuent 6 mutants supplémentaires, tous sur des méthodes qu'ils ciblent spécifiquement :
- `setOtherTesseractConfig` : 4 mutants (`NegateConditionalsMutator` ×2, `EmptyObjectReturnValsMutator`, `VoidMethodCallMutator`) — les tests `testSetOtherTesseractConfig_InvalidKey/_MultipleEntries/_NullMap` exercent la boucle `for (Map.Entry...)` et sa condition de garde `if (otherTesseractConfig != null)`, jamais testée directement avant.
- `getColorspace` : 2 mutants (`EmptyObjectReturnValsMutator`, `NegateConditionalsMutator`) — `testGetColorspaceWithNull`/`WithSet` relisent la valeur après construction, jamais vérifié avant.
- `isSkipOcr` et `setMaxFileSizeToOcr` : 1 mutant chacun (retours booléen/primitif), déjà couverts partiellement mais désormais tués avec certitude par la relecture explicite du getter.

Il reste **30 mutants non détectés** (20 sans aucune couverture, 10 couverts mais survivants) après les tests générés seuls — ChatUniTest ne détecte donc pas tous les mutants, ce qui motive les tests manuels ci-dessous.

### Tests manuels ajoutés

13 tests ont été ajoutés dans `TesseractOCRConfigTest.java` pour cibler
précisément les mutants survivants ou non couverts identifiés par pitest.
Chaque test est documenté dans le code (intention, motivation des données de
test, explication de l'oracle) ; résumé ci-dessous :

| Test | Intention | Données choisies | Oracle |
|---|---|---|---|
| `testValidateDensityBoundaries` | Tuer les mutants de frontière sur `density < 150 \|\| density > 1200` | 150, 1200 (limites valides), 149, 1201 (juste hors limites) | Les limites sont acceptées, les valeurs juste hors limites lèvent `IllegalArgumentException` |
| `testColorspaceNullThrows` | Tuer la négation du contrôle de nullité de `setColorspace` | `null` | `IllegalArgumentException` attendue |
| `testOutputTypeStringRoundTrip` | Vérifier le résultat réel de `setOutputType("txt"/"hocr")`, jamais vérifié avant | `"txt"`, `"hocr"` | `getOutputType()` reflète `TXT`/`HOCR` |
| `testEnableImagePreprocessingRoundTrip` | Getter booléen jamais vérifié après le setter | `false` (défaut), `true` | Le getter reflète la valeur passée |
| `testPreloadLangsRoundTrip` | Idem pour `isPreloadLangs` | `false`, `true` | Idem |
| `testTesseractPathRoundTrip` | `getTesseractPath` jamais vérifié avec un chemin valide | chemin sans séparateur final | Chemin normalisé avec séparateur final ajouté |
| `testImageMagickPathRoundTrip` | Idem pour `imageMagickPath` | idem | idem |
| `testTessdataPathRoundTrip` | Idem pour `tessdataPath` | idem | idem |
| `testSetOtherTesseractConfigMap` | `setOtherTesseractConfig(Map)` jamais appelé directement | Map à une entrée valide | `getOtherTesseractConfig()` contient l'entrée |
| `testInlineContentRoundTrip` | Getter/setter jamais exercés | `false`, `true` | Le getter reflète la valeur |
| `testPageSeparatorRoundTrip` | Valeur vide (no-op), séparateur valide, séparateur invalide jamais testés | `""`, `"end-of-page"`, `"<script>"` | Vide → inchangé ; valide → appliqué ; invalide → exception |
| `testPreserveInterwordSpacingRoundTrip` | Getter/setter jamais exercés | `false`, `true` | Le getter reflète la valeur |
| `testSkipOcrRoundTrip` | Getter/setter jamais exercés | `false`, `true` | Le getter reflète la valeur |
| `testRuntimeConfigRejectsPathChanges` | `RuntimeConfig` était à 0% de couverture ; documente son comportement métier (interdiction de modifier les chemins à l'exécution) | valeur non vide pour chaque chemin | `TikaConfigException` attendue |
| `testRuntimeConfigAllowsBlankPaths` | Vérifie la branche `StringUtils.isBlank` de `RuntimeConfig` | `""`, `null` | Aucune exception |
| `testRuntimeConfigRejectsTrustedPageSeparator` | Méthode jamais exercée | toute valeur | `IllegalArgumentException` attendue |

### Score de mutation final

Les tests manuels ci-dessus ont été écrits et validés contre les tests originaux (49% → 100%), indépendamment des tests générés (qui, seuls, atteignent 57% — voir ci-dessus).

| Métrique | Avant | Après |
|---|---|---|
| Mutants tués | 34 / 70 (49%) | **70 / 70 (100%)** |
| Mutants non couverts | 25 | **0** |
| Force des tests | 76% | **100%** |
| Couverture de lignes | 72% | 96% |

Tous les mutants survivants et non couverts identifiés initialement ont été
éliminés par les 13 tests manuels ci-dessus — aucun mutant ne survit sur
`TesseractOCRConfig` ni sur `TesseractOCRConfig.RuntimeConfig`.

### Exécution

Vérifié localement (deux exécutions consécutives, résultats identiques) et
en intégration continue : [![build](https://github.com/chucky5264/tache2-guy-daher/actions/workflows/build.yml/badge.svg)](https://github.com/chucky5264/tache2-guy-daher/actions/workflows/build.yml)

---

## Partie 3 — Comparaison IA vs tests manuels

### Les deux approches ciblent des méthodes différentes

Le filtre par défaut de ChatUniTest (voir Partie 1) exclut les accesseurs, ce qui fait que les deux suites sont presque entièrement **complémentaires plutôt que redondantes** : nos 13 tests manuels de la Partie 2 couvrent des getters/setters/booléens (`isEnableImagePreprocessing`, `setColorspace`, bornes de `setDensity`, `RuntimeConfig`, etc.) — exactement le type de méthode que ChatUniTest écarte d'emblée. ChatUniTest, lui, n'a tenté que les 2 méthodes qui ne sont pas de simples accesseurs.

### Qualité des oracles sur le seul test IA qui compile et s'exécute

Le seul test généré utilisable est `TesseractOCRConfig_addOtherTesseractConfig_41_1_Test` (6 méthodes de test, 3 réussissent, 3 échouent à l'exécution — vérifié avec `mvn test -Dtest=...`) :

| Test généré par l'IA | Oracle | Verdict |
|---|---|---|
| `testAddOtherTesseractConfigWithValidKeyAndValue` | `addOtherTesseractConfig("key1","value1")` puis vérifie que `getOtherTesseractConfig()` contient l'entrée | **Correct et pertinent** — exerce le chemin nominal |
| `testAddOtherTesseractConfigWithInvalidKey` | clé `null` → attend `IllegalArgumentException` avec le message exact `"key must not be null"` | **Correct et précis** — le message correspond mot pour mot au code source (`TesseractOCRConfig.java:524`) |
| `testAddOtherTesseractConfigWithInvalidValue` | valeur `null` → attend `IllegalArgumentException("value must not be null")` | **Correct et précis**, même constat |
| `testAddOtherTesseractConfigWithInvalidKeyPattern` | appelle `addOtherTesseractConfig("key1", value)` — **la même chaîne valide que le test 1** — et attend quand même une `IllegalArgumentException` | **Faux** — "key1" ne contient aucun caractère interdit par `ALLOWABLE_OTHER_PARAMS_PATTERN`, la méthode ne lève rien ; le test échoue. Le nom du test promet une clé invalide, les données ne le sont pas. |
| `testAddOtherTesseractConfigWithInvalidValuePattern` | même défaut, sur la valeur cette fois | **Faux**, même raison |
| `testAddOtherTesseractConfigWithExistingKey` | ajoute `key1`→`value1`, ré-ajoute `key1`→`value2`, attend `IllegalArgumentException("Key already exists: key1")` | **Faux — comportement inventé** : `addOtherTesseractConfig` n'a aucune vérification de clé dupliquée ; un second appel écrase silencieusement la valeur (`otherTesseractConfig.put(...)`). L'oracle décrit un comportement qui n'existe pas dans le code. |

La moitié des oracles générés sont donc **corrects et même assez précis** (messages d'exception vérifiés au caractère près) : à chaque fois que le test exerce un chemin que le modèle "comprend" bien (contrôle de nullité explicite, très visible dans du code générique de validation), l'oracle est bon. Mais dès que le nom du test implique une invariant plus subtile (motif regex, unicité de clé), le modèle invente une donnée de test qui ne correspond pas à l'intention annoncée par le nom de la méthode de test, et un comportement attendu qui n'existe pas dans le code réel — contrairement à nos tests manuels (Partie 2), construits directement à partir des mutants pitest survivants et de la lecture du code source (`ALLOWABLE_PAGE_SEPARATORS_PATTERN`, bornes `150`/`1200`, etc.), donc nécessairement ancrés dans le comportement réel.

### Deuxième jeu de données : 12 méthodes supplémentaires (modèle `codeqwen:v1.5-chat`)

Vérifié par exécution réelle (`mvn test`), pas par lecture :

| Méthode | Tests | Résultat | Cause des échecs |
|---|---|---|---|
| `getColorspace`, `getDepth`, `getPageSeparator`, `isSkipOcr`, `setPageSeparator`, `setMaxFileSizeToOcr`, `setOtherTesseractConfig` | 25 | **25/25 passent** | — |
| `isPreserveInterwordSpacing` | 3 | 2/3 | Le test lui-même est buggé : son commentaire dit "default should be false" mais il appelle `assertTrue` au lieu de `assertFalse`. |
| `setColorspace` | 12 | 6/12 | Le modèle suppose que chiffres/majuscules/tiret/underscore sont invalides, alors que le vrai pattern `(?i)^[-_A-Z0-9]+$` les accepte tous. |
| `setFilter` | 10 | 4/10 | Même erreur que ci-dessus pour `setFilter(null)` (le code source a un vrai bug : `filter.equals(null)` lève `NullPointerException`, pas `IllegalArgumentException` — le modèle assume le comportement conventionnel) ; et le modèle suppose à tort que le setter normalise la casse (ex. `"TriaNgLE"` → `"triangle"`), alors que la valeur est stockée telle quelle. |
| `setTrustedPageSeparator` | 9 | 5/9 | 3 échecs : le modèle croit que cette méthode valide son entrée alors que c'est justement la variante *sans* validation (son javadoc le dit explicitement). 1 erreur : bug Java dans le test généré lui-même — `invoke(config, null)` est interprété par la réflexion comme "aucun argument" plutôt que "un argument nul". |
| `addOtherTesseractConfig` (variante 41_0, modèle différent de la nôtre) | 5 | 3/5 | Même erreur que `setColorspace` : `"invalid-key"` contient un tiret, autorisé par `ALLOWABLE_OTHER_PARAMS_PATTERN`, donc pas rejeté — contrairement à l'hypothèse du test. |

Le même biais revient sur 3 méthodes indépendantes (`setColorspace`, `setFilter`, `addOtherTesseractConfig`) : le modèle suppose qu'une chaîne "qui a l'air bizarre" (tiret, chiffre, casse mixte) doit être rejetée, sans jamais vérifier le pattern regex réel — et à l'inverse pour `setTrustedPageSeparator`, il suppose une validation qui n'existe pas. C'est le même défaut que celui identifié plus haut sur `addOtherTesseractConfig_41_1` (Partie 3), à plus grande échelle : l'oracle est plausible, pas vérifié contre le code.

### En résumé

- **Couverture** : avec le modèle `code-llama`/1,5B, l'IA ne touche pas aux accesseurs (filtrés par conception) ; avec `codeqwen:v1.5-chat`/3B, elle les couvre aussi. Le manuel (Partie 2) ciblait les mêmes accesseurs, donc les deux runs IA se recoupent partiellement avec le travail manuel une fois le modèle plus gros utilisé.
- **Fiabilité d'exécution, cumulée sur les 13 fichiers générés (les deux modèles)** : 70 tests exécutés, **48 passent (69%)**, 21 échouent, 1 erreur.
- **Pertinence des oracles** : bonne sur les cas triviaux (contrôle de nullité, lecture d'un accesseur), mauvaise dès qu'il faut vérifier une règle précise (pattern regex, normalisation de casse, présence/absence de validation) — le même biais (« ça a l'air invalide donc ça doit lever une exception ») revient sur `setColorspace`, `setFilter`, `addOtherTesseractConfig` et `setTrustedPageSeparator`, sans jamais être vérifié contre le code réel.
- **Bugs propres aux tests générés, indépendants de la logique métier** : une assertion inversée (`isPreserveInterwordSpacing`) et un bug d'API Java (appel de réflexion avec un argument `null` mal formé).
- **Mutants** : les mutants que nos tests manuels de la Partie 2 ont tués (cf. tableau mutation) portent tous sur des méthodes aussi couvertes par le deuxième run IA (`codeqwen`) ; il y a donc un vrai recoupement possible pour une analyse de mutation combinée, non réalisée ici par manque de temps.

---

## Déclaration d'utilisation de l'IA

Conformément à la politique de l'Université de Montréal sur l'IA générative
([Citer, signaler, déclarer et documenter](https://boite-outils.bib.umontreal.ca/c.php?g=743753&p=5377614)),
nous déclarons avoir utilisé l'assistant Claude (Claude Code, Anthropic)
comme outil d'appui, selon les modalités suivantes.

### Partie 2 — Analyse de mutation et tests manuels

Claude a été consulté pour :
- diagnostiquer le conflit de dépendances JUnit lors de l'intégration de pitest ;
- suggérer des ébauches de tests pour certains mutants, que nous avons
  ensuite adaptées et complétées ;
- reformuler certains passages du README.
- le dépouillement des journaux et des fichiers `chatunitest-info`/`history`,
  afin de dresser la liste des méthodes testées et des tests en échec ;
  - le débogage technique de l'exécution de ChatUniTest avec un modèle Ollama
  local (dépendance Mockito manquante, décompilation de `chatunitest-core`
  pour comprendre le filtrage des méthodes dans `Counter.filter`) ;


Tout contenu produit avec l'aide de Claude a été relu et vérifié par
l'équipe avant d'être intégré.
