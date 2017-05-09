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

Also note that the factor must be chosen carefully. The factor indicates the mean block size, in other words, the mean amount of bytes that must appear consecutively in both files such that some similarity is added to the score. This means that choosing too small factors would divide files in too small blocks, which may lead to similarities higher than expected and false possitives in similarity detections, while choosing too big factors would divide files in too big blocks, which may cause similarities lower than expected and false negatives.

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

The argument `--recursive` or `-r` can be introduced to indicate that directories inside directories must be travarsed recursively.

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
