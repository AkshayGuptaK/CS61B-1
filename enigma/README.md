# Enigma
# Table of Contents
- Introduction
- Background
- Describing Permutations
- Input & Output
- Handling Errors

## Introduction
This programming assignment is intended to exercise a few useful data structures and an object-based view of a programming problem. 

We will be grading largely on whether you manage to get your program to work (according to our tests). In addition, we will be looking at your own tests (which you should be sure to turn in as well). While we have supplied a few unit tests and some simple integration tests and testing utilities, the tests in skeleton are entirely inadequate for testing your program. There is also a stylistic component: the submission and grading machinery require that your program pass a mechanized style check (style61b), which mainly checks for formatting and the presence of comments in the proper places. See the course website for a brief description of the style rules. You may change any of the code we've provided, as long as the resulting program works according to the specifications here.

## Background
You may have heard of the Enigma machines that Germany used during World War II to encrypt its military communications. This project involves building a simulator for a generalized version of this machine (which itself had several different versions.) Your program will take descriptions of possible initial configurations of the machine and messages to encode or decode (the Enigma algorithms were reciprocal, meaning that encryption is its own inverse operation.)

The Enigmas effect a *substitution cipher* on the letters of a message. That is, at any given time, the machine performs a permutation—a one-to-one mapping—of the alphabet onto itself. The alphabet consists solely of the 26 letters in one case (there were various conventions for spaces and punctuation).

Plain substitution ciphers are easy to break (you've probably seen puzzles in newspapers that consist of breaking such ciphers). The Enigma, however, implements a *progressive substitution*, different for each subsequent letter of the message. This made decryption considerably more difficult.

Let's illustrate with a much simplified version. Suppose our alphabet has only the letters A-C and we have four rotors (numbered 1-4) each of which has one notch on its ring at the C position. Suppose also that there are 3 pawls, one for each of rotors 2-4. We will still refer to these as pawls 2-4, to maintain that pawl i belongs to rotor i. There is no pawl for rotor 1, which will therefore not rotate. We'll start with the rotors set at AAAA. The next 19 positions are as follows:
```
AAAB  AAAC  AABA  AABB  AABC  AACA  ABAB  ABAC
ABBA  ABBB  ABBC  ABCA  ACAB  ACAC  ACBA  ACBB
ACBC  ACCA  AAAB
```
As you can see,

- Rotor 4, the fast rotor, advances each time, pushed by pawl 4. Rotor 4 has no rotor to its right, so there isn't a ring blocking it from engaging with its ratchet.
- Rotor 3 advances whenever Rotor 4 is at C. Rotor 4 has a notch at C, so pawl 3 can engage with the corresponding ratchet (the ratchet belonging to Rotor 3) and advance Rotor 3 by pushing on its ratchet. This would also rotate Rotor 4, since pawl 3 contacts its ratchet through the notch of Rotor 4, and therefore pushes the side of the notch when it moves. However, since Rotor 4 always rotates anyway (because pawl 4 is always unblocked), this doesn't really change anything.
- Rotor 2 advances whenever Rotor 3 is at C, pushed by pawl 2. Rotor 3 has a notch at C, so pawl 2 slips into the notch and engages with its ratchet (the ratchet belonging to Rotor 2). Rotor 3 also advances when it is at C, because when pawl 2 is engaged through Rotor 3's notch it will push against that notch when it moves, moving Rotor 3, as well as moving Rotor 2 by pushing on Rotor 2's ratchet.
- There is no pawl 1, so Rotor 2 (unlike Rotor 3) does not advance just because it is at C.
- Rotor 1 never changes, since there is no pawl on either side of it.
Each rotor can only advance at most one position per keypress.

So the advancement of the rotors, while similar to that of the wheels of an odometer, is not quite the same. If it were, then the next position after AACA would be AACB, rather than ABAB.

The contacts on the rightmost rotor's right side connect with stationary input and output contacts, which run to keys that, when pressed, direct current to the contact from a battery or, when not pressed, direct current back from the contact to a light bulb indicating a letter of the alphabet. Since a letter never encrypts or decrypts to itself after going back and forth through the rotors, the to and from directions never conflict.

The German Navy used a machine with 12 rotors and five slots for them:

- Eight rotors labeled with roman numerals I--VIII, of which three will be used in any given configuration as the rightmost rotors,
- Two additional non-moving rotors (Zusatzwalzen) labeled "Beta" and "Gamma", of which one will be used in any configuration, as the fourth-from-right rotor, and
- Two reflectors (Umkehrwalzen), labeled 'B' and 'C', of which one will be used in any given configuration as the leftmost rotor.
Given just this equipment, there are 614,175,744 possible configurations (or keys):

## Describing Permutations
Since the rotors and the plugboard implement permutations, we'll need a standard way to describe them. We could simply have a table showing each letter and what it maps to, but we'll use a more compact notation known as cycle representation. The idea is that any permutation of a set may be described as a set of cyclic permutations. For example, the notation
```
(AELTPHQXRU) (BKNW) (CMOY) (DFG) (IV) (JZ) (S)
```
describes the permutation in Figure 1. It describes seven cycles:

- A maps to E, E to L, L to T, ..., R to U, and U back to A.
- B maps to K, K to N, N to W, and W back to B.
- C maps to M, M to O, O to Y, and Y back to C.
- D maps to F, F to G, and G back to D.
- I maps to V and V back to I.
- J maps to Z and Z back to J.
- S maps to itself.

The inverse permutation just reverses these cycles:
- U maps to R, R to X, ..., E to A, and A back to U.
- ...
- S maps to itself.

Each letter appears in one and only one cycle, so the mapping is unambiguous. As a shorthand, we'll say that if a letter is left out of all cycles, it maps to itself (so that we could have left off "(S)" In the example above.)
```
Rotor	Permutation (as cycles)	Notch
Rotor I	(AELTPHQXRU) (BKNW) (CMOY) (DFG) (IV) (JZ) (S)	Q
Rotor II	(FIXVYOMW) (CDKLHUP) (ESZ) (BJ) (GR) (NT) (A) (Q)	E
Rotor III	(ABDHPEJT) (CFLVMZOYQIRWUKXSG) (N)	V
Rotor IV	(AEPLIYWCOXMRFZBSTGJQNH) (DV) (KU)	J
Rotor V	(AVOLDRWFIUQ)(BZKSMNHYC) (EGTJPX)	Z
Rotor VI	(AJQDVLEOZWIYTS) (CGMNHFUX) (BPRK)	Z and M
Rotor VII	(ANOUPFRIMBZTLWKSVEGCJYDHXQ)	Z and M
Rotor VIII	(AFLSETWUNDHOZVICQ) (BKJ) (GXY) (MPR)	Z and M
Rotor Beta	(ALBEVFCYODJWUGNMQTZSKPR) (HIX)
Rotor Gamma	(AFNIRLBSQWVXGUZDKMTPCOYJHE)
Reflector B	(AE) (BN) (CK) (DQ) (FU) (GY) (HW) (IJ) (LO) (MP) (RX) (SZ) (TV)
Reflector C	(AR) (BD) (CO) (EJ) (FN) (GT) (HK) (IV) (LM) (PW) (QZ) (SX) (UY)
```
## Input and Output
The data are in free format. That is, they consist of strings of non-whitespace characters separated by arbitrary whitespace (spaces, tabs, and newlines), so that indentation, spacing, and line breaks are irrelevant. Each file has the following contents:

- A string of the form *C1-C2* where *C1* and *C2* are non-blank characters other than "-", "(", and ")", with *C1≤C2*. This specifies that the alphabet consists of characters ≥C1 and ≤C2, with lower-case letters mapped to upper-case. 
Unless you do the extra credit, *C1* and *C2* will always be upper-case letters.
- Two integer numerals, *S*>*P*≥0, where *S* is the number of rotor slots (including the reflector) and *P* is the number of pawls—that is, the number of rotors that move. The moving rotors and their pawls are all to the right of any non-moving ones.
- Any number of rotor descriptors. Each has the following components (separated by whitespace):

    - A name containing any non-blank characters other than parentheses.
    - One of the characters *R*, *N*, or *M*, indicating that the rotor is a reflector, a non-moving rotor, or a moving rotor, respectively. Non-moving rotors can only be used in positions 2 through *S−P* and moving rotors in positions *S−P*+1 through *S*.
    - Immediately after the *M* for a moving rotor come(s) the letter(s) at which there is a notch on the rotor's ring (no space between *M* and these letters).
    - The cycles of the permutation, using the notation discussed above.

Given the configuration file above, a settings line looks like this:
```
* B BETA III IV I AXLE (YF) (ZH)
```
(all upper case.) This particular example means that the rotors used are reflector B, and rotors Beta, III, IV, and I, with rotor I in the rightmost, or fast, slot. The remaining parenthesized items indicate that the letter pair Y and F and the pair Z and M are steckered (swapped going in from the keyboard and going out to the lights).

In general for this particular configuration, rotor 1 is always the reflector; rotor 2 is Beta or Gamma, and each of 3-5 is one of rotors I-VIII. A rotor may not be repeated. The four letters of the following word (AXLE in the example) give the initial positions of rotors 2-5, respectively (i.e., not including the reflector). Any number of steckered pairs may follow (including none).

After each settings line comes a message on any number of lines. Each line of a message consists only of letters, spaces, and tabs (0 or more). The program should ignore the blanks and tabs and convert all letters to upper case. The end of message is indicated either by the end of the input or by a new configuration line (distinguished by its leading asterisk). The machine is not reset between lines, but continues stepping from where it left off on the previous message line. Because the Enigma is a reciprocal cipher, a given translation may either be a decryption or encryption; you don't have to worry about which, since the process is the same in any case.

Output the translation for each message line in groups of five upper-case letters, separated by a space (the last group may have fewer characters, depending on the message length). Figure 3 contains an example that shows an encryption followed by a decryption of the encrypted message. Since we have yet to cover the details of File I/O, you will be provided the File IO machinery for this project.
```
             Input                              |         Output
* B BETA III IV I AXLE (HQ) (EX) (IP) (TR) (BY) | QVPQS OKOIL PUBKJ ZPISF XDW
FROM his shoulder Hiawatha                      | BHCNS CXNUO AATZX SRCFY DGU
Took the camera of rosewood                     | FLPNX GXIXT YJUJR CAUGE UNCFM KUF
Made of sliding folding rosewood                | WJFGK CIIRG XODJG VCGPQ OH
Neatly put it all together                      | ALWEB UHTZM OXIIV XUEFP RPR
In its case it lay compactly                    | KCGVP FPYKI KITLB URVGT SFU
Folded into nearly nothing                      | SMBNK FRIIM PDOFJ VTTUG RZM
But he opened out the hinges                    | UVCYL FDZPG IBXRE WXUEB ZQJO
Pushed and pulled the joints                    | YMHIP GRRE
   and hinges                                   | GOHET UXDTW LCMMW AVNVJ VH
Till it looked all squares                      | OUFAN TQACK
   and oblongs                                  | KTOZZ RDABQ NNVPO IEFQA FS
Like a complicated figure                       | VVICV UDUER EYNPF FMNBJ VGQ
in the Second Book of Euclid                    |
                                                | FROMH ISSHO ULDER HIAWA THA
* B BETA III IV I AXLE (HQ) (EX) (IP) (TR) (BY) | TOOKT HECAM ERAOF ROSEW OOD
QVPQS OKOIL PUBKJ ZPISF XDW                     | MADEO FSLID INGFO LDING ROSEW OOD
BHCNS CXNUO AATZX SRCFY DGU                     | NEATL YPUTI TALLT OGETH ER
FLPNX GXIXT YJUJR CAUGE UNCFM KUF               | INITS CASEI TLAYC OMPAC TLY
WJFGK CIIRG XODJG VCGPQ OH                      | FOLDE DINTO NEARL YNOTH ING
ALWEB UHTZM OXIIV XUEFP RPR                     | BUTHE OPENE DOUTT HEHIN GES
KCGVP FPYKI KITLB URVGT SFU                     | PUSHE DANDP ULLED THEJO INTS
SMBNK FRIIM PDOFJ VTTUG RZM                     | ANDHI NGES
UVCYL FDZPG IBXRE WXUEB ZQJO                    | TILLI TLOOK EDALL SQUAR ES
YMHIP GRRE                                      | ANDOB LONGS
GOHET UXDTW LCMMW AVNVJ VH                      | LIKEA COMPL ICATE DFIGU RE
OUFAN TQACK                                     | INTHE SECON DBOOK OFEUC LID
KTOZZ RDABQ NNVPO IEFQA FS                      |
VVICV UDUER EYNPF FMNBJ VGQ                     |
```
## Handling Errors
- You can see a number of opportunities for input errors:
    - The configuration file may have the wrong format.
    - The input might not start with a setting.
    - The setting line can contain the wrong number of arguments.
    - The rotors might be misnamed.
    - A rotor might be repeated in the setting line.
    - The first rotor might not be a reflector.
    - The initial positions string might be the wrong length or contain characters not in the alphabet.
    
A significant amount of a program will typically be devoted to detecting such errors, and taking corrective action. In our case, the main program is set up in such a way that the only corrective action needed is to throw an EnigmaException with an explanatory message. The existing code in the main program will catch this exception, print its message, and exit with a standard Unix error indication. The skeleton supplies a simple static utility method, error, which provides a convenient way to create error exceptions.
