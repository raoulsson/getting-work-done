# getting-work-done

A Kotlin utility for encoding files and directory trees into a single encrypted, Base64-encoded text file — and decoding them back.

## Why?

This tool was born out of necessity. While working on-site for a client, the only way to bring code into their environment was via email. The email system aggressively scanned and filtered attachments, stripping HTML tags and flagging anything that looked like source code.

The workaround: encrypt your own code, Base64-encode it, and paste it into an email body. On the other side, decode and decrypt it back into the original file and directory structure. No attachments, no filters, no problem.

## What It Does

- **Encode:** Takes a file or an entire directory tree, encrypts it with AES (password + salt), and produces a single `.b64` text file. Directory structure and filenames are preserved via embedded metadata.
- **Decode:** Takes a `.b64` file, decrypts it, and reconstructs the original files and directories.

## Usage

Both `Encoder` and `Decoder` require a password, a salt (byte array), and an output directory.

### Encoding

```kotlin
val encoder = Encoder(password = "yourPassword", salt = yourSalt, outDir = "/path/to/output")

// Encode a single file
encoder.encodeToBase64("/path/to/file.txt")

// Encode an entire directory tree
encoder.encodeToBase64("/path/to/project/")
```

### Decoding

```kotlin
val decoder = Decoder(password = "yourPassword", salt = yourSalt, outDir = "/path/to/output")

// Decode a .b64 file back to the original file(s)
decoder.decodeFromBase64("/path/to/file.b64")
```

## Building & Testing

```bash
./gradlew build
./gradlew test
```

## Disclaimer

Only use this on your own code. Do not use this on other people's code without their permission. This tool exists to help you deliver your own work to your customers — nothing more.
