# pop-music-parser

The [Document](/src/ir/Document.java) class generates the term frequencies by processing a single string of all lyrics into tokens of individuals words. The [vector space model](/src/ir/VectorSpaceModel.java) then generates the term frequency-inverse document frequency (tf-idf) weights.

Using the Java jsoup API, I searched the Genius website with the [parser](/src/parser/GeniusParser.java) to find the most popular artists and sample their discography. After computing the cosine similarity between all artists, the end result was that the vector space model did not show any correlation of lyrical similarity.