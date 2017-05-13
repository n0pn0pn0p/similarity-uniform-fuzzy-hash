# Similarity Uniform Fuzzy Hash

Similarity Uniform Fuzzy Hash is a tool that allows to accurately and efficiently compute the similarity between two files (or sets of bytes) as a 0 to 1 score.

For that purpose, it first computes for each file a Context Triggered Piecewise Hash (CTPH), also known as fuzzy hash, and then compares the hashes.

Both, the hash computation and the hashes comparison algorithms present linear complexity, the former with respect to the file size (or the amount of bytes), and the latter with respect to the hashes length, which is proportional to the files size divided by a choosable factor. This fact makes the tool very efficient and ideal for clustering (finding the most or least similar files to a given one between a set or database of many files). In fact, there is no need to store the files, storing the hashes is enough.

The tool provides methods to:

  * Compute a file hash.

  * Compute the hashes of a set of files.

  * Compute the similarity between two hashes.

  * Compute and show in a table the similarity between a hash and a set of hashes, ordering them by similarity to the first one.

  * Compute and show in a table the similarity between all the hashes in a set.

  * Save and load hashes into / from a text file.

  * Visually compare two files or hashes, identifying their common parts.

The tool is a Java JAR and can be used in two ways:

  * By means of the command line interface.

  * As a library or dependency that can be imported into a Java project.

The latest release is available here:

https://github.com/s3curitybug/similarity-uniform-fuzzy-hash/releases/latest

# The Algorithm

The hash computation algorithm divides the file in blocks. The location of the divisions depends on the file contents. Thus, the blocks size is not constant, but the mean block size is chosen by the user through a parameter called "factor". So the file is divided in blocks of size around factor. Then, each block is converted into two hexadecimal numbers, the first one representing its content and the second one representing its size. Finally, the hash is written as the factor followed by each block.

<p align="center"><img src="readme-media/hash-algorithm.png" width=400/></p>

This way, two files sharing some content would produce two hashes that share some blocks. The comparison algorithm finds the blocks of the first hash which are present in the second one (independently on their position), and returns a 0 to 1 similarity score based on the sum of their size, divided by the file total size, which is very accurate.

Note that the similarity score between File 1 and File 2 indicates the proportion of content of File 1 which is present in File 2. This is different to the similarity score between File 2 and File 1, which indicates the proportion of content of File 2 which is present in File 1. For files with similar size, both scores will be close. However, comparing a small file which is part of a big file to that big file would return a high score between the small file and the big one, but a low score between the big file and the small one. This means that the algorithm is able to detect small files inside big ones. For instance, it can detect images inside documents, and malwares inside executables. The tool also provides methods to compute the maximum, minimum, arithmetic mean and geometric mean between the two similarity scores of two files.

<p align="center"><img src="readme-media/similarity-algorithm.png" width=500/></p>

Also note that the factor must be chosen carefully. The factor indicates the mean block size, in other words, the mean amount of bytes that must appear consecutively in both files such that some similarity is added to the score. This means that choosing too small factors would divide files in too small blocks, which may lead to similarities higher than expected and false positives in similarity detections, while choosing too big factors would divide files in too big blocks, which may cause similarities lower than expected and false negatives.

Additionally, the hash length (which depends on the amount of blocks) is proportional to the file size divided by the factor. This means that big files and small factors produce large hashes (high amount of blocks), while small files and big factors produce small hashes (low amount of blocks). Consequently, it is recommended using a big factor when comparing big files, and a small factor when comparing small ones. However, two hashes can only be compared if they were computed with the same factor. This means that, when comparing small files to big ones, a small factor must be used.

Due to the hash computation algorithm nature, factor must always be an odd number and larger than 2.

# The Command Line Interface

In order to use the command line interface, there is no need to download or compile the project, downloading the JAR is enough.

The JAR can be executed using the following command:

```shell
java -jar similarity-uniform-fuzzy-hash-{version}.jar
```

A Java JRE installation is required to run the JAR.

Running the JAR without any argument or with the `--help` or `-h` argument will display the usage:

<p align="center"><img src="readme-media/cmd-help.png" width=800/></p>

Arguments:

  * `--computeFileHash` or `-cfh`

Computes the hash of one or several files (one per argument).

The argument `--factor` or `-f` must be introduced, indicating the factor that will be used for the hash or hashes computation (remember that it must be an odd number and larger than 2).

<p align="center"><img src="readme-media/cmd-cfh.png" width=800/></p>

  * `--computeDirectoryHashes` or `-cdh`

Computes the hashes of all the files inside one or several directories (one per argument).

The argument `--factor` or `-f` must be introduced, indicating the factor that will be used for the hash or hashes computation (remember that it must be an odd number and larger than 2).

The argument `--recursive` or `-r` can be introduced to indicate that directories inside directories must be traversed recursively.

<p align="center"><img src="readme-media/cmd-cdh.png" width=800/></p>

  * `--saveToTextFile` or `-stf`

Saves all computed hashes into one or several text files (one per argument) in their hexadecimal representation. The hashes are appended to the end of the file.

The argument `--overwrite` or `-o` can be introduced to indicate that the file must be overwritten, instead of appending the hashes to its end.

<p align="center"><img src="readme-media/cmd-stf.png" width=600/></p>

  * `--saveToAsciiFile` or `-saf`

Saves all computed hashes into one or several text files (one per argument) in their ascii representation, which is less human readable than the hexadecimal representation, but occupies less disk space. The hashes are appended to the end of the file.

The argument `--overwrite` or `-o` can be introduced to indicate that the file must be overwritten, instead of appending the hashes to its end.

<p align="center"><img src="readme-media/cmd-saf.png" width=600/></p>

  * `--loadFromTextFile` or `-ltf`

Loads all the hashes saved in one or several text files (one per argument). All hashes must be in their hexadecimal representation. Lines starting by # are ignored.

<p align="center"><img src="readme-media/cmd-ltf.png" width=800/></p>

  * `--loadFromAsciiFile` or `-laf`

Loads all the hashes saved in one or several text files (one per argument). All hashes must be in their ascii representation. Lines starting by # are ignored.

<p align="center"><img src="readme-media/cmd-laf.png" width=800/></p>

  * `--exportToTextFile` or `-etf`

Exports all the hashes saved in a text file (first argument) in their ascii representation to another text file (second argument) saving them in their hexadecimal representation. The hashes are appended to the end of the file.

The argument `--overwrite` or `-o` can be introduced to indicate that the file must be overwritten, instead of appending the hashes to its end.

<p align="center"><img src="readme-media/cmd-etf.png" width=600/></p>

  * `--exportToAsciiFile` or `-eaf`

Exports all the hashes saved in a text file (first argument) in their hexadecimal representation to another text file (second argument) saving them in their ascii representation. The hashes are appended to the end of the file.

The argument `--overwrite` or `-o` can be introduced to indicate that the file must be overwritten, instead of appending the hashes to its end.

<p align="center"><img src="readme-media/cmd-eaf.png" width=600/></p>

  * `--compare` or `-x`

Compares two hashes.

-If no argument is introduced, and two hashes were computed with the argument `--computeFileHash` or `-cfh`, they are compared.

<p align="center"><img src="readme-media/cmd-x-1.png" width=600/></p>

-If one argument is introduced indicating a computed or loaded hash, and another hash was computed with the argument `--computeFileHash` or `-cfh`, the computed hash is compared to the indicated one.

<p align="center"><img src="readme-media/cmd-x-2.png" width=600/></p>

-If two arguments are introduced indicating computed or loaded hashes, they are compared.

<p align="center"><img src="readme-media/cmd-x-3.png" width=600/></p>

  * `--compareToAll` or `-xya`

Compares in a table a hash to all computed and loaded hashes, showing in the table the direct similarity (hash to hashes), the reverse similarity (hashes to hash), the maximum and the minimum between both, and their arithmetic and geometric mean.

The argument `--sortingBy` or `-sort` can be introduced to sort the table by similarity. If no argument is introduced, the default sorting criterion will be by descending direct similarity. An argument can be introduced to specify a different criterion. Check the JAR `--help` or `-h` argument to see all the possible criteria.

The argument `--rowsLimit` or `-limit` can be introduced, indicating the maximum number of rows to display in the table.

The argument `--truncateNames` or `-trunc` can be introduced, indicating the maximum number of characters to display in the hashes names.

The argument `--markAbove` or `-ma` can be introduced, indicating an upper threshold (0 to 1) to mark all similarities above or equal to it with a color.

The argument `--markBelow` or `-mb` can be introduced, indicating a lower threshold (0 to 1) to mark all similarities below it with a color.

About the `--compareToAll` or `-xya` argument:

-If no argument is introduced, and a hash was computed with the argument `--computeFileHash` or `-cfh`, the computed hash is compared to all computed and loaded hashes.

<p align="center"><img src="readme-media/cmd-xya-1.png" width=800/></p>

-If one argument is introduced indicating a computed or loaded hash, it is compared to all computed and loaded hashes.

<p align="center"><img src="readme-media/cmd-xya-2.png" width=800/></p>

-If multiple arguments are introduced indicating computed or loaded hashes, the first one is compared to all the indicated ones.

<p align="center"><img src="readme-media/cmd-xya-3.png" width=800/></p>

  * `--compareAll` or `-xa`

Compares in a table all computed and loaded hashes, showing in the table for each hash its similarity to every other one.

The argument `--truncateNames` or `-trunc` can be introduced, indicating the maximum number of characters to display in the hashes names.

The argument `--markAbove` or `-ma` can be introduced, indicating an upper threshold (0 to 1) to mark all similarities above or equal to it with a color.

The argument `--markBelow` or `-mb` can be introduced, indicating a lower threshold (0 to 1) to mark all similarities below it with a color.

About the `--compareToAll` or `-xya` argument:

-If no argument is introduced, all computed and loaded hashes are compared.

<p align="center"><img src="readme-media/cmd-xa-1.png" width=800/></p>

-If multiple arguments are introduced indicating computed or loaded hashes, all the indicated ones are compared.

<p align="center"><img src="readme-media/cmd-xa-2.png" width=800/></p>

  * `--representVisually` or `-rv`

Shows a visual representation of a hash. Each block is represented as one or several characters, depending on the block size.

The argument `--lineWrap` or `-wrap` can be introduced, indicating the length at which lines will be wrapped. At the begining of each line, a percentage will be displayed indicating the file size scroll.

About the `--representVisually` or `-rv` argument:

-If no argument is introduced, and a hash was computed with the argument `--computeFileHash` or `-cfh`, the computed hash is represented visually.

<p align="center"><img src="readme-media/cmd-rv-1.png" width=500/></p>

-If one argument is introduced indicating a computed or loaded hash, it is visually represented.

<p align="center"><img src="readme-media/cmd-rv-2.png" width=500/></p>

  * `--compareVisually` or `-xv`

Shows a visual comparison of two hashes. Each block is represented as one or several characters, depending on the block size. The blocks which are present on both hashes are marked with a different color to the ones which are only present on one of them.

The argument `--lineWrap` or `-wrap` can be introduced, indicating the length at which lines will be wrapped. At the begining of each line, a percentage will be displayed indicating the file size scroll.

About the `--compareVisually` or `-xv` argument:

-If no argument is introduced, and two hashes were computed with the argument `--computeFileHash` or `-cfh`, they are compared visually.

<p align="center"><img src="readme-media/cmd-xv-1.png" width=800/></p>

-If one argument is introduced indicating a computed or loaded hash, and another hash was computed with the argument `--computeFileHash` or `-cfh`, the computed hash is compared visually to the indicated one.

<p align="center"><img src="readme-media/cmd-xv-2.png" width=800/></p>

-If two arguments are introduced indicating computed or loaded hashes, they are compared visually.

<p align="center"><img src="readme-media/cmd-xv-3.png" width=800/></p>

# The Java Library

There are two ways to import the Java library into another Java project:

  * As an external JAR: There is no need to download or compile the project, downloading the JAR and adding it to the project as a library is enough.

  * As a Maven dependency:

  ```xml
<dependency>
      <groupId>com.github.s3curitybug</groupId>
      <artifactId>similarity-uniform-fuzzy-hash</artifactId>
      <version>1.5.1</version>
</dependency>
  ```
