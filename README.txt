UTEID: am52293;
FIRSTNAME: Arya; 
LASTNAME: Mohades;
CSACCOUNT: am611;
EMAIL: am611@utexas.edu; 

[Program 3]
[Description]
There are 2 java files: Encoder.java and HuffmanCode.java

To compile the program, use:

	"javac *.java".

To run the program, you need to use:
	
	"java Encoder <frequenciesFile> <n>"
	
where n is a positive integer and frequenciesFile is the name of a file containing character frequencies.

HuffmanCode.java was taken from http://rosettacode.org/wiki/Huffman_coding#Java
In order for it to work with my program, because uppercase letters start at 65 in
ascii, I had to modify the buildTree method to start adding characters to the tree at 65
rather than 0, for as many letters there were in that particular alphabet.

Then, I simply added a method "getSymbolEncodings" which mimics printCodes method very closely.
The only difference is that it puts the pairings of prefix to char and char to prefix in
their respective maps rather than just printing it. A boolean is passed in to determine whether
the encoding is for single character symbols or double character symbols (in which case the 
final prefix is simply added to a list of all prefixes). We need to save these obviously to 
write the results of the encoding/decoding to the various files created.

Encoder.java contains the main method as well as 6 helper methods. There is a method generateText
which will generate some plain text to be encoded. It employs the dartboard process to get a good
character distribution based on relative frequencies of each character.

The encodeText method simply iterates through the plaintext and writes the corresponding encoding
for each character to file. The decodeText method will simply perform the reverse process in taking
the character encoding and produce the original character and write it to file. The process for two
character language is not so different. For encoding, simply take two characters at a time from the
plaintext and match to the encoding. For decoding, we simply build and match prefixes to the original
symbol.

The writeAndClose method is just a simple helper method to use a BufferedWriter to write to file,
and then flush and close the writer.

The main method starts by taking the input frequency file and using it to calculate an actual entropy.
Then, we use the Huffman algorithm to generate encodings for each of the symbols of our language. We use
these encodings to encode the text and decode it, storing the results to file. Then, we use the number of bits
needed to encode the text to calculate averageBits and compare that to the actual entropy. 

The process is performed for both single char and double char language and their encoding efficiencies are
compared. The two symbol derived language was much closer to the optimal entropy than the single.

[Finish]
I finished the assignment

[Test Cases]

[Input of test 1]
[command line]java Encoder frequenciesFile 4

inputFile (frequenciesFile)
4
2
3
1

[Output of test 1]
ONE CHAR SYMBOL RESULTS

LETTER	WEIGHT	ENCODING
A	4	0
C	3	10
D	1	110
B	2	111

Entropy: 1.8464393446710154
Average bits per symbol: 11.75
Difference compared to entropy: 536.3599234337983 %

TWO CHAR SYMBOL RESULTS

LETTER	WEIGHT	ENCODING
AA	16	0000
AB	8	00010
AC	12	000110
AD	4	000111
BA	8	001
BB	4	010
BC	6	0110
BD	2	0111
CA	12	100
CB	6	10100
CC	9	10101
CD	3	1011
DA	4	1100
DB	2	11010
DC	3	11011
DD	1	111

Average bits per symbol: 2.75
Difference compared to entropy: 48.935301229186834%
Difference compared to single char encoding: -76.59574468085107%
   
[Input of test 2]
[command line]java Encoder frequenciesFile 4

inputFile (frequenciesFile)
1
2
3
4

[Output of test 2]
ONE CHAR SYMBOL RESULTS

LETTER	WEIGHT	ENCODING
D	4	0
C	3	10
A	1	110
B	2	111

Entropy: 1.8464393446710154
Average bits per symbol: 11.75
Difference compared to entropy: 536.3599234337983 %

TWO CHAR SYMBOL RESULTS

LETTER	WEIGHT	ENCODING
AA	1	0000
AB	2	00010
AC	3	00011
AD	4	001
BA	2	010
BB	4	0110
BC	6	0111
BD	8	100
CA	3	10100
CB	6	101010
CC	9	101011
CD	12	1011
DA	4	1100
DB	8	11010
DC	12	11011
DD	16	111

Average bits per symbol: 2.0
Difference compared to entropy: 8.316582712135867%
Difference compared to single char encoding: -82.97872340425532%

[Input of test 3]
[command line]java Encoder frequenciesFile 10

inputFile (frequenciesFile)
1
2
3
4
5

[Output of test 3]
ONE CHAR SYMBOL RESULTS

LETTER	WEIGHT	ENCODING
C	3	00
A	1	010
B	2	011
D	4	10
E	5	11

Entropy: 2.1492553971685
Average bits per symbol: 4.7
Difference compared to entropy: 118.6803860626306 %

TWO CHAR SYMBOL RESULTS

LETTER	WEIGHT	ENCODING
AA	1	000
AB	2	0010
AC	3	00110
AD	4	001110
AE	5	0011110
BA	2	0011111
BB	4	0100
BC	6	0101
BD	8	011
BE	10	10000
CA	3	10001
CB	6	1001
CC	9	1010
CD	12	1011
CE	15	11000
DA	4	110010
DB	8	110011
DC	12	110100
DD	16	110101
DE	20	11011
EA	5	11100
EB	10	111010
EC	15	1110110
ED	20	1110111
EE	25	1111

Average bits per symbol: 2.6
Difference compared to entropy: 20.972128460178617%
Difference compared to single char encoding: -44.680851063829785%

[Input of test 4]
[command line]java Encoder frequenciesFile 3

inputFile (frequenciesFile)
10
2
1

[Output of test 4]
ONE CHAR SYMBOL RESULTS

LETTER	WEIGHT	ENCODING
C	1	00
B	2	01
A	10	1

Entropy: 0.9912642605354289
Average bits per symbol: 4.7
Difference compared to entropy: 374.1419808136033 %

TWO CHAR SYMBOL RESULTS

LETTER	WEIGHT	ENCODING
AA	100	000
AB	20	00100
AC	10	001010
BA	20	0010110
BB	4	0010111
BC	2	0011
CA	10	010
CB	2	011
CC	1	1

Average bits per symbol: 1.3
Difference compared to entropy: 31.145654267592416%
Difference compared to single char encoding: -72.34042553191489%