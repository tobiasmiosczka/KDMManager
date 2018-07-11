# KDMManager
KDMManager is a tool for projectionists to automatically download KDMs from e-mail servers and upload them to screen servers.

Download
-------------------------------------
You can download the latest release [here][1].

Setup
-------------------------------------
In order to assign each downloaded KDM to a server/projector, each ftp login needs a specific string value (in the program it is called "serial"), which can be copied from an existing KDM. The code below shows where this attribute is stored inside a KDM. 

```
<?xml version="1.0" encoding="UTF-8" standalone="no" ?>
<DCinemaSecurityMessage xmlns="http://www.smpte-ra.org/schemas/430-3/2006/ETM" xmlns:dsig="http://www.w3.org/2000/09/xmldsig#" xmlns:enc="http://www.w3.org/2001/04/xmlenc#">
  <AuthenticatedPublic Id="ID_AuthenticatedPublic">
    <RequiredExtensions>
      <KDMRequiredExtensions xmlns="http://www.smpte-ra.org/schemas/430-1/2006/KDM">
        <Recipient>
          <X509SubjectName>  [needed information]  </X509SubjectName>
        </Recipient>
      </KDMRequiredExtensions>
    </RequiredExtensions>
    <NonCriticalExtensions/>
  </AuthenticatedPublic>
</DCinemaSecurityMessage>
```


Requirements
-------------------------------------
For the KDMManager a Java Virtual Machine JRE 1.8 or higher is required. 
The latest JRE can be downloaded [here][2].
In order to work, the program must be run on a computer, which is connected to the screen servers (i.e. via a private network) and the email servers (i.e. via internet).

Further work
-------------------------------------
- [ ] Passwords should be saved encoded.
- [ ] Implement a better way to 

[1]:https://github.com/TobiasMiosczka/KDMManager/releases
[2]:https://java.com/de/download/
