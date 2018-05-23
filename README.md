# CryptGUI
### Running the Program
Make sure that you have the Java Runtime Environment installed:  
`java -version`.

Download the `CryptoGUI.jar` file and double click to run. If you are on Linux you may have to give the file permission to execute:  
`$ chmod +x CryptoGui.jar`.

### Features
Key: Enter the key you wish to encrypt your file with.  
Input: Select an input file.  
Output: Select an output file or directory. If you select a directory a file will be created for you.  
Encrypt: Encrypts your input file with AES256 encryption and outputs the result to your selected output file. If no file is selected the result is output to `<inputfilename>.enc.<inputfileextension>`.  
Decrypt: Decrypts your input file and outputs the result to your selected output file. If no file is selected the result is output to `<inputfilename>.dec.<inputfileextension>`.
