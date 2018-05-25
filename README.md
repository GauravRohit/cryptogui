# CryptoGUI
### Requirements
Make sure that you have the Java Runtime Environment installed: `java -version`.
### Running the Program
Download the `CryptoGUI.jar` file and double click to run. If you are on Linux you may have to give the file permission to execute: `$ chmod +x CryptoGui.jar`.
### Building the Program
If you wish to build the jar yourself download the `*.java` files and compile: `javac CryptoGUI.java`. Download the `CryptoGUI.mf` file and build: `jar cvfm  <jarfilename>.jar CryptoGUI.mf *.class`.
### Features
Key: Enter the key you wish to encrypt your file with.  
Input: Select an input file.  
Output: Select an output file or directory. If you select a directory a file will be created for you.  
Encrypt: Encrypts your input file with AES256 encryption and outputs the result to your selected output file. If no file is selected the result is output to `<inputfilename>.enc.<inputfileextension>`.  
Decrypt: Decrypts your input file and outputs the result to your selected output file. If no file is selected the result is output to `<inputfilename>.dec.<inputfileextension>`.
