# Similarity-Vectors

#### Command line arguments need to be passed as program arguments in the run configuration
###### When multiple arguments are required, separate them with ","
* **f**  -> Requires a text file argument. Required for all other arguements to run. Cleans text file
* **v** -> Creates all vectors. When -s is added, all vectors will be printed out. Neither argument is needed for other arguments to work
* **t** -> Calculates cosine similarity. Requires 2 arguments - word for all others to be compared against, number of top words to be printed out
  * **m** -> To be combined with -t. Requires either "euc" for euclidean distance, or "eucnorm" for euclidean distance between normalized vectors
* **k** -> K-means clustering. Requires 2 arguments, number of clusters and number of iterations
* **j** -> K-means clustering combined with cosine similarity. Requires same arguments as -k, plus another for the number of most similar words in each cluster to be printed. This is determined by cosine similarity
