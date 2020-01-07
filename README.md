# FaultyNetwork
The aim is to provide fault tolerance for corrupted and dropped packets sent through a faulty network.  

The faults that are handled are:  

- Corruption of packets  
- Dropped packets  

Our client program relies on the FaultyNetwork to send data (say a 5 GB image of Mars captured by the camera on a satellite back to earth). The FaultyNetork is not very reliable. Our goal is to survive the faults and recover the message sent by the client program.  

The program uses retries and acknowledgements to handle dropped packets and Forward Error Correction such as Hamming Error Correction codes for corruption.  

The program can handle upto 100% corruption and 95% packet drop rates.
