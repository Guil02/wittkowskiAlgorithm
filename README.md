# wittkowskiAlgorithm

This project is an attempt at creating the wittkowski sorting algorithm explained in (Wittkowski, 2003).

## current state
The project currently executes the algorithm. It has 2 modes, with gpu and without use of the gpu.
The use of the gpu speeds up the running drastically. On a dataset with 112000 rows and 8 columns
the time required went from 3 days to 14 minutes. 28 seconds is needed for the gpu to do it's work.
The rest of the time is taken to prepare a 1d array that contains all the factors in a row. This was
done because JOCL, the package used to work with the cpu cannot use a 2d array as a pointer. I believe
that the speed could be improved even more, because the implementation of the 1d array forming is definitely
not optimal.

## usage
A main method is provided in org.guil.main run that and the program will start.

## authors
* **Guillaume Bams** - *Bsc DSAI student* - [Guillaume](https://github.com/Guil02) - *Lead Developer*

## references
Wittkowski, Knut M.. (2003). Novel Methods for Multivariate Ordinal Data applied to Genetic Diplotypes, Genomic Pathways, Risk Profiles, and Pattern Similarity. University Library of Munich, Germany, MPRA Paper. 35. 
