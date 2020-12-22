New Cluster

    Manual Tasks
    1. Create SSH key on GITLab runner primary machine -> ssh-keygen
    2. Enable SSH on all the machines
    3. GITlab runner public key -> add to -> target machine authorizeKey (folder name .ssh)
    4. Install GITLab runner
    5. Register GITLab 

    Execute Steps

    1. Run pipeline
        1.1 Runs elk.yml
            1.1.1 Installs java
            1.1.2 Install Elasticsearch
            1.1.3 TLS Configuration
        1.2 Installs Kibana
        1.3 Installs Logstash

Update Case

    For example Elasticsearch configuration change:
    1. Change elk.yml
    2. Run pipeline -> only elk.yml

