package securitybug.similarityuniformfuzzyhash;

import static securitybug.similarityuniformfuzzyhash.ToStringUtils.DECIMALS_FORMAT;
import static securitybug.similarityuniformfuzzyhash.ToStringUtils.DECIMALS_FORMAT_STR;
import static securitybug.similarityuniformfuzzyhash.ToStringUtils.HASHES_TO_HASH_STR;
import static securitybug.similarityuniformfuzzyhash.ToStringUtils.HASH_TO_HASHES_STR;
import static securitybug.similarityuniformfuzzyhash.ToStringUtils.IGNORE_MARK;
import static securitybug.similarityuniformfuzzyhash.ToStringUtils.NAME_SEPARATOR;
import static securitybug.similarityuniformfuzzyhash.ToStringUtils.NULL_VALUE;
import static securitybug.similarityuniformfuzzyhash.ToStringUtils.TAB;
import static securitybug.similarityuniformfuzzyhash.ToStringUtils.UFH_FILES_ECONDING;
import static securitybug.similarityuniformfuzzyhash.ToStringUtils.checkHashName;
import static securitybug.similarityuniformfuzzyhash.ToStringUtils.checkName;
import static securitybug.similarityuniformfuzzyhash.ToStringUtils.getMaxLength;
import static securitybug.similarityuniformfuzzyhash.ToStringUtils.hyphens;
import static securitybug.similarityuniformfuzzyhash.ToStringUtils.spaces;

import securitybug.similarityuniformfuzzyhash.ToStringUtils.HashCharacteristics;

import org.apache.commons.io.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * This class provides utility static methods related to the Uniform Fuzzy Hash usage.
 * 
 * @author s3curitybug@gmail.com
 *
 */
public final class UniformFuzzyHashes {

    /**
     * Enum of criterias to sort Uniform Fuzzy Hashes by their similarity to another Uniform Fuzzy
     * Hash.
     */
    public enum SimilaritySortCriterias {

        /** Sort by ascending hash similarity to the hashes. */
        HASH_TO_HASHES_ASC,

        /** Sort by descending hash similarity to the hashes. */
        HASH_TO_HASHES_DESC,

        /** Sort by ascending hashes similarity to the hash. */
        HASHES_TO_HASH_ASC,

        /** Sort by descending hashes similarity to the hash. */
        HASHES_TO_HASH_DESC

    }

    /**
     * Private constructor.
     */
    private UniformFuzzyHashes() {

    }

    /**
     * Computes a list of Uniform Fuzzy Hashes from a collection of byte arrays of data and a
     * factor.
     * 
     * @param byteArrays Collection of byte arrays of data.
     * @param factor Relation between data length and the hash mean number of blocks for each byte
     *        array of data.
     * @return List of computed Uniform Fuzzy Hashes.
     */
    public static List<UniformFuzzyHash> computeHashesFromByteArrays(
            Collection<byte[]> byteArrays,
            int factor) {

        if (byteArrays == null) {
            throw new NullPointerException("Collection of byte arrays is null.");
        }

        List<UniformFuzzyHash> hashes =
                new ArrayList<UniformFuzzyHash>(byteArrays.size());

        for (byte[] byteArray : byteArrays) {

            if (byteArray == null) {
                hashes.add(null);
            } else {
                UniformFuzzyHash hash = new UniformFuzzyHash(byteArray, factor);
                hashes.add(hash);
            }

        }

        return hashes;

    }

    /**
     * Computes a map from names to Uniform Fuzzy Hashes from a map from names to byte arrays of
     * data and a factor.
     * 
     * @param namesToByteArrays Map from names to byte arrays of data.
     * @param factor Relation between data length and the hash mean number of blocks for each byte
     *        array of data.
     * @return Map from names to computed Uniform Fuzzy Hashes.
     */
    public static Map<String, UniformFuzzyHash> computeNamedHashesFromNamedByteArrays(
            Map<String, byte[]> namesToByteArrays,
            int factor) {

        if (namesToByteArrays == null) {
            throw new NullPointerException("Map of byte arrays is null.");
        }

        Set<String> names = namesToByteArrays.keySet();
        Map<String, UniformFuzzyHash> namesToHashes =
                new LinkedHashMap<String, UniformFuzzyHash>(names.size());

        for (String name : names) {

            byte[] byteArray = namesToByteArrays.get(name);

            if (byteArray == null) {
                namesToHashes.put(name, null);
            } else {
                UniformFuzzyHash hash = new UniformFuzzyHash(byteArray, factor);
                namesToHashes.put(name, hash);
            }

        }

        return namesToHashes;

    }

    /**
     * Computes a list of Uniform Fuzzy Hashes from a collection of strings of data (using the
     * platform's default charset) and a factor.
     * 
     * @param strings Collection of strings of data.
     * @param factor Relation between data length and the hash mean number of blocks for each string
     *        of data.
     * @return List of computed Uniform Fuzzy Hashes.
     */
    public static List<UniformFuzzyHash> computeHashesFromStrings(
            Collection<String> strings,
            int factor) {

        if (strings == null) {
            throw new NullPointerException("Collection of strings is null.");
        }

        List<UniformFuzzyHash> hashes =
                new ArrayList<UniformFuzzyHash>(strings.size());

        for (String string : strings) {

            if (string == null) {
                hashes.add(null);
            } else {
                UniformFuzzyHash hash = new UniformFuzzyHash(string, factor);
                hashes.add(hash);
            }

        }

        return hashes;

    }

    /**
     * Computes a map from names to Uniform Fuzzy Hashes from a map from names to strings of data
     * (using the platform's default charset) and a factor.
     * 
     * @param namesToStrings Map from names to strings of data.
     * @param factor Relation between data length and the hash mean number of blocks for each string
     *        of data.
     * @return Map from names to computed Uniform Fuzzy Hashes.
     */
    public static Map<String, UniformFuzzyHash> computeNamedHashesFromNamedStrings(
            Map<String, String> namesToStrings,
            int factor) {

        if (namesToStrings == null) {
            throw new NullPointerException("Map of strings is null.");
        }

        Set<String> names = namesToStrings.keySet();
        Map<String, UniformFuzzyHash> namesToHashes =
                new LinkedHashMap<String, UniformFuzzyHash>(names.size());

        for (String name : names) {

            String string = namesToStrings.get(name);

            if (string == null) {
                namesToHashes.put(name, null);
            } else {
                UniformFuzzyHash hash = new UniformFuzzyHash(string, factor);
                namesToHashes.put(name, hash);
            }

        }

        return namesToHashes;

    }

    /**
     * Computes a list of Uniform Fuzzy Hashes from a collection of input streams of data and a
     * factor.
     * 
     * @param inputStreams Collection of input streams of data.
     * @param factor Relation between data length and the hash mean number of blocks for each input
     *        stream of data.
     * @return List of computed Uniform Fuzzy Hashes.
     * @throws IOException If an IOException occurs reading any of the input streams of data.
     */
    public static List<UniformFuzzyHash> computeHashesFromInputStreams(
            Collection<InputStream> inputStreams,
            int factor)
            throws IOException {

        if (inputStreams == null) {
            throw new NullPointerException("Collection of input streams is null.");
        }

        List<UniformFuzzyHash> hashes =
                new ArrayList<UniformFuzzyHash>(inputStreams.size());

        for (InputStream inputStream : inputStreams) {

            if (inputStream == null) {
                hashes.add(null);
            } else {
                UniformFuzzyHash hash = new UniformFuzzyHash(inputStream, factor);
                hashes.add(hash);
            }

        }

        return hashes;

    }

    /**
     * Computes a map from names to Uniform Fuzzy Hashes from a map from names to input streams of
     * data and a factor.
     * 
     * @param namesToInputStreams Map from names to input streams of data.
     * @param factor Relation between data length and the hash mean number of blocks for each input
     *        stream of data.
     * @return Map from names to computed Uniform Fuzzy Hashes.
     * @throws IOException If an IOException occurs reading any of the input streams of data.
     */
    public static Map<String, UniformFuzzyHash> computeNamedHashesFromNamedInputStreams(
            Map<String, InputStream> namesToInputStreams,
            int factor)
            throws IOException {

        if (namesToInputStreams == null) {
            throw new NullPointerException("Map of input streams is null.");
        }

        Set<String> names = namesToInputStreams.keySet();
        Map<String, UniformFuzzyHash> namesToHashes =
                new LinkedHashMap<String, UniformFuzzyHash>(names.size());

        for (String name : names) {

            InputStream inputStream = namesToInputStreams.get(name);

            if (inputStream == null) {
                namesToHashes.put(name, null);
            } else {
                UniformFuzzyHash hash = new UniformFuzzyHash(inputStream, factor);
                namesToHashes.put(name, hash);
            }

        }

        return namesToHashes;

    }

    /**
     * Computes a list of Uniform Fuzzy Hashes from a collection of byte array output streams of
     * data and a factor.
     * 
     * @param byteArrayOutputStreams Collection of byte array output streams of data.
     * @param factor Relation between data length and the hash mean number of blocks for each string
     *        of data.
     * @return List of computed Uniform Fuzzy Hashes.
     */
    public static List<UniformFuzzyHash> computeHashesFromByteArrayOutputStreams(
            Collection<ByteArrayOutputStream> byteArrayOutputStreams,
            int factor) {

        if (byteArrayOutputStreams == null) {
            throw new NullPointerException("Collection of byte array output streams is null.");
        }

        List<UniformFuzzyHash> hashes =
                new ArrayList<UniformFuzzyHash>(byteArrayOutputStreams.size());

        for (ByteArrayOutputStream byteArrayOutputStream : byteArrayOutputStreams) {

            if (byteArrayOutputStream == null) {
                hashes.add(null);
            } else {
                UniformFuzzyHash hash = new UniformFuzzyHash(byteArrayOutputStream, factor);
                hashes.add(hash);
            }

        }

        return hashes;

    }

    /**
     * Computes a map from names to Uniform Fuzzy Hashes from a map from names to byte array output
     * streams of data and a factor.
     * 
     * @param namesToByteArrayOutputStreams Map from names to byte array output streams of data.
     * @param factor Relation between data length and the hash mean number of blocks for each input
     *        stream of data.
     * @return Map from names to computed Uniform Fuzzy Hashes.
     */
    public static Map<String, UniformFuzzyHash> computeNamedHashesFromNamedByteArrayOutputStreams(
            Map<String, ByteArrayOutputStream> namesToByteArrayOutputStreams,
            int factor) {

        if (namesToByteArrayOutputStreams == null) {
            throw new NullPointerException("Map of byte array output streams is null.");
        }

        Set<String> names = namesToByteArrayOutputStreams.keySet();
        Map<String, UniformFuzzyHash> namesToHashes =
                new LinkedHashMap<String, UniformFuzzyHash>(names.size());

        for (String name : names) {

            ByteArrayOutputStream byteArrayOutputStream = namesToByteArrayOutputStreams.get(name);

            if (byteArrayOutputStream == null) {
                namesToHashes.put(name, null);
            } else {
                UniformFuzzyHash hash = new UniformFuzzyHash(byteArrayOutputStream, factor);
                namesToHashes.put(name, hash);
            }

        }

        return namesToHashes;

    }

    /**
     * Computes a list of Uniform Fuzzy Hashes from a collection of files of data and a factor.
     * Files which do not exist are ignored.
     * 
     * @param files Collection of files of data.
     * @param factor Relation between data length and the hash mean number of blocks for each file
     *        of data.
     * @param nested True to read files inside directories recursively. False to ignore directories.
     * @return List of computed Uniform Fuzzy Hashes.
     * @throws IOException If an IOException occurs reading any of the files of data.
     */
    public static List<UniformFuzzyHash> computeHashesFromFiles(
            Collection<File> files,
            int factor,
            boolean nested)
            throws IOException {

        if (files == null) {
            throw new NullPointerException("Collection of files is null.");
        }

        List<UniformFuzzyHash> hashes = new ArrayList<UniformFuzzyHash>(files.size());

        for (File file : files) {

            if (file == null) {
                hashes.add(null);
            } else if (file.exists()) {
                if (file.isFile()) {
                    UniformFuzzyHash hash = new UniformFuzzyHash(file, factor);
                    hashes.add(hash);
                } else if (file.isDirectory() && nested) {
                    hashes.addAll(computeHashesFromFiles(
                            Arrays.asList(file.listFiles()), factor, nested));
                }
            }

        }

        return hashes;

    }

    /**
     * Computes a map from names to Uniform Fuzzy Hashes from a map from names to files of data and
     * a factor. Files which do not exist and directories are ignored.
     * 
     * @param namesToFiles Map from names to files of data.
     * @param factor Relation between data length and the hash mean number of blocks for each file
     *        of data.
     * @return Map from names to computed Uniform Fuzzy Hashes.
     * @throws IOException If an IOException occurs reading any of the files of data.
     */
    public static Map<String, UniformFuzzyHash> computeNamedHashesFromNamedFiles(
            Map<String, File> namesToFiles,
            int factor)
            throws IOException {

        if (namesToFiles == null) {
            throw new NullPointerException("Map of files is null.");
        }

        Set<String> names = namesToFiles.keySet();
        Map<String, UniformFuzzyHash> namesToHashes =
                new LinkedHashMap<String, UniformFuzzyHash>(names.size());

        for (String name : names) {

            File file = namesToFiles.get(name);

            if (file == null) {
                namesToHashes.put(name, null);
            } else if (file.exists() && file.isFile()) {
                UniformFuzzyHash hash = new UniformFuzzyHash(file, factor);
                namesToHashes.put(name, hash);
            }

        }

        return namesToHashes;

    }

    /**
     * Computes a map from names to Uniform Fuzzy Hashes from a collection of files of data (using
     * its names) and a factor. Files which do not exist are ignored.
     * 
     * @param files Collection of files of data.
     * @param factor Relation between data length and the hash mean number of blocks for each file
     *        of data.
     * @param nested True to read files inside directories recursively. False to ignore directories.
     * @return Map from names to computed Uniform Fuzzy Hashes.
     * @throws IOException If an IOException occurs reading any of the files of data.
     */
    public static Map<String, UniformFuzzyHash> computeNamedHashesFromFiles(
            Collection<File> files,
            int factor,
            boolean nested)
            throws IOException {

        if (files == null) {
            throw new NullPointerException("Collection of files is null.");
        }

        Map<String, UniformFuzzyHash> namesToHashes =
                new LinkedHashMap<String, UniformFuzzyHash>(files.size());

        for (File file : files) {

            if (file != null && file.exists()) {
                if (file.isFile()) {
                    UniformFuzzyHash hash = new UniformFuzzyHash(file, factor);
                    namesToHashes.put(file.getName(), hash);
                } else if (file.isDirectory() && nested) {
                    namesToHashes.putAll(computeNamedHashesFromFiles(
                            Arrays.asList(file.listFiles()), factor, nested));
                }
            }

        }

        return namesToHashes;

    }

    /**
     * Computes a list of Uniform Fuzzy Hashes from the files of a directory and a factor. Files
     * which do not exist are ignored.
     * 
     * @param directory Directory of files.
     * @param factor Relation between data length and the hash mean number of blocks for each file
     *        of data.
     * @param nested True to read files inside directories recursively. False to ignore directories.
     * @return List of computed Uniform Fuzzy Hashes.
     * @throws IOException If an IOException occurs reading any of the files of data.
     */
    public static List<UniformFuzzyHash> computeHashesFromDirectoryFiles(
            File directory,
            int factor,
            boolean nested)
            throws IOException {

        if (directory == null) {
            throw new NullPointerException("Directory is null.");
        }

        if (!directory.exists()) {
            throw new IllegalArgumentException("Directory does not exist.");
        }

        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("Directory is not a directory.");
        }

        return computeHashesFromFiles(Arrays.asList(directory.listFiles()), factor, nested);

    }

    /**
     * Computes a map from names to Uniform Fuzzy Hashes from the files of a directory (using its
     * names) and a factor. Files which do not exist are ignored.
     * 
     * @param directory Directory of files.
     * @param factor Relation between data length and the hash mean number of blocks for each file
     *        of data.
     * @param nested True to read files inside directories recursively. False to ignore directories.
     * @return Map from names to computed Uniform Fuzzy Hashes.
     * @throws IOException If an IOException occurs reading any of the files of data.
     */
    public static Map<String, UniformFuzzyHash> computeNamedHashesFromDirectoryFiles(
            File directory,
            int factor,
            boolean nested)
            throws IOException {

        if (directory == null) {
            throw new NullPointerException("Directory is null.");
        }

        if (!directory.exists()) {
            throw new IllegalArgumentException("Directory does not exist.");
        }

        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("Directory is not a directory.");
        }

        return computeNamedHashesFromFiles(Arrays.asList(directory.listFiles()), factor, nested);

    }

    /**
     * Rebuilds a list of Uniform Fuzzy Hashes from a collection of strings representing them.
     * 
     * @param hashStrings Collection of strings representing Uniform Fuzzy Hashes.
     * @return List of rebuilt Uniform Fuzzy Hashes.
     */
    public static List<UniformFuzzyHash> rebuildHashes(
            Collection<String> hashStrings) {

        if (hashStrings == null) {
            throw new NullPointerException("Collection of hash strings is null.");
        }

        List<UniformFuzzyHash> hashes = new ArrayList<UniformFuzzyHash>(hashStrings.size());

        int i = 0;
        for (String hashString : hashStrings) {

            if (hashString == null) {

                hashes.add(null);

            } else {

                UniformFuzzyHash hash = null;

                try {
                    hash = new UniformFuzzyHash(hashString);
                } catch (IllegalArgumentException illegalArgumentException) {
                    throw new IllegalArgumentException(String.format(
                            "Hash %d could not be parsed. %s",
                            i,
                            illegalArgumentException.getMessage()));
                }

                hashes.add(hash);

            }

            i++;

        }

        return hashes;

    }

    /**
     * Rebuilds a map from names to Uniform Fuzzy Hashes from a map from names to strings
     * representing them.
     * 
     * @param namesToHashStrings Map from names to strings representing Uniform Fuzzy Hashes.
     * @return Map from names to rebuilt Uniform Fuzzy Hashes.
     */
    public static Map<String, UniformFuzzyHash> rebuildNamedHashes(
            Map<String, String> namesToHashStrings) {

        if (namesToHashStrings == null) {
            throw new NullPointerException("Map of hash strings is null.");
        }

        Set<String> names = namesToHashStrings.keySet();
        Map<String, UniformFuzzyHash> namesToHashes =
                new LinkedHashMap<String, UniformFuzzyHash>(names.size());

        for (String name : names) {

            String hashString = namesToHashStrings.get(name);

            if (hashString == null) {

                namesToHashes.put(name, null);

            } else {

                UniformFuzzyHash hash = null;

                try {
                    hash = new UniformFuzzyHash(hashString);
                } catch (IllegalArgumentException illegalArgumentException) {
                    throw new IllegalArgumentException(String.format(
                            "Hash %s could not be parsed. %s",
                            name,
                            illegalArgumentException.getMessage()));
                }

                namesToHashes.put(name, hash);

            }

        }

        return namesToHashes;

    }

    /**
     * Builds a list of strings representing a collection of Uniform Fuzzy Hashes.
     * 
     * @param hashes Collection of Uniform Fuzzy Hashes.
     * @return List of strings representing the hashes.
     */
    public static List<String> hashesToString(
            Collection<UniformFuzzyHash> hashes) {

        if (hashes == null) {
            throw new NullPointerException("Collection of hashes is null.");
        }

        List<String> hashStrings = new ArrayList<String>(hashes.size());

        for (UniformFuzzyHash hash : hashes) {

            if (hash == null) {
                hashStrings.add(null);
            } else {
                String hashString = hash.toString();
                hashStrings.add(hashString);
            }

        }

        return hashStrings;

    }

    /**
     * Builds a map from names to strings representing a map from names to Uniform Fuzzy Hashes.
     * 
     * @param namesToHashes Map from names to Uniform Fuzzy Hashes.
     * @return Map from names to strings representing the hashes.
     */
    public static Map<String, String> namedHashesToString(
            Map<String, UniformFuzzyHash> namesToHashes) {

        if (namesToHashes == null) {
            throw new NullPointerException("Map of hashes is null.");
        }

        Set<String> names = namesToHashes.keySet();
        Map<String, String> namesToHashStrings = new LinkedHashMap<String, String>(names.size());

        for (String name : names) {

            UniformFuzzyHash hash = namesToHashes.get(name);

            if (hash == null) {
                namesToHashStrings.put(name, null);
            } else {
                String hashString = hash.toString();
                namesToHashStrings.put(name, hashString);
            }

        }

        return namesToHashStrings;

    }

    /**
     * Writes a Uniform Fuzzy Hash into a text file.
     * 
     * @param hashName The Uniform Fuzzy Hash name.
     * @param hash The Uniform Fuzzy Hash.
     * @param file The file to save the hash.
     * @param append True to append the hash at the end of the file. False to overwrite the file.
     * @throws IOException If an IOException occurs writing into the file.
     */
    public static void saveToFile(
            String hashName,
            UniformFuzzyHash hash,
            File file,
            boolean append)
            throws IOException {

        if (hash == null) {
            throw new NullPointerException("Hash is null.");
        }

        if (file == null) {
            throw new NullPointerException("File is null");
        }

        Map<String, UniformFuzzyHash> namesToHashes =
                new LinkedHashMap<String, UniformFuzzyHash>(1);
        namesToHashes.put(hashName, hash);

        saveToFile(namesToHashes, file, append);

    }

    /**
     * Writes a map from names to Uniform Fuzzy Hashes into a text file.
     * 
     * @param namesToHashes Map from names to Uniform Fuzzy Hashes.
     * @param file The file to save the hashes.
     * @param append True to append the hash at the end of the file. False to overwrite the file.
     * @throws IOException If an IOException occurs writing into the file.
     */
    public static void saveToFile(
            Map<String, UniformFuzzyHash> namesToHashes,
            File file,
            boolean append)
            throws IOException {

        if (namesToHashes == null) {
            throw new NullPointerException("Map of hashes is null.");
        }

        if (file == null) {
            throw new NullPointerException("File is null");
        }

        if (file.exists() && !file.isFile()) {
            throw new IllegalArgumentException("File is not a file.");
        }

        Set<String> names = namesToHashes.keySet();
        List<String> lines = new ArrayList<String>(names.size());

        for (String name : names) {

            UniformFuzzyHash hash = namesToHashes.get(name);
            name = checkName(name);

            if (hash != null) {
                String hashString = hash.toString();
                String line = name + NAME_SEPARATOR + hashString;
                lines.add(line);
            }

        }

        FileUtils.writeLines(file, UFH_FILES_ECONDING.name(), lines, append);

    }

    /**
     * Loads a map from names to Uniform Fuzzy Hashes from a text file.
     * Lines starting by # are ignored.
     * 
     * @param file The file to load the hashes.
     * @return Map from names to Uniform Fuzzy Hashes.
     * @throws IOException IOException If an IOException occurs reading from the file.
     */
    public static Map<String, UniformFuzzyHash> loadFromFile(
            File file)
            throws IOException {

        // Parameters check.
        if (file == null) {
            throw new NullPointerException("File is null");
        }

        if (!file.exists()) {
            throw new IllegalArgumentException("File does not exist.");
        }

        if (!file.isFile()) {
            throw new IllegalArgumentException("File is not a file.");
        }

        // Read file by lines.
        List<String> lines = FileUtils.readLines(file, UFH_FILES_ECONDING.name());
        Map<String, UniformFuzzyHash> namesToHashes =
                new LinkedHashMap<String, UniformFuzzyHash>(lines.size());

        int i = 1;
        for (String line : lines) {

            line = line.trim();

            // Check ignore mark.
            if (line.startsWith(IGNORE_MARK)) {
                continue;
            }

            // Split name from hash.
            String[] nameSplit = line.split(NAME_SEPARATOR);

            if (nameSplit.length != 2) {
                throw new IllegalArgumentException(String.format(
                        "Line %d does not fit the format hashName %s hash.",
                        i,
                        NAME_SEPARATOR));
            }

            // Name.
            String name = nameSplit[0].trim();
            name = checkName(name);

            // Hash.
            String hashString = nameSplit[1].trim();
            UniformFuzzyHash hash = null;

            try {
                hash = new UniformFuzzyHash(hashString);
            } catch (IllegalArgumentException illegalArgumentException) {
                throw new IllegalArgumentException(String.format(
                        "Line %d hash could not be parsed. %s",
                        i,
                        illegalArgumentException.getMessage()));
            }

            namesToHashes.put(name, hash);

            i++;

        }

        return namesToHashes;

    }

    /**
     * Sorts a collection of Uniform Fuzzy Hashes by their similarity to another Uniform Fuzzy Hash,
     * according to a sorting criteria, and returns it as a new list of Uniform Fuzzy Hashes. The
     * introduced collection is not modified. If the criteria is null, no sorting operation is
     * performed.
     * 
     * @param hashes Collection of Uniform Fuzzy Hashes.
     * @param hash Uniform Fuzzy Hash to compute the similarities.
     * @param criteria Sorting criteria.
     * @return New list of Uniform Fuzzy Hashes representing the introduced collection, sorted
     *         according to the introduced criteria.
     */
    public static List<UniformFuzzyHash> sortBySimilarity(
            Collection<UniformFuzzyHash> hashes,
            final UniformFuzzyHash hash,
            final SimilaritySortCriterias criteria) {

        if (hashes == null) {
            throw new NullPointerException("Collection of hashes is null.");
        }

        if (hash == null) {
            throw new NullPointerException("Hash is null.");
        }

        List<UniformFuzzyHash> sortedHashes = new ArrayList<UniformFuzzyHash>(hashes);

        if (criteria != null) {

            Collections.sort(sortedHashes, new Comparator<UniformFuzzyHash>() {

                @Override
                public int compare(UniformFuzzyHash hash1, UniformFuzzyHash hash2) {

                    if (hash1 == null && hash2 == null) {
                        return 0;
                    } else if (hash1 == null) {
                        return 1;
                    } else if (hash2 == null) {
                        return -1;
                    }

                    switch (criteria) {
                        case HASH_TO_HASHES_ASC:
                            return Double.compare(hash.similarity(hash1), hash.similarity(hash2));
                        case HASH_TO_HASHES_DESC:
                            return Double.compare(hash.similarity(hash2), hash.similarity(hash1));
                        case HASHES_TO_HASH_ASC:
                            return Double.compare(hash1.similarity(hash), hash2.similarity(hash));
                        case HASHES_TO_HASH_DESC:
                            return Double.compare(hash2.similarity(hash), hash1.similarity(hash));
                        default:
                            return 0;
                    }

                }

            });

        }

        return sortedHashes;

    }

    /**
     * Sorts a map from names to Uniform Fuzzy Hashes by their similarity to another Uniform Fuzzy
     * Hash, according to a sorting criteria, and returns it as a new map from names to Uniform
     * Fuzzy Hashes. The introduced map is not modified. If the criteria is null, no sorting
     * operation is performed.
     * 
     * @param namesToHashes Map from names to Uniform Fuzzy Hashes.
     * @param hash Uniform Fuzzy Hash to compute the similarities.
     * @param criteria Sorting criteria.
     * @return New map from names to Uniform Fuzzy Hashes representing the introduced map, sorted
     *         according to the introduced criteria.
     */
    public static Map<String, UniformFuzzyHash> sortBySimilarity(
            Map<String, UniformFuzzyHash> namesToHashes,
            final UniformFuzzyHash hash,
            final SimilaritySortCriterias criteria) {

        if (namesToHashes == null) {
            throw new NullPointerException("Map of hashes is null.");
        }

        if (hash == null) {
            throw new NullPointerException("Hash is null.");
        }

        LinkedHashMap<String, UniformFuzzyHash> sortedNamesToHashes = null;

        if (criteria != null) {

            List<Entry<String, UniformFuzzyHash>> entries =
                    new ArrayList<Entry<String, UniformFuzzyHash>>(namesToHashes.entrySet());

            Collections.sort(entries, new Comparator<Entry<String, UniformFuzzyHash>>() {

                @Override
                public int compare(Entry<String, UniformFuzzyHash> entry1,
                        Entry<String, UniformFuzzyHash> entry2) {

                    UniformFuzzyHash hash1 = entry1.getValue();
                    UniformFuzzyHash hash2 = entry2.getValue();

                    if (hash1 == null && hash2 == null) {
                        return 0;
                    } else if (hash1 == null) {
                        return 1;
                    } else if (hash2 == null) {
                        return -1;
                    }

                    switch (criteria) {
                        case HASH_TO_HASHES_ASC:
                            return Double.compare(hash.similarity(hash1), hash.similarity(hash2));
                        case HASH_TO_HASHES_DESC:
                            return Double.compare(hash.similarity(hash2), hash.similarity(hash1));
                        case HASHES_TO_HASH_ASC:
                            return Double.compare(hash1.similarity(hash), hash2.similarity(hash));
                        case HASHES_TO_HASH_DESC:
                            return Double.compare(hash2.similarity(hash), hash1.similarity(hash));
                        default:
                            return 0;
                    }

                }

            });

            sortedNamesToHashes = new LinkedHashMap<String, UniformFuzzyHash>(namesToHashes.size());

            for (Entry<String, UniformFuzzyHash> entry : entries) {
                sortedNamesToHashes.put(entry.getKey(), entry.getValue());
            }

        } else {

            sortedNamesToHashes = new LinkedHashMap<String, UniformFuzzyHash>(namesToHashes);

        }

        return sortedNamesToHashes;

    }

    /**
     * Prints a table of Uniform Fuzzy Hashes.
     * 
     * @param hashes Collection of Uniform Fuzzy Hashes.
     * @param printStatistics Indicates whether hashes statistics must be printed or not.
     * @param printHashes Indicates whether hashes must be printed or not.
     */
    public static void printHashesTable(
            Collection<UniformFuzzyHash> hashes,
            boolean printStatistics,
            boolean printHashes) {

        if (hashes == null) {
            throw new NullPointerException("Collection of hashes is null.");
        }

        Map<String, UniformFuzzyHash> namesToHashes = nameHashesCollectionByIndex(hashes);

        printHashesTable(namesToHashes, printStatistics, printHashes);

    }

    /**
     * Prints a table of Uniform Fuzzy Hashes.
     * 
     * @param namesToHashes Map from names to Uniform Fuzzy Hashes.
     * @param printStatistics Indicates whether hashes statistics must be printed or not.
     * @param printHashes Indicates whether hashes must be printed or not.
     */
    public static void printHashesTable(
            Map<String, UniformFuzzyHash> namesToHashes,
            boolean printStatistics,
            boolean printHashes) {

        // Parameters check.
        if (namesToHashes == null) {
            throw new NullPointerException("Map of hashes is null.");
        }

        // Hash names.
        Set<String> names = namesToHashes.keySet();
        int namesMaxLength = getMaxLength(true, names);

        // Hash characteristics names.
        List<String> characteristicsNames = HashCharacteristics.names();
        int characteristicsNamesMaxLength = getMaxLength(true, characteristicsNames);

        // Column size.
        int firstColumnSize = namesMaxLength + TAB.length();
        int columnSize = getMaxLength(true,
                Integer.toString(Integer.MAX_VALUE), DECIMALS_FORMAT_STR);
        columnSize = Math.max(columnSize, characteristicsNamesMaxLength) + TAB.length();

        // Table print.
        System.out.println();

        printColumn("", firstColumnSize);
        System.out.print('|' + TAB);
        int numCharacteristics = 0;
        for (String hashCharacteristicName : characteristicsNames) {
            if (hashCharacteristicName.equals(HashCharacteristics.HASH.getName())) {
                if (printHashes) {
                    printColumn(hashCharacteristicName, columnSize);
                    numCharacteristics++;
                }
            } else {
                if (printStatistics) {
                    printColumn(hashCharacteristicName, columnSize);
                    numCharacteristics++;
                }
            }
        }
        System.out.println();

        System.out.println(hyphens(firstColumnSize) + '+'
                + hyphens(TAB.length() + columnSize * numCharacteristics));

        for (String name : names) {

            UniformFuzzyHash hash = namesToHashes.get(name);
            name = checkName(name);

            printColumn(name, firstColumnSize);
            System.out.print('|' + TAB);
            for (HashCharacteristics hashCharacteristic : HashCharacteristics.values()) {
                if (hashCharacteristic.equals(HashCharacteristics.HASH)) {
                    if (printHashes) {
                        printColumn(hashCharacteristic.getCharaceristicValue(hash), columnSize);
                    }
                } else {
                    if (printStatistics) {
                        printColumn(hashCharacteristic.getCharaceristicValue(hash), columnSize);
                    }
                }
            }

            System.out.println();

        }

        System.out.println();

    }

    /**
     * Prints a table showing the similarity between all the introduced Uniform Fuzzy Hashes.
     * 
     * @param hashes Collection of Uniform Fuzzy Hashes.
     */
    public static void printSimilarityTable(
            Collection<UniformFuzzyHash> hashes) {

        if (hashes == null) {
            throw new NullPointerException("Collection of hashes is null.");
        }

        Map<String, UniformFuzzyHash> namesToHashes = nameHashesCollectionByIndex(hashes);

        printSimilarityTable(namesToHashes);

    }

    /**
     * Prints a table showing the similarity between all the introduced Uniform Fuzzy Hashes.
     * 
     * @param namesToHashes Map from names to Uniform Fuzzy Hashes.
     */
    public static void printSimilarityTable(
            Map<String, UniformFuzzyHash> namesToHashes) {

        // Parameters check.
        if (namesToHashes == null) {
            throw new NullPointerException("Map of hashes is null.");
        }

        // Hash names.
        Set<String> names = namesToHashes.keySet();
        int namesMaxLength = getMaxLength(true, names);

        // Column size.
        int firstColumnSize = namesMaxLength + TAB.length();
        int columnSize = Math.max(DECIMALS_FORMAT_STR.length(), namesMaxLength) + TAB.length();

        // Table print.
        System.out.println();

        printColumn("", firstColumnSize);
        System.out.print('|' + TAB);
        for (String name : names) {
            name = checkName(name);
            printColumn(name, columnSize);
        }
        System.out.println();

        System.out.println(hyphens(firstColumnSize) + '+'
                + hyphens(TAB.length() + columnSize * names.size()));

        for (String name1 : names) {

            UniformFuzzyHash hash1 = namesToHashes.get(name1);
            name1 = checkName(name1);
            printColumn(name1, firstColumnSize);
            System.out.print('|' + TAB);

            for (String name2 : names) {

                UniformFuzzyHash hash2 = namesToHashes.get(name2);

                String similarityString = null;
                if (hash1 == null || hash2 == null) {
                    similarityString = NULL_VALUE;
                } else {
                    similarityString = DECIMALS_FORMAT.format(hash1.similarity(hash2));
                }
                printColumn(similarityString, columnSize);

            }

            System.out.println();

        }

        System.out.println();

    }

    /**
     * Prints a table showing the similarity between a Uniform Fuzzy Hash and the introduced Uniform
     * Fuzzy Hashes.
     * 
     * @param hash The Uniform Fuzzy Hash.
     * @param hashes Collection of Uniform Fuzzy Hashes.
     * @param similaritySortCriteria Sorting criteria to sort the table by similarity. Null not to
     *        sort it.
     */
    public static void printSimilarities(
            UniformFuzzyHash hash,
            Collection<UniformFuzzyHash> hashes,
            SimilaritySortCriterias similaritySortCriteria) {

        if (hash == null) {
            throw new NullPointerException("Hash is null.");
        }

        if (hashes == null) {
            throw new NullPointerException("Collection of hashes is null.");
        }

        Map<String, UniformFuzzyHash> namesToHashes = nameHashesCollectionByIndex(hashes);

        printSimilarities(null, hash, namesToHashes, similaritySortCriteria);

    }

    /**
     * Prints a table showing the similarity between a Uniform Fuzzy Hash and the introduced Uniform
     * Fuzzy Hashes.
     * 
     * @param hashName The Uniform Fuzzy Hash name.
     * @param hash The Uniform Fuzzy Hash.
     * @param namesToHashes Map from names to Uniform Fuzzy Hashes.
     * @param similaritySortCriteria Sorting criteria to sort the table by similarity. Null not to
     *        sort it.
     */
    public static void printSimilarities(
            String hashName,
            UniformFuzzyHash hash,
            Map<String, UniformFuzzyHash> namesToHashes,
            SimilaritySortCriterias similaritySortCriteria) {

        // Parameters check.
        hashName = checkHashName(hashName);

        if (hash == null) {
            throw new NullPointerException("Hash is null.");
        }

        if (namesToHashes == null) {
            throw new NullPointerException("Map of hashes is null.");
        }

        // Constants.
        final String hashToHashesString = String.format(HASH_TO_HASHES_STR, hashName);
        final String hashesToHashString = String.format(HASHES_TO_HASH_STR, hashName);

        // Sort.
        namesToHashes = sortBySimilarity(namesToHashes, hash, similaritySortCriteria);

        // Hash names.
        Set<String> names = namesToHashes.keySet();
        int namesMaxLength = getMaxLength(true, names);

        // Column size.
        int firstColumnSize = namesMaxLength + TAB.length();
        int columnSize = getMaxLength(true,
                DECIMALS_FORMAT_STR, hashToHashesString, hashesToHashString)
                + TAB.length();

        // Table print.
        System.out.println();

        printColumn("", firstColumnSize);
        System.out.print('|' + TAB);
        printColumn(hashToHashesString, columnSize);
        printColumn(hashesToHashString, columnSize);
        System.out.println();

        System.out.println(hyphens(firstColumnSize) + '+' + hyphens(TAB.length() + 2 * columnSize));

        for (String name1 : names) {

            UniformFuzzyHash hash1 = namesToHashes.get(name1);
            name1 = checkName(name1);

            printColumn(name1, firstColumnSize);
            System.out.print('|' + TAB);

            String similarityString = null;
            String similarityString1 = null;
            if (hash1 == null) {
                similarityString = NULL_VALUE;
                similarityString1 = NULL_VALUE;
            } else {
                similarityString = DECIMALS_FORMAT.format(hash.similarity(hash1));
                similarityString1 = DECIMALS_FORMAT.format(hash1.similarity(hash));
            }
            printColumn(similarityString, columnSize);
            printColumn(similarityString1, columnSize);

            System.out.println();

        }

        System.out.println();

    }

    /**
     * Builds a map from names to Uniform Fuzzy Hashes based on a collection of Uniform Fuzzy
     * Hashes, naming them by their index in the collection.
     * 
     * @param hashes Collection of Uniform Fuzzy Hashes.
     * @return Map from names to Uniform Fuzzy Hashes.
     */
    private static Map<String, UniformFuzzyHash> nameHashesCollectionByIndex(
            Collection<UniformFuzzyHash> hashes) {

        Map<String, UniformFuzzyHash> namesToHashes =
                new LinkedHashMap<String, UniformFuzzyHash>(hashes.size());

        int i = 0;
        for (UniformFuzzyHash hash : hashes) {
            namesToHashes.put(Integer.toString(i), hash);
            i++;
        }

        return namesToHashes;

    }

    /**
     * Prints a string followed by an amount of spaces such that columnSize characters are printed.
     * 
     * @param text The string to print.
     * @param columnSize Amount of characters to print.
     */
    private static void printColumn(
            String text,
            int columnSize) {

        System.out.print(text + spaces(columnSize - text.length()));

    }

}
