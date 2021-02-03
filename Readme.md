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

Manual pipeline execution
    Variable description as below
    
    1. development env        
        | **name**  | **value**       |
        | ------| ----------- |
        | ENV   | dev         |
        | NODE1 | 10.10.73.15 |
        | NODE2 | 10.10.73.16 |
        | NODE3 | 10.10.73.17 |

    2. UAT env
        | **name**  | **value**        |
        | ------| ------------ |
        | ENV   | uat          |
        | NODE1 | 10.10.94.140 |
        | NODE2 | 10.10.94.141 |
        | NODE3 | 10.10.74.142 |

    3. Production Env
        | **name**  | **value**        |
        | ------| ------------ |
        | ENV   | prod         |
        | NODE1 | xx.xx.xx.xx  |
        | NODE2 | xx.xx.xx.xx  |
        | NODE3 | xx.xx.xx.xx  |


