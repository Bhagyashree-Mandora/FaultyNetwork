# FaultyNetwork
The purpose of the project is Provides fault tolerance for corrupted and dropped packets sent through a faulty network.  

The faults that are handled are:  

- Corruption of packets  
- Dropped packets  

The program uses retries and acknowledgements to handle dropped packets and Forward Error Correction such as Hamming Error Correction codes for corruption.    

The program can handle upto 100% corruption and 95% packet drop rates.

