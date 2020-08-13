# KdmManager
KdmManager is a tool for projectionists to download KDMs from e-mail servers and upload them to screen servers automatically.

Download
-------------------------------------
The latest release can be downloaded [here][1].

Setup
-------------------------------------
In order to assign each downloaded KDM to a server/projector, each FTP login needs a specific string value (serial), which can be copied from an existing KDM. The code below shows the location, where the attribute is stored inside a KDM. 

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
Java Runtime Enviroment JRE 1.8 or higher is required. 
The latest JRE can be downloaded [here][2].
In order to work correctly, the computer must be connected to the screen servers (i.e. via LAN) and the email servers (i.e. via internet).

Further work
-------------------------------------
- [ ] Passwords should be saved encoded.
- [ ] Implement a better way to distinguish the servers.

[1]:https://github.com/TobiasMiosczka/KDMManager/releases
[2]:https://java.com/de/download/
