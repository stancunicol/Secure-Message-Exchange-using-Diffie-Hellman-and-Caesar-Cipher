# Secure Message Exchange using Diffie-Hellman and Caesar Cipher
A Java client-server application that implements secure message exchange using Diffie-Hellman key exchange and Caesar cipher encryption.

# Features
🔒 Diffie-Hellman Key Exchange: Secure generation of shared secret keys

✉️ Caesar Cipher Encryption: Classic encryption algorithm using the shared key

🌐 Network Communication: TCP socket-based client-server architecture

🛡️ Key Validation: Ensures the shared key is never zero

# How It Works
Key Exchange:

Client and server agree on public parameters (prime number q and primitive root a)

Each generates private keys (xClient, xServer) and public keys (yClient, yServer)

Computes shared secret key using Diffie-Hellman algorithm

Message Encryption:

Client encrypts messages using Caesar cipher with the shared key

Server decrypts messages using the same key

Validation:

Ensures private keys are always ≥ 1

Regenerates keys if shared secret key would be zero

# Prerequisites
Java JDK 8 or later

Basic understanding of cryptography concepts

# Example
![image](https://github.com/user-attachments/assets/273f50c3-3c93-4296-988e-203d01ffb389)

![image](https://github.com/user-attachments/assets/4f79c3c0-8aab-4edc-91a9-f05a6d801f5a)
